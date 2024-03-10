package com.alps.core.location;

import java.util.HashMap;
import java.util.Map;

public interface Location {
    default String getName() {
        return "Location Name";
    }

    default String getAddress() {
        return "Location Address";
    }

    default int getCapacity() {
        return 100;
    }

    default Map<String, Object> getFeatures() {
        Map<String, Object> features = new HashMap<>();
        features.put("Feature 1", "Value 1");
        features.put("Feature 2", "Value 2");
        return features;
    }

    default String getDescription() {
        return "This is a generic location.";
    }
}
