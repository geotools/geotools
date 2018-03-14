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
              <RedChannel>
                <SourceChannelName>3</SourceChannelName>
                <ContrastEnhancement>
                  <Normalize>
                    <VendorOption name="algorithm">StretchToMinimumMaximum</VendorOption>
                    <VendorOption name="minValue">0</VendorOption>
                    <VendorOption name="maxValue">30</VendorOption>
                  </Normalize>
                </ContrastEnhancement>
              </RedChannel>
              <GreenChannel>
                <SourceChannelName>2</SourceChannelName>
                <ContrastEnhancement>
                  <Normalize>
                    <VendorOption name="algorithm">StretchToMinimumMaximum</VendorOption>
                    <VendorOption name="minValue">10</VendorOption>
                    <VendorOption name="maxValue">40</VendorOption>
                  </Normalize>
                </ContrastEnhancement>
              </GreenChannel>
              <BlueChannel>
                <SourceChannelName>1</SourceChannelName>
                <ContrastEnhancement>
                  <Normalize>
                    <VendorOption name="algorithm">StretchToMinimumMaximum</VendorOption>
                    <VendorOption name="minValue">20</VendorOption>
                    <VendorOption name="maxValue">50</VendorOption>
                  </Normalize>
                </ContrastEnhancement>
              </BlueChannel>
            </ChannelSelection>
          </RasterSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </UserLayer>
</StyledLayerDescriptor>