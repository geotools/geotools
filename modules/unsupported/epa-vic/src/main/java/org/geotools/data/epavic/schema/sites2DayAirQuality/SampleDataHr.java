
package org.geotools.data.epavic.schema.sites2DayAirQuality;

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
@JsonPropertyOrder({ "AQI", "Colour", "DateTime", "DoNotDisplayHealthCategoryBar", "ForegroundColour",
    "HealthCategoryLevel", "PointId", "ResultFlag", "Value" })
public class SampleDataHr {

  @JsonProperty("AQI")
  private Integer aQI;

  @JsonProperty("Colour")
  private String colour;

  @JsonProperty("DateTime")
  private String dateTime;

  @JsonProperty("DoNotDisplayHealthCategoryBar")
  private Boolean doNotDisplayHealthCategoryBar;

  @JsonProperty("ForegroundColour")
  private String foregroundColour;

  @JsonProperty("HealthCategoryLevel")
  private Integer healthCategoryLevel;

  @JsonProperty("PointId")
  private Integer pointId;

  @JsonProperty("ResultFlag")
  private Integer resultFlag;

  @JsonProperty("Value")
  private String value;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("AQI")
  public Integer getAQI() {
    return aQI;
  }

  @JsonProperty("AQI")
  public void setAQI(Integer aQI) {
    this.aQI = aQI;
  }

  @JsonProperty("Colour")
  public String getColour() {
    return colour;
  }

  @JsonProperty("Colour")
  public void setColour(String colour) {
    this.colour = colour;
  }

  @JsonProperty("DateTime")
  public String getDateTime() {
    return dateTime;
  }

  @JsonProperty("DateTime")
  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  @JsonProperty("DoNotDisplayHealthCategoryBar")
  public Boolean getDoNotDisplayHealthCategoryBar() {
    return doNotDisplayHealthCategoryBar;
  }

  @JsonProperty("DoNotDisplayHealthCategoryBar")
  public void setDoNotDisplayHealthCategoryBar(Boolean doNotDisplayHealthCategoryBar) {
    this.doNotDisplayHealthCategoryBar = doNotDisplayHealthCategoryBar;
  }

  @JsonProperty("ForegroundColour")
  public String getForegroundColour() {
    return foregroundColour;
  }

  @JsonProperty("ForegroundColour")
  public void setForegroundColour(String foregroundColour) {
    this.foregroundColour = foregroundColour;
  }

  @JsonProperty("HealthCategoryLevel")
  public Integer getHealthCategoryLevel() {
    return healthCategoryLevel;
  }

  @JsonProperty("HealthCategoryLevel")
  public void setHealthCategoryLevel(Integer healthCategoryLevel) {
    this.healthCategoryLevel = healthCategoryLevel;
  }

  @JsonProperty("PointId")
  public Integer getPointId() {
    return pointId;
  }

  @JsonProperty("PointId")
  public void setPointId(Integer pointId) {
    this.pointId = pointId;
  }

  @JsonProperty("ResultFlag")
  public Integer getResultFlag() {
    return resultFlag;
  }

  @JsonProperty("ResultFlag")
  public void setResultFlag(Integer resultFlag) {
    this.resultFlag = resultFlag;
  }

  @JsonProperty("Value")
  public String getValue() {
    return value;
  }

  @JsonProperty("Value")
  public void setValue(String value) {
    this.value = value;
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
    return new HashCodeBuilder().append(aQI).append(colour).append(dateTime).append(doNotDisplayHealthCategoryBar)
        .append(foregroundColour).append(healthCategoryLevel).append(pointId).append(resultFlag).append(value)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof SampleDataHr) == false) {
      return false;
    }
    SampleDataHr rhs = ((SampleDataHr) other);
    return new EqualsBuilder().append(aQI, rhs.aQI).append(colour, rhs.colour).append(dateTime, rhs.dateTime)
        .append(doNotDisplayHealthCategoryBar, rhs.doNotDisplayHealthCategoryBar)
        .append(foregroundColour, rhs.foregroundColour).append(healthCategoryLevel, rhs.healthCategoryLevel)
        .append(pointId, rhs.pointId).append(resultFlag, rhs.resultFlag).append(value, rhs.value)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
