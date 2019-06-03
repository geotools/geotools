curl -XPOST "http://172.17.0.2:8080/geoserver/wfs"\
 --header "Content-Type: application/xml"\
 --data '<?xml version="1.0" encoding="UTF-8"?><wfs:GetFeature xmlns:aurin="http://aurin.org.au" xmlns:ogc="http://www.opengis.net/ogc" xmlns:wfs="http://www.opengis.net/wfs" xmlns:ows="http://www.opengis.net/ows" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gml="http://www.opengis.net/gml" handle="GeoTools 11.x-AURIN.1 WFS DataStore" outputFormat="json" resultType="results" service="WFS" version="1.1.0">
 <wfs:Query srsName="urn:x-ogc:def:crs:EPSG:4283" typeName="aurin:measurement">
  <wfs:PropertyName>TimeBasisId</wfs:PropertyName>
  <wfs:PropertyName>TimeBaseId</wfs:PropertyName>
  <wfs:PropertyName>MonitorId</wfs:PropertyName>
  <wfs:PropertyName>Value</wfs:PropertyName>
  <wfs:PropertyName>SiteId</wfs:PropertyName>
  <wfs:PropertyName>QualityStatus</wfs:PropertyName>
  <wfs:PropertyName>MonitorName</wfs:PropertyName>
  <wfs:PropertyName>MonitorShortName</wfs:PropertyName>
  <wfs:PropertyName>MonitorTimeBasis</wfs:PropertyName>
  <wfs:PropertyName>MonitorCommonName</wfs:PropertyName>
  <wfs:PropertyName>MonitorEPADescriptionURL</wfs:PropertyName>
  <wfs:PropertyName>MonitorPresentationPrecision</wfs:PropertyName>
  <wfs:PropertyName>MonitorUnitOfMeasure</wfs:PropertyName>
  <wfs:PropertyName>SiteListName</wfs:PropertyName>
  <wfs:PropertyName>FireHazardCategory</wfs:PropertyName>
  <wfs:PropertyName>Longitude</wfs:PropertyName>
  <wfs:PropertyName>Latitude</wfs:PropertyName>
  <wfs:PropertyName>IsStationOffline</wfs:PropertyName>
  <wfs:PropertyName>EquipmentType</wfs:PropertyName>
  <wfs:PropertyName>DateTimeStart</wfs:PropertyName>
  <wfs:PropertyName>DateTimeRecorded</wfs:PropertyName>
  <wfs:PropertyName>AQIIndex</wfs:PropertyName>
  <wfs:PropertyName>HealthCategoryVisibilityText</wfs:PropertyName>
  <wfs:PropertyName>HealthCategoryValueRangeText</wfs:PropertyName>
  <wfs:PropertyName>HealthCategoryMessage</wfs:PropertyName>
  <wfs:PropertyName>HealthCategoryLevel</wfs:PropertyName>
  <wfs:PropertyName>HealthCategoryForegroundColour</wfs:PropertyName>
  <wfs:PropertyName>HealthCategoryDescription</wfs:PropertyName>
  <wfs:PropertyName>HealthCategoryBackgroundColour</wfs:PropertyName>
  <wfs:PropertyName>AQIForegroundColour</wfs:PropertyName>
  <wfs:PropertyName>AQICategoryDescription</wfs:PropertyName>
  <wfs:PropertyName>AQICategoryAbbreviation</wfs:PropertyName>
  <wfs:PropertyName>AQIBackgroundColour</wfs:PropertyName>
  <wfs:PropertyName>geometry</wfs:PropertyName>
  <ogc:Filter>
   <ogc:And>
    <ogc:PropertyIsEqualTo matchCase="true">
     <ogc:PropertyName>TimeBasisId</ogc:PropertyName>
     <ogc:Literal>1HR_AV</ogc:Literal>
    </ogc:PropertyIsEqualTo>
    <ogc:PropertyIsEqualTo matchCase="true">
     <ogc:PropertyName>MonitorId</ogc:PropertyName>
     <ogc:Literal>CO</ogc:Literal>
    </ogc:PropertyIsEqualTo>
    <ogc:PropertyIsBetween>
     <ogc:PropertyName>DateTimeRecorded</ogc:PropertyName>
     <ogc:LowerBoundary>
      <ogc:Literal>2019-03-21T10:00:00</ogc:Literal>
     </ogc:LowerBoundary>
     <ogc:UpperBoundary>
      <ogc:Literal>2019-03-23T10:23:00</ogc:Literal>
     </ogc:UpperBoundary>
    </ogc:PropertyIsBetween>
    <ogc:BBOX>
     <ogc:PropertyName>geometry</ogc:PropertyName>
     <gml:Envelope>
      <gml:lowerCorner>145.03 -37.8</gml:lowerCorner>
      <gml:upperCorner>145.06 -37.5</gml:upperCorner>
     </gml:Envelope>
    </ogc:BBOX>
   </ogc:And>
  </ogc:Filter>
 </wfs:Query>
</wfs:GetFeature>' | jq '.'
