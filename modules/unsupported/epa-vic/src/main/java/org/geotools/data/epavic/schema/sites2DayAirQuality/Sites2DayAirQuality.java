
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
@JsonPropertyOrder({ "DateTime", "ServiceName", "Stations" })
public class Sites2DayAirQuality {

  @JsonProperty("DateTime")
  private String dateTime;

  @JsonProperty("ServiceName")
  private String serviceName;

  @JsonProperty("Stations")
  private List<Station> stations = new ArrayList<Station>();

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

  @JsonProperty("ServiceName")
  public String getServiceName() {
    return serviceName;
  }

  @JsonProperty("ServiceName")
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  @JsonProperty("Stations")
  public List<Station> getStations() {
    return stations;
  }

  @JsonProperty("Stations")
  public void setStations(List<Station> stations) {
    this.stations = stations;
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
    return new HashCodeBuilder().append(dateTime).append(serviceName).append(stations).append(additionalProperties)
        .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Sites2DayAirQuality) == false) {
      return false;
    }
    Sites2DayAirQuality rhs = ((Sites2DayAirQuality) other);
    return new EqualsBuilder().append(dateTime, rhs.dateTime).append(serviceName, rhs.serviceName)
        .append(stations, rhs.stations).append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
