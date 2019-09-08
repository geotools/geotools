package org.geotools.data.epavic.schema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"EMVUrl", "IncidentIcon"})
public class IncidentSite {

    @JsonProperty("EMVUrl")
    private String eMVUrl;

    @JsonProperty("IncidentIcon")
    private String incidentIcon;

    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("EMVUrl")
    public String getEMVUrl() {
        return eMVUrl;
    }

    @JsonProperty("EMVUrl")
    public void setEMVUrl(String eMVUrl) {
        this.eMVUrl = eMVUrl;
    }

    @JsonProperty("IncidentIcon")
    public String getIncidentIcon() {
        return incidentIcon;
    }

    @JsonProperty("IncidentIcon")
    public void setIncidentIcon(String incidentIcon) {
        this.incidentIcon = incidentIcon;
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
                .append(eMVUrl)
                .append(incidentIcon)
                .append(additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof IncidentSite) == false) {
            return false;
        }
        IncidentSite rhs = ((IncidentSite) other);
        return new EqualsBuilder()
                .append(eMVUrl, rhs.eMVUrl)
                .append(incidentIcon, rhs.incidentIcon)
                .append(additionalProperties, rhs.additionalProperties)
                .isEquals();
    }
}
