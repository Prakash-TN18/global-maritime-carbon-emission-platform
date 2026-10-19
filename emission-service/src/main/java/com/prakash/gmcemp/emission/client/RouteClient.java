package com.prakash.gmcemp.emission.client;

import com.prakash.gmcemp.emission.dto.RouteView;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "route-service")
public interface RouteClient {

    @GetMapping("/api/routes/{id}")
    RouteView getRoute(
            @PathVariable("id") Long id);
}