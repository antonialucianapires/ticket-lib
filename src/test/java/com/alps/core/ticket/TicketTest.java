package com.alps.core.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alps.core.location.Location;
import com.alps.core.location.LocationSeat;
import com.alps.core.lock.LockProvider;
import com.alps.core.price.Price;
import com.alps.core.reservation.Reservation;
import com.alps.core.reservation.ReservationStatus;
import com.alps.core.session.Session;
import com.alps.core.user.User;
import com.alps.infrastructure.lock.ReentrantLockProvider;

public class TicketTest {

    private static final String TICKET_ID = "ticket123";
    User user;
    LocationSeat locationSeat;
    Session session;
    Reservation reservation;
    Price price;
    LocalDate validUntil;
    LockProvider lockProvider;
    Location location;

    @BeforeEach
    public void setup() {
        location = new SomeLocation();
        lockProvider = new ReentrantLockProvider();
        user = User.create("1", "John Doe", "john.doe@example.com");
        locationSeat = LocationSeat.create("1", "locationSeat", location, lockProvider, true);
        session = mock(Session.class);
        price = new Price(BigDecimal.valueOf(100));
        reservation = Reservation.create(
                "1",
                user,
                session,
                Collections.singleton(locationSeat),
                LocalDateTime.now(),
                Duration.ofHours(2),
                lockProvider,
                new ReservationStatus(ReservationStatus.StandardStatus.PENDING),
                price);
        validUntil = LocalDate.now().plusDays(1);
    }

    @Test
    void assertNonNull() {
        assertThrows(NullPointerException.class, () -> {
            Ticket.create(
                    null,
                    user,
                    locationSeat,
                    session,
                    reservation,
                    price,
                    validUntil,
                    false);
        });

        assertThrows(NullPointerException.class, () -> {
            Ticket.create(
                    TICKET_ID,
                    null,
                    locationSeat,
                    session,
                    reservation,
                    price,
                    validUntil,
                    false);
        });

        assertThrows(NullPointerException.class, () -> {
            Ticket.create(
                    TICKET_ID,
                    user,
                    null,
                    session,
                    reservation,
                    price,
                    validUntil,
                    false);
        });

        assertThrows(NullPointerException.class, () -> {
            Ticket.create(
                    TICKET_ID,
                    user,
                    locationSeat,
                    null,
                    reservation,
                    price,
                    validUntil,
                    false);
        });

        assertThrows(NullPointerException.class, () -> {
            Ticket.create(
                    TICKET_ID,
                    user,
                    locationSeat,
                    session,
                    null,
                    price,
                    validUntil,
                    false);
        });

        assertThrows(NullPointerException.class, () -> {
            Ticket.create(
                    TICKET_ID,
                    user,
                    locationSeat,
                    session,
                    reservation,
                    null,
                    validUntil,
                    false);
        });

        assertThrows(NullPointerException.class, () -> {
            Ticket.create(
                    TICKET_ID,
                    user,
                    locationSeat,
                    session,
                    reservation,
                    price,
                    null,
                    false);
        });
    }

    @Test
    void shouldCreateTicketWithCorrectDetails() {
        Ticket ticket = Ticket.create(
                TICKET_ID,
                user,
                locationSeat,
                session,
                reservation,
                price,
                validUntil,
                false);

        assertNotNull(ticket);
        assertEquals(TICKET_ID, ticket.getTicketId());
        assertEquals(user, ticket.getUser());
        assertEquals(locationSeat, ticket.getLocationSeat());
        assertEquals(session, ticket.getSession());
        assertEquals(reservation, ticket.getReservation());
        assertEquals(price, ticket.getPrice());
        assertEquals(validUntil, ticket.getValidUntil());
        assertFalse(ticket.isUsed());
    }

    @Test
    void shouldMarkTicketAsUsed() {
        Ticket ticket = Ticket.create(
                TICKET_ID,
                user,
                locationSeat,
                session,
                reservation,
                price,
                validUntil,
                false);

        assertFalse(ticket.isUsed());
        ticket.markAsUsed();
        assertTrue(ticket.isUsed());
    }

    @Test
    void shouldValidateTicketAsValid() {
        validUntil = LocalDate.now().plusDays(5);
        when(session.isActive()).thenReturn(true);

        Ticket ticket = Ticket.create(
                TICKET_ID,
                user,
                locationSeat,
                session,
                reservation,
                price,
                validUntil,
                false);

        assertTrue(ticket.isValid());
    }

    @Test
    void shouldValidateTicketAsInvalidWhenExpired() {
        validUntil = LocalDate.now().minusDays(1);
        Ticket ticket = Ticket.create(
                TICKET_ID,
                user,
                locationSeat,
                session,
                reservation,
                price,
                validUntil,
                false);

        assertFalse(ticket.isValid());
    }

    @Test
    void shouldValidateTicketAsInvalidWhenUsed() {
        Ticket ticket = Ticket.create(
                TICKET_ID,
                user,
                locationSeat,
                session,
                reservation,
                price,
                validUntil,
                false);

        ticket.markAsUsed();

        assertFalse(ticket.isValid());
    }

    class SomeLocation implements Location {
    }

}
