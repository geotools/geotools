<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>
    <Name>Layer</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
          <TextSymbolizer>
            <Label>lbl<ogc:PropertyName>id</ogc:PropertyName></Label>
            <Font>
              <CssParameter name="font-family">SansSerif</CssParameter>
              <CssParameter name="font-size">16</CssParameter>
            </Font>
            <LabelPlacement>
              <PointPlacement>
                <AnchorPoint>
                    <AnchorPointX><ogc:PropertyName>ax</ogc:PropertyName></AnchorPointX>
                    <AnchorPointY><ogc:PropertyName>ay</ogc:PropertyName></AnchorPointY>
                </AnchorPoint>
                <Rotation><ogc:PropertyName>rotation</ogc:PropertyName></Rotation>
              </PointPlacement>
            </LabelPlacement>
            <Fill>
              <CssParameter name="fill">#0000AA</CssParameter>
            </Fill>
          </TextSymbolizer>
        </Rule>


      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
