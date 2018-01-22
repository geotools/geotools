
package org.geotools.data.epavic.schema.sitesHourlyAirQuality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@JsonPropertyOrder({ "DateTime", "IncidentMonitors", "IncidentSites", "NonIncidentMonitors", "NonIncidentSites",
    "ServiceName" })
public class SitesHourlyAirQuality {

  @JsonProperty("DateTime")
  private String dateTime;

  @JsonProperty("IncidentMonitors")
  private List<IncidentMonitor> incidentMonitors = new ArrayList<IncidentMonitor>();

  @JsonProperty("IncidentSites")
  private List<IncidentSite> incidentSites = new ArrayList<IncidentSite>();

  @JsonProperty("NonIncidentMonitors")
  private List<IncidentMonitor> nonIncidentMonitors = new ArrayList<IncidentMonitor>();

  @JsonProperty("NonIncidentSites")
  private List<IncidentSite> nonIncidentSites = new ArrayList<IncidentSite>();

  @JsonProperty("ServiceName")
  private String serviceName;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("DateTime")
  public String getDateTime() {
    return dateTime;
  }

  @JsonProperty("DateTime")
  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  @JsonProperty("IncidentMonitors")
  public List<IncidentMonitor> getIncidentMonitors() {
    return incidentMonitors;
  }

  @JsonProperty("IncidentMonitors")
  public void setIncidentMonitors(List<IncidentMonitor> incidentMonitors) {
    this.incidentMonitors = incidentMonitors;
  }

  @JsonProperty("IncidentSites")
  public List<IncidentSite> getIncidentSites() {
    return incidentSites;
  }

  @JsonProperty("IncidentSites")
  public void setIncidentSites(List<IncidentSite> incidentSites) {
    this.incidentSites = incidentSites;
  }

  @JsonProperty("NonIncidentMonitors")
  public List<IncidentMonitor> getNonIncidentMonitors() {
    return nonIncidentMonitors;
  }

  @JsonProperty("NonIncidentMonitors")
  public void setNonIncidentMonitors(List<IncidentMonitor> nonIncidentMonitors) {
    this.nonIncidentMonitors = nonIncidentMonitors;
  }

  @JsonProperty("NonIncidentSites")
  public List<IncidentSite> getNonIncidentSites() {
    return nonIncidentSites;
  }

  @JsonProperty("NonIncidentSites")
  public void setNonIncidentSites(List<IncidentSite> nonIncidentSites) {
    this.nonIncidentSites = nonIncidentSites;
  }

  @JsonProperty("ServiceName")
  public String getServiceName() {
    return serviceName;
  }

  @JsonProperty("ServiceName")
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
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
    return new HashCodeBuilder().append(dateTime).append(incidentMonitors).append(incidentSites)
        .append(nonIncidentMonitors).append(nonIncidentSites).append(serviceName).append(additionalProperties)
        .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof SitesHourlyAirQuality) == false) {
      return false;
    }
    SitesHourlyAirQuality rhs = ((SitesHourlyAirQuality) other);
    return new EqualsBuilder().append(dateTime, rhs.dateTime).append(incidentMonitors, rhs.incidentMonitors)
        .append(incidentSites, rhs.incidentSites).append(nonIncidentMonitors, rhs.nonIncidentMonitors)
        .append(nonIncidentSites, rhs.nonIncidentSites).append(serviceName, rhs.serviceName)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
