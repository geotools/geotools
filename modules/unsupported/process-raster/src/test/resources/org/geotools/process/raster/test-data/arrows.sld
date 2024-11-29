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
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <!--
                Using stroke rather than fill to safeguard against small scale changes
                If the shape becomes too small the fill will disappear, but the stroke will remain
                -->
                <Stroke>
                  <CssParameter name="stroke">
                    <ogc:Function name="if_then_else">
                      <ogc:Function name="equalTo">
                        <ogc:PropertyName>GRAY_INDEX</ogc:PropertyName>
                        <ogc:Literal>-32767.0</ogc:Literal>
                      </ogc:Function>
                      <ogc:Literal>#808080</ogc:Literal>
                      <ogc:Literal>#000000</ogc:Literal>
                    </ogc:Function>
                  </CssParameter>
                </Stroke>
              </Mark>
              <Size>1</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule> 
      </FeatureTypeStyle> 
    </UserStyle> 
  </NamedLayer> 
</StyledLayerDescriptor>