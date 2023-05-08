<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xmlns:xlink="http://www.w3.org/1999/xlink" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" xmlns:se="http://www.opengis.net/se">
  <NamedLayer>
    <Name>Mark-margin-rotation</Name>
    <UserStyle>
      <Name>Mark-margin-rotation</Name>
      <FeatureTypeStyle>
        <Rule>
          <PolygonSymbolizer>
            <Fill>
              <GraphicFill>
                <Graphic>
                  <Mark>
                    <WellKnownName>triangle</WellKnownName>
                    <Fill>
                      <CssParameter name="fill">0x00FF00</CssParameter>
                    </Fill>
                  </Mark>
                  <Size>20</Size>
                  <Rotation>45</Rotation>
                </Graphic>
              </GraphicFill>
            </Fill>
            <VendorOption name="graphic-margin">5 5 0 0</VendorOption>
          </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
