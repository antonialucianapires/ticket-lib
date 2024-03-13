package com.alps.core.ticket;

import java.time.LocalDate;

import com.alps.core.location.LocationSeat;
import com.alps.core.price.Price;
import com.alps.core.reservation.Reservation;
import com.alps.core.session.Session;
import com.alps.core.user.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a ticket for a specific session and seat within an event, showing, or service.
 * This class encapsulates details about the ticket holder, the associated seat, session,
 * reservation, price, and validity of the ticket.
 */
@Getter
@EqualsAndHashCode
public class Ticket {

    private final String ticketId;
    private final User user;
    private final LocationSeat locationSeat;
    private final Session session;
    private final Reservation reservation;
    private final Price price;
    private final LocalDate validUntil;
    private boolean used;

    /**
     * Constructs a new Ticket instance with the specified details.
     *
     * @param ticketId Unique identifier for the ticket.
     * @param user The user to whom the ticket is issued.
     * @param locationSeat The seat associated with the ticket within the location.
     * @param session The session for which the ticket is valid.
     * @param reservation The reservation associated with this ticket.
     * @param price The price of the ticket.
     * @param validUntil The date until which the ticket is valid.
     * @param used Indicates whether the ticket has already been used.
     */
    private Ticket(
            @NonNull String ticketId,
            @NonNull User user,
            @NonNull LocationSeat locationSeat,
            @NonNull Session session,
            @NonNull Reservation reservation,
            @NonNull Price price,
            @NonNull LocalDate validUntil,
            boolean used) {

        this.ticketId = ticketId;
        this.user = user;
        this.locationSeat = locationSeat;
        this.session = session;
        this.reservation = reservation;
        this.price = price;
        this.validUntil = validUntil;
        this.used = used;
    }

    /**
     * Factory method to create and return a new Ticket instance.
     *
     * @param ticketId Unique identifier for the ticket.
     * @param user The user to whom the ticket is issued.
     * @param locationSeat The seat associated with the ticket.
     * @param session The session for which the ticket is valid.
     * @param reservation The reservation associated with this ticket.
     * @param price The price of the ticket.
     * @param validUntil The date until which the ticket is valid.
     * @param used The initial usage state of the ticket.
     * @return A new instance of Ticket with the specified details.
     */
    public static Ticket create(
            String ticketId,
            User user,
            LocationSeat locationSeat,
            Session session,
            Reservation reservation,
            Price price,
            LocalDate validUntil,
            boolean used) {

        return new Ticket(
                ticketId,
                user,
                locationSeat,
                session,
                reservation,
                price,
                validUntil,
                used);
    }

    /**
     * Marks the ticket as used. This operation is idempotent and will only change the state
     * if the ticket is currently unused.
     */
    public void markAsUsed() {
        if (!used) {
            this.used = true;
        }
    }

    /**
     * Checks whether the ticket is valid. A ticket is considered valid if it has not been used,
     * is before its valid until date, and the associated session is active.
     *
     * @return True if the ticket is valid, otherwise false.
     */
    public boolean isValid() {
        return !used && LocalDate.now().isBefore(validUntil) && session.isActive();
    }

}