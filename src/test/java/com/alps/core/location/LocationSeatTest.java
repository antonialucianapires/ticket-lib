package com.alps.core.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alps.core.lock.LockProvider;
import com.alps.infrastructure.lock.ReentrantLockProvider;

public class LocationSeatTest {

    Location location;
    LockProvider lockProvider;
    LocationSeat locationSeat;

    @BeforeEach
    void setup() {
        lockProvider = new ReentrantLockProvider();
        location = new SomeLocation();
        locationSeat = LocationSeat.create(UUID.randomUUID().toString(), "M1 - Seat", location, lockProvider, true);
    }

    @Test
    void shoudlReserveSeat() {
        LocationSeat reservedLocation = locationSeat.reserve();
        assertFalse(reservedLocation.isAvailable());
    }

    @Test
    void shoudlReleaseSeat() {
        LocationSeat reservedLocation = locationSeat.reserve();
        LocationSeat locationReleased = reservedLocation.release();
        assertEquals(reservedLocation.getSeatId(), locationReleased.getSeatId());
        assertTrue(locationReleased.isAvailable());
    }

    @Test
    void shoudlThrowExceptionWhenReservingReservedSeat() {
        LocationSeat reservedLocation = locationSeat.reserve();
        assertThrows(IllegalStateException.class, () -> reservedLocation.reserve());
    }

    @Test
    void shoudlThrowExceptionWhenReleasingAvailableSeat() {
        assertThrows(IllegalStateException.class, () -> locationSeat.release());
    }

    class SomeLocation implements Location {
    }

}
