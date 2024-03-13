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

    public void markAsUsed() {
        if (!used) {
            this.used = true;
        }
    }

    public boolean isValid() {
        return !used && LocalDate.now().isBefore(validUntil) && session.isActive();
    }

}
