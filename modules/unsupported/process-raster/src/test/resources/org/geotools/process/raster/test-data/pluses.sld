<StyledLayerDescriptor version="1.0.0"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:gml="http://www.opengis.net/gml"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://www.opengis.net/sld ./StyledLayerDescriptor.xsd">
    <NamedLayer>
        <Name>CurrentArrows</Name>
        <UserStyle>
            <FeatureTypeStyle>
                <Transformation>
                    <ogc:Function name="ras:RasterAsPointCollection">
                        <ogc:Function name="parameter">
                            <ogc:Literal>data</ogc:Literal>
                        </ogc:Function>
                        <ogc:Function name="parameter">
                            <ogc:Literal>scale</ogc:Literal>
                            <ogc:Function name="env">
                                <ogc:Literal>scale</ogc:Literal>
                            </ogc:Function>
                        </ogc:Function>
                    </ogc:Function>
                </Transformation>
                <Rule>
                    <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>shape://plus</WellKnownName>
                                <Stroke>
                                    <CssParameter name="stroke">#000000</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>8</Size>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>