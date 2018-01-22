
package org.geotools.data.epavic.schema.sitesHourlyAirQuality;

import java.util.HashMap;
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
@JsonPropertyOrder({ "CommonName", "DescriptionURL", "HasMeasurement", "MonitorId", "Precision", "ShortName",
    "TimeBasis", "TimeBasisID", "UnitOfMeasure" })
public class IncidentMonitor {

  @JsonProperty("CommonName")
  private String commonName;

  @JsonProperty("DescriptionURL")
  private String descriptionURL;

  @JsonProperty("HasMeasurement")
  private Boolean hasMeasurement;

  @JsonProperty("MonitorId")
  private String monitorId;

  @JsonProperty("Precision")
  private Integer precision;

  @JsonProperty("ShortName")
  private String shortName;

  @JsonProperty("TimeBasis")
  private String timeBasis;

  @JsonProperty("TimeBasisID")
  private String timeBasisID;

  @JsonProperty("UnitOfMeasure")
  private String unitOfMeasure;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("CommonName")
  public String getCommonName() {
    return commonName;
  }

  @JsonProperty("CommonName")
  public void setCommonName(String commonName) {
    this.commonName = commonName;
  }

  @JsonProperty("DescriptionURL")
  public String getDescriptionURL() {
    return descriptionURL;
  }

  @JsonProperty("DescriptionURL")
  public void setDescriptionURL(String descriptionURL) {
    this.descriptionURL = descriptionURL;
  }

  @JsonProperty("HasMeasurement")
  public Boolean getHasMeasurement() {
    return hasMeasurement;
  }

  @JsonProperty("HasMeasurement")
  public void setHasMeasurement(Boolean hasMeasurement) {
    this.hasMeasurement = hasMeasurement;
  }

  @JsonProperty("MonitorId")
  public String getMonitorId() {
    return monitorId;
  }

  @JsonProperty("MonitorId")
  public void setMonitorId(String monitorId) {
    this.monitorId = monitorId;
  }

  @JsonProperty("Precision")
  public Integer getPrecision() {
    return precision;
  }

  @JsonProperty("Precision")
  public void setPrecision(Integer precision) {
    this.precision = precision;
  }

  @JsonProperty("ShortName")
  public String getShortName() {
    return shortName;
  }

  @JsonProperty("ShortName")
  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  @JsonProperty("TimeBasis")
  public String getTimeBasis() {
    return timeBasis;
  }

  @JsonProperty("TimeBasis")
  public void setTimeBasis(String timeBasis) {
    this.timeBasis = timeBasis;
  }

  @JsonProperty("TimeBasisID")
  public String getTimeBasisID() {
    return timeBasisID;
  }

  @JsonProperty("TimeBasisID")
  public void setTimeBasisID(String timeBasisID) {
    this.timeBasisID = timeBasisID;
  }

  @JsonProperty("UnitOfMeasure")
  public String getUnitOfMeasure() {
    return unitOfMeasure;
  }

  @JsonProperty("UnitOfMeasure")
  public void setUnitOfMeasure(String unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
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
    return new HashCodeBuilder().append(commonName).append(descriptionURL).append(hasMeasurement).append(monitorId)
        .append(precision).append(shortName).append(timeBasis).append(timeBasisID).append(unitOfMeasure)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof IncidentMonitor) == false) {
      return false;
    }
    IncidentMonitor rhs = ((IncidentMonitor) other);
    return new EqualsBuilder().append(commonName, rhs.commonName).append(descriptionURL, rhs.descriptionURL)
        .append(hasMeasurement, rhs.hasMeasurement).append(monitorId, rhs.monitorId).append(precision, rhs.precision)
        .append(shortName, rhs.shortName).append(timeBasis, rhs.timeBasis).append(timeBasisID, rhs.timeBasisID)
        .append(unitOfMeasure, rhs.unitOfMeasure).append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
