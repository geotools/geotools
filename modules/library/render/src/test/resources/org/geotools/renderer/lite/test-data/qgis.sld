<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor units="mm" version="1.1.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:se="http://www.opengis.net/se" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.1.0/StyledLayerDescriptor.xsd">
    <NamedLayer>
        <se:Name>qgis_styles</se:Name>
        <UserStyle>
            <se:FeatureTypeStyle>
                <se:Rule>
                    <se:Name>generic_style</se:Name>
                    <se:PointSymbolizer>
                        <se:Graphic>
                            <se:Mark>
                                <se:WellKnownName>${wellknownname}</se:WellKnownName>
                                <se:Fill>
                                    <se:SvgParameter name="fill">#FF0000</se:SvgParameter>
                                </se:Fill>
                                <se:Stroke>
                                    <se:SvgParameter name="stroke">#000000</se:SvgParameter>
                                </se:Stroke>                                
                            </se:Mark>
                            <se:Size>55</se:Size>
                        </se:Graphic>
                    </se:PointSymbolizer>
                </se:Rule>
            </se:FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>