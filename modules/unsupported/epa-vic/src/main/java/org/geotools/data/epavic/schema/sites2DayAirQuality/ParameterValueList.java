
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
@JsonPropertyOrder({ "AqiIndex", "HealthCategoryLevel", "Id", "IdString", "PIndexValue", "PValue", "SampleData1Hr",
    "SampleData24Hr", "ServiceProvider", "UOM", "Value" })
public class ParameterValueList {

  @JsonProperty("AqiIndex")
  private Integer aqiIndex;

  @JsonProperty("HealthCategoryLevel")
  private Integer healthCategoryLevel;

  @JsonProperty("Id")
  private String id;

  @JsonProperty("IdString")
  private String idString;

  @JsonProperty("PIndexValue")
  private String pIndexValue;

  @JsonProperty("PValue")
  private String pValue;

  @JsonProperty("SampleData1Hr")
  private List<SampleDataHr> sampleData1Hr = new ArrayList<SampleDataHr>();

  @JsonProperty("SampleData24Hr")
  private List<SampleDataHr> sampleData24Hr = new ArrayList<SampleDataHr>();

  @JsonProperty("ServiceProvider")
  private String serviceProvider;

  @JsonProperty("UOM")
  private String uOM;

  @JsonProperty("Value")
  private Integer value;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("AqiIndex")
  public Integer getAqiIndex() {
    return aqiIndex;
  }

  @JsonProperty("AqiIndex")
  public void setAqiIndex(Integer aqiIndex) {
    this.aqiIndex = aqiIndex;
  }

  @JsonProperty("HealthCategoryLevel")
  public Integer getHealthCategoryLevel() {
    return healthCategoryLevel;
  }

  @JsonProperty("HealthCategoryLevel")
  public void setHealthCategoryLevel(Integer healthCategoryLevel) {
    this.healthCategoryLevel = healthCategoryLevel;
  }

  @JsonProperty("Id")
  public String getId() {
    return id;
  }

  @JsonProperty("Id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("IdString")
  public String getIdString() {
    return idString;
  }

  @JsonProperty("IdString")
  public void setIdString(String idString) {
    this.idString = idString;
  }

  @JsonProperty("PIndexValue")
  public String getPIndexValue() {
    return pIndexValue;
  }

  @JsonProperty("PIndexValue")
  public void setPIndexValue(String pIndexValue) {
    this.pIndexValue = pIndexValue;
  }

  @JsonProperty("PValue")
  public String getPValue() {
    return pValue;
  }

  @JsonProperty("PValue")
  public void setPValue(String pValue) {
    this.pValue = pValue;
  }

  @JsonProperty("SampleData1Hr")
  public List<SampleDataHr> getSampleData1Hr() {
    return sampleData1Hr;
  }

  @JsonProperty("SampleData1Hr")
  public void setSampleData1Hr(List<SampleDataHr> sampleData1Hr) {
    this.sampleData1Hr = sampleData1Hr;
  }

  @JsonProperty("SampleData24Hr")
  public List<SampleDataHr> getSampleData24Hr() {
    return sampleData24Hr;
  }

  @JsonProperty("SampleData24Hr")
  public void setSampleData24Hr(List<SampleDataHr> sampleData24Hr) {
    this.sampleData24Hr = sampleData24Hr;
  }

  @JsonProperty("ServiceProvider")
  public String getServiceProvider() {
    return serviceProvider;
  }

  @JsonProperty("ServiceProvider")
  public void setServiceProvider(String serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @JsonProperty("UOM")
  public String getUOM() {
    return uOM;
  }

  @JsonProperty("UOM")
  public void setUOM(String uOM) {
    this.uOM = uOM;
  }

  @JsonProperty("Value")
  public Integer getValue() {
    return value;
  }

  @JsonProperty("Value")
  public void setValue(Integer value) {
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
    return new HashCodeBuilder().append(aqiIndex).append(healthCategoryLevel).append(id).append(idString)
        .append(pIndexValue).append(pValue).append(sampleData1Hr).append(sampleData24Hr).append(serviceProvider)
        .append(uOM).append(value).append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof ParameterValueList) == false) {
      return false;
    }
    ParameterValueList rhs = ((ParameterValueList) other);
    return new EqualsBuilder().append(aqiIndex, rhs.aqiIndex).append(healthCategoryLevel, rhs.healthCategoryLevel)
        .append(id, rhs.id).append(idString, rhs.idString).append(pIndexValue, rhs.pIndexValue)
        .append(pValue, rhs.pValue).append(sampleData1Hr, rhs.sampleData1Hr).append(sampleData24Hr, rhs.sampleData24Hr)
        .append(serviceProvider, rhs.serviceProvider).append(uOM, rhs.uOM).append(value, rhs.value)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
