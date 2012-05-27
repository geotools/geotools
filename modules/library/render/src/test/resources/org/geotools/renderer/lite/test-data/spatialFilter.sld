<?xml version="1.0" encoding="UTF-8"?>
<sld:UserStyle xmlns="http://www.opengis.net/sld"
  xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:gml="http://www.opengis.net/gml">
  <sld:Name>Default Styler</sld:Name>
  <sld:Title />
  <sld:FeatureTypeStyle>
    <sld:Name>name</sld:Name>
    <sld:Rule>
      <ogc:Filter>
        <ogc:Intersects>
          <ogc:PropertyName>geom</ogc:PropertyName>
          <gml:Polygon srsName="EPSG:32631">
            <gml:outerBoundaryIs>
              <gml:LinearRing>
                <gml:coordinates decimal="." cs="," ts=" ">278246.6541,552664.2969
                  500000,552664.2969 500000,774218.9664
                  278246.6541,774218.9664 278246.6541,552664.2969
                </gml:coordinates>
              </gml:LinearRing>
            </gml:outerBoundaryIs>
          </gml:Polygon>
        </ogc:Intersects>
      </ogc:Filter>
      <sld:PointSymbolizer>
        <sld:Graphic>
          <sld:Mark>
            <sld:Fill />
            <sld:Stroke />
          </sld:Mark>
        </sld:Graphic>
      </sld:PointSymbolizer>
    </sld:Rule>
  </sld:FeatureTypeStyle>
</sld:UserStyle>