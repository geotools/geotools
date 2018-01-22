
package org.geotools.data.epavic.schema;

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
@JsonPropertyOrder({ "AQIPollutantStandard", "CalcAQI", "CalcHealthCategory", "IncidentType", "MonitorId",
    "PresentationOrder", "TimeBasisId" })
public class MonitorTimeBasis {

  @JsonProperty("AQIPollutantStandard")
  private Integer aQIPollutantStandard;

  @JsonProperty("CalcAQI")
  private Boolean calcAQI;

  @JsonProperty("CalcHealthCategory")
  private Boolean calcHealthCategory;

  @JsonProperty("IncidentType")
  private String incidentType;

  @JsonProperty("MonitorId")
  private String monitorId;

  @JsonProperty("PresentationOrder")
  private Integer presentationOrder;

  @JsonProperty("TimeBasisId")
  private String timeBasisId;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("AQIPollutantStandard")
  public Integer getAQIPollutantStandard() {
    return aQIPollutantStandard;
  }

  @JsonProperty("AQIPollutantStandard")
  public void setAQIPollutantStandard(Integer aQIPollutantStandard) {
    this.aQIPollutantStandard = aQIPollutantStandard;
  }

  @JsonProperty("CalcAQI")
  public Boolean getCalcAQI() {
    return calcAQI;
  }

  @JsonProperty("CalcAQI")
  public void setCalcAQI(Boolean calcAQI) {
    this.calcAQI = calcAQI;
  }

  @JsonProperty("CalcHealthCategory")
  public Boolean getCalcHealthCategory() {
    return calcHealthCategory;
  }

  @JsonProperty("CalcHealthCategory")
  public void setCalcHealthCategory(Boolean calcHealthCategory) {
    this.calcHealthCategory = calcHealthCategory;
  }

  @JsonProperty("IncidentType")
  public String getIncidentType() {
    return incidentType;
  }

  @JsonProperty("IncidentType")
  public void setIncidentType(String incidentType) {
    this.incidentType = incidentType;
  }

  @JsonProperty("MonitorId")
  public String getMonitorId() {
    return monitorId;
  }

  @JsonProperty("MonitorId")
  public void setMonitorId(String monitorId) {
    this.monitorId = monitorId;
  }

  @JsonProperty("PresentationOrder")
  public Integer getPresentationOrder() {
    return presentationOrder;
  }

  @JsonProperty("PresentationOrder")
  public void setPresentationOrder(Integer presentationOrder) {
    this.presentationOrder = presentationOrder;
  }

  @JsonProperty("TimeBasisId")
  public String getTimeBasisId() {
    return timeBasisId;
  }

  @JsonProperty("TimeBasisId")
  public void setTimeBasisId(String timeBasisId) {
    this.timeBasisId = timeBasisId;
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
    return new HashCodeBuilder().append(aQIPollutantStandard).append(calcAQI).append(calcHealthCategory)
        .append(incidentType).append(monitorId).append(presentationOrder).append(timeBasisId)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof MonitorTimeBasis) == false) {
      return false;
    }
    MonitorTimeBasis rhs = ((MonitorTimeBasis) other);
    return new EqualsBuilder().append(aQIPollutantStandard, rhs.aQIPollutantStandard).append(calcAQI, rhs.calcAQI)
        .append(calcHealthCategory, rhs.calcHealthCategory).append(incidentType, rhs.incidentType)
        .append(monitorId, rhs.monitorId).append(presentationOrder, rhs.presentationOrder)
        .append(timeBasisId, rhs.timeBasisId).append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
