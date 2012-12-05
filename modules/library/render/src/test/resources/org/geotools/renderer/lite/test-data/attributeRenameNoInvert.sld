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
            <ogc:Literal>id</ogc:Literal>
            <ogc:Literal>idx</ogc:Literal>
          </ogc:Function>
        </Transformation>
        <Rule>
          <ogc:Filter>
            <ogc:PropertyIsLessThan>
              <ogc:PropertyName>idx</ogc:PropertyName>
              <ogc:Literal>7</ogc:Literal>
            </ogc:PropertyIsLessThan>
          </ogc:Filter>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">0xFF0000
                  </CssParameter>
                </Fill>
              </Mark>
              <Size>32</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>