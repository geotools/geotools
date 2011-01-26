<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld"
  xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>

    <Name>Grass</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">0x666666</CssParameter>
              <CssParameter name="stroke-width">4</CssParameter>
            </Stroke>
          </LineSymbolizer>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">0xFFFFFF</CssParameter>
              <CssParameter name="stroke-width">2</CssParameter>
            </Stroke>
          </LineSymbolizer>
          <LineSymbolizer>
            <Stroke>
              <GraphicStroke>
                <Graphic>
                  <Mark>
                    <WellKnownName>shape://vertline</WellKnownName>
                    <Stroke>
                      <CssParameter name="stroke">0x666666</CssParameter>
                      <CssParameter name="stroke-width">2</CssParameter>
                    </Stroke>
                  </Mark>
                  <Size>8</Size>
                </Graphic>
              </GraphicStroke>
            </Stroke>
          </LineSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>