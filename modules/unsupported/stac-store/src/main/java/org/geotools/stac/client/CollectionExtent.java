/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.stac.client;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;
import java.util.List;

/** Extent details (including spatial and temporal). */
@JsonPropertyOrder({"spatial", "temporal"})
public class CollectionExtent extends AnyJSONObject {

    SpatialExtents spatial;
    TemporalExtents temporal;

    /** Spatial extent bboxs, each bbox xmin,ymin,xmax,ymax crs CRS84. */
    public static class SpatialExtents extends AnyJSONObject {

        List<List<Double>> bbox;
        String crs;

        public List<List<Double>> getBbox() {
            return bbox;
        }

        public void setBbox(List<List<Double>> bbox) {
            this.bbox = bbox;
        }

        public String getCrs() {
            return crs;
        }

        public void setCrs(String crs) {
            this.crs = crs;
        }
    }

    /** Temporal extent intervals, each interval between two UTC times. */
    public static class TemporalExtents extends AnyJSONObject {

        List<List<Date>> interval;
        String trs;

        public List<List<Date>> getInterval() {
            return interval;
        }

        public void setInterval(List<List<Date>> interval) {
            this.interval = interval;
        }

        public String getTrs() {
            return trs;
        }

        public void setTrs(String trs) {
            this.trs = trs;
        }
    }

    public CollectionExtent() {
        // for Jackson
    }

    public SpatialExtents getSpatial() {
        return spatial;
    }

    public void setSpatial(SpatialExtents spatial) {
        this.spatial = spatial;
    }

    public TemporalExtents getTemporal() {
        return temporal;
    }

    public void setTemporal(TemporalExtents temporal) {
        this.temporal = temporal;
    }
}
