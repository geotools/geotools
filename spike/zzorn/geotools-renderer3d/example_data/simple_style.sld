<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
		xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
		xmlns="http://www.opengis.net/sld" 
		xmlns:ogc="http://www.opengis.net/ogc" 
		xmlns:xlink="http://www.w3.org/1999/xlink" 
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<!-- a named layer is the basic building block of an sld document -->

	<NamedLayer>
		<Name>Default Line</Name>
		<UserStyle>
		    <!-- they have names, titles and abstracts -->
		  
			<Title>A boring default style</Title>
			<Abstract>A sample style that just prints out a black line</Abstract>
			<!-- FeatureTypeStyles describe how to render different features -->
			<!-- a feature type for lines -->

			<FeatureTypeStyle>
				<!--FeatureTypeName>Feature</FeatureTypeName-->
				<Rule>
					<Name>Rule 1</Name>
					<Title>Outline</Title>
					<Abstract>A simple example line</Abstract>

					<!-- like a polygonsymbolizer -->
					<LineSymbolizer>
						<Stroke>
							<CssParameter name="stroke">#333333</CssParameter>
							<CssParameter name="stroke-width">3</CssParameter>
						</Stroke>
					</LineSymbolizer>
				</Rule>

				<Rule>
					<Name>Rule 2</Name>
					<Title>Fill</Title>
					<Abstract>A simple example line</Abstract>

					<!-- like a polygonsymbolizer -->
					<LineSymbolizer>
						<Stroke>
							<CssParameter name="stroke">#ddff33</CssParameter>
							<CssParameter name="stroke-width">1</CssParameter>
						</Stroke>
					</LineSymbolizer>
				</Rule>

		    </FeatureTypeStyle>
		</UserStyle>
	</NamedLayer>
</StyledLayerDescriptor>

