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
@JsonPropertyOrder({"Code", "Description", "IdNumber"})
public class EquipmentType {

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("IdNumber")
    private Integer idNumber;

    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Code")
    public String getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("IdNumber")
    public Integer getIdNumber() {
        return idNumber;
    }

    @JsonProperty("IdNumber")
    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
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
                .append(code)
                .append(description)
                .append(idNumber)
                .append(additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EquipmentType) == false) {
            return false;
        }
        EquipmentType rhs = ((EquipmentType) other);
        return new EqualsBuilder()
                .append(code, rhs.code)
                .append(description, rhs.description)
                .append(idNumber, rhs.idNumber)
                .append(additionalProperties, rhs.additionalProperties)
                .isEquals();
    }
}
