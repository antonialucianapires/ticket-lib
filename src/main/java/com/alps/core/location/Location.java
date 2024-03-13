package com.alps.core.location;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a generic location with capabilities to be described through
 * basic properties such as name, address, capacity, and dynamically through
 * features.
 * This interface is designed to be extensible, allowing implementations to add
 * specific
 * characteristics unique to each location type.
 */
public interface Location {

    /**
     * Gets the name of the location.
     * 
     * @return The name of the location. Default is "Location Name".
     */
    default String getName() {
        return "Location Name";
    }

    /**
     * Gets the address of the location.
     * 
     * @return The address of the location. Default is "Location Address".
     */
    default String getAddress() {
        return "Location Address";
    }

    /**
     * Gets the capacity of the location.
     * 
     * @return The capacity of the location as an integer. Default capacity is 100.
     */
    default int getCapacity() {
        return 10;
    }

    /**
     * Gets a thread-safe map of features specific to the location.
     * This method allows for the addition of specific characteristics or amenities
     * unique to each Location implementation. By default, it returns an empty map,
     * enabling implementations to add their own specific features.
     * 
     * @return A thread-safe {@link Map} representing specific features of the
     *         location.
     *         The map is empty by default.
     */
    default Map<String, Object> getFeatures() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Gets a general description of the location.
     * 
     * @return A description of the location. Default description is "This is a
     *         generic location."
     */
    default String getDescription() {
        return "This is a generic location.";
    }
}