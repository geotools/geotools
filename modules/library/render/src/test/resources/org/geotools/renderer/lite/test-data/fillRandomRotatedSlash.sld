<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld"
  xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>

    <Name>Grass</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
          <PolygonSymbolizer>
            <Fill>
              <GraphicFill>
                <Graphic>
                  <Mark>
                    <WellKnownName>shape://slash</WellKnownName>
                    <Stroke>
                      <CssParameter name="stroke">0x000088</CssParameter>
                      <CssParameter name="stroke-width">4</CssParameter>
                      <CssParameter name="stroke-linecap">round</CssParameter>
                    </Stroke>
                  </Mark>
                  <Size>8</Size>
                </Graphic>
              </GraphicFill>
            </Fill>
            <Stroke/>
            <VendorOption name="random">free</VendorOption>
            <VendorOption name="random-symbol-count">30</VendorOption>
            <VendorOption name="random-seed">1</VendorOption>
            <VendorOption name="random-tile-size">100</VendorOption>
            <VendorOption name="random-rotation">free</VendorOption>
          </PolygonSymbolizer>

        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>