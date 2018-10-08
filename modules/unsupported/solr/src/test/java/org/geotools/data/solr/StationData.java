/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.common.SolrInputDocument;

/** Station data model, for load from XML */
@XmlRootElement
public class StationData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String measurementName;
    private String measurementTime;
    private String measurementUnit;
    private Integer measurementValue;
    private String stationCode;
    private Integer stationId;
    private String stationLocation;
    private String stationName;
    private List<String> stationTag = new ArrayList<>();
    private List<String> stationComment = new ArrayList<>();

    public StationData() {}

    public StationData(
            String measurementName,
            String measurementTime,
            String measurementUnit,
            Integer measurementValue,
            String stationCode,
            Integer stationId,
            String stationLocation,
            String stationName,
            List<String> stationTag,
            List<String> station_comment) {
        super();
        this.measurementName = measurementName;
        this.measurementTime = measurementTime;
        this.measurementUnit = measurementUnit;
        this.measurementValue = measurementValue;
        this.stationCode = stationCode;
        this.stationId = stationId;
        this.stationLocation = stationLocation;
        this.stationName = stationName;
        this.stationTag = stationTag;
        this.stationComment = station_comment;
    }

    public SolrInputDocument toSolrDoc() {
        SolrInputDocument result = new SolrInputDocument();
        result.addField("measurement_name", this.measurementName);
        result.addField("measurement_time", this.measurementTime);
        result.addField("measurement_unit", this.measurementUnit);
        result.addField("measurement_value", this.measurementValue);
        result.addField("station_code", this.stationCode);
        result.addField("station_id", this.stationId);
        result.addField("station_location", this.stationLocation);
        result.addField("station_name", this.stationName);
        if (this.stationTag != null) {
            for (String str1 : this.stationTag) {
                result.addField("station_tag", str1);
            }
        }
        if (this.stationComment != null) {
            for (String str1 : this.stationComment) {
                result.addField("station_comment", str1);
            }
        }

        return result;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Integer getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Integer measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getStationLocation() {
        return stationLocation;
    }

    public void setStationLocation(String stationLocation) {
        this.stationLocation = stationLocation;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public List<String> getStationTag() {
        return stationTag;
    }

    public void setStationTag(List<String> stationTag) {
        this.stationTag = stationTag;
    }

    public List<String> getStationComment() {
        return stationComment;
    }

    public void setStationComment(List<String> stationComment) {
        this.stationComment = stationComment;
    }

    @XmlRootElement
    public static class Stations implements Serializable {
        private static final long serialVersionUID = 1L;
        private List<StationData> stations = new ArrayList<>();

        public Stations() {}

        public List<StationData> getStations() {
            return stations;
        }

        public void setStations(List<StationData> stations) {
            this.stations = stations;
        }
    }
}
