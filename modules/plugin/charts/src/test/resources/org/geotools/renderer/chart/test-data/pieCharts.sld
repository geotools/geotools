<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
	xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
	xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<NamedLayer>
		<Name></Name>
		<title></title>
		<abstract></abstract>
		<UserStyle>
			<Name>Pie charts</Name>
			<FeatureTypeStyle>
				<Rule>
					<PointSymbolizer>
						<Graphic>
							<ExternalGraphic>
								<OnlineResource xlink:href="http://chart?cht=p&amp;chl=male|female&amp;chd=t:${100 * male / (male + female)},${100 * female / (male + female)}&amp;chs=200x100&amp;chf=bg,s,FFFFFF00"/>
								<Format>application/chart</Format>
							</ExternalGraphic>
						</Graphic>
					</PointSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</NamedLayer>
</StyledLayerDescriptor>
