<!-- 
	This document is a mixture of test-sld.xml and namedLayers.sld
	to help testing that SLDParser manages both types of layers
	without problems
-->
<StyledLayerDescriptor version="1.0.0">
	<NamedLayer>
		<Name>First Named Layer</Name>
		<NamedStyle>
			<Name>CenterLine</Name>
		</NamedStyle>
	</NamedLayer>
	<UserLayer>
		<UserStyle>
		       <Name>First User Style</Name>
			<Title>A style by me</Title>
			<Abstract>this is a sample style</Abstract>
			<FeatureTypeStyle>
				<Rule>
					<LineSymbolizer>
						<Stroke>
							<CssParameter name="width">4</CssParameter>
						</Stroke>
					</LineSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</UserLayer>
	<NamedLayer>
		<Name>Second_Named_Layer</Name>
		<NamedStyle>
			<Name>CenterLine</Name>
		</NamedStyle>
	</NamedLayer>
	<UserLayer>
		<Name>Second_User_Layer</Name>
		<RemoteOWS>
			<Service>WFS</Service>
			<OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://some.site.com/WFS?"/>
		</RemoteOWS>
		<UserStyle>
		       <Name>Second User Style</Name>
			<Title>A style by me</Title>
			<Abstract>this is a sample style</Abstract>
			<FeatureTypeStyle>
				<Rule>
					<LineSymbolizer>
						<Stroke>
							<CssParameter name="width">4</CssParameter>
						</Stroke>
					</LineSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</UserLayer>
</StyledLayerDescriptor>