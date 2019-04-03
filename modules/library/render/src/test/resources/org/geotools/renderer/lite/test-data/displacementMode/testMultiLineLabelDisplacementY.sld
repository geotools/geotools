<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <NamedLayer>
    <Name>Toponym_annotation_straight_T</Name>
    <UserStyle>
      <Title>MultiLineLabelDisplacementY</Title>
      
      <FeatureTypeStyle>
        <Rule>
          <Name>Test</Name>          
          <TextSymbolizer> <!-- uom="http://www.opengeospatial.org/se/units/metre" -->
            <Geometry>
                <ogc:Function name="centroid">
                    <ogc:PropertyName>the_geom</ogc:PropertyName>
                </ogc:Function>
            </Geometry>
            <Label>
              <ogc:PropertyName>TEXTSTRING</ogc:PropertyName>
            </Label>
            <Font>
                <CssParameter name="font-family">
                    <ogc:PropertyName>FONTNAME</ogc:PropertyName>
                </CssParameter>
                <CssParameter name="font-size">
                    <ogc:Mul>
                        <ogc:PropertyName>FONTSIZE</ogc:PropertyName>
                        <ogc:Literal>2</ogc:Literal>                     
                    </ogc:Mul>
                </CssParameter>
                <CssParameter name="font-weight">
                    <ogc:PropertyName>FONTSTYLE</ogc:PropertyName>
                </CssParameter>
            </Font>
            <LabelPlacement>
                <PointPlacement>
                    <AnchorPoint>
                        <AnchorPointX>0.5</AnchorPointX> <!--0.5 -->
                        <AnchorPointY>
                            <ogc:PropertyName>ANCHORY</ogc:PropertyName>
                        </AnchorPointY>
                    </AnchorPoint>
                    <Displacement>
                        <DisplacementX>0</DisplacementX>
                        <DisplacementY>0</DisplacementY> <!--22-->
                    </Displacement>
                    <Rotation>
                        <ogc:Sub>
                            <ogc:Literal>360</ogc:Literal>
                            <ogc:PropertyName>ANGLE</ogc:PropertyName>
                        </ogc:Sub>
                    </Rotation>
                </PointPlacement>
            </LabelPlacement>
            <Halo>
                <Radius>1</Radius>
                <Fill>
                    <CssParameter name="fill">#00FF00</CssParameter>
                    <CssParameter name="fill-opacity">0.7</CssParameter>
                </Fill>
            </Halo>
            <Fill>
                <CssParameter name="fill">#FF0000</CssParameter>
                <CssParameter name="fill-opacity">1</CssParameter>
            </Fill>
            <VendorOption name="conflictResolution">false</VendorOption>
            <VendorOption name="partials">true</VendorOption>
          </TextSymbolizer>
        </Rule>
        
        <Rule>
            <Title>Reference point</Title>
            <PointSymbolizer>  <!-- uom="http://www.opengeospatial.org/se/units/metre" -->
                <Geometry>
                    <ogc:Function name="centroid">
                        <ogc:PropertyName>the_geom</ogc:PropertyName>
                    </ogc:Function>
                </Geometry> 
                <Graphic>
                    <Mark>
                        <WellKnownName>circle</WellKnownName>
                        <Fill>
                            <CssParameter name="fill">#000eff</CssParameter>
                        </Fill>
                    </Mark>
                    <Size>10</Size>
                    <Rotation>0</Rotation>
                </Graphic>
            </PointSymbolizer>
        </Rule>
        
        <Rule>
            <Title>Reference baseline</Title>
            <LineSymbolizer>  <!-- uom="http://www.opengeospatial.org/se/units/metre" -->
                <Stroke>
                    <CssParameter name="stroke">#0033ff</CssParameter>
                    <CssParameter name="stroke-width">2</CssParameter>
                </Stroke>
            </LineSymbolizer>
        </Rule>
      </FeatureTypeStyle>
      
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>