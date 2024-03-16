package com.alps.core.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alps.core.location.Location;
import com.alps.core.location.LocationSeat;
import com.alps.core.lock.LockProvider;
import com.alps.core.price.Price;
import com.alps.core.session.Session;
import com.alps.core.user.User;
import com.alps.infrastructure.lock.ReentrantLockProvider;

public class ReservationTest {

    Reservation reservation;
    User user;
    Location location;
    Session session;
    Set<LocationSeat> seats;
    LocalDateTime creationTime;
    Duration expirationTime;
    ReservationStatus reservationStatus;
    LockProvider lockProvider;
    Price price;

    @BeforeEach
    void setup() {
        reservationStatus = new ReservationStatus(ReservationStatus.StandardStatus.PENDING);
        location = new SomeLocation();
        lockProvider = new ReentrantLockProvider();
        user = User.create("1", "user one", "user@email.com");
        seats = createLocationSeats();
        session = Session.create("1", "session", LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1),
                location, seats);
        seats = createLocationSeats();
        creationTime = LocalDateTime.now().minusDays(2); 
        expirationTime = Duration.ofHours(24); 
        price = new Price(BigDecimal.valueOf(100));

        reservation = Reservation.create(
                "1",
                user,
                session,
                seats,
                creationTime,
                expirationTime,
                lockProvider,
                reservationStatus,
                price);
    }

    @Test
    void assertNonNull() {
        assertThrows(NullPointerException.class, () -> {
            Reservation.create(
                    null,
                    user,
                    session,
                    seats,
                    creationTime,
                    expirationTime,
                    lockProvider,
                    reservationStatus,
                    price);
        });

        assertThrows(NullPointerException.class, () -> {
            Reservation.create(
                    "1",
                    null,
                    session,
                    seats,
                    creationTime,
                    expirationTime,
                    lockProvider,
                    reservationStatus,
                    price);
        });

        assertThrows(NullPointerException.class, () -> {
            Reservation.create(
                    "1",
                    user,
                    null,
                    seats,
                    creationTime,
                    expirationTime,
                    lockProvider,
                    reservationStatus,
                    price);
        });

        assertThrows(NullPointerException.class, () -> {
            Reservation.create(
                    "1",
                    user,
                    session,
                    null,
                    creationTime,
                    expirationTime,
                    lockProvider,
                    reservationStatus,
                    price);
        });

        assertThrows(NullPointerException.class, () -> {
            Reservation.create(
                    "1",
                    user,
                    session,
                    seats,
                    null,
                    expirationTime,
                    lockProvider,
                    reservationStatus,
                    price);
        });

        assertThrows(NullPointerException.class, () -> {
            Reservation.create(
                    "1",
                    user,
                    session,
                    seats,
                    creationTime,
                    null,
                    lockProvider,
                    reservationStatus,
                    price);
        });

        assertThrows(NullPointerException.class, () -> {
            Reservation.create(
                    "1",
                    user,
                    session,
                    seats,
                    creationTime,
                    expirationTime,
                    null,
                    reservationStatus,
                    price);
        });

        assertThrows(NullPointerException.class, () -> {
            Reservation.create(
                    "1",
                    user,
                    session,
                    seats,
                    creationTime,
                    expirationTime,
                    lockProvider,
                    reservationStatus,
                    null);
        });

    }

    @Test
    void shouldCancelReservationWhenStatusIsPending() {
        Reservation cancelledReservation = reservation.cancel();
        boolean hasAvailableSeats = cancelledReservation.getSeats().stream().allMatch(LocationSeat::isAvailable);
        assertEquals(ReservationStatus.StandardStatus.CANCELLED, cancelledReservation.getStatus().getStandardStatus());
        assertTrue(hasAvailableSeats);
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonPendingReservation() {
        reservation = Reservation.create(
                "1",
                user,
                session,
                seats,
                creationTime,
                expirationTime,
                lockProvider,
                new ReservationStatus(ReservationStatus.StandardStatus.COMPLETE),
                price);

        assertThrows(IllegalStateException.class, () -> {
            reservation.cancel();
        });
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        Reservation otherReservation = Reservation.create(
                "1",
                user,
                session,
                seats,
                creationTime,
                expirationTime,
                lockProvider,
                new ReservationStatus(ReservationStatus.StandardStatus.PENDING),
                price);

        assertEquals(reservation, otherReservation);
        assertEquals(reservation.hashCode(), otherReservation.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenAnyFieldIsDifferent() {
        Reservation otherReservation = Reservation.create(
                "1",
                user,
                session,
                seats,
                creationTime,
                expirationTime,
                lockProvider,
                new ReservationStatus(ReservationStatus.StandardStatus.COMPLETE),
                price);

        assertNotEquals(reservation, otherReservation);
    }

    @Test
    void shouldReturnTrueWhenReservationIsExpired() {
        assertTrue(reservation.isExpired());
    }

    @Test
void shouldReturnFalseWhenReservationIsNotExpired() {
    LocalDateTime newCreationTime = LocalDateTime.now().minusHours(1);
    Duration newExpirationTime = Duration.ofDays(1);

    Reservation notExpiredReservation = Reservation.create(
            "2",
            user,
            session,
            seats,
            newCreationTime,
            newExpirationTime,
            lockProvider,
            reservationStatus,
            price);

    assertFalse(notExpiredReservation.isExpired());
}
    Set<LocationSeat> createLocationSeats() {
        LocationSeat locationSeat1 = LocationSeat.create("1", "location one", location, lockProvider, true);
        LocationSeat locationSeat2 = LocationSeat.create("2", "location two", location, lockProvider, true);
        LocationSeat locationSeat3 = LocationSeat.create("3", "location three", location, lockProvider, true);

        Set<LocationSeat> locationSeats = new HashSet<>();
        locationSeats.add(locationSeat1);
        locationSeats.add(locationSeat2);
        locationSeats.add(locationSeat3);

        return locationSeats;
    }

    class SomeLocation implements Location {
    }

}
