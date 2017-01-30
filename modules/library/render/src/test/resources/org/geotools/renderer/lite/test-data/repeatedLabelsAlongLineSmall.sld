<?xml version="1.0" encoding="UTF-8"?>
<sld:UserStyle xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld"
  xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml"
>
  <sld:Name>Default Styler</sld:Name>
  <sld:Title />
  <sld:FeatureTypeStyle>
    <sld:Name>name</sld:Name>
    <sld:Rule>
      <sld:LineSymbolizer>
        <sld:Stroke>
          <sld:CssParameter name="stroke">#AAAAAA</sld:CssParameter>
        </sld:Stroke>
      </sld:LineSymbolizer>
      <sld:TextSymbolizer>
        <sld:Label>
          <ogc:PropertyName>name</ogc:PropertyName>
        </sld:Label>
        <sld:Font>
          <sld:CssParameter name="font-family">Bitstream Vera Sans</sld:CssParameter>
          <sld:CssParameter name="font-size">10.0</sld:CssParameter>
        </sld:Font>
        <sld:Fill>
          <sld:CssParameter name="fill">#000000</sld:CssParameter>
        </sld:Fill>
        <sld:VendorOption name="followLine">true</sld:VendorOption>
        <sld:VendorOption name="repeat">10</sld:VendorOption>
        <sld:VendorOption name="maxDisplacement">1</sld:VendorOption>
      </sld:TextSymbolizer>
    </sld:Rule>
  </sld:FeatureTypeStyle>
</sld:UserStyle>