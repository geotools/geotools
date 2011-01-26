<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld
http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" version="1.0.0">
<UserLayer>
	<Name>raster_layer</Name>
        <LayerFeatureConstraints>
            <FeatureTypeConstraint/>
        </LayerFeatureConstraints>
	<UserStyle>
		<Name>raster</Name>
		<Title>A boring default style</Title>
		<Abstract>A sample style for rasters</Abstract>
		<FeatureTypeStyle>
	        <FeatureTypeName>Feature</FeatureTypeName>
			<Rule>
				<RasterSymbolizer>
					<ChannelSelection>
						<GrayChannel>
							<SourceChannelName>1</SourceChannelName>
								<ContrastEnhancement>
									<Exponential/>
								</ContrastEnhancement>
						</GrayChannel>
					</ChannelSelection>
					<ShadedRelief>
						<ReliefFactor>0.3</ReliefFactor>
					</ShadedRelief>
					<ImageOutline>
						<Stroke>
							<CssParameter name="stroke">
								<ogc:Literal>#AA3333</ogc:Literal>
							</CssParameter>
							<CssParameter name="stroke-width">
								<ogc:Literal>2</ogc:Literal>
							</CssParameter>
						</Stroke>
					</ImageOutline>
					<OverlapBehavior>
						AVERAGE
					</OverlapBehavior>
				    <Opacity>1.0</Opacity>
				</RasterSymbolizer>
			</Rule>
		</FeatureTypeStyle>
	</UserStyle>
</UserLayer>
</StyledLayerDescriptor>
