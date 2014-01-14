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
                    <WellKnownName>square</WellKnownName>
                    <Stroke>
                      <CssParameter name="stroke">0xAAAAAA</CssParameter>
                      <CssParameter name="stroke-width">1</CssParameter>
                    </Stroke>
                  </Mark>
                  <Size>120</Size>
                </Graphic>
              </GraphicFill>
            </Fill>
          </PolygonSymbolizer>
          <PolygonSymbolizer>
            <Fill>
              <GraphicFill>
                <Graphic>
                  <ExternalGraphic>
                    <OnlineResource xlink:href="./draw.png" />
                    <Format>image/png</Format>
                  </ExternalGraphic>
                </Graphic>
              </GraphicFill>
            </Fill>
            <Stroke/>
            <VendorOption name="random">grid</VendorOption>
            <VendorOption name="random-symbol-count">9</VendorOption>
            <VendorOption name="random-seed">5</VendorOption>
            <VendorOption name="random-tile-size">120</VendorOption>
            <VendorOption name="random-rotation">free</VendorOption>
          </PolygonSymbolizer>

        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>