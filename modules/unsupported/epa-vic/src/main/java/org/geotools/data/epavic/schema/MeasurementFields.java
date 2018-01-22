package org.geotools.data.epavic.schema;

public enum MeasurementFields {

  VALUE(Measurement.VALUE, String.class),
  TIME_BASE_ID(Measurement.TIME_BASE_ID, String.class),
  SITE_ID(Site.SITE_ID, Integer.class),
  QUALITY_STATUS(Measurement.QUALITY_STATUS, Integer.class),
  MONITOR_ID(Measurement.MONITOR_ID, String.class),
  MONITOR_NAME(Measurement.MONITOR_NAME, String.class),
  MONITOR_SHORT_NAME(Measurement.MONITOR_SHORT_NAME, String.class),
  MONITOR_TIME_BASIS(Measurement.MONITOR_TIME_BASIS, String.class),
  MONITOR_COMMON_NAME(Measurement.MONITOR_COMMON_NAME, String.class),
  MONITOR_EPA_URL(Measurement.MONITOR_EPA_URL, String.class),
  MONITOR_PRES_PRECISIONS(Measurement.MONITOR_PRESENTATION_PRECISION, Integer.class),
  MONITOR_UNIT_OF_MEASURE(Measurement.MONITOR_UNIT_OF_MEASURE, Integer.class),
  SITE_LIST_NAME(Measurement.SITE_LIST_NAME, String.class),
  SITE_FIRE_HAZARD(Site.FIRE_HAZARD_CATEGORY, Integer.class),
  LONG(Site.LONG, Double.class),
  LAT(Site.LAT, Double.class),
  IS_STATION_OFFLINE(Measurement.IS_STATION_OFFLINE, Boolean.class),
  EQUIPMENT_TYPE(Measurement.EQUIPMENT_TYPE, String.class),
  DATE_TIME_START(Measurement.DATE_TIME_START, String.class),
  DATE_TIME_RECORDED(Measurement.DATE_TIME_RECORDED, String.class),
  AQI_INDEX(Measurement.AQI_INDEX, Integer.class),
  HEALTH_CATEGORY_VISIBILITY_TEXT(HealthCategoryThreshold.HEALTH_CATEGORY_VISIBILITY_TEXT, String.class),
  HEALTH_CATEGORY_VALUE_RANGE_TEXT(HealthCategoryThreshold.HEALTH_CATEGORY_VALUE_RANGE_TEXT, String.class),
  HEALTH_CATEGORY_MESSAGE(HealthCategoryThreshold.HEALTH_CATEGORY_MESSAGE, String.class),
  HEALTH_CATEGORY_LEVEL(HealthCategoryThreshold.HEALTH_CATEGORY_LEVEL, Integer.class),
  HEALTH_CATEGORY_FOREGROUND_COLOUR(HealthCategoryThreshold.HEALTH_CATEGORY_FOREGROUND_COLOUR, String.class),
  HEALTH_CATEGORY_DESCRIPTION(HealthCategoryThreshold.HEALTH_CATEGORY_DESCRIPTION, String.class),
  HEALTH_CATEGORY_BACKGROUND_COLOUR(HealthCategoryThreshold.HEALTH_CATEGORY_BACKGROUND_COLOUR, String.class),
  AQI_FOREGROUND_COLOUR(AQICategoryThreshold.AQI_FOREGROUND_COLOUR, String.class),
  AQI_CATEGORY_DESCRIPTION(AQICategoryThreshold.AQI_CATEGORY_DESCRIPTION, String.class),
  AQI_CATEGORY_ABBREVIATION(AQICategoryThreshold.AQI_CATEGORY_ABBREVIATION, String.class),
  AQI_BACKGROUND_COLOUR(AQICategoryThreshold.AQI_BACKGROUND_COLOUR, String.class);

  private final Class<?> type;

  private final String fieldName;

  private MeasurementFields(String fieldName, Class<?> type) {
    this.fieldName = fieldName;
    this.type = type;
  }

  public Class<?> getType() {
    return this.type;
  }

  public String getFieldName() {
    return this.fieldName;
  }
}