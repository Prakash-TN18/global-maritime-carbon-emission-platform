package com.prakash.gmcemp.shipment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.prakash.gmcemp.shipment.audit.Auditable;
import com.prakash.gmcemp.shipment.dto.ShipmentRequest;
import com.prakash.gmcemp.shipment.exception.BusinessException;
import com.prakash.gmcemp.shipment.exception.ResourceNotFoundException;
import com.prakash.gmcemp.shipment.model.Shipment;
import com.prakash.gmcemp.shipment.repository.ShipmentRepository;

@Service
public class ShipmentService {

    private final ShipmentRepository repo;

    public ShipmentService(ShipmentRepository repo) {
        this.repo = repo;
    }

@Auditable(
        action = "CREATE_SHIPMENT",
        entityType = "SHIPMENT")
    public Shipment create(ShipmentRequest request) {

        validateRequest(request);

        return repo.save(
                map(new Shipment(), request));
    }

    @Auditable(
        action = "GET_SHIPMENT",
        entityType = "SHIPMENT")
    public Shipment get(Long id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shipment not found : " + id));
    }

    public List<Shipment> all() {

        List<Shipment> shipments =
                repo.findAll();

        if (shipments.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No shipments found");
        }

        return shipments;
    }

    @Auditable(
        action = "UPDATE_SHIPMENT",
        entityType = "SHIPMENT")
    public Shipment update(
            Long id,
            ShipmentRequest request) {

        validateRequest(request);

        Shipment shipment = get(id);

        return repo.save(
                map(shipment, request));
    }

    private void validateRequest(ShipmentRequest request) {

    if (request.bookingNo() == null
            || request.bookingNo().isBlank()) {

        throw new BusinessException(
                "Booking No is mandatory");
    }

    if (request.vesselName() == null
            || request.vesselName().isBlank()) {

        throw new BusinessException(
                "Vessel Name is mandatory");
    }

    if (request.originPort() == null
            || request.originPort().isBlank()) {

        throw new BusinessException(
                "Origin Port is mandatory");
    }

    if (request.destinationPort() == null
            || request.destinationPort().isBlank()) {

        throw new BusinessException(
                "Destination Port is mandatory");
    }

    if (request.containerType() == null
            || request.containerType().isBlank()) {

        throw new BusinessException(
                "Container Type is mandatory");
    }

    if (request.cargoWeightTons() == null
            || request.cargoWeightTons() <= 0) {

        throw new BusinessException(
                "Cargo Weight must be greater than 0");
    }

    if (request.status() == null) {

        throw new BusinessException(
                "Status is mandatory");
    }
}

    private Shipment map(
            Shipment entity,
            ShipmentRequest request) {

        entity.setBookingNo(request.bookingNo());
        entity.setVesselName(request.vesselName());
        entity.setOriginPort(request.originPort());
        entity.setDestinationPort(request.destinationPort());
        entity.setContainerType(request.containerType());
        entity.setCargoWeightTons(request.cargoWeightTons());
        entity.setStatus(request.status());

        return entity;
    }
}