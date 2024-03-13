package com.alps.core.location;

import com.alps.core.lock.LockProvider;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a seat within a specific location, providing details such as
 * seat identification, description, availability, and associated location.
 * This class also handles seat reservation and release operations, utilizing
 * a {@link LockProvider} for thread-safe modifications.
 */
@Getter
@EqualsAndHashCode
public class LocationSeat {
    private final String seatId;
    private final String description;
    private final boolean isAvailable;
    private final Location location;
    private final LockProvider lockProvider;

    /**
     * Constructs a new LocationSeat instance with the given parameters.
     * 
     * @param seatId       The unique identifier for the seat.
     * @param description  A textual description of the seat.
     * @param location     The {@link Location} the seat belongs to.
     * @param lockProvider A {@link LockProvider} for handling concurrent
     *                     modifications.
     * @param isAvailable  The availability status of the seat. True if available,
     *                     false otherwise.
     */
    private LocationSeat(
            @NonNull String seatId,
            @NonNull String description,
            @NonNull Location location,
            @NonNull LockProvider lockProvider,
            boolean isAvailable) {

        this.seatId = seatId;
        this.description = description;
        this.isAvailable = isAvailable;
        this.location = location;
        this.lockProvider = lockProvider;
    }

    /**
     * Factory method to create a new instance of LocationSeat.
     * 
     * @param seatId       The unique identifier for the seat.
     * @param description  A textual description of the seat.
     * @param location     The {@link Location} the seat belongs to.
     * @param lockProvider A {@link LockProvider} for handling concurrent
     *                     modifications.
     * @param isAvailable  The availability status of the seat.
     * @return A new instance of LocationSeat with the specified properties.
     */
    public static LocationSeat create(
            @NonNull String seatId,
            @NonNull String description,
            @NonNull Location location,
            @NonNull LockProvider lockProvider,
            boolean isAvailable) {
        return new LocationSeat(
                seatId,
                description,
                location,
                lockProvider,
                isAvailable);
    }

    /**
     * Reserves the seat if it is available, making it unavailable.
     * 
     * @return A new LocationSeat instance representing the reserved seat.
     * @throws IllegalStateException if the seat is already reserved.
     */
    public LocationSeat reserve() {
        lockProvider.lock();
        try {
            if (isAvailable) {
                return new LocationSeat(
                        seatId,
                        description,
                        location,
                        lockProvider,
                        false);
            } else {
                throw new IllegalStateException("Seat is already reserved.");
            }
        } finally {
            lockProvider.unlock();
        }
    }

    /**
     * Releases the seat if it is reserved, making it available again.
     * 
     * @return A new LocationSeat instance representing the available seat.
     * @throws IllegalStateException if the seat is already available.
     */
    public LocationSeat release() {
        lockProvider.lock();
        try {
            if (!isAvailable) {
                return new LocationSeat(
                        seatId,
                        description,
                        location,
                        lockProvider,
                        true);
            } else {
                throw new IllegalStateException("Seat is already available.");
            }
        } finally {
            lockProvider.unlock();
        }
    }
}