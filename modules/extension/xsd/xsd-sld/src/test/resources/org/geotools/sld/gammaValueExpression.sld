<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0"
    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <NamedLayer>

        <Name>raster</Name>
        <UserStyle>
            <FeatureTypeStyle>
                <Rule>
                    <RasterSymbolizer>
                        <ContrastEnhancement>
                            <GammaValue>
                                <ogc:Add>
                                    <ogc:Literal>1.0</ogc:Literal>
                                    <ogc:Literal>0.5</ogc:Literal>
                                </ogc:Add>
                            </GammaValue>
                        </ContrastEnhancement>
                    </RasterSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>