<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld
http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" version="1.0.0">
	<UserLayer>
		<Name>raster_layer</Name>
		<UserStyle>
			<Name></Name>
			<Title></Title>
			<Abstract></Abstract>
			<FeatureTypeStyle>
		        <FeatureTypeName>coverage</FeatureTypeName>
					<Rule>
						<RasterSymbolizer>
							<Opacity>1.0</Opacity>
							<ColorMap type="ramp" extended="true">
								<ColorMapEntry color="#ff0000" quantity="100" opacity="1.0"/>
								<ColorMapEntry color="#00ff00" quantity="500" opacity="0.8"/>
								<ColorMapEntry color="#0000ff" quantity="900" opacity="0.2"/>
							</ColorMap>					
							<OverlapBehavior>
								AVERAGE
							</OverlapBehavior>
							<ShadedRelief/>
						</RasterSymbolizer>
					</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</UserLayer>
</StyledLayerDescriptor>
