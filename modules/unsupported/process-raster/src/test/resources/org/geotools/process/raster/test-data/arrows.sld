<StyledLayerDescriptor version="1.0.0" 
                       xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" 
                       xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" 
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                       xsi:schemaLocation="http://www.opengis.net/sld ./StyledLayerDescriptor.xsd"> 
  <NamedLayer> 
    <Name>CurrentArrows</Name> 
    <UserStyle> 
      <FeatureTypeStyle> 
        <Transformation> 
          <ogc:Function name="ras:RasterAsPointCollection">
            <ogc:Function name="parameter"> 
              <ogc:Literal>data</ogc:Literal> 
            </ogc:Function>
          </ogc:Function>
        </Transformation> 
        <Rule>
          <ogc:Filter>
            <ogc:PropertyIsNotEqualTo>
              <ogc:PropertyName>GRAY_INDEX</ogc:PropertyName>
              <ogc:Literal>NaN</ogc:Literal>
            </ogc:PropertyIsNotEqualTo>
          </ogc:Filter>
          <TextSymbolizer> 
            <Label><![CDATA[ ]]></Label> <!-- fake label -->
             <Graphic>
                  <Mark>
                    <WellKnownName>circle</WellKnownName>
                    <Fill>
                      <CssParameter name="fill">0x000000
                      </CssParameter>
                    </Fill>
                  </Mark>
                 <Size>
                    <ogc:Mul>
                      <ogc:PropertyName>GRAY_INDEX</ogc:PropertyName>
                      <ogc:Literal>0.5</ogc:Literal>
                    </ogc:Mul>
                  </Size>
            </Graphic> 
          </TextSymbolizer> 
        </Rule> 
      </FeatureTypeStyle> 
    </UserStyle> 
  </NamedLayer> 
</StyledLayerDescriptor>