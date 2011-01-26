<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld"
	xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.opengis.net/sld
http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
	version="1.0.0">
	<UserLayer>
		<Name>raster_layer</Name>
		<LayerFeatureConstraints>
			<FeatureTypeConstraint />
		</LayerFeatureConstraints>
		<UserStyle>
			<Name>watemp</Name>
			<Title>Test opacity</Title>
			<Abstract>Test opacity option</Abstract>
			<FeatureTypeStyle>
				<FeatureTypeName>Feature</FeatureTypeName>
				<Rule>
					<RasterSymbolizer>
						<ColorMap type="intervals">
							<ColorMapEntry color="#008000" quantity="150" />
							<ColorMapEntry color="#663333" quantity="256" />
						</ColorMap>
					</RasterSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</UserLayer>
</StyledLayerDescriptor>