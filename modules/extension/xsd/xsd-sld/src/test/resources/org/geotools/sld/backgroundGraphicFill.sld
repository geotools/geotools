<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
                       xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <NamedLayer>
        <Name>Test</Name>
        <UserStyle>
            <Name>Default Styler</Name>
            <Background>
                <GraphicFill>
                    <Graphic>
                        <Mark>
                            <WellKnownName>square</WellKnownName>
                            <Fill>
                                <CssParameter name="fill">#808080</CssParameter>
                                <CssParameter name="fill-opacity">1.0</CssParameter>
                            </Fill>
                            <Stroke>
                                <CssParameter name="stroke">#000000</CssParameter>
                                <CssParameter name="stroke-linecap">butt</CssParameter>
                                <CssParameter name="stroke-linejoin">miter</CssParameter>
                                <CssParameter name="stroke-opacity">1</CssParameter>
                                <CssParameter name="stroke-width">1</CssParameter>
                                <CssParameter name="stroke-dashoffset">0</CssParameter>
                            </Stroke>
                        </Mark>
                        <Opacity>1.0</Opacity>
                        <Rotation>0.0</Rotation>
                    </Graphic>
                </GraphicFill>
            </Background>
            <FeatureTypeStyle>
                <Name>name</Name>
                <Rule>
                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">#000000</CssParameter>
                            <CssParameter name="stroke-linecap">butt</CssParameter>
                            <CssParameter name="stroke-linejoin">miter</CssParameter>
                            <CssParameter name="stroke-opacity">1</CssParameter>
                            <CssParameter name="stroke-width">1</CssParameter>
                            <CssParameter name="stroke-dashoffset">0</CssParameter>
                        </Stroke>
                    </LineSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>

</StyledLayerDescriptor>
