package com.alps.core.session;

import java.time.LocalDateTime;
import java.util.List;

import com.alps.core.location.Location;
import com.alps.core.location.LocationSeat;

import lombok.NonNull;

public class Session {
    private final String sessionId;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Location location;
    private final List<LocationSeat> seats;

    public Session(
            @NonNull String sessionId,
            @NonNull String name,
            @NonNull LocalDateTime startTime,
            @NonNull LocalDateTime endTime,
            @NonNull Location location,
            @NonNull List<LocationSeat> seats) {
        this.sessionId = sessionId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.seats = seats;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Location getLocation() {
        return location;
    }

    public List<LocationSeat> getSeats() {
        return seats;
    }
}
