package org.geotools.data.epavic.schema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"ServiceName", "Version", "Parameters", "NumberOfMonitors", "Monitors"})
public class Monitors {

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Parameters")
    private String parameters;

    @JsonProperty("NumberOfMonitors")
    private Integer numberOfMonitors;

    @JsonProperty("Monitors")
    private List<Monitor> monitors = new ArrayList<Monitor>();

    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("NumberOfMonitors")
    public Integer getNumberOfMonitors() {
        return numberOfMonitors;
    }

    @JsonProperty("NumberOfMonitors")
    public void setNumberOfMonitors(Integer numberOfMonitors) {
        this.numberOfMonitors = numberOfMonitors;
    }

    @JsonProperty("Monitors")
    public List<Monitor> getMonitors() {
        return monitors;
    }

    @JsonProperty("Monitors")
    public void setMonitors(List<Monitor> monitors) {
        this.monitors = monitors;
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
        return new HashCodeBuilder()
                .append(serviceName)
                .append(version)
                .append(parameters)
                .append(numberOfMonitors)
                .append(monitors)
                .append(additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Monitors) == false) {
            return false;
        }
        Monitors rhs = ((Monitors) other);
        return new EqualsBuilder()
                .append(serviceName, rhs.serviceName)
                .append(version, rhs.version)
                .append(parameters, rhs.parameters)
                .append(numberOfMonitors, rhs.numberOfMonitors)
                .append(monitors, rhs.monitors)
                .append(additionalProperties, rhs.additionalProperties)
                .isEquals();
    }
}
