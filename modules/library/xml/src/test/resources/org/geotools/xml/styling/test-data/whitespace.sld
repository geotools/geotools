<StyledLayerDescriptor version="1.0.0"
        xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
        xmlns="http://www.opengis.net/sld"
        xmlns:ogc="http://www.opengis.net/ogc"
        xmlns:xlink="http://www.w3.org/1999/xlink"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

        <NamedLayer>
                <Name>A Test Layer</Name>
                <UserStyle>
                        <Name>population</Name>
                        <Title>Population in the United States</Title>
                        <Abstract>
                                A sample filter that filters the United States into
                                three categories of population, drawn in different
                                colors
                        </Abstract>
					    <FeatureTypeStyle>
                                <Rule>
                                        <Title>Test Rule</Title>
                                        <TextSymbolizer>
                                                <Label>
                                                        <ogc:PropertyName>
                                                                testProperty
                                                        </ogc:PropertyName>
                                                </Label>
                                                       
                                                <Fill>
                                                        <CssParameter name="fill">
                                                                #000000
                                                        </CssParameter> 

                                                </Fill>
                                        </TextSymbolizer>
                                </Rule>
                        </FeatureTypeStyle>
                </UserStyle>
        </NamedLayer>
</StyledLayerDescriptor>
                                      
                        