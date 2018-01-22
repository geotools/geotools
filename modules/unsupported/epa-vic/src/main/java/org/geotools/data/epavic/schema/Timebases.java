
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
@JsonPropertyOrder({ "ServiceName", "Version", "Parameters", "NumberOfTimeBasis", "TimeBasis" })
public class Timebases {

  @JsonProperty("ServiceName")
  private String serviceName;

  @JsonProperty("Version")
  private String version;

  @JsonProperty("Parameters")
  private String parameters;

  @JsonProperty("NumberOfTimeBasis")
  private Integer numberOfTimeBasis;

  @JsonProperty("TimeBasis")
  private List<TimeBasis> timeBasis = new ArrayList<TimeBasis>();

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

  @JsonProperty("Parameters")
  public String getParameters() {
    return parameters;
  }

  @JsonProperty("Parameters")
  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  @JsonProperty("NumberOfTimeBasis")
  public Integer getNumberOfTimeBasis() {
    return numberOfTimeBasis;
  }

  @JsonProperty("NumberOfTimeBasis")
  public void setNumberOfTimeBasis(Integer numberOfTimeBasis) {
    this.numberOfTimeBasis = numberOfTimeBasis;
  }

  @JsonProperty("TimeBasis")
  public List<TimeBasis> getTimeBasis() {
    return timeBasis;
  }

  @JsonProperty("TimeBasis")
  public void setTimeBasis(List<TimeBasis> timeBasis) {
    this.timeBasis = timeBasis;
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
    return new HashCodeBuilder().append(serviceName).append(version).append(parameters).append(numberOfTimeBasis)
        .append(timeBasis).append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Timebases) == false) {
      return false;
    }
    Timebases rhs = ((Timebases) other);
    return new EqualsBuilder().append(serviceName, rhs.serviceName).append(version, rhs.version)
        .append(parameters, rhs.parameters).append(numberOfTimeBasis, rhs.numberOfTimeBasis)
        .append(timeBasis, rhs.timeBasis).append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
