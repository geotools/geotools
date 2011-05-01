<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
  version="1.0.0">
  <UserLayer>
    <Name>sunshine fraction image</Name>
    <UserStyle>
      <Name>sunfractionimage_style</Name>
      <FeatureTypeStyle>
        <Rule>
          <RasterSymbolizer>
            <Opacity>1</Opacity>
            <ColorMap
              type="intervals">
              <!--ColorMapEntry
                color="#828282"
                opacity="0"
                quantity="-999"
                label="-999" /-->
              <ColorMapEntry
                color="#00A884"
                opacity="0"
                quantity="-998.999999999999"
                label="-999" />
              <ColorMapEntry
                color="#00A884"
                opacity="1"
                quantity="40"
                label="-998.999999 - 40" />
              <ColorMapEntry
                color="#2FA84B"
                opacity="1"
                quantity="40.000001"
                label="40.00000001 - 50" />
              <ColorMapEntry
                color="#2FA84B"
                opacity="1"
                quantity="50"
                label="40.00000001 - 50" />
              <ColorMapEntry
                color="#38A800"
                opacity="1"
                quantity="50.000001"
                label="50.0000001 - 60" />
              <ColorMapEntry
                color="#38A800"
                opacity="1"
                quantity="60"
                label="50.0000001 - 60" />
              <ColorMapEntry
                color="#98D43D"
                opacity="1"
                quantity="60.000001"
                label="60.0000001 - 70" />
              <ColorMapEntry
                color="#98D43D"
                opacity="1"
                quantity="70"
                label="60.0000001 - 70" />
              <ColorMapEntry
                color="#FFFF73"
                opacity="1"
                quantity="70.000001"
                label="70.0000001 - 80" />
              <ColorMapEntry
                color="#FFFF73"
                opacity="1"
                quantity="80"
                label="70.0000001 - 80" />
              <ColorMapEntry
                color="#FFAA00"
                opacity="1"
                quantity="80.000001"
                label="80.0000001 - 90" />
              <ColorMapEntry
                color="#FFAA00"
                opacity="1"
                quantity="90"
                label="80.0000001 - 90" />
            </ColorMap>
          </RasterSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </UserLayer>
</StyledLayerDescriptor>