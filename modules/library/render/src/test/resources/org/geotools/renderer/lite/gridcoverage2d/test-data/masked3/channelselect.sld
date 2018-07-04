<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld
http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" version="1.0.0">
<UserLayer>
	<Name>raster_layer</Name>
	<UserStyle>
		<Name>raster</Name>
		<Title>A boring default style</Title>
		<Abstract>A sample style for rasters</Abstract>
		<FeatureTypeStyle>
	        <FeatureTypeName>Feature</FeatureTypeName>
			<Rule>
				<RasterSymbolizer>
					<ChannelSelection>
						<RedChannel>
							<SourceChannelName>2</SourceChannelName>
						</RedChannel>
						<GreenChannel>
							<SourceChannelName>3</SourceChannelName>
						</GreenChannel>
						<BlueChannel>
							<SourceChannelName>1</SourceChannelName>
						</BlueChannel>
					</ChannelSelection>
				    <Opacity>1.0</Opacity>
				</RasterSymbolizer>
			</Rule>
		</FeatureTypeStyle>
	</UserStyle>
</UserLayer>
</StyledLayerDescriptor>
