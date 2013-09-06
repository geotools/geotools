<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xmlns:xlink="http://www.w3.org/1999/xlink" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" xmlns:se="http://www.opengis.net/se">
  <NamedLayer>
    <Name>topo_area</Name>
    <UserStyle>
      <Name>topo_area</Name>
      <FeatureTypeStyle>
        <Rule>
          <Name>Rough Grass Scattered Rocks And Scattered Boulders</Name>
          <PolygonSymbolizer>
            <Fill>
              <GraphicFill>
                <Graphic>
                  <Mark>
                    <WellKnownName>square</WellKnownName>
                    <Fill>
                      <CssParameter name="fill">0xccffcc</CssParameter>
                    </Fill>
                    <Stroke>
                      <CssParameter name="stroke">0xffffff</CssParameter>
                      <CssParameter name="stroke-width">2</CssParameter>
                    </Stroke>
                  </Mark>
                  <Size>64</Size>
                </Graphic>
              </GraphicFill>
            </Fill>
          </PolygonSymbolizer>
          <PolygonSymbolizer>
            <Fill>
              <GraphicFill>
                <Graphic>
                  <ExternalGraphic>
                    <OnlineResource xlink:type="simple" xlink:href="./rockFillSymbol.png"/>
                    <Format>image/png</Format>
                  </ExternalGraphic>
                </Graphic>
              </GraphicFill>
            </Fill>
            <VendorOption name="graphic-margin">17 35 35 17</VendorOption>
          </PolygonSymbolizer>
          <PolygonSymbolizer>
            <Fill>
              <GraphicFill>
                <Graphic>
                  <ExternalGraphic>
                    <OnlineResource xlink:type="simple" xlink:href="./boulderGeometry.png"/>
                    <Format>image/png</Format>
                  </ExternalGraphic>
                </Graphic>
              </GraphicFill>
            </Fill>
            <VendorOption name="graphic-margin">35 17 17 35</VendorOption>
          </PolygonSymbolizer>
          <PolygonSymbolizer>
            <Fill>
              <GraphicFill>
                <Graphic>
                  <ExternalGraphic>
                    <OnlineResource xlink:type="simple" xlink:href="./roughGrassFillSymbol.png"/>
                    <Format>image/png</Format>
                  </ExternalGraphic>
                </Graphic>
              </GraphicFill>
            </Fill>
            <VendorOption name="graphic-margin">16 16 32 32</VendorOption>
          </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
