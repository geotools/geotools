package org.geotools.data.epavic.schema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    Site.FIRE_HAZARD_CATEGORY,
    Site.HAS_INCIDENT,
    Site.INCIDENT_SITE,
    Site.INCIDENT_TYPE,
    Site.IS_STATION_OFFLINE,
    Site.LAT,
    Site.LONG,
    Site.NAME,
    Site.SITE_ID,
    Site.SITE_LIST
})
public class Site {

    public static final String FIRE_HAZARD_CATEGORY = "FireHazardCategory";

    public static final String HAS_INCIDENT = "HasIncident";

    public static final String NAME = "Name";

    public static final String LONG = "Longitude";

    public static final String LAT = "Latitude";

    public static final String IS_STATION_OFFLINE = "IsStationOffline";

    public static final String INCIDENT_TYPE = "IncidentType";

    public static final String INCIDENT_SITE = "IncidentSite";

    public static final String SITE_ID = "SiteId";

    public static final String SITE_LIST = "SiteList";

    @JsonProperty(FIRE_HAZARD_CATEGORY)
    private Integer fireHazardCategory;

    @JsonProperty(HAS_INCIDENT)
    private Boolean hasIncident;

    @JsonProperty(INCIDENT_SITE)
    private IncidentSite incidentSite;

    @JsonProperty(INCIDENT_TYPE)
    private String incidentType;

    @JsonProperty(IS_STATION_OFFLINE)
    private Boolean isStationOffline;

    @JsonProperty(LAT)
    private Double latitude;

    @JsonProperty(LONG)
    private Double longitude;

    @JsonProperty(NAME)
    private String name;

    @JsonProperty(SITE_ID)
    private Integer siteId;

    @JsonProperty(SITE_LIST)
    private SiteList siteList;

    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty(FIRE_HAZARD_CATEGORY)
    public Integer getFireHazardCategory() {
        return fireHazardCategory;
    }

    @JsonProperty(FIRE_HAZARD_CATEGORY)
    public void setFireHazardCategory(Integer fireHazardCategory) {
        this.fireHazardCategory = fireHazardCategory;
    }

    @JsonProperty(HAS_INCIDENT)
    public Boolean getHasIncident() {
        return hasIncident;
    }

    @JsonProperty(HAS_INCIDENT)
    public void setHasIncident(Boolean hasIncident) {
        this.hasIncident = hasIncident;
    }

    @JsonProperty(INCIDENT_SITE)
    public IncidentSite getIncidentSite() {
        return incidentSite;
    }

    @JsonProperty(INCIDENT_SITE)
    public void setIncidentSite(IncidentSite incidentSite) {
        this.incidentSite = incidentSite;
    }

    @JsonProperty(INCIDENT_TYPE)
    public String getIncidentType() {
        return incidentType;
    }

    @JsonProperty(INCIDENT_TYPE)
    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    @JsonProperty(IS_STATION_OFFLINE)
    public Boolean getIsStationOffline() {
        return isStationOffline;
    }

    @JsonProperty(IS_STATION_OFFLINE)
    public void setIsStationOffline(Boolean isStationOffline) {
        this.isStationOffline = isStationOffline;
    }

    @JsonProperty(LAT)
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty(LAT)
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty(LONG)
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty(LONG)
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty(NAME)
    public String getName() {
        return name;
    }

    @JsonProperty(NAME)
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(SITE_ID)
    public Integer getSiteId() {
        return siteId;
    }

    @JsonProperty(SITE_ID)
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    @JsonProperty(SITE_LIST)
    public SiteList getSiteList() {
        return siteList;
    }

    @JsonProperty(SITE_LIST)
    public void setSiteList(SiteList siteList) {
        this.siteList = siteList;
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
                .append(fireHazardCategory)
                .append(hasIncident)
                .append(incidentSite)
                .append(incidentType)
                .append(isStationOffline)
                .append(latitude)
                .append(longitude)
                .append(name)
                .append(siteId)
                .append(siteList)
                .append(additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Site) == false) {
            return false;
        }
        Site rhs = ((Site) other);
        return new EqualsBuilder()
                .append(fireHazardCategory, rhs.fireHazardCategory)
                .append(hasIncident, rhs.hasIncident)
                .append(incidentSite, rhs.incidentSite)
                .append(incidentType, rhs.incidentType)
                .append(isStationOffline, rhs.isStationOffline)
                .append(latitude, rhs.latitude)
                .append(longitude, rhs.longitude)
                .append(name, rhs.name)
                .append(siteId, rhs.siteId)
                .append(siteList, rhs.siteList)
                .append(additionalProperties, rhs.additionalProperties)
                .isEquals();
    }
}
