<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
 xmlns="http://www.opengis.net/sld" 
 xmlns:ogc="http://www.opengis.net/ogc" 
 xmlns:xlink="http://www.w3.org/1999/xlink" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>BarnesContours</Name>
    <UserStyle>
      <Title>Barnes Surface Contours</Title>
      <Abstract>Extracts contours from a computed surface</Abstract>
      <FeatureTypeStyle>
        <Transformation>
          <ogc:Function name="ras:Contour">
            <ogc:Function name="parameter">
              <ogc:Literal>data</ogc:Literal>
              
          <ogc:Function name="vec:BarnesSurface">
            <ogc:Function name="parameter">
              <ogc:Literal>data</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>valueAttr</ogc:Literal>
              <ogc:Literal>MxTmp</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>dataLimit</ogc:Literal>
              <ogc:Literal>500</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>scale</ogc:Literal>
              <ogc:Literal>15.0</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>convergence</ogc:Literal>
              <ogc:Literal>0.2</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>passes</ogc:Literal>
              <ogc:Literal>3</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>minObservations</ogc:Literal>
              <ogc:Literal>2</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>maxObservationDistance</ogc:Literal>
              <ogc:Literal>15</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>pixelsPerCell</ogc:Literal>
              <ogc:Literal>8</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>outputBBOX</ogc:Literal>
              <ogc:Function name="env">
                <ogc:Literal>wms_bbox</ogc:Literal>
              </ogc:Function>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>outputWidth</ogc:Literal>
              <ogc:Function name="env">
                <ogc:Literal>wms_width</ogc:Literal>
              </ogc:Function>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>outputHeight</ogc:Literal>
              <ogc:Function name="env">
                <ogc:Literal>wms_height</ogc:Literal>
              </ogc:Function>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>queryBuffer</ogc:Literal>
              <ogc:Literal>40</ogc:Literal>
            </ogc:Function>
          </ogc:Function>
              
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>levels</ogc:Literal>
              <ogc:Literal>-10</ogc:Literal>
              <ogc:Literal>-5</ogc:Literal>
              <ogc:Literal>0</ogc:Literal>
              <ogc:Literal>5</ogc:Literal>
              <ogc:Literal>10</ogc:Literal>
              <ogc:Literal>15</ogc:Literal>
              <ogc:Literal>20</ogc:Literal>
              <ogc:Literal>25</ogc:Literal>
              <ogc:Literal>30</ogc:Literal>
              <ogc:Literal>35</ogc:Literal>
              <ogc:Literal>40</ogc:Literal>
            </ogc:Function>
            <ogc:Function name="parameter">
              <ogc:Literal>simplify</ogc:Literal>
              <ogc:Literal>true</ogc:Literal>
            </ogc:Function>   
          </ogc:Function>
        </Transformation>
        <Rule>
          <Name>rule1</Name>
          <Title>Isotherm</Title>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#000000</CssParameter>
              <CssParameter name="stroke-width">1</CssParameter>
            </Stroke>
          </LineSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:Function name="round">
                <ogc:PropertyName>value</ogc:PropertyName>
              </ogc:Function>
            </Label>
            <Font>
              <CssParameter name="font-family">Arial</CssParameter>
              <CssParameter name="font-style">Normal</CssParameter>
              <CssParameter name="font-size">12</CssParameter>
            </Font>
            <LabelPlacement>
              <LinePlacement/>
            </LabelPlacement>
            <Halo>
              <Radius>
                <ogc:Literal>2</ogc:Literal>
              </Radius>
              <Fill>
                <CssParameter name="fill">#FFFFFF</CssParameter>
                <CssParameter name="fill-opacity">0.6</CssParameter>        
              </Fill>
            </Halo>
            <Fill>
              <CssParameter name="fill">#000000</CssParameter>
            </Fill>
            <Priority>2000</Priority>
            <VendorOption name="followLine">true</VendorOption>
            <VendorOption name="repeat">100</VendorOption>
            <VendorOption name="maxDisplacement">50</VendorOption>
            <VendorOption name="maxAngleDelta">30</VendorOption>
          </TextSymbolizer>
         </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>