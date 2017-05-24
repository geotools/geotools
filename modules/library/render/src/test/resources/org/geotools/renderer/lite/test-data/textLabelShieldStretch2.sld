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
            <Label><ogc:PropertyName>name</ogc:PropertyName></Label>
            <Graphic>
              <Mark>
                <WellKnownName>square</WellKnownName>
                <Fill>
                  <CssParameter name="fill">0x55FF55</CssParameter>
                </Fill>
              </Mark>
            </Graphic>
            <Font>
              <CssParameter name="font-family">Bitstream Vera Sans</CssParameter>
              <CssParameter name="font-size">24</CssParameter>
            </Font>
             <LabelPlacement>
              <PointPlacement>
        
                <AnchorPoint>
                  <AnchorPointX>0</AnchorPointX>
                  <AnchorPointY>0</AnchorPointY>
                </AnchorPoint>

                <Displacement>
                  <DisplacementX>0</DisplacementX>
                  <DisplacementY>-10</DisplacementY>
                </Displacement>  
              </PointPlacement>
            </LabelPlacement>   
            <VendorOption name="graphic-resize">stretch</VendorOption>
            <VendorOption name="graphic-margin">4</VendorOption>
          </TextSymbolizer>
        </Rule>


      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
