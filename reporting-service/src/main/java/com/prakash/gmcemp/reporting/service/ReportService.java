package com.prakash.gmcemp.reporting.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.prakash.gmcemp.reporting.audit.Auditable;
import com.prakash.gmcemp.reporting.client.EmissionClient;
import com.prakash.gmcemp.reporting.dto.EmissionView;
import com.prakash.gmcemp.reporting.exception.ResourceNotFoundException;
import com.prakash.gmcemp.reporting.model.ReportAudit;
import com.prakash.gmcemp.reporting.repository.ReportAuditRepository;

@Service
public class ReportService {

    private final EmissionClient emissionClient;
    private final ReportAuditRepository auditRepository;

    public ReportService(
            EmissionClient emissionClient,
            ReportAuditRepository auditRepository) {

        this.emissionClient = emissionClient;
        this.auditRepository = auditRepository;
    }

    @Auditable(
        action = "GENERATE_SUMMARY_REPORT",
        entityType = "REPORT")
    public Map<String, Object> summary() {

        EmissionView[] rows = emissionClient.all();

        List<EmissionView> emissions =
                rows == null
                        ? List.of()
                        : Arrays.asList(rows);

        if (emissions.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No emission data found");
        }

        double totalCo2Kg =
                emissions.stream()
                        .mapToDouble(emission ->
                                emission.totalCo2Kg() == null
                                        ? 0
                                        : emission.totalCo2Kg())
                        .sum();

        double averageCo2Kg =
                emissions.stream()
                        .mapToDouble(emission ->
                                emission.totalCo2Kg() == null
                                        ? 0
                                        : emission.totalCo2Kg())
                        .average()
                        .orElse(0);

        auditRepository.save(
                new ReportAudit("SUMMARY"));

        return Map.of(
                "calculationCount", emissions.size(),
                "totalCo2Kg", totalCo2Kg,
                "averageCo2Kg", averageCo2Kg
        );
    }

    @Auditable(
        action = "GENERATE_SHIPMENT_REPORT",
        entityType = "REPORT")
    public EmissionView[] shipment(Long shipmentId) {

        if (shipmentId == null) {

            throw new ResourceNotFoundException(
                    "Shipment Id is mandatory");
        }

        EmissionView[] emissions =
                emissionClient.byShipment(shipmentId);

        if (emissions == null
                || emissions.length == 0) {

            throw new ResourceNotFoundException(
                    "No emission records found for shipment : "
                            + shipmentId);
        }

        auditRepository.save(
                new ReportAudit("SHIPMENT"));

        return emissions;
    }
}