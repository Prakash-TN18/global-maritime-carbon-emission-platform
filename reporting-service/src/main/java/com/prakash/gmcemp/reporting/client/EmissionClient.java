package com.prakash.gmcemp.reporting.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.prakash.gmcemp.reporting.dto.EmissionView;

@FeignClient(name = "emission-service")
public interface EmissionClient {

    @GetMapping("/api/emissions")
    EmissionView[] all();

    @GetMapping("/api/emissions/shipment/{id}")
    EmissionView[] byShipment(
            @PathVariable("id") Long id);
}