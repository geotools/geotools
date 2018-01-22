
package org.geotools.data.epavic.schema;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Description", "IsRollingAverage", "MinDataPercent", "MonitorId", "MonitorName",
    "MonitorShortName", "MonitorTimeBasis", "RollingAveragePeriod", "SiteId", "TimeBaseId", "TimeBasisDescURL" })
public class TimeBasis {

  @JsonProperty("Description")
  private String description;

  @JsonProperty("IsRollingAverage")
  private Integer isRollingAverage;

  @JsonProperty("MinDataPercent")
  private Integer minDataPercent;

  @JsonProperty("MonitorId")
  private String monitorId;

  @JsonProperty("MonitorName")
  private String monitorName;

  @JsonProperty("MonitorShortName")
  private String monitorShortName;

  @JsonProperty("MonitorTimeBasis")
  private MonitorTimeBasis monitorTimeBasis;

  @JsonProperty("RollingAveragePeriod")
  private String rollingAveragePeriod;

  @JsonProperty("SiteId")
  private Integer siteId;

  @JsonProperty("TimeBaseId")
  private String timeBaseId;

  @JsonProperty("TimeBasisDescURL")
  private String timeBasisDescURL;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("Description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("Description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("IsRollingAverage")
  public Integer getIsRollingAverage() {
    return isRollingAverage;
  }

  @JsonProperty("IsRollingAverage")
  public void setIsRollingAverage(Integer isRollingAverage) {
    this.isRollingAverage = isRollingAverage;
  }

  @JsonProperty("MinDataPercent")
  public Integer getMinDataPercent() {
    return minDataPercent;
  }

  @JsonProperty("MinDataPercent")
  public void setMinDataPercent(Integer minDataPercent) {
    this.minDataPercent = minDataPercent;
  }

  @JsonProperty("MonitorId")
  public String getMonitorId() {
    return monitorId;
  }

  @JsonProperty("MonitorId")
  public void setMonitorId(String monitorId) {
    this.monitorId = monitorId;
  }

  @JsonProperty("MonitorName")
  public String getMonitorName() {
    return monitorName;
  }

  @JsonProperty("MonitorName")
  public void setMonitorName(String monitorName) {
    this.monitorName = monitorName;
  }

  @JsonProperty("MonitorShortName")
  public String getMonitorShortName() {
    return monitorShortName;
  }

  @JsonProperty("MonitorShortName")
  public void setMonitorShortName(String monitorShortName) {
    this.monitorShortName = monitorShortName;
  }

  @JsonProperty("MonitorTimeBasis")
  public MonitorTimeBasis getMonitorTimeBasis() {
    return monitorTimeBasis;
  }

  @JsonProperty("MonitorTimeBasis")
  public void setMonitorTimeBasis(MonitorTimeBasis monitorTimeBasis) {
    this.monitorTimeBasis = monitorTimeBasis;
  }

  @JsonProperty("RollingAveragePeriod")
  public String getRollingAveragePeriod() {
    return rollingAveragePeriod;
  }

  @JsonProperty("RollingAveragePeriod")
  public void setRollingAveragePeriod(String rollingAveragePeriod) {
    this.rollingAveragePeriod = rollingAveragePeriod;
  }

  @JsonProperty("SiteId")
  public Integer getSiteId() {
    return siteId;
  }

  @JsonProperty("SiteId")
  public void setSiteId(Integer siteId) {
    this.siteId = siteId;
  }

  @JsonProperty("TimeBaseId")
  public String getTimeBaseId() {
    return timeBaseId;
  }

  @JsonProperty("TimeBaseId")
  public void setTimeBaseId(String timeBaseId) {
    this.timeBaseId = timeBaseId;
  }

  @JsonProperty("TimeBasisDescURL")
  public String getTimeBasisDescURL() {
    return timeBasisDescURL;
  }

  @JsonProperty("TimeBasisDescURL")
  public void setTimeBasisDescURL(String timeBasisDescURL) {
    this.timeBasisDescURL = timeBasisDescURL;
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
    return new HashCodeBuilder().append(description).append(isRollingAverage).append(minDataPercent).append(monitorId)
        .append(monitorName).append(monitorShortName).append(monitorTimeBasis).append(rollingAveragePeriod)
        .append(siteId).append(timeBaseId).append(timeBasisDescURL).append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof TimeBasis) == false) {
      return false;
    }
    TimeBasis rhs = ((TimeBasis) other);
    return new EqualsBuilder().append(description, rhs.description).append(isRollingAverage, rhs.isRollingAverage)
        .append(minDataPercent, rhs.minDataPercent).append(monitorId, rhs.monitorId)
        .append(monitorName, rhs.monitorName).append(monitorShortName, rhs.monitorShortName)
        .append(monitorTimeBasis, rhs.monitorTimeBasis).append(rollingAveragePeriod, rhs.rollingAveragePeriod)
        .append(siteId, rhs.siteId).append(timeBaseId, rhs.timeBaseId).append(timeBasisDescURL, rhs.timeBasisDescURL)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
