<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld
http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" version="1.0.0">
	<UserLayer>
		<Name>raster_layer</Name>
		<UserStyle>
			<Name>Landsat 7 false-color composite</Name>
			<Title>A boring default style</Title>
			<Abstract>A sample style for Landsat 7</Abstract>
			<FeatureTypeStyle>
		        <FeatureTypeName>coverage</FeatureTypeName>
				<Rule>
					<RasterSymbolizer>
					    <Opacity>1.0</Opacity>
						<ChannelSelection>
							<Gray>
								<SourceChannelName>1</SourceChannelName>
							</Gray>
						</ChannelSelection>
						<ColorMap type="values" extended="false">
							<ColorMapEntry color="#ff0000" quantity="20" opacity="1.0"/>
						</ColorMap>						
					</RasterSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</UserLayer>
</StyledLayerDescriptor>
