
package org.geotools.data.epavic.schema.sites2DayAirQuality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Aqi", "AqiCategory", "EmergencyUrl", "FireHazardCategory", "HasIncident", "HasPm25",
    "IncidentType", "IsADRStation", "IsStationOffline", "Latitude", "Longitude", "ParameterValueList", "PointId",
    "Region", "Station" })
public class Station {

  @JsonProperty("Aqi")
  private Aqi aqi;

  @JsonProperty("AqiCategory")
  private String aqiCategory;

  @JsonProperty("EmergencyUrl")
  private String emergencyUrl;

  @JsonProperty("FireHazardCategory")
  private Integer fireHazardCategory;

  @JsonProperty("HasIncident")
  private Boolean hasIncident;

  @JsonProperty("HasPm25")
  private Boolean hasPm25;

  @JsonProperty("IncidentType")
  private String incidentType;

  @JsonProperty("IsADRStation")
  private Boolean isADRStation;

  @JsonProperty("IsStationOffline")
  private Boolean isStationOffline;

  @JsonProperty("Latitude")
  private Double latitude;

  @JsonProperty("Longitude")
  private Double longitude;

  @JsonProperty("ParameterValueList")
  private List<ParameterValueList> parameterValueList = new ArrayList<ParameterValueList>();

  @JsonProperty("PointId")
  private Integer pointId;

  @JsonProperty("Region")
  private String region;

  @JsonProperty("Station")
  private String station;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("Aqi")
  public Aqi getAqi() {
    return aqi;
  }

  @JsonProperty("Aqi")
  public void setAqi(Aqi aqi) {
    this.aqi = aqi;
  }

  @JsonProperty("AqiCategory")
  public String getAqiCategory() {
    return aqiCategory;
  }

  @JsonProperty("AqiCategory")
  public void setAqiCategory(String aqiCategory) {
    this.aqiCategory = aqiCategory;
  }

  @JsonProperty("EmergencyUrl")
  public String getEmergencyUrl() {
    return emergencyUrl;
  }

  @JsonProperty("EmergencyUrl")
  public void setEmergencyUrl(String emergencyUrl) {
    this.emergencyUrl = emergencyUrl;
  }

  @JsonProperty("FireHazardCategory")
  public Integer getFireHazardCategory() {
    return fireHazardCategory;
  }

  @JsonProperty("FireHazardCategory")
  public void setFireHazardCategory(Integer fireHazardCategory) {
    this.fireHazardCategory = fireHazardCategory;
  }

  @JsonProperty("HasIncident")
  public Boolean getHasIncident() {
    return hasIncident;
  }

  @JsonProperty("HasIncident")
  public void setHasIncident(Boolean hasIncident) {
    this.hasIncident = hasIncident;
  }

  @JsonProperty("HasPm25")
  public Boolean getHasPm25() {
    return hasPm25;
  }

  @JsonProperty("HasPm25")
  public void setHasPm25(Boolean hasPm25) {
    this.hasPm25 = hasPm25;
  }

  @JsonProperty("IncidentType")
  public String getIncidentType() {
    return incidentType;
  }

  @JsonProperty("IncidentType")
  public void setIncidentType(String incidentType) {
    this.incidentType = incidentType;
  }

  @JsonProperty("IsADRStation")
  public Boolean getIsADRStation() {
    return isADRStation;
  }

  @JsonProperty("IsADRStation")
  public void setIsADRStation(Boolean isADRStation) {
    this.isADRStation = isADRStation;
  }

  @JsonProperty("IsStationOffline")
  public Boolean getIsStationOffline() {
    return isStationOffline;
  }

  @JsonProperty("IsStationOffline")
  public void setIsStationOffline(Boolean isStationOffline) {
    this.isStationOffline = isStationOffline;
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

  @JsonProperty("ParameterValueList")
  public List<ParameterValueList> getParameterValueList() {
    return parameterValueList;
  }

  @JsonProperty("ParameterValueList")
  public void setParameterValueList(List<ParameterValueList> parameterValueList) {
    this.parameterValueList = parameterValueList;
  }

  @JsonProperty("PointId")
  public Integer getPointId() {
    return pointId;
  }

  @JsonProperty("PointId")
  public void setPointId(Integer pointId) {
    this.pointId = pointId;
  }

  @JsonProperty("Region")
  public String getRegion() {
    return region;
  }

  @JsonProperty("Region")
  public void setRegion(String region) {
    this.region = region;
  }

  @JsonProperty("Station")
  public String getStation() {
    return station;
  }

  @JsonProperty("Station")
  public void setStation(String station) {
    this.station = station;
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
    return new HashCodeBuilder().append(aqi).append(aqiCategory).append(emergencyUrl).append(fireHazardCategory)
        .append(hasIncident).append(hasPm25).append(incidentType).append(isADRStation).append(isStationOffline)
        .append(latitude).append(longitude).append(parameterValueList).append(pointId).append(region).append(station)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Station) == false) {
      return false;
    }
    Station rhs = ((Station) other);
    return new EqualsBuilder().append(aqi, rhs.aqi).append(aqiCategory, rhs.aqiCategory)
        .append(emergencyUrl, rhs.emergencyUrl).append(fireHazardCategory, rhs.fireHazardCategory)
        .append(hasIncident, rhs.hasIncident).append(hasPm25, rhs.hasPm25).append(incidentType, rhs.incidentType)
        .append(isADRStation, rhs.isADRStation).append(isStationOffline, rhs.isStationOffline)
        .append(latitude, rhs.latitude).append(longitude, rhs.longitude)
        .append(parameterValueList, rhs.parameterValueList).append(pointId, rhs.pointId).append(region, rhs.region)
        .append(station, rhs.station).append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
