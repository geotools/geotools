/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

public interface OverlapBehavior {
    String AVERAGE_RESCTRICTION = "AVERAGE";
    String RANDOM_RESCTRICTION = "RANDOM";
    String LATEST_ON_TOP_RESCTRICTION = "LATEST_ON_TOP";
    String EARLIEST_ON_TOP_RESCTRICTION = "EARLIEST_ON_TOP";
    String UNSPECIFIED_RESCTRICTION = "UNSPECIFIED";

    void accept(StyleVisitor visitor);
}
