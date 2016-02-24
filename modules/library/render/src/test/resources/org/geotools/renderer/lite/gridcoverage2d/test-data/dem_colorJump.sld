<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
    <sld:UserLayer>
        <sld:LayerFeatureConstraints>
            <sld:FeatureTypeConstraint/>
        </sld:LayerFeatureConstraints>
        <sld:UserStyle>
            <sld:Name>srtm_boulder</sld:Name>
            <sld:Title/>
            <sld:FeatureTypeStyle>
                <sld:Name>name</sld:Name>
                <sld:Rule>
                    <sld:MinScaleDenominator>75000</sld:MinScaleDenominator>
                    <sld:RasterSymbolizer>
                        <sld:Geometry>
                            <ogc:PropertyName>grid</ogc:PropertyName>
                        </sld:Geometry>
                        <sld:ColorMap>
                            <sld:ColorMapEntry color="#00BFBF" opacity="0.0" quantity="-100.0"/>
                            <sld:ColorMapEntry color="#00FF00" opacity="0.0" quantity="920.0"/>
                            <sld:ColorMapEntry color="#00FF00" opacity="1.0" quantity="920.0"/>
                            <sld:ColorMapEntry color="#FFFF00" opacity="1.0" quantity="1940.0"/>
                            <sld:ColorMapEntry color="#FFFF00" opacity="1.0" quantity="1940.0"/>
                            <sld:ColorMapEntry color="#FF7F00" opacity="1.0" quantity="2960.0"/>
                            <sld:ColorMapEntry color="#FF7F00" opacity="1.0" quantity="2960.0"/>
                            <sld:ColorMapEntry color="#BF7F3F" opacity="1.0" quantity="3980.0"/>
                            <sld:ColorMapEntry color="#BF7F3F" opacity="1.0" quantity="3980.0"/>
                            <sld:ColorMapEntry color="#141514" opacity="1.0" quantity="5000.0"/>
                        </sld:ColorMap>
                    </sld:RasterSymbolizer>
                </sld:Rule>
            </sld:FeatureTypeStyle>
        </sld:UserStyle>
    </sld:UserLayer>
</sld:StyledLayerDescriptor>