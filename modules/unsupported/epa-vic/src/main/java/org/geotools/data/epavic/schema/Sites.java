
package org.geotools.data.epavic.schema;

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
@JsonPropertyOrder({ "ServiceName", "Version", "NumberOfSites", "Parameters", "Sites" })
public class Sites {

  @JsonProperty("ServiceName")
  private String serviceName;

  @JsonProperty("Version")
  private String version;

  @JsonProperty("NumberOfSites")
  private Integer numberOfSites;

  @JsonProperty("Parameters")
  private String parameters;

  @JsonProperty("Sites")
  private List<Site> sites = new ArrayList<Site>();

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("ServiceName")
  public String getServiceName() {
    return serviceName;
  }

  @JsonProperty("ServiceName")
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  @JsonProperty("Version")
  public String getVersion() {
    return version;
  }

  @JsonProperty("Version")
  public void setVersion(String version) {
    this.version = version;
  }

  @JsonProperty("NumberOfSites")
  public Integer getNumberOfSites() {
    return numberOfSites;
  }

  @JsonProperty("NumberOfSites")
  public void setNumberOfSites(Integer numberOfSites) {
    this.numberOfSites = numberOfSites;
  }

  @JsonProperty("Parameters")
  public String getParameters() {
    return parameters;
  }

  @JsonProperty("Parameters")
  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  @JsonProperty("Sites")
  public List<Site> getSites() {
    return sites;
  }

  @JsonProperty("Sites")
  public void setSites(List<Site> sites) {
    this.sites = sites;
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
    return new HashCodeBuilder().append(serviceName).append(version).append(numberOfSites).append(parameters)
        .append(sites).append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Sites) == false) {
      return false;
    }
    Sites rhs = ((Sites) other);
    return new EqualsBuilder().append(serviceName, rhs.serviceName).append(version, rhs.version)
        .append(numberOfSites, rhs.numberOfSites).append(parameters, rhs.parameters).append(sites, rhs.sites)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
