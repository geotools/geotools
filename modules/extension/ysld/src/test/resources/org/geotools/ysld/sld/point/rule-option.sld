<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>Rule Vendor Option</Name>
    <UserStyle>
      <Title>Simple Point With Rule Vendor Option</Title>
      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#FF0000</CssParameter>
                </Fill>
              </Mark>
              <Size>6</Size>
            </Graphic>
          </PointSymbolizer>
          <VendorOption name="inclusion">mapOnly</VendorOption>
        </Rule>
        <Rule>
          <PointSymbolizer>
            <Graphic>
              <ExternalGraphic>
                 <OnlineResource xlink:type="simple" xlink:href="smileyface.png" />
                <Format>image/png</Format>
              </ExternalGraphic>
              <Size>20</Size>
            </Graphic>
          </PointSymbolizer>
          <VendorOption name="inclusion">legendOnly</VendorOption>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
