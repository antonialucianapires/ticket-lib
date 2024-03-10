package com.alps.core.location;

import com.alps.core.lock.LockProvider;

import lombok.NonNull;

public class LocationSeat {
    private final String seatId;
    private final String description;
    private boolean isAvailable;
    private final Location location;
    private final LockProvider lockProvider;

    public LocationSeat(
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

    public String getSeatId() {
        return seatId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Location getLocation() {
        return location;
    }

    public void reserveSeat() {
        lockProvider.lock();
        try {
            if (isAvailable) {
                isAvailable = false;
            } else {
                throw new IllegalStateException("Seat is already reserved.");
            }
        } finally {
            lockProvider.unlock();

        }
    }

    public void releaseSeat() {
        lockProvider.lock();
        try {
            if (!isAvailable) {
                isAvailable = true;
            } else {
                throw new IllegalStateException("Seat is already available.");
            }
        } finally {
            lockProvider.unlock();
        }
    }
}