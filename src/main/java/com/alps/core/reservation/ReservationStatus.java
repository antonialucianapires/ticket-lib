package com.alps.core.reservation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ReservationStatus {
    public enum StandardStatus {
        PENDING, CONFIRMED, CANCELLED, EXPIRED, COMPLETE, ACTIVE, INACTIVE
    }

    private StandardStatus standardStatus;
    private Map<String, String> customStatuses;

    public ReservationStatus(StandardStatus standardStatus) {
        this.standardStatus = standardStatus;
        this.customStatuses = new ConcurrentHashMap<>();
    }

    public void addCustomStatus(String key, String value) {
        customStatuses.put(key, value);
    }

    public void removeCustomStatus(String key) {
        customStatuses.remove(key);
    }

    public StandardStatus getStandardStatus() {
        return standardStatus;
    }

    public Map<String, String> getCustomStatuses() {
        return customStatuses;
    }
}
