package org.geotools.data.epavic.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthCategoryThreshold {

  public static final String HEALTH_CATEGORY_VISIBILITY_TEXT = "HealthCategoryVisibilityText";

  public static final String HEALTH_CATEGORY_VALUE_RANGE_TEXT = "HealthCategoryValueRangeText";

  public static final String HEALTH_CATEGORY_MESSAGE = "HealthCategoryMessage";

  public static final String HEALTH_CATEGORY_LEVEL = "HealthCategoryLevel";

  public static final String HEALTH_CATEGORY_FOREGROUND_COLOUR = "HealthCategoryForegroundColour";

  public static final String HEALTH_CATEGORY_DESCRIPTION = "HealthCategoryDescription";

  public static final String HEALTH_CATEGORY_BACKGROUND_COLOUR = "HealthCategoryBackgroundColour";

  @JsonProperty(HEALTH_CATEGORY_BACKGROUND_COLOUR)
  private String healthCategoryBackgroundColour;

  @JsonProperty(HEALTH_CATEGORY_DESCRIPTION)
  private String healthCategoryDescription;

  @JsonProperty(HEALTH_CATEGORY_FOREGROUND_COLOUR)
  private String healthCategoryForegroundColour;

  @JsonProperty(HEALTH_CATEGORY_LEVEL)
  private Integer healthCategoryLevel;

  @JsonProperty(HEALTH_CATEGORY_MESSAGE)
  private String healthCategoryMessage;

  @JsonProperty(HEALTH_CATEGORY_VALUE_RANGE_TEXT)
  private String healthCategoryValueRangeText;

  @JsonProperty(HEALTH_CATEGORY_VISIBILITY_TEXT)
  private String healthCategoryVisibilityText;

  public HealthCategoryThreshold() {
  }

  public String getHealthCategoryBackgroundColour() {
    return healthCategoryBackgroundColour;
  }

  public void setHealthCategoryBackgroundColour(String healthCategoryBackgroundColour) {
    this.healthCategoryBackgroundColour = healthCategoryBackgroundColour;
  }

  public String getHealthCategoryDescription() {
    return healthCategoryDescription;
  }

  public void setHealthCategoryDescription(String healthCategoryDescription) {
    this.healthCategoryDescription = healthCategoryDescription;
  }

  public String getHealthCategoryForegroundColour() {
    return healthCategoryForegroundColour;
  }

  public void setHealthCategoryForegroundColour(String healthCategoryForegroundColour) {
    this.healthCategoryForegroundColour = healthCategoryForegroundColour;
  }

  public Integer getHealthCategoryLevel() {
    return healthCategoryLevel;
  }

  public void setHealthCategoryLevel(Integer healthCategoryLevel) {
    this.healthCategoryLevel = healthCategoryLevel;
  }

  public String getHealthCategoryMessage() {
    return healthCategoryMessage;
  }

  public void setHealthCategoryMessage(String healthCategoryMessage) {
    this.healthCategoryMessage = healthCategoryMessage;
  }

  public String getHealthCategoryValueRangeText() {
    return healthCategoryValueRangeText;
  }

  public void setHealthCategoryValueRangeText(String healthCategoryValueRangeText) {
    this.healthCategoryValueRangeText = healthCategoryValueRangeText;
  }

  public String getHealthCategoryVisibilityText() {
    return healthCategoryVisibilityText;
  }

  public void setHealthCategoryVisibilityText(String healthCategoryVisibilityText) {
    this.healthCategoryVisibilityText = healthCategoryVisibilityText;
  }
}