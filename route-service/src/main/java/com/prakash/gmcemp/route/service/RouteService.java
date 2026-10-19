package com.prakash.gmcemp.route.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.prakash.gmcemp.route.audit.Auditable;
import com.prakash.gmcemp.route.dto.RouteRequest;
import com.prakash.gmcemp.route.exception.BusinessException;
import com.prakash.gmcemp.route.exception.ResourceNotFoundException;
import com.prakash.gmcemp.route.model.Route;
import com.prakash.gmcemp.route.repository.RouteRepository;

@Service
public class RouteService {

    private final RouteRepository repo;

    public RouteService(RouteRepository repo) {
        this.repo = repo;
    }

    @Auditable(
        action = "CREATE_ROUTE",
        entityType = "ROUTE")
    public Route create(RouteRequest request) {

        validateRequest(request);

        Route route = new Route();

        route.setOriginPort(request.originPort());
        route.setDestinationPort(request.destinationPort());
        route.setDistanceKm(request.distanceKm());
        route.setOriginCountry(request.originCountry());
        route.setDestinationCountry(request.destinationCountry());

        return repo.save(route);
    }

    public List<Route> all() {

        List<Route> routes = repo.findAll();

        if (routes.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No routes found");
        }

        return routes;
    }

    @Auditable(
        action = "GET_ROUTE",
        entityType = "ROUTE")
    public Route get(Long id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found : " + id));
    }

    public Route lookup(
            String origin,
            String destination) {

        return repo
                .findByOriginPortIgnoreCaseAndDestinationPortIgnoreCase(
                        origin,
                        destination)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found for "
                                        + origin
                                        + " -> "
                                        + destination));
    }

    private void validateRequest(
            RouteRequest request) {

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

        if (request.distanceKm() == null
                || request.distanceKm() <= 0) {

            throw new BusinessException(
                    "Distance must be greater than 0");
        }

        if (request.originCountry() == null
                || request.originCountry().isBlank()) {

            throw new BusinessException(
                    "Origin Country is mandatory");
        }

        if (request.destinationCountry() == null
                || request.destinationCountry().isBlank()) {

            throw new BusinessException(
                    "Destination Country is mandatory");
        }
    }
}