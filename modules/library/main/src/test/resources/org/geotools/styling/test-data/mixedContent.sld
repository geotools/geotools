<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
  xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <Name>OCEANSEA_1M:Foundation</Name>
    <UserStyle>
      <Name>GEOSYM</Name>
      <FeatureTypeStyle>
        <Rule>
          <Name>Polygon fill mixed</Name>
          <PolygonSymbolizer>
            <Fill>
              <CssParameter name="fill"><ogc:Literal>#96</ogc:Literal>C3F5</CssParameter>
            </Fill>
          </PolygonSymbolizer>
          <TextSymbolizer>
            <Label>   
            this is a prefix; <ogc:Literal>this is an expression</ogc:Literal>; this is a postfix
            </Label>
          </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
