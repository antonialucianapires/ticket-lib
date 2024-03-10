package com.alps.core.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationStatusTest {
    
    ReservationStatus status;

    @BeforeEach
    void setup() {
        status = new ReservationStatus(ReservationStatus.StandardStatus.PENDING);
    }

    @Test
    void shouldCreateReservationStatusWithPendingStatus() {
        assertEquals(ReservationStatus.StandardStatus.PENDING, status.getStandardStatus());
    }

    @Test
    void shouldAddCustomStatus() {
        status.addCustomStatus("customKey", "customValue");
        assertTrue(status.getCustomStatuses().containsKey("customKey"));
        assertEquals("customValue", status.getCustomStatuses().get("customKey"));
    }

    @Test
    void shouldRemoveCustomStatus() {
        status.addCustomStatus("customKey", "customValue");
        status.removeCustomStatus("customKey");
        assertFalse(status.getCustomStatuses().containsKey("customKey"));
    }

    @Test
    void shouldNotAddCustomStatusWithNullKey() {
        assertThrows(NullPointerException.class, () -> {
            status.addCustomStatus(null, "customValue");
        });
    }

    @Test
    void shouldNotAddCustomStatusWithNullValue() {
        assertThrows(NullPointerException.class, () -> {
            status.addCustomStatus("customKey", null);
        });
    }

    @Test
    void shouldNotRemoveCustomStatusWithNullKey() {
        assertThrows(NullPointerException.class, () -> {
            status.removeCustomStatus(null);
        });
    }
}
