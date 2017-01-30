<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0"
    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <NamedLayer>

        <Name>LineBuffering</Name>
        <UserStyle>
            <FeatureTypeStyle>
                <Rule>
                    <LineSymbolizer>
                        <Stroke />
                    </LineSymbolizer>

                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">0x0000FF
                            </CssParameter>
                        </Stroke>
                        <PerpendicularOffset>10</PerpendicularOffset>
                    </LineSymbolizer>

                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">0xFF0000
                            </CssParameter>
                        </Stroke>
                        <PerpendicularOffset>-10</PerpendicularOffset>
                    </LineSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>