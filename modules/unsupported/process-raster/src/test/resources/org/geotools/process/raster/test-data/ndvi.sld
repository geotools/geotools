<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" version="1.0.0">
    <NamedLayer>
        <Name>Sentinel2 NDVI</Name>
        <UserStyle>
            <Title>NDVI</Title>
            <FeatureTypeStyle>
                <Transformation>
                    <ogc:Function name="ras:Jiffle">
                        <ogc:Function name="parameter">
                            <ogc:Literal>coverage</ogc:Literal>
                        </ogc:Function>
                        <ogc:Function name="parameter">
                            <ogc:Literal>script</ogc:Literal>
                            <ogc:Literal>
                                nir = src[7];
                                vir = src[3];
                                dest = (nir - vir) / (nir + vir);
                            </ogc:Literal>
                        </ogc:Function>
                    </ogc:Function>
                </Transformation>
                <Rule>
                    <RasterSymbolizer>
                        <Opacity>1.0</Opacity>
                        <ColorMap>
                            <ColorMapEntry color="#000000" quantity="-1"/>
                            <ColorMapEntry color="#0000ff" quantity="-0.75"/>
                            <ColorMapEntry color="#ff00ff" quantity="-0.25"/>
                            <ColorMapEntry color="#ff0000" quantity="0"/>
                            <ColorMapEntry color="#ffff00" quantity="0.5"/>
                            <ColorMapEntry color="#00ff00" quantity="1"/>
                        </ColorMap>
                    </RasterSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>