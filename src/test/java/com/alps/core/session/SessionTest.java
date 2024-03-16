package com.alps.core.session;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alps.core.location.Location;
import com.alps.core.location.LocationSeat;
import com.alps.core.lock.LockProvider;
import com.alps.infrastructure.lock.ReentrantLockProvider;

public class SessionTest {

    Location location;
    Set<LocationSeat> seats;
    Session session;
    LockProvider lockProvider;
    LocalDateTime startTime;
    LocalDateTime endTime;

    @BeforeEach
    void setup() {
        lockProvider = new ReentrantLockProvider();
        location = new SomeLocation();
        boolean isAvailable = true;
        seats = createLocationSeats(isAvailable);
        startTime = LocalDateTime.now().minusHours(1);
        endTime = LocalDateTime.now().plusHours(1);
        session = Session.create(
                "1",
                "session",
                startTime,
                endTime,
                location,
                seats);
    }

    @Test
    void assertNonNull() {
        assertThrows(NullPointerException.class, () -> {
            Session.create(
                    null,
                    "some name",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    location,
                    seats);
        });

        assertThrows(NullPointerException.class, () -> {
            Session.create(
                    "1",
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    location,
                    seats);
        });

        assertThrows(NullPointerException.class, () -> {
            Session.create(
                    "1",
                    "some name",
                    null,
                    LocalDateTime.now(),
                    location,
                    seats);
        });

        assertThrows(NullPointerException.class, () -> {
            Session.create(
                    "1",
                    "some name",
                    LocalDateTime.now(),
                    null,
                    location,
                    seats);
        });

        assertThrows(NullPointerException.class, () -> {
            Session.create(
                    "1",
                    "some name",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    null,
                    seats);
        });

        assertThrows(NullPointerException.class, () -> {
            Session.create(
                    "1",
                    "some name",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    location,
                    null);
        });
    }

    @Test
    void shoudReturnTrueWhenSessionIsActive() {
        assertTrue(session.isActive());
    }

    @Test
    void shouldReturnFalseWhenSessionIsInactiveBeforeStartTime() {
        startTime = LocalDateTime.now().minusHours(1);
        endTime = LocalDateTime.now();
        session = Session.create(
                "1",
                "session",
                startTime,
                endTime,
                location,
                seats);
        assertFalse(session.isActive());
    }

    @Test
    void shouldReturnTrueForAvailableSeats() {
        assertTrue(session.hasAvailableSeats());
    }

    @Test
    void shouldReturnFalseForUnavailableSeats() {
        seats = createLocationSeats(false);
        session = Session.create(
                "1",
                "session",
                startTime,
                endTime,
                location,
                seats);

        assertFalse(session.hasAvailableSeats());
    }

    @Test
    void shouldReturnTrueIfSpecificSeatIsAvailable() {
        assertTrue(session.isSeatAvailable("1"));
    }

    @Test
    void shouldReturnFalseIfSpecificSeatIsNotAvailable() {
        seats = createLocationSeats(false);
        session = Session.create(
                "1",
                "session",
                startTime,
                endTime,
                location,
                seats);

        assertFalse(session.isSeatAvailable("1"));
    }

    private Set<LocationSeat> createLocationSeats(boolean isAvailable) {
        LocationSeat locationSeat1 = LocationSeat.create("1", "location one", location, lockProvider, isAvailable);
        LocationSeat locationSeat2 = LocationSeat.create("2", "location two", location, lockProvider, isAvailable);
        LocationSeat locationSeat3 = LocationSeat.create("3", "location three", location, lockProvider, isAvailable);

        Set<LocationSeat> locationSeats = new HashSet<>();
        locationSeats.add(locationSeat1);
        locationSeats.add(locationSeat2);
        locationSeats.add(locationSeat3);

        return locationSeats;
    }

    class SomeLocation implements Location {
    }

}
