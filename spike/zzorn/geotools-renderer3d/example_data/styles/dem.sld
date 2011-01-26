<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld
http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" version="1.0.0">
<UserLayer>
	<Name>gtopo</Name>
	<UserStyle>
		<Name>DEM_GTOPO</Name>
		<Title>dem style</Title>
		<Abstract>dem style</Abstract>
		<FeatureTypeStyle>
	        <FeatureTypeName>Feature</FeatureTypeName>
			<Rule>
				<RasterSymbolizer>
				    <Opacity>1.0</Opacity>
				    <ColorMap>
				       <ColorMapEntry color="#000000" quantity="-500" label="nodata" opacity="0.0"/>
				       <ColorMapEntry color="#003300" quantity="0" label="values"/>
				       <ColorMapEntry color="#333300" quantity="20" label="values"/>
				       <ColorMapEntry color="#CC9900" quantity="50" label="values"/>
				       <ColorMapEntry color="#996600" quantity="100" label="values"/>
				       <ColorMapEntry color="#996633" quantity="150" label="values"/>
				       <ColorMapEntry color="#CC6600" quantity="300" label="values"/>
				       <ColorMapEntry color="#993300" quantity="800" label="values"/>
				       <ColorMapEntry color="#663300" quantity="1100" label="values"/>
				       <ColorMapEntry color="#663333" quantity="1800" label="values"/>
				       <ColorMapEntry color="#ffffff" quantity="2500" label="values"/>
				       <ColorMapEntry color="#ffffff" quantity="3000" label="values"/>
				       <ColorMapEntry color="#ffffff" quantity="4000" label="values"/>
				       <ColorMapEntry color="#ffffff" quantity="6000" label="values"/>
				       <ColorMapEntry color="#ffffff" quantity="7000" label="values"/>
				       <ColorMapEntry color="#ffffff" quantity="8000" label="values"/>
				       <ColorMapEntry color="#ffffff" quantity="9000" label="values"/>
				    </ColorMap>
				    <OverlapBehavior>
				       <AVERAGE/>
				    </OverlapBehavior>
				    <ShadedRelief/>
				</RasterSymbolizer>
			</Rule>
		</FeatureTypeStyle>
	</UserStyle>
</UserLayer>
</StyledLayerDescriptor>