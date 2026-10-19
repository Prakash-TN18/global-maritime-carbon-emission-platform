package com.prakash.gmcemp.emission.service;

import java.util.List;

import com.prakash.gmcemp.emission.audit.Auditable;
import org.springframework.stereotype.Service;
import com.prakash.gmcemp.emission.exception.BusinessException;
import com.prakash.gmcemp.emission.exception.ResourceNotFoundException;
import com.prakash.gmcemp.emission.client.RouteClient;
import com.prakash.gmcemp.emission.client.ShipmentClient;
import com.prakash.gmcemp.emission.dto.CalculationRequest;
import com.prakash.gmcemp.emission.dto.RouteView;
import com.prakash.gmcemp.emission.dto.ShipmentView;
import com.prakash.gmcemp.emission.exception.ResourceNotFoundException;
import com.prakash.gmcemp.emission.model.EmissionCalculation;
import com.prakash.gmcemp.emission.repository.EmissionRepository;

@Service
public class EmissionService {

    private final EmissionRepository repo;

    private final ShipmentClient shipmentClient;

    private final RouteClient routeClient;

    public EmissionService(
            EmissionRepository repo,
            ShipmentClient shipmentClient,
            RouteClient routeClient) {

        this.repo = repo;
        this.shipmentClient = shipmentClient;
        this.routeClient = routeClient;
    }

    @Auditable(
        action = "CALCULATE_EMISSION",
        entityType = "EMISSION")
    public EmissionCalculation calculate(
        CalculationRequest request) {

    if (request.shipmentId() == null) {
        throw new BusinessException(
                "Shipment Id is mandatory");
    }

    if (request.routeId() == null) {
        throw new BusinessException(
                "Route Id is mandatory");
    }

    ShipmentView shipment =
            shipmentClient.getShipment(
                    request.shipmentId());

    if (shipment == null) {
        throw new ResourceNotFoundException(
                "Shipment not found : "
                        + request.shipmentId());
    }

    RouteView route =
            routeClient.getRoute(
                    request.routeId());

    if (route == null) {
        throw new ResourceNotFoundException(
                "Route not found : "
                        + request.routeId());
    }

    double factor =
            request.emissionFactor() == null
                    ? 0.015
                    : request.emissionFactor();

    if (factor <= 0) {
        throw new BusinessException(
                "Emission factor must be greater than 0");
    }

    if (shipment.cargoWeightTons() <= 0) {
        throw new BusinessException(
                "Cargo weight must be greater than 0");
    }

    if (route.distanceKm() <= 0) {
        throw new BusinessException(
                "Distance must be greater than 0");
    }

    EmissionCalculation calculation =
            new EmissionCalculation();

    calculation.setShipmentId(
            shipment.id());

    calculation.setRouteId(
            route.id());

    calculation.setDistanceKm(
            route.distanceKm());

    calculation.setWeightTons(
            shipment.cargoWeightTons());

    calculation.setEmissionFactor(
            factor);

    calculation.setTotalCo2Kg(
            route.distanceKm()
                    * shipment.cargoWeightTons()
                    * factor);

    return repo.save(calculation);
}
    
public List<EmissionCalculation> byShipment(
        Long shipmentId) {

    List<EmissionCalculation> result =
            repo.findByShipmentId(
                    shipmentId);

    if (result.isEmpty()) {

        throw new ResourceNotFoundException(
                "No emission records found for shipment : "
                        + shipmentId);
    }

    return result;
}

public List<EmissionCalculation> all() {

    List<EmissionCalculation> result =
            repo.findAll();

    if (result.isEmpty()) {

        throw new ResourceNotFoundException(
                "No emission records found");
    }

    return result;
}
}