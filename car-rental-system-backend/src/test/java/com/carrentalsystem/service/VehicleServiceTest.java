package com.carrentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrentalsystem.dao.VehicleDao;
import com.carrentalsystem.entity.Variant;
import com.carrentalsystem.entity.Vehicle;
import com.carrentalsystem.utility.Constants.ActiveStatus;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleDao vehicleDao;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle sampleVehicle;
    private Variant sampleVariant;

    @BeforeEach
    void setUp() {
        sampleVariant = new Variant();
        sampleVariant.setId(1);
        sampleVariant.setName("Tesla Model S");

        sampleVehicle = new Vehicle();
        sampleVehicle.setId(10);
        sampleVehicle.setRegistrationNumber("TES-1234");
        sampleVehicle.setVariant(sampleVariant);
        sampleVehicle.setStatus(ActiveStatus.ACTIVE.value());
    }

    @Test
    void testAddVehicle() {
        when(vehicleDao.save(any(Vehicle.class))).thenReturn(sampleVehicle);

        Vehicle savedVehicle = vehicleService.addVehicle(sampleVehicle);

        assertNotNull(savedVehicle);
        assertEquals("TES-1234", savedVehicle.getRegistrationNumber());
    }

    @Test
    void testGetVehicleById_Found() {
        when(vehicleDao.findById(10)).thenReturn(Optional.of(sampleVehicle));

        Vehicle foundVehicle = vehicleService.getById(10);

        assertNotNull(foundVehicle);
        assertEquals(10, foundVehicle.getId());
    }

    @Test
    void testGetVehiclesByVariantAndStatus() {
        when(vehicleDao.findByVariantAndStatus(any(Variant.class), any(String.class)))
                .thenReturn(Collections.singletonList(sampleVehicle));

        List<Vehicle> vehicles = vehicleService.getByVariantAndStatus(sampleVariant, ActiveStatus.ACTIVE.value());

        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        assertEquals("TES-1234", vehicles.get(0).getRegistrationNumber());
    }
}
