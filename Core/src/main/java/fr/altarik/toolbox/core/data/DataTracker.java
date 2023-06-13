package fr.altarik.toolbox.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataTracker {

    private final Map<TrackedData, String> trackedData;

    public DataTracker() {
        this.trackedData = new HashMap<>();
    }

    public void startTracking(TrackedData data) {
        trackedData.put(data, data.defaultValue());
    }

    public String getOrDefault(TrackedData data) {
        return Objects.requireNonNull(trackedData.get(data));
    }

    public void set(TrackedData data, String value) {
        String v = trackedData.get(data);
        if(v != null) {
            trackedData.putIfAbsent(data, value);
        } else {
            throw new IllegalArgumentException("Data " + data.name() + " is not tracked, please initialize it with DataTracker#startTracking(TrackedData, String) first");
        }

    }

    public void saveToDb() {

    }

}