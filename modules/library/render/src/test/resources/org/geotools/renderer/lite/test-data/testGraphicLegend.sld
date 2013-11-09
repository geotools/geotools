<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <NamedLayer>
        <Name>testGraphicLegend</Name>
        <UserStyle>
            <Name>testGraphicLegend</Name>
            <Title>Graphic Legend Test</Title>
            <FeatureTypeStyle>
                <Rule>
                    <Name>Hello</Name>
                    <Title>Hello</Title>
                    <LegendGraphic>
                        <Graphic>
                            <ExternalGraphic>
                                <OnlineResource xlink:href="icon64.png"/>
                                <Format>image/png</Format>
                            </ExternalGraphic>
                        </Graphic>
                    </LegendGraphic>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>