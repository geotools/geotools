<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
    <NamedLayer>
        <Name/>
        <UserStyle>
            <FeatureTypeStyle>
                <Rule>
                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">0x440000</CssParameter>
                            <CssParameter name="stroke-width">4</CssParameter>
                        </Stroke>
                    </LineSymbolizer>
                    <TextSymbolizer>
                        <Label> <ogc:PropertyName>name</ogc:PropertyName> </Label>
                        <Font>
                            <CssParameter name="font-family">Serif</CssParameter>
                            <CssParameter name="font-style">italic</CssParameter>
                            <CssParameter name="font-weight">bold</CssParameter>
                            <CssParameter name="font-size">11</CssParameter>
                            <CssParameter name="font-color">#222222</CssParameter>
                        </Font>
                        <Fill>
                            <CssParameter name="fill">#333333</CssParameter>
                        </Fill>
                        <LabelPlacement>
                            <PointPlacement auto="true"/>
                        </LabelPlacement>
                    </TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
