package com.carrentalsystem.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.carrentalsystem.dto.AddVehicleRequest;
import com.carrentalsystem.dto.CommonApiResponse;
import com.carrentalsystem.dto.VehicleResponse;
import com.carrentalsystem.entity.Variant;
import com.carrentalsystem.entity.Vehicle;
import com.carrentalsystem.service.VariantService;
import com.carrentalsystem.service.VehicleService;
import com.carrentalsystem.utility.Constants.ActiveStatus;

@ExtendWith(MockitoExtension.class)
public class VehicleResourceTest {

    @InjectMocks
    private VehicleResource vehicleResource;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private VariantService variantService;

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
    void testAddVehicle_Success() {
        AddVehicleRequest request = new AddVehicleRequest();
        request.setRegistrationNumber("TES-1234");
        request.setVariantId(1);

        when(variantService.getById(1)).thenReturn(sampleVariant);
        when(vehicleService.addVehicle(any(Vehicle.class))).thenReturn(sampleVehicle);

        ResponseEntity<CommonApiResponse> response = vehicleResource.addVehicle(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Araç başarıyla eklendi.", response.getBody().getResponseMessage());
    }

    @Test
    void testFetchAllVehicles_Found() {
        when(vehicleService.getByStatus(ActiveStatus.ACTIVE.value()))
                .thenReturn(Collections.singletonList(sampleVehicle));

        ResponseEntity<VehicleResponse> response = vehicleResource.fetchAllVehicles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getVehicles().size());
    }
}
