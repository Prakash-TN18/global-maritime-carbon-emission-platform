package com.prakash.gmcemp.emission.client;

import com.prakash.gmcemp.emission.dto.ShipmentView;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "shipment-service")
public interface ShipmentClient {

    @GetMapping("/api/shipments/{id}")
    ShipmentView getShipment(
            @PathVariable("id") Long id);
}