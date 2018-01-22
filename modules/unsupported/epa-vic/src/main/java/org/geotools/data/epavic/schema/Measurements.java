
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
@JsonPropertyOrder({ "ServiceName", "Version", "NumberOfMeasurements", "Parameters", "Measurements" })
public class Measurements {

  @JsonProperty("ServiceName")
  private String serviceName;

  @JsonProperty("Version")
  private String version;

  @JsonProperty("NumberOfMeasurements")
  private Integer numberOfMeasurements;

  @JsonProperty("Parameters")
  private String parameters;

  @JsonProperty("Measurements")
  private List<Measurement> measurements = new ArrayList<Measurement>();

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

  @JsonProperty("NumberOfMeasurements")
  public Integer getNumberOfMeasurements() {
    return numberOfMeasurements;
  }

  @JsonProperty("NumberOfMeasurements")
  public void setNumberOfMeasurements(Integer numberOfMeasurements) {
    this.numberOfMeasurements = numberOfMeasurements;
  }

  @JsonProperty("Parameters")
  public String getParameters() {
    return parameters;
  }

  @JsonProperty("Parameters")
  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  @JsonProperty("Measurements")
  public List<Measurement> getMeasurements() {
    return measurements;
  }

  @JsonProperty("Measurements")
  public void setMeasurements(List<Measurement> measurements) {
    this.measurements = measurements;
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
    return new HashCodeBuilder().append(serviceName).append(version).append(numberOfMeasurements).append(parameters)
        .append(measurements).append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Measurements) == false) {
      return false;
    }
    Measurements rhs = ((Measurements) other);
    return new EqualsBuilder().append(serviceName, rhs.serviceName).append(version, rhs.version)
        .append(numberOfMeasurements, rhs.numberOfMeasurements).append(parameters, rhs.parameters)
        .append(measurements, rhs.measurements).append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
