package org.geotools.data.epavic.schema.sitesHourlyAirQuality;

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
@JsonPropertyOrder({
    "AQIValue",
    "Abbreviation",
    "BackgroundColour",
    "Description",
    "ForegroundColour",
    "HealthMessage",
    "Precision",
    "ShortName",
    "TimeBasisID",
    "Value"
})
public class Measurement {

    @JsonProperty("AQIValue")
    private Integer aQIValue;

    @JsonProperty("Abbreviation")
    private String abbreviation;

    @JsonProperty("BackgroundColour")
    private String backgroundColour;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("ForegroundColour")
    private String foregroundColour;

    @JsonProperty("HealthMessage")
    private Object healthMessage;

    @JsonProperty("Precision")
    private Integer precision;

    @JsonProperty("ShortName")
    private String shortName;

    @JsonProperty("TimeBasisID")
    private String timeBasisID;

    @JsonProperty("Value")
    private Double value;

    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("AQIValue")
    public Integer getAQIValue() {
        return aQIValue;
    }

    @JsonProperty("AQIValue")
    public void setAQIValue(Integer aQIValue) {
        this.aQIValue = aQIValue;
    }

    @JsonProperty("Abbreviation")
    public String getAbbreviation() {
        return abbreviation;
    }

    @JsonProperty("Abbreviation")
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @JsonProperty("BackgroundColour")
    public String getBackgroundColour() {
        return backgroundColour;
    }

    @JsonProperty("BackgroundColour")
    public void setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("ForegroundColour")
    public String getForegroundColour() {
        return foregroundColour;
    }

    @JsonProperty("ForegroundColour")
    public void setForegroundColour(String foregroundColour) {
        this.foregroundColour = foregroundColour;
    }

    @JsonProperty("HealthMessage")
    public Object getHealthMessage() {
        return healthMessage;
    }

    @JsonProperty("HealthMessage")
    public void setHealthMessage(Object healthMessage) {
        this.healthMessage = healthMessage;
    }

    @JsonProperty("Precision")
    public Integer getPrecision() {
        return precision;
    }

    @JsonProperty("Precision")
    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    @JsonProperty("ShortName")
    public String getShortName() {
        return shortName;
    }

    @JsonProperty("ShortName")
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @JsonProperty("TimeBasisID")
    public String getTimeBasisID() {
        return timeBasisID;
    }

    @JsonProperty("TimeBasisID")
    public void setTimeBasisID(String timeBasisID) {
        this.timeBasisID = timeBasisID;
    }

    @JsonProperty("Value")
    public Double getValue() {
        return value;
    }

    @JsonProperty("Value")
    public void setValue(Double value) {
        this.value = value;
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
                .append(aQIValue)
                .append(abbreviation)
                .append(backgroundColour)
                .append(description)
                .append(foregroundColour)
                .append(healthMessage)
                .append(precision)
                .append(shortName)
                .append(timeBasisID)
                .append(value)
                .append(additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Measurement) == false) {
            return false;
        }
        Measurement rhs = ((Measurement) other);
        return new EqualsBuilder()
                .append(aQIValue, rhs.aQIValue)
                .append(abbreviation, rhs.abbreviation)
                .append(backgroundColour, rhs.backgroundColour)
                .append(description, rhs.description)
                .append(foregroundColour, rhs.foregroundColour)
                .append(healthMessage, rhs.healthMessage)
                .append(precision, rhs.precision)
                .append(shortName, rhs.shortName)
                .append(timeBasisID, rhs.timeBasisID)
                .append(value, rhs.value)
                .append(additionalProperties, rhs.additionalProperties)
                .isEquals();
    }
}
