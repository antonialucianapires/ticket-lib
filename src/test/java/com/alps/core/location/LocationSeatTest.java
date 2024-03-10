package com.alps.core.location;

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
        locationSeat = new LocationSeat(UUID.randomUUID().toString(), "M1 - Seat", location, lockProvider, true);
    }

    @Test
    void shoudlReserveSeat() {
        locationSeat.reserveSeat();
        assertFalse(locationSeat.isAvailable());
    }

    @Test
    void shoudlReleaseSeat() {
        locationSeat.reserveSeat();
        locationSeat.releaseSeat();
        assertTrue(locationSeat.isAvailable());
    }

    @Test
    void shoudlThrowExceptionWhenReservingReservedSeat() {
        locationSeat.reserveSeat();
        assertThrows(IllegalStateException.class, () -> locationSeat.reserveSeat());
    }

    @Test
    void shoudlThrowExceptionWhenReleasingAvailableSeat() {
        assertThrows(IllegalStateException.class, () -> locationSeat.releaseSeat());
    }

    class SomeLocation implements Location {
    }

}
