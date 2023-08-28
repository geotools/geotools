package org.geotools.api.style;

public interface OverlapBehavior {
    String AVERAGE_RESCTRICTION = "AVERAGE";
    String RANDOM_RESCTRICTION = "RANDOM";
    String LATEST_ON_TOP_RESCTRICTION = "LATEST_ON_TOP";
    String EARLIEST_ON_TOP_RESCTRICTION = "EARLIEST_ON_TOP";
    String UNSPECIFIED_RESCTRICTION = "UNSPECIFIED";

    void accept(StyleVisitor visitor);
}
