<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
    xmlns="http://www.opengis.net/sld"
    xmlns:sld="http://www.opengis.net/sld"
    xmlns:se="http://www.opengis.net/se"
    xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.1.0/StyledLayerDescriptor.xsd
                        http://www.opengis.net/se http://schemas.opengis.net/se/1.1.0/FeatureStyle.xsd"
    version="1.1.0"
    >
    <UserLayer>
        <LayerFeatureConstraints>
            <FeatureTypeConstraint/>
        </LayerFeatureConstraints>
        <UserStyle>
            <se:FeatureTypeStyle>
                <se:Rule>
                    <se:PolygonSymbolizer>
                        <se:Fill>
                            <se:GraphicFill>
                                <se:Graphic>
                                    <se:ExternalGraphic>
                                        <se:InlineContent encoding="xml"><![CDATA[<?xml version="1.0" encoding="UTF-8"?><svg/>]]></se:InlineContent>
                                        <se:Format>image/svg</se:Format>
                                    </se:ExternalGraphic>
                                </se:Graphic>
                            </se:GraphicFill>
                        </se:Fill>
                    </se:PolygonSymbolizer>
                </se:Rule>
            </se:FeatureTypeStyle>
        </UserStyle>
    </UserLayer>
</sld:StyledLayerDescriptor>
