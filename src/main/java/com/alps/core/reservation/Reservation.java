package com.alps.core.reservation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.alps.core.location.LocationSeat;
import com.alps.core.lock.LockProvider;
import com.alps.core.price.Price;
import com.alps.core.reservation.ReservationStatus.StandardStatus;
import com.alps.core.session.Session;
import com.alps.core.user.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a reservation for a session, detailing the user, session, reserved seats,
 * and reservation status. This class manages expiration and cancellation logic for a reservation.
 */
@Getter
@EqualsAndHashCode
public class Reservation {

    private final String reservationId;
    private final User user;
    private final Session session;
    private final Set<LocationSeat> seats;
    private final LocalDateTime creationTime;
    private final Duration expirationTime;
    private final ReservationStatus status;
    private final LockProvider lockProvider;
    private final Price price;

    /**
     * Constructs a new Reservation instance with specified details. This constructor is private
     * and used internally by the factory method.
     *
     * @param reservationId  Unique identifier for the reservation.
     * @param user           The user who made the reservation.
     * @param session        The session for which the reservation is made.
     * @param seats          The set of seats reserved.
     * @param creationTime   The time at which the reservation was created.
     * @param expirationTime The duration after which the reservation expires.
     * @param status         The current status of the reservation.
     * @param lockProvider   A {@link LockProvider} for handling concurrency.
     * @param price          The {@link Price} object representing the total cost of the reservation.
     */
    private Reservation(
            @NonNull String reservationId,
            @NonNull User user,
            @NonNull Session session,
            @NonNull Set<LocationSeat> seats,
            @NonNull LocalDateTime creationTime,
            @NonNull Duration expirationTime,
            @NonNull ReservationStatus status,
            @NonNull LockProvider lockProvider,
            @NonNull Price price) {
        this.reservationId = reservationId;
        this.user = user;
        this.session = session;
        this.seats = seats;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.status = status;
        this.lockProvider = lockProvider;
        this.price = price;
    }

    /**
     * Factory method to create a new Reservation instance, automatically reserving the specified seats.
     *
     * @param reservationId  Unique identifier for the reservation.
     * @param user           The user making the reservation.
     * @param session        The session for which the reservation is made.
     * @param seats          The set of seats to be reserved.
     * @param creationTime   The time at which the reservation is created.
     * @param expirationTime The duration after which the reservation expires.
     * @param lockProvider   A {@link LockProvider} for concurrency management.
     * @param status         The initial status of the reservation, defaults to PENDING if null.
     * @param price          The total price of the reservation, including any applicable fees or discounts.
     * @return A new instance of Reservation with the specified details and reserved seats.
     */
    public static Reservation create(
            @NonNull String reservationId,
            @NonNull User user,
            @NonNull Session session,
            @NonNull Set<LocationSeat> seats,
            @NonNull LocalDateTime creationTime,
            @NonNull Duration expirationTime,
            @NonNull LockProvider lockProvider,
            ReservationStatus status,
            @NonNull Price price) {

        Set<LocationSeat> reservedSeats = seats.stream()
                .map(LocationSeat::reserve)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new Reservation(
                reservationId,
                user,
                session,
                reservedSeats,
                creationTime,
                expirationTime,
                status == null ? new ReservationStatus(StandardStatus.PENDING) : status,
                lockProvider,
                price);
    }

    /**
     * Provides an unmodifiable view of the seats associated with this reservation.
     *
     * @return An unmodifiable set of {@link LocationSeat}s.
     */
    public Set<LocationSeat> getSeats() {
        return Collections.unmodifiableSet(this.seats);
    }

    /**
     * Determines if the reservation is expired based on the current system time.
     *
     * @return True if the reservation is expired, otherwise false.
     */
    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(creationTime.plus(expirationTime));
    }

    /**
     * Attempts to cancel the reservation, setting its status to CANCELLED if currently PENDING.
     * If the reservation is not in a cancellable state, an exception is thrown.
     *
     * @return A new {@link Reservation} instance with updated status, if cancellation is successful.
     * @throws IllegalStateException if the reservation's status is not PENDING.
     */
    public Reservation cancel() {
        lockProvider.lock();
        try {
            if (status.getStandardStatus() == ReservationStatus.StandardStatus.PENDING) {

                Set<LocationSeat> locationsReleased = seats.stream()
                        .map(LocationSeat::release)
                        .collect(Collectors.toCollection(LinkedHashSet::new));

                return new Reservation(
                        this.reservationId,
                        this.user,
                        this.session,
                        locationsReleased,
                        this.creationTime,
                        this.expirationTime,
                        new ReservationStatus(ReservationStatus.StandardStatus.CANCELLED),
                        this.lockProvider,
                        this.price);
            } else {
                throw new IllegalStateException("The reservation cannot be canceled in its current state.");
            }
        } finally {
            lockProvider.unlock();
        }
    }
}
