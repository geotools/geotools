<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
	xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
	xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<NamedLayer>
		<Name>Raster Symbolizer Test SLD</Name>
		<UserStyle>
			<Name>Raster SLD</Name>
			<FeatureTypeStyle>
				<Rule>
					<Name>Raster Symbolizer Test</Name>
					<PolygonSymbolizer>
						<Geometry>
							<ogc:PropertyName>the_geom</ogc:PropertyName>
						</Geometry>
					</PolygonSymbolizer>
				</Rule>
			</FeatureTypeStyle>
		</UserStyle>
	</NamedLayer>
</StyledLayerDescriptor>
