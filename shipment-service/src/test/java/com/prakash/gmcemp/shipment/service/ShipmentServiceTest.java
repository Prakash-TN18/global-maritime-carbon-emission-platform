package com.prakash.gmcemp.shipment.service;

import com.prakash.gmcemp.shipment.dto.*;
import com.prakash.gmcemp.shipment.model.*;
import com.prakash.gmcemp.shipment.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ShipmentServiceTest {
    @Test
    void createsShipment() {
        ShipmentRepository r = mock(ShipmentRepository.class);
        when(r.save(any())).thenAnswer(i -> i.getArgument(0));
        ShipmentService s = new ShipmentService(r);
        Shipment x = s.create(
                new ShipmentRequest("B001", "Vessel A", "SGSIN", "INMAA", "40HC", 10.0, ShipmentStatus.CREATED));
        assertEquals("B001", x.getBookingNo());
        verify(r).save(any());
    }
}