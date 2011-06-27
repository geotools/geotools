<?xml version="1.0" encoding="UTF-8"?>
<sld:UserStyle xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
  <sld:Name>Default Styler</sld:Name>
  <sld:Title/>
  <sld:FeatureTypeStyle>
    <sld:Name>name</sld:Name>
    <sld:Rule>
      <sld:LineSymbolizer>
        <sld:Stroke/>
      </sld:LineSymbolizer>
    </sld:Rule>
  </sld:FeatureTypeStyle>
  <sld:FeatureTypeStyle>
    <sld:Name>name</sld:Name>
    <sld:Rule>
      <sld:LineSymbolizer>
        <sld:Stroke>
          <sld:GraphicStroke>
            <sld:Graphic>
              <sld:Mark>
                <sld:WellKnownName>shape://vertline</sld:WellKnownName>
                <sld:Fill/>
                <sld:Stroke/>
              </sld:Mark>
              <sld:Size>
                <ogc:Literal>25</ogc:Literal>
              </sld:Size>
            </sld:Graphic>
          </sld:GraphicStroke>
        </sld:Stroke>
        <sld:VendorOption name="labelObstacle">True</sld:VendorOption>
      </sld:LineSymbolizer>
      <sld:TextSymbolizer>
        <sld:Label>
          <ogc:PropertyName>name</ogc:PropertyName>
        </sld:Label>
        <sld:Font>
          <sld:CssParameter name="font-family">Dialog</sld:CssParameter>
          <sld:CssParameter name="font-size">10.0</sld:CssParameter>
          <sld:CssParameter name="font-style">normal</sld:CssParameter>
          <sld:CssParameter name="font-weight">normal</sld:CssParameter>
        </sld:Font>
        <sld:LabelPlacement>
          <sld:LinePlacement>
            <sld:PerpendicularOffset>
              <ogc:Literal>15</ogc:Literal>
            </sld:PerpendicularOffset>
          </sld:LinePlacement>
        </sld:LabelPlacement>
        <sld:Fill>
          <sld:CssParameter name="fill">#000000</sld:CssParameter>
        </sld:Fill>
        <sld:VendorOption name="group">False</sld:VendorOption>
        <sld:VendorOption name="followLine">False</sld:VendorOption>
      </sld:TextSymbolizer>
    </sld:Rule>
  </sld:FeatureTypeStyle>
</sld:UserStyle>
