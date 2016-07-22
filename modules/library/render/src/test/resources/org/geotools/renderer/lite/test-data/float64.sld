<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld"
  xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:UserLayer>
    <sld:LayerFeatureConstraints>
      <sld:FeatureTypeConstraint />
    </sld:LayerFeatureConstraints>
    <sld:UserStyle>
      <sld:Name>1</sld:Name>
      <sld:Title>Default Raster</sld:Title>
      <sld:Abstract>A sample style that draws a raster, good for
        displaying imagery</sld:Abstract>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:FeatureTypeName>Feature</sld:FeatureTypeName>
        <sld:Rule>
          <sld:Name>rule1</sld:Name>
          <sld:Title>Opaque Raster</sld:Title>
          <sld:Abstract>A raster with 100% opacity</sld:Abstract>
          <sld:RasterSymbolizer>
            <sld:Geometry>
              <ogc:PropertyName>geom</ogc:PropertyName>
            </sld:Geometry>
            <sld:ColorMap>
              <sld:ColorMapEntry color="${env('lowColor', '#D7191C')}" opacity="1"
                quantity="${env('low', 0)}" label="1" />
              <sld:ColorMapEntry color="#FDAE61" opacity="1"
                quantity="0.002383" label="2" />
              <sld:ColorMapEntry color="#FFFFBF" opacity="1"
                quantity="0.004766" label="3" />
              <sld:ColorMapEntry color="#ABDDA4" opacity="1"
                quantity="0.007149" label="4" />
              <sld:ColorMapEntry color="#2B83BA" opacity="1"
                quantity="0.009532" label="5" />
            </sld:ColorMap>
          </sld:RasterSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:UserLayer>
</sld:StyledLayerDescriptor>