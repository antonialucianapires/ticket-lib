package com.alps.core.session;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import com.alps.core.location.Location;
import com.alps.core.location.LocationSeat;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class Session {
    private final String sessionId;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Location location;
    private final Set<LocationSeat> seats;

    private Session(
            @NonNull String sessionId,
            @NonNull String name,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime endTime,
            @NonNull Location location,
            @NonNull Set<LocationSeat> seats) {
        this.sessionId = sessionId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.seats = seats;
    }

    public static Session create(
            @NonNull String sessionId,
            @NonNull String name,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime endTime,
            @NonNull Location location,
            @NonNull Set<LocationSeat> seats) {

        return new Session(
                sessionId,
                name,
                startTime,
                endTime,
                location,
                seats);
    }

    public Set<LocationSeat> getSeats() {
        return Collections.unmodifiableSet(seats);
    }
}
