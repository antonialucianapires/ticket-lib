package com.alps.core.session;

import com.alps.core.location.Location;
import com.alps.core.location.LocationSeat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a scheduled session that occurs at a specific location and within a specified time frame.
 * Each session is unique and identified by a session ID, and contains details such as the session's name,
 * start and end times, location, and a set of seats. This class is used to determine its active state based on the current time.
 */
@Getter
@EqualsAndHashCode
public class Session {
    private final String sessionId;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Location location;
    private final Set<LocationSeat> seats;

    /**
     * Constructs a new Session instance with specified details.
     * 
     * @param sessionId   Unique identifier for the session.
     * @param name        Name of the session.
     * @param startTime   Start time of the session.
     * @param endTime     End time of the session.
     * @param location    The location where the session is held.
     * @param seats       A set of {@link LocationSeat} representing the seats available for the session.
     */
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

    /**
     * Factory method to create and return a new Session instance.
     * 
     * @param sessionId   Unique identifier for the session.
     * @param name        Name of the session.
     * @param startTime   Start time of the session.
     * @param endTime     End time of the session.
     * @param location    The location where the session is held.
     * @param seats       A set of {@link LocationSeat} representing the seats available for the session.
     * @return A new Session instance.
     */
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

    /**
     * Determines whether the session is currently active based on the current system time.
     * 
     * @return {@code true} if the current time is after the session's start time and before its end time, otherwise {@code false}.
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
    }

    /**
     * Checks if there are any available seats for this session.
     * 
     * @return {@code true} if at least one seat is available, otherwise {@code false}.
     */
    public boolean hasAvailableSeats() {
        return seats.stream().anyMatch(LocationSeat::isAvailable);
    }

    /**
     * Checks if a specific seat is available for this session.
     * 
     * @param seatId The ID of the seat to check.
     * @return {@code true} if the specified seat is available, otherwise {@code false}.
     */
    public boolean isSeatAvailable(String seatId) {
        return seats.stream()
                    .filter(seat -> seat.getSeatId().equals(seatId))
                    .findFirst()
                    .map(LocationSeat::isAvailable)
                    .orElse(false);
    }
}
