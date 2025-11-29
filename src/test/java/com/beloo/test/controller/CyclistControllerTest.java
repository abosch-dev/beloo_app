package com.beloo.test.controller;

import com.beloo.test.service.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class CyclistControllerTest {

    @Mock
    private LocationService locationService;
    @InjectMocks
    private CyclistController cyclistController;

    @Test
    void get_cyclist_route_should_call_location_service_to_return_route() {
        var userId = "1";

        cyclistController.getLocations(userId);

        var userCapture = ArgumentCaptor.forClass(String.class);
        verify(locationService, times(1)).getCyclistLocations(userCapture.capture());
        assertEquals(userId, userCapture.getValue());
    }
}