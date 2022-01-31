<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
                       xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <Name>hel-zigzag</Name>
    <UserStyle>
      <Title>A teal line style</Title>
      <FeatureTypeStyle>
        <Rule>
          <Name>Ruhjeen_linja</Name>
          <LineSymbolizer>
            <Stroke>
                      <CssParameter name="stroke">0x0000AA
                      </CssParameter>
                      <CssParameter name="stroke-width">2</CssParameter>
                      <CssParameter name="stroke-linecap">round</CssParameter>
              </Stroke>			  
          </LineSymbolizer>
          <LineSymbolizer>
            <Stroke>
              <GraphicStroke>
                <Graphic>
                  <Mark>                    					
					<WellKnownName>wkt://LINESTRING (0 0, 0 -0.5, 0.5 -0.5, 0.5 0.5, 1 0.5, 1 0)</WellKnownName>
                    <Stroke>
                      <CssParameter name="stroke">0xFF0000
                      </CssParameter>
                      <CssParameter name="stroke-width">2</CssParameter>
                      <CssParameter name="stroke-linecap">round</CssParameter>
                    </Stroke>															
                  </Mark>				  
                  <Size>20</Size>  				  				  
                </Graphic>                
              </GraphicStroke>			  
            </Stroke>			
			<VendorOption name="markAlongLine">true</VendorOption>					
          </LineSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>