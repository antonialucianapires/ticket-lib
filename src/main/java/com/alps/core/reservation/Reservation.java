package com.alps.core.reservation;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.alps.core.clock.SystemClock;
import com.alps.core.location.LocationSeat;
import com.alps.core.lock.LockProvider;
import com.alps.core.reservation.ReservationStatus.StandardStatus;
import com.alps.core.session.Session;
import com.alps.core.user.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(exclude = "systemClock")
public class Reservation {

    private final String reservationId;
    private final User user;
    private final Session session;
    private final Set<LocationSeat> seats;
    private final LocalDateTime creationTime;
    private final Duration expirationTime;
    private final ReservationStatus status;
    private final LockProvider lockProvider;
    private final SystemClock systemClock;

    private Reservation(
            @NonNull String reservationId,
            @NonNull User user,
            @NonNull Session session,
            @NonNull Set<LocationSeat> seats,
            @NonNull LocalDateTime creationTime,
            @NonNull Duration expirationTime,
            @NonNull ReservationStatus status,
            @NonNull LockProvider lockProvider) {
        this.reservationId = reservationId;
        this.user = user;
        this.session = session;
        this.seats = seats;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.status = status == null ? new ReservationStatus(StandardStatus.PENDING) : status;
        this.lockProvider = lockProvider;
        this.systemClock = new SystemClock(lockProvider);

    }

    private Reservation(
            @NonNull String reservationId,
            @NonNull User user,
            @NonNull Session session,
            @NonNull Set<LocationSeat> seats,
            @NonNull LocalDateTime creationTime,
            @NonNull Duration expirationTime,
            @NonNull ReservationStatus status) {

        this.reservationId = reservationId;
        this.user = user;
        this.session = session;
        this.seats = seats;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.status = status;
        this.lockProvider = null;
        this.systemClock = new SystemClock(lockProvider);
    }

    public static Reservation create(
            @NonNull String reservationId,
            @NonNull User user,
            @NonNull Session session,
            @NonNull Set<LocationSeat> seats,
            @NonNull LocalDateTime creationTime,
            @NonNull Duration expirationTime,
            @NonNull LockProvider lockProvider,
            ReservationStatus status) {

        Set<LocationSeat> reservedSeats = seats.stream()
                .map(LocationSeat::reserveSeat)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new Reservation(
                reservationId,
                user,
                session,
                reservedSeats,
                creationTime,
                expirationTime,
                status == null ? new ReservationStatus(StandardStatus.PENDING) : status,
                lockProvider);
    }

    public Set<LocationSeat> getSeats() {
        return Collections.unmodifiableSet(this.seats);
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(systemClock.currentTimeMillis()),
                ZoneId.systemDefault());

        return now.isAfter(creationTime.plus(expirationTime));
    }

    public Reservation cancel() {
        lockProvider.lock();
        try {
            if (status.getStandardStatus() == ReservationStatus.StandardStatus.PENDING) {

                Set<LocationSeat> locationsReleased = seats.stream()
                        .map(LocationSeat::releaseSeat)
                        .collect(Collectors.toCollection(LinkedHashSet::new));

                return new Reservation(
                        this.reservationId,
                        this.user,
                        this.session,
                        locationsReleased,
                        this.creationTime,
                        this.expirationTime,
                        new ReservationStatus(ReservationStatus.StandardStatus.CANCELLED));
            } else {
                throw new IllegalStateException("The reservation cannot be canceled in its current state.");
            }
        } finally {
            lockProvider.unlock();
        }
    }

}
