<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld"
  xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>

    <Name>Grass</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
          <ogc:Filter>
        	<ogc:PropertyIsEqualTo>
        	  <ogc:PropertyName>type</ogc:PropertyName>
        	  <ogc:Literal>minor</ogc:Literal>
        	</ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <PolygonSymbolizer>
            <Fill>
              <CssParameter name="fill">0x0000FF</CssParameter>
            </Fill>
            <Stroke>
              <CssParameter name="stroke">0x000000</CssParameter>
              <CssParameter name="stroke-width">1</CssParameter>
              <!-- In the bug the test checks, this will alter the composition and make fill colors
                   of the major square blend with the bg -->
              <CssParameter name="stroke-opacity">0.5</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
      <FeatureTypeStyle>
        <Rule>
          <ogc:Filter>
        	<ogc:PropertyIsEqualTo>
        	  <ogc:PropertyName>type</ogc:PropertyName>
        	  <ogc:Literal>major</ogc:Literal>
        	</ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <PolygonSymbolizer>
            <Fill>
              <CssParameter name="fill">0x0000FF</CssParameter>
            </Fill>
            <Stroke>
              <CssParameter name="stroke">0x000000</CssParameter>
              <CssParameter name="stroke-width">1</CssParameter>
              <CssParameter name="stroke-opacity">0.5</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
      
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>