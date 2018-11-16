<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
    <NamedLayer>
        <Name>Classes Quality</Name>
        <UserStyle>
            <FeatureTypeStyle>
                <Rule>
                    <RasterSymbolizer>
                        <ColorMap>
                            <ColorMapEntry color="#00FF00" quantity="0" opacity="1" label="Land"/>
                            <ColorMapEntry color="#0000FF" quantity="2" opacity="1" label="  Water"/>
                        </ColorMap>
                    </RasterSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
