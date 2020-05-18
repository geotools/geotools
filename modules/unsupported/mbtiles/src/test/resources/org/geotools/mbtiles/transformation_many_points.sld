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
            <ogc:Literal>Double3</ogc:Literal>
            <ogc:Literal>TheDoubleValue</ogc:Literal>
            <ogc:Literal>true</ogc:Literal>
          </ogc:Function>
        </Transformation>
        <Rule>
          <Title>Double3</Title>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>square</WellKnownName>
                <Fill>
     <CssParameter name="fill">
       <ogc:Function name="Interpolate">
         <!-- Property to transform -->
         <ogc:PropertyName>TheDoubleValue</ogc:PropertyName>

         <!-- Mapping curve definition pairs (input, output) -->
         <ogc:Literal>0</ogc:Literal>
         <ogc:Literal>#FFFFFF</ogc:Literal>

         <ogc:Literal>3.2</ogc:Literal>
         <ogc:Literal>#4444FF</ogc:Literal>

         <ogc:Literal>3.5</ogc:Literal>
         <ogc:Literal>#FF0000</ogc:Literal>

         <ogc:Literal>4.0</ogc:Literal>
         <ogc:Literal>#FFFF00</ogc:Literal>

         <!-- Interpolation method -->
         <ogc:Literal>color</ogc:Literal>

         <!-- Interpolation mode - defaults to linear -->
       </ogc:Function>
     </CssParameter>
                       <CssParameter name="fill-opacity">0.1</CssParameter>

   </Fill>
              </Mark>
              <Size>3</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>