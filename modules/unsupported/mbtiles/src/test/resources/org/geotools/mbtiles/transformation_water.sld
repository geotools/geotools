<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
  xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
  xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>transformation test</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Transformation>
          <ogc:Function name="AttributeRename">
            <ogc:Literal>class</ogc:Literal>
            <ogc:Literal>clazz</ogc:Literal>
            <ogc:Literal>true</ogc:Literal>
          </ogc:Function>
        </Transformation>
        <Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>clazz</ogc:PropertyName>
              <ogc:Literal>lake</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <PolygonSymbolizer>
                <Fill>
                  <CssParameter name="fill">0xFF0000
                  </CssParameter>
                </Fill>
          </PolygonSymbolizer>
        </Rule>
        <Rule>
                  <ogc:Filter>
                    <ogc:PropertyIsEqualTo>
                      <ogc:PropertyName>clazz</ogc:PropertyName>
                      <ogc:Literal>ocean</ogc:Literal>
                    </ogc:PropertyIsEqualTo>
                  </ogc:Filter>
                  <PolygonSymbolizer>
                        <Fill>
                          <CssParameter name="fill">0x00FF00
                          </CssParameter>
                        </Fill>
                  </PolygonSymbolizer>
                </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>