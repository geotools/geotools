<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld
http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" version="1.0.0">
  <UserLayer>
    <Name>raster_layer</Name>
    <UserStyle>
      <Name>raster</Name>
	  <Title>A boring default style</Title>
      <FeatureTypeStyle>
        <Rule>
          <RasterSymbolizer>
            <Opacity>1.0</Opacity>
            <ChannelSelection>
              <GrayChannel>
                <SourceChannelName>1</SourceChannelName>
              </GrayChannel>
            </ChannelSelection>
            <ColorMap>
                <ColorMapEntry color="#FF0000" opacity="1.0" quantity="0"/>
                <ColorMapEntry color="#FFFF00" opacity="1.0" quantity="10"/>
                <ColorMapEntry color="#00FF00" opacity="1.0" quantity="30"/>
                <ColorMapEntry color="#00FFFF" opacity="1.0" quantity="50"/>
                <ColorMapEntry color="#0000FF" opacity="1.0" quantity="70"/>
                <ColorMapEntry color="#FFFFFF" opacity="1.0" quantity="255"/>
            </ColorMap>
          </RasterSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </UserLayer>
</StyledLayerDescriptor>