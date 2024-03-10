package com.alps.core.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.alps.core.reservation.Reservation;
import com.alps.core.reservation.ReservationStatus;
import com.alps.core.session.Session;
import com.alps.core.user.User;
import com.alps.infrastructure.lock.ReentrantLockProvider;

public class TicketTest {

    User user;
    LocationSeat locationSeat;
    Session session;
    Reservation reservation;
    BigDecimal price;
    LocalDate validUntil;
    LockProvider lockProvider;
    Location location;

    @BeforeEach
    public void setup() {
        location = new SomeLocation();
        lockProvider = new ReentrantLockProvider();
        user = User.create("1", "John Doe", "john.doe@example.com");
        locationSeat = LocationSeat.create("1", "locationSeat", location, lockProvider, true);
        session = Session.create("1", "Session 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1), location,
                Collections.singleton(locationSeat));
        reservation = Reservation.create("1", user, session, Collections.singleton(locationSeat), LocalDateTime.now(),
                Duration.ofHours(2), lockProvider, new ReservationStatus(ReservationStatus.StandardStatus.PENDING));
        price = BigDecimal.valueOf(100);
        validUntil = LocalDate.now().plusDays(1);
    }

    @Test
    void shouldCreateTicketWithCorrectDetails() {
        String ticketId = "ticket123";
        Ticket ticket = Ticket.create(ticketId, user, locationSeat, session, reservation, price, validUntil, false);

        assertNotNull(ticket);
        assertEquals(ticketId, ticket.getTicketId());
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
        Ticket ticket = Ticket.create("ticket123", user, locationSeat, session, reservation, price, validUntil, false);

        assertFalse(ticket.isUsed());
        ticket.markAsUsed();
        assertTrue(ticket.isUsed());
    }

    @Test
    void shouldValidateTicketAsValid() {
        Ticket ticket = Ticket.create("ticket123", user, locationSeat, session, reservation, price, validUntil, false);

        System.out.println("Ticket used: " + ticket.isUsed());
        System.out.println("Ticket valid until: " + ticket.getValidUntil());
        System.out.println("Session active: " + ticket.getSession().isActive());

        assertTrue(ticket.isValid());
    }

    @Test
    void shouldValidateTicketAsInvalidWhenExpired() {
        validUntil = LocalDate.now().minusDays(1);
        Ticket ticket = Ticket.create("ticket123", user, locationSeat, session, reservation, price, validUntil, false);

        assertFalse(ticket.isValid());
    }

    @Test
    void shouldValidateTicketAsInvalidWhenUsed() {
        Ticket ticket = Ticket.create("ticket123", user, locationSeat, session, reservation, price, validUntil, false);
        ticket.markAsUsed();

        assertFalse(ticket.isValid());
    }

    class SomeLocation implements Location {
    }

}
