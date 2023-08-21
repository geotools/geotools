<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <NamedLayer>
        <Name>Simplify me</Name>
        <UserStyle>
            <FeatureTypeStyle>
                <Rule>
                    <ogc:Filter>
                        <ogc:PropertyIsEqualTo>
                            <ogc:PropertyName>value</ogc:PropertyName>
                            <ogc:Add>
                                <ogc:Literal>10</ogc:Literal>
                                <ogc:Function name="env">
                                    <ogc:Literal>test</ogc:Literal>
                                    <ogc:Literal>1</ogc:Literal>
                                </ogc:Function>
                            </ogc:Add>
                        </ogc:PropertyIsEqualTo>
                    </ogc:Filter>
                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">#0000</CssParameter>
                            <CssParameter name="stroke-width">
                                <ogc:Function name="pow">
                                    <ogc:Literal>2</ogc:Literal>
                                    <ogc:Literal>16</ogc:Literal>
                                </ogc:Function>
                            </CssParameter>
                        </Stroke>
                    </LineSymbolizer>
                </Rule>

            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
