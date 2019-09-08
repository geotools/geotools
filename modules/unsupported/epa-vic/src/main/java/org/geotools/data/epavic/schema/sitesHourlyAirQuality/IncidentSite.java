package org.geotools.data.epavic.schema.sitesHourlyAirQuality;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "EmergencyUrl",
    "IncidentIcon",
    "Latitude",
    "Longitude",
    "Measurements",
    "Name",
    "Region",
    "SiteId",
    "SiteType"
})
public class IncidentSite {

    @JsonProperty("EmergencyUrl")
    private String emergencyUrl;

    @JsonProperty("IncidentIcon")
    private String incidentIcon;

    @JsonProperty("Latitude")
    private Double latitude;

    @JsonProperty("Longitude")
    private Double longitude;

    @JsonProperty("Measurements")
    private List<Measurement> measurements = new ArrayList<Measurement>();

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Region")
    private String region;

    @JsonProperty("SiteId")
    private Integer siteId;

    @JsonProperty("SiteType")
    private String siteType;

    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("EmergencyUrl")
    public String getEmergencyUrl() {
        return emergencyUrl;
    }

    @JsonProperty("EmergencyUrl")
    public void setEmergencyUrl(String emergencyUrl) {
        this.emergencyUrl = emergencyUrl;
    }

    @JsonProperty("IncidentIcon")
    public String getIncidentIcon() {
        return incidentIcon;
    }

    @JsonProperty("IncidentIcon")
    public void setIncidentIcon(String incidentIcon) {
        this.incidentIcon = incidentIcon;
    }

    @JsonProperty("Latitude")
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty("Latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("Longitude")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("Longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("Measurements")
    public List<Measurement> getMeasurements() {
        return measurements;
    }

    @JsonProperty("Measurements")
    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("Region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("SiteId")
    public Integer getSiteId() {
        return siteId;
    }

    @JsonProperty("SiteId")
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    @JsonProperty("SiteType")
    public String getSiteType() {
        return siteType;
    }

    @JsonProperty("SiteType")
    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(emergencyUrl)
                .append(incidentIcon)
                .append(latitude)
                .append(longitude)
                .append(measurements)
                .append(name)
                .append(region)
                .append(siteId)
                .append(siteType)
                .append(additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof IncidentSite) == false) {
            return false;
        }
        IncidentSite rhs = ((IncidentSite) other);
        return new EqualsBuilder()
                .append(emergencyUrl, rhs.emergencyUrl)
                .append(incidentIcon, rhs.incidentIcon)
                .append(latitude, rhs.latitude)
                .append(longitude, rhs.longitude)
                .append(measurements, rhs.measurements)
                .append(name, rhs.name)
                .append(region, rhs.region)
                .append(siteId, rhs.siteId)
                .append(siteType, rhs.siteType)
                .append(additionalProperties, rhs.additionalProperties)
                .isEquals();
    }
}
