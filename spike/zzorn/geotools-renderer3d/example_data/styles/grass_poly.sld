<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" 
	xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
	xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" 
	xmlns:xlink="http://www.w3.org/1999/xlink" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<NamedLayer>

		<Name>Grass</Name>

		<UserStyle>
			<Name>Grass Styler</Name>
			<Title>Grass Styler</Title>
			<Abstract></Abstract>
			<FeatureTypeStyle>
				<FeatureTypeName>Feature</FeatureTypeName>
				<Rule>
					<Name>Grass</Name>
					<Abstract>Grass style that uses a texture</Abstract>

					<PolygonSymbolizer>
						<Fill>
							<GraphicFill>
								<Graphic>
									<ExternalGraphic>
										<OnlineResource xlink:type="simple" xlink:href="grass_fill.png"/>
										<Format>image/png</Format>
									</ExternalGraphic>
									<Opacity>
										<ogc:Literal>1.0</ogc:Literal>
									</Opacity>
									<Size>
										<ogc:Literal>30</ogc:Literal>
									</Size>
									<Rotation>
										<ogc:Literal>0.5</ogc:Literal>
									</Rotation>
								</Graphic>
							</GraphicFill>
						</Fill>

						<Stroke>
							<CssParameter name="stroke">#FF0000</CssParameter>
							<CssParameter name="stroke-width">1</CssParameter>
						</Stroke>
					</PolygonSymbolizer>

				</Rule>
			</FeatureTypeStyle>
		</UserStyle>

    </NamedLayer>

</StyledLayerDescriptor>
