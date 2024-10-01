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
                <CssParameter name="stroke-width">10</CssParameter>
            </Stroke>
          </LineSymbolizer>
          <TextSymbolizer>
            <Label><![CDATA[ ]]></Label>
            <Font>
              <CssParameter name="font-family">Sans</CssParameter>
              <CssParameter name="font-size">2</CssParameter>
            </Font>
            <LabelPlacement>
                <LinePlacement/>
            </LabelPlacement>
            <Graphic>
              <Mark>
                <WellKnownName>extshape://arrow?hr=3</WellKnownName>
                <Fill>
                  <CssParameter name="fill">0xFFFF00</CssParameter>
                </Fill>
              </Mark>
              <Rotation>90.0</Rotation>
              <Size>20</Size>
            </Graphic>
            <VendorOption name="forceLeftToRight">false</VendorOption>
            <VendorOption name="repeat">100</VendorOption>
          </TextSymbolizer>
        </Rule>


      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
