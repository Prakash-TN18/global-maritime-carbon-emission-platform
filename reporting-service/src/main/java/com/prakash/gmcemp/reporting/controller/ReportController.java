package com.prakash.gmcemp.reporting.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.prakash.gmcemp.reporting.dto.EmissionView;
import com.prakash.gmcemp.reporting.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(
            ReportService service) {

        this.service = service;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {

        return service.summary();
    }

    @GetMapping("/shipment/{id}")
    public EmissionView[] shipment(
            @PathVariable Long id) {

        return service.shipment(id);
    }
}