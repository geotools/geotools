<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld"
  xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>

    <Name>LineBuffering</Name>
    <UserStyle>
      <FeatureTypeStyle>
        <Rule>
          <PolygonSymbolizer>
            <Geometry>
            	<ogc:Function name="buffer">
            		<ogc:PropertyName>geom</ogc:PropertyName>
            		<ogc:Literal>0.5</ogc:Literal>
            	</ogc:Function>
            </Geometry>
            <Fill/>
          </PolygonSymbolizer>
          
          
          <LineSymbolizer>
          	<Stroke/>
          </LineSymbolizer>

        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>