<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor>
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
              <CssParameter name="stroke-dasharray">
                <Literal>5 35</Literal>
              </CssParameter>
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
              <CssParameter name="stroke-dasharray">
                <Add>
                  <Literal>5</Literal>
                  <Literal>5</Literal>
                </Add>
                <Sub>
                    <Literal>35</Literal>
                    <Literal>5</Literal>
                </Sub>
              </CssParameter>
              <CssParameter name="stroke-dashoffset">20</CssParameter>
            </Stroke>
          </LineSymbolizer>

        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
