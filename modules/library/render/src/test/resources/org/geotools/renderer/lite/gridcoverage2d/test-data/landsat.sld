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
							<RedChannel>
								<SourceChannelName>4</SourceChannelName>
							</RedChannel>
							<GreenChannel>
								<SourceChannelName>3</SourceChannelName>
							</GreenChannel>
							<BlueChannel>
								<SourceChannelName>2</SourceChannelName>
							</BlueChannel>
						</ChannelSelection>
						<!--ContrastEnhancement>
							<Histogram/>
							<GammaValue>1.001</GammaValue>
					    </ContrastEnhancement-->
					</RasterSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</UserLayer>
</StyledLayerDescriptor>
