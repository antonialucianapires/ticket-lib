package com.alps.core.location;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class LocationTest {

    @Test
    void shouldCreateLocationWithValidDetails() {
        Location location = new SomeLocation();
        assertEquals("Some Address", location.getAddress());
        assertEquals(200, location.getCapacity());
        assertEquals("This is a specific location.", location.getDescription());
        assertEquals("Some Name", location.getName());
        assertEquals(3, location.getFeatures().size());
        assertEquals("Value 1", location.getFeatures().get("Feature 1"));
        assertEquals("Value 2", location.getFeatures().get("Feature 2"));
        assertEquals("Value 3", location.getFeatures().get("Feature 3"));
    }

    class SomeLocation implements Location {
        @Override
        public String getAddress() {
            return "Some Address";
        }

        @Override
        public int getCapacity() {
            return 200;
        }

        @Override
        public String getDescription() {
            return "This is a specific location.";
        }

        @Override
        public String getName() {
            return "Some Name";
        }

        @Override
        public Map<String, Object> getFeatures() {
            Map<String, Object> features = new HashMap<>();
            features.put("Feature 1", "Value 1");
            features.put("Feature 2", "Value 2");
            features.put("Feature 3", "Value 3");
            return features;
        }
    }

}
