package org.geotools.data.epavic.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AQICategoryThreshold {

  public static final String AQI_FOREGROUND_COLOUR = "AQIForegroundColour";

  public static final String AQI_CATEGORY_DESCRIPTION = "AQICategoryDescription";

  public static final String AQI_CATEGORY_ABBREVIATION = "AQICategoryAbbreviation";

  public static final String AQI_BACKGROUND_COLOUR = "AQIBackgroundColour";

  @JsonProperty(AQI_BACKGROUND_COLOUR)
  private String aQIBackgroundColour;

  @JsonProperty(AQI_CATEGORY_ABBREVIATION)
  private String aQICategoryAbbreviation;

  @JsonProperty(AQI_CATEGORY_DESCRIPTION)
  private String aQICategoryDescription;

  @JsonProperty(AQI_FOREGROUND_COLOUR)
  private String aQIForegroundColour;

  public AQICategoryThreshold() {
  }

  public String getaQIBackgroundColour() {
    return aQIBackgroundColour;
  }

  public void setaQIBackgroundColour(String aQIBackgroundColour) {
    this.aQIBackgroundColour = aQIBackgroundColour;
  }

  public String getaQICategoryAbbreviation() {
    return aQICategoryAbbreviation;
  }

  public void setaQICategoryAbbreviation(String aQICategoryAbbreviation) {
    this.aQICategoryAbbreviation = aQICategoryAbbreviation;
  }

  public String getaQICategoryDescription() {
    return aQICategoryDescription;
  }

  public void setaQICategoryDescription(String aQICategoryDescription) {
    this.aQICategoryDescription = aQICategoryDescription;
  }

  public String getaQIForegroundColour() {
    return aQIForegroundColour;
  }

  public void setaQIForegroundColour(String aQIForegroundColour) {
    this.aQIForegroundColour = aQIForegroundColour;
  }
}