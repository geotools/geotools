<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
    <UserLayer>
        <Name>NoopColormap</Name>
        <LayerFeatureConstraints>
            <FeatureTypeConstraint/>
        </LayerFeatureConstraints>
        <UserStyle>
            <Title>NoOp Colormap</Title>
            <Abstract>NoOp</Abstract>
            <FeatureTypeStyle>
                <Name>name</Name>
                <Transformation>
                    <ogc:Function name="NoOp">
                    </ogc:Function>
                </Transformation>
                <Rule>
                    <RasterSymbolizer>
                        <Opacity>1.0</Opacity>
                        <Geometry>
                          <ogc:PropertyName>grid</ogc:PropertyName>
                        </Geometry>
                        <ColorMap extended="true" type="ramp">
                            <ColorMapEntry color="000000" opacity="0" quantity="-9999"/>
                            <ColorMapEntry color="000000" opacity="0" quantity="0"/>
                            <ColorMapEntry color="#efedf5" quantity="15.0"/>
                            <ColorMapEntry color="#dadaeb" quantity="16.0"/>
                            <ColorMapEntry color="#bcbddc" quantity="17.0"/>
                            <ColorMapEntry color="#9e9ac8" quantity="18.0"/>
                            <ColorMapEntry color="#807dba" quantity="19.0"/>
                            <ColorMapEntry color="#6a51a3" quantity="20.0"/>
                        </ColorMap>
                        <ContrastEnhancement/>
                    </RasterSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </UserLayer>
</StyledLayerDescriptor>
