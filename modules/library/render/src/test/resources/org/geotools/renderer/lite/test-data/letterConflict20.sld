<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>
    <Name>Layer</Name>
    <UserStyle>
      <FeatureTypeStyle>

        <Rule>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">0xAAAAAA</CssParameter>
            </Stroke>
          </LineSymbolizer>
          <TextSymbolizer>
            <VendorOption name="followLine">true</VendorOption>
            <Label><ogc:PropertyName>name</ogc:PropertyName></Label>
            <Font>
              <CssParameter name="font-family">SansSerif</CssParameter>
              <CssParameter name="font-size">20</CssParameter>
            </Font>
            <LabelPlacement>
              <LinePlacement/>
            </LabelPlacement>
          </TextSymbolizer>
        </Rule>

      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>