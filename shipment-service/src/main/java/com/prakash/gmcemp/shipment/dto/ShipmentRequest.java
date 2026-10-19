package com.prakash.gmcemp.shipment.dto;

import com.prakash.gmcemp.shipment.model.ShipmentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ShipmentRequest(

        @NotBlank(message = "Booking No is mandatory")
        String bookingNo,

        @NotBlank(message = "Vessel Name is mandatory")
        String vesselName,

        @NotBlank(message = "Origin Port is mandatory")
        String originPort,

        @NotBlank(message = "Destination Port is mandatory")
        String destinationPort,

        @NotBlank(message = "Container Type is mandatory")
        String containerType,

        @NotNull(message = "Cargo Weight is mandatory")
        @Positive(message = "Cargo Weight must be greater than 0")
        Double cargoWeightTons,

        @NotNull(message = "Status is mandatory")
        ShipmentStatus status

) {
}