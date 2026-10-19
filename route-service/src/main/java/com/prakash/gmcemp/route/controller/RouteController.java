package com.prakash.gmcemp.route.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prakash.gmcemp.route.dto.RouteRequest;
import com.prakash.gmcemp.route.model.Route;
import com.prakash.gmcemp.route.service.RouteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService service;

    public RouteController(RouteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Route> create(
            @Valid @RequestBody RouteRequest request) {

        return ResponseEntity
                .status(201)
                .body(service.create(request));
    }

    @GetMapping
    public List<Route> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    public Route get(
            @PathVariable Long id) {

        return service.get(id);
    }

    @GetMapping("/lookup")
    public Route lookup(
            @RequestParam String origin,
            @RequestParam String destination) {

        return service.lookup(
                origin,
                destination);
    }
}