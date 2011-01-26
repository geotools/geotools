<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" 
	xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
	xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
	xmlns:xlink="http://www.w3.org/1999/xlink" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer> <Name> poi </Name>
    <UserStyle>
        
        
        <FeatureTypeStyle>

            
<!-- all the same -->

            <Rule>  
		

		  <PointSymbolizer>
		    <Graphic>
			<Mark>
			    <WellKnownName>circle</WellKnownName>
			    <Fill>
				<CssParameter name="fill">#FF0000</CssParameter>
				<CssParameter name="fill-opacity">1.0</CssParameter>
			    </Fill>
			</Mark>
			<Size>11</Size>
		    </Graphic>

		</PointSymbolizer>
		
		<PointSymbolizer>
		    <Graphic>
			<Mark>
			    <WellKnownName>circle</WellKnownName>
			    <Fill>
				<CssParameter name="fill">#EDE513</CssParameter>
				<CssParameter name="fill-opacity">1.0</CssParameter>
			    </Fill>
			</Mark>
			<Size>7</Size>
		    </Graphic>
		
		</PointSymbolizer>
         <!--
		 <TextSymbolizer>
			    <Label>
				<ogc:PropertyName>laname</ogc:PropertyName>
			    </Label>
				
			    <Font>
				<CssParameter name="font-family">Arial</CssParameter>
				<CssParameter name="font-style">Normal</CssParameter>
				<CssParameter name="font-size">14</CssParameter>
			    </Font>
			    <Fill>
				<CssParameter name="fill">#000000</CssParameter>
			    </Fill>
                </TextSymbolizer>
           -->
               
            </Rule>
            

            
            
        </FeatureTypeStyle>
    </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
