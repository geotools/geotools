<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
 xmlns="http://www.opengis.net/sld" 
 xmlns:ogc="http://www.opengis.net/ogc" 
 xmlns:xlink="http://www.w3.org/1999/xlink" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>default_polygon</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
         <PolygonSymbolizer>
           <Stroke>
             <CssParameter name="stroke">
               #<ogc:Function name="env">
                  <ogc:Literal>stroke</ogc:Literal>
                  <ogc:Literal>0000FF</ogc:Literal>
               </ogc:Function>
             </CssParameter>
           </Stroke>
         </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
