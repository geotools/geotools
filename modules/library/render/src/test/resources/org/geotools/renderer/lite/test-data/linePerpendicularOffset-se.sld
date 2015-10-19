<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.1.0"
    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:se="http://www.opengis.net/se"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.1.0/StyledLayerDescriptor.xsd">
    <NamedLayer>
        <se:Name>LineBuffering</se:Name>
        <UserStyle>
            <se:FeatureTypeStyle>
                <se:Rule>
                    <se:LineSymbolizer>
                        <se:Stroke />
                    </se:LineSymbolizer>

                    <se:LineSymbolizer>
                        <se:Stroke>
                            <se:SvgParameter name="stroke">#0000FF</se:SvgParameter>
                        </se:Stroke>
                        <se:PerpendicularOffset>10</se:PerpendicularOffset>
                    </se:LineSymbolizer>

                    <se:LineSymbolizer>
                        <se:Stroke>
                            <se:SvgParameter name="stroke">#FF0000</se:SvgParameter>
                        </se:Stroke>
                        <se:PerpendicularOffset>-10</se:PerpendicularOffset>
                    </se:LineSymbolizer>
                </se:Rule>
            </se:FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>