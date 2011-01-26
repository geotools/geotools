<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0"
  xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>

    <Name>Grass</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
          <PolygonSymbolizer>
            <Fill>
              <CssParameter name="fill">0x00BB00</CssParameter>
              <CssParameter name="fill-opacity">0.1</CssParameter>
            </Fill>
            <Stroke>
              <CssParameter name="stroke">0x0000FF</CssParameter>
              <CssParameter name="stroke-width">0.1</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
          <PolygonSymbolizer>
            <Geometry>
              <ogc:Function name="isometric">
                <ogc:PropertyName>geom</ogc:PropertyName>
                <ogc:Literal>2</ogc:Literal>
              </ogc:Function>
            </Geometry>
            <Fill>
              <CssParameter name="fill">0x00BB00</CssParameter>
              <CssParameter name="fill-opacity">0.1</CssParameter>
            </Fill>
            <Stroke>
              <CssParameter name="stroke">0x0000FF</CssParameter>
              <CssParameter name="stroke-width">0.1</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
            
          <PolygonSymbolizer>
            <Geometry>
              <ogc:Function name="offset">
                <ogc:PropertyName>geom</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
                <ogc:Literal>2</ogc:Literal>
              </ogc:Function>
            </Geometry>
            <Fill>
              <CssParameter name="fill">0xAAFFAA</CssParameter>
              <CssParameter name="fill-opacity">0.6</CssParameter>
            </Fill>
            <Stroke>
              <CssParameter name="stroke">0x0000FF</CssParameter>
              <CssParameter name="stroke-width">0.1</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
         </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>