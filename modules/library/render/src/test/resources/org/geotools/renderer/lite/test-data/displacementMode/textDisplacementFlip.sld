<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>
    <Name>Layer</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
          <PointSymbolizer>
            <Graphic>
                <Mark>
                    <WellKnownName>square</WellKnownName>
                    <Fill>
				      <CssParameter name="fill">#FF0000</CssParameter>
				    </Fill>
                </Mark>
                <Size>4</Size>
            </Graphic>
          </PointSymbolizer>
          <TextSymbolizer>
            <Label><ogc:PropertyName>label</ogc:PropertyName></Label>
            <Font>
              <CssParameter name="font-family">Bitstream Vera Sans</CssParameter>
              <CssParameter name="font-size">14</CssParameter>
            </Font>
            <LabelPlacement>
                <PointPlacement>    
                <AnchorPoint>
                    <AnchorPointX>0</AnchorPointX>
                    <AnchorPointY>0.5</AnchorPointY>
                </AnchorPoint>
                <Displacement>
                    <DisplacementX>10</DisplacementX>
                    <DisplacementY>0</DisplacementY>
                </Displacement>
                </PointPlacement>
            </LabelPlacement>
            <VendorOption name="maxDisplacement">11</VendorOption>
            <VendorOption name="displacementMode">E, W</VendorOption>
            <VendorOption name="displacementAnchor">outside</VendorOption>
          </TextSymbolizer>
        </Rule>


      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
