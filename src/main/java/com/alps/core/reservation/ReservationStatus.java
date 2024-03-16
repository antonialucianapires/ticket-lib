package com.alps.core.reservation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the status of a reservation, including both standard and custom statuses.
 */
@lombok.EqualsAndHashCode
public class ReservationStatus {
    /**
     * Enumeration of standard statuses for reservations.
     */
    public enum StandardStatus {
        PENDING, CONFIRMED, CANCELLED, EXPIRED, COMPLETE, ACTIVE, INACTIVE
    }

    private StandardStatus standardStatus;
    private Map<String, String> customStatuses;

    /**
     * Constructs a ReservationStatus instance with an initial standard status.
     *
     * @param standardStatus The initial standard status of the reservation.
     */
    public ReservationStatus(StandardStatus standardStatus) {
        this.standardStatus = standardStatus;
        this.customStatuses = new ConcurrentHashMap<>();
    }

    /**
     * Adds a custom status to the reservation.
     *
     * @param key   The key of the custom status.
     * @param value The value of the custom status.
     */
    public void addCustomStatus(String key, String value) {
        customStatuses.put(key, value);
    }

    /**
     * Removes a custom status from the reservation.
     *
     * @param key The key of the custom status to be removed.
     */
    public void removeCustomStatus(String key) {
        customStatuses.remove(key);
    }

    /**
     * Returns the standard status of the reservation.
     *
     * @return The current standard status of the reservation.
     */
    public StandardStatus getStandardStatus() {
        return standardStatus;
    }

    /**
     * Returns the custom statuses of the reservation.
     *
     * @return A map of the custom statuses of the reservation.
     */
    public Map<String, String> getCustomStatuses() {
        return customStatuses;
    }

    /**
     * Returns a unified representation of all statuses of the reservation, including both standard and custom statuses.
     *
     * @return A map containing both the standard status and custom statuses of the reservation.
     */
    public Map<String, String> getAllStatuses() {
        Map<String, String> allStatuses = new ConcurrentHashMap<>();
        allStatuses.put("StandardStatus", standardStatus.toString());
        allStatuses.putAll(customStatuses);
        return allStatuses;
    }
}