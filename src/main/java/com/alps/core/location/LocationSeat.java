package com.alps.core.location;

import com.alps.core.lock.LockProvider;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class LocationSeat {
    private final String seatId;
    private final String description;
    private final boolean isAvailable;
    private final Location location;
    private final LockProvider lockProvider;

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

    public LocationSeat reserveSeat() {
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

    public LocationSeat releaseSeat() {
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