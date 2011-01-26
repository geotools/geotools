<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
  xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>
    <Name>Dots and stars</Name>
    <UserStyle>

      <FeatureTypeStyle>
        <Rule>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#000000</CssParameter>
              <CssParameter name="stroke-width">15</CssParameter>
              <CssParameter name="stroke-linejoin">round</CssParameter>
              <CssParameter name="stroke-linecap">round</CssParameter>
            </Stroke>
          </LineSymbolizer>
        </Rule>
      </FeatureTypeStyle>
      <FeatureTypeStyle>
        <Rule>
          <LineSymbolizer>
            <Stroke>
              <GraphicStroke>
                <Graphic>
                  <Mark>
                    <WellKnownName>Circle</WellKnownName>
                    <Fill>
                      <CssParameter name="fill">#FFFFFF
                      </CssParameter>
                    </Fill>
                  </Mark>
                  <Size>5</Size>
                </Graphic>
              </GraphicStroke>
              <CssParameter name="stroke-dasharray">5 35</CssParameter>
              <CssParameter name="stroke-dashoffset">0</CssParameter>
            </Stroke>
          </LineSymbolizer>
          <LineSymbolizer>
            <Stroke>
              <GraphicStroke>
                <Graphic>
                  <Mark>
                    <WellKnownName>Star</WellKnownName>
                    <Fill>
                      <CssParameter name="fill">#FFFFFF
                      </CssParameter>
                    </Fill>
                  </Mark>
                  <Size>10</Size>
                </Graphic>
              </GraphicStroke>
              <CssParameter name="stroke-dasharray">10 30</CssParameter>
              <CssParameter name="stroke-dashoffset">20</CssParameter>
            </Stroke>
          </LineSymbolizer>

        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
