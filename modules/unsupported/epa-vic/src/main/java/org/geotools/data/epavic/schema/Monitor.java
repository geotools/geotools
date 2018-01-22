
package org.geotools.data.epavic.schema;

import java.util.HashMap;
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
@JsonPropertyOrder({ Monitor.COMMON_NAME, Monitor.EPA_DESCRIPTION_URL, Monitor.EQUIPMENT_TYPE, Monitor.MONITOR_ID, Monitor.PRESENTATION_PRECISION,
    Monitor.SHORT_NAME, "SiteId", Monitor.UNIT_OF_MEASURE })
public class Monitor {

  public static final String UNIT_OF_MEASURE = "UnitOfMeasure";

  public static final String SHORT_NAME = "ShortName";

  public static final String PRESENTATION_PRECISION = "PresentationPrecision";

  public static final String MONITOR_ID = "MonitorId";

  public static final String EQUIPMENT_TYPE = "EquipmentType";

  public static final String EPA_DESCRIPTION_URL = "EPADescriptionURL";

  public static final String COMMON_NAME = "CommonName";

  @JsonProperty(COMMON_NAME)
  private String commonName;

  @JsonProperty(EPA_DESCRIPTION_URL)
  private String ePADescriptionURL;

  @JsonProperty(EQUIPMENT_TYPE)
  private EquipmentType equipmentType;

  @JsonProperty(MONITOR_ID)
  private String monitorId;

  @JsonProperty(PRESENTATION_PRECISION)
  private Integer presentationPrecision;

  @JsonProperty(SHORT_NAME)
  private String shortName;

  @JsonProperty(Site.SITE_ID)
  private Integer siteId;

  @JsonProperty(UNIT_OF_MEASURE)
  private String unitOfMeasure;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty(COMMON_NAME)
  public String getCommonName() {
    return commonName;
  }

  @JsonProperty(COMMON_NAME)
  public void setCommonName(String commonName) {
    this.commonName = commonName;
  }

  @JsonProperty(EPA_DESCRIPTION_URL)
  public String getEPADescriptionURL() {
    return ePADescriptionURL;
  }

  @JsonProperty(EPA_DESCRIPTION_URL)
  public void setEPADescriptionURL(String ePADescriptionURL) {
    this.ePADescriptionURL = ePADescriptionURL;
  }

  @JsonProperty(EQUIPMENT_TYPE)
  public EquipmentType getEquipmentType() {
    return equipmentType;
  }

  @JsonProperty(EQUIPMENT_TYPE)
  public void setEquipmentType(EquipmentType equipmentType) {
    this.equipmentType = equipmentType;
  }

  @JsonProperty(MONITOR_ID)
  public String getMonitorId() {
    return monitorId;
  }

  @JsonProperty(MONITOR_ID)
  public void setMonitorId(String monitorId) {
    this.monitorId = monitorId;
  }

  @JsonProperty(PRESENTATION_PRECISION)
  public Integer getPresentationPrecision() {
    return presentationPrecision;
  }

  @JsonProperty(PRESENTATION_PRECISION)
  public void setPresentationPrecision(Integer presentationPrecision) {
    this.presentationPrecision = presentationPrecision;
  }

  @JsonProperty(SHORT_NAME)
  public String getShortName() {
    return shortName;
  }

  @JsonProperty(SHORT_NAME)
  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  @JsonProperty("SiteId")
  public Integer getSiteId() {
    return siteId;
  }

  @JsonProperty("SiteId")
  public void setSiteId(Integer siteId) {
    this.siteId = siteId;
  }

  @JsonProperty(UNIT_OF_MEASURE)
  public String getUnitOfMeasure() {
    return unitOfMeasure;
  }

  @JsonProperty(UNIT_OF_MEASURE)
  public void setUnitOfMeasure(String unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
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
    return new HashCodeBuilder().append(commonName).append(ePADescriptionURL).append(equipmentType).append(monitorId)
        .append(presentationPrecision).append(shortName).append(siteId).append(unitOfMeasure)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Monitor) == false) {
      return false;
    }
    Monitor rhs = ((Monitor) other);
    return new EqualsBuilder().append(commonName, rhs.commonName).append(ePADescriptionURL, rhs.ePADescriptionURL)
        .append(equipmentType, rhs.equipmentType).append(monitorId, rhs.monitorId)
        .append(presentationPrecision, rhs.presentationPrecision).append(shortName, rhs.shortName)
        .append(siteId, rhs.siteId).append(unitOfMeasure, rhs.unitOfMeasure)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
