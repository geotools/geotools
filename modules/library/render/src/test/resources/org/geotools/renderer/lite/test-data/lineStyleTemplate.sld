<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
    <NamedLayer>
        <Name>underline_lines</Name>
        <UserStyle>
            <FeatureTypeStyle>
                <Rule>
                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">#0000FF</CssParameter>
                        </Stroke>
                    </LineSymbolizer>
                </Rule>
                <Rule>
                    <ogc:Filter>
                        <ogc:PropertyIsEqualTo>
                            <ogc:Function name="in3">
                                <ogc:Function name="id"/>
                                <ogc:Literal>Line.1</ogc:Literal>
                                <ogc:Literal>Line.10</ogc:Literal>
                                <ogc:Literal>Line.6</ogc:Literal>
                            </ogc:Function>
                            <ogc:Literal>true</ogc:Literal>
                        </ogc:PropertyIsEqualTo>
                    </ogc:Filter>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>name</ogc:PropertyName>
                        </Label>
                        <Font>
                            <CssParameter name="font-family">Bitstream Vera Sans</CssParameter>
                            <CssParameter name="font-size">16</CssParameter>
                            <CssParameter name="font-weight">bold</CssParameter>
                        </Font>
                        <LabelPlacement>
                            <LinePlacement/>
                        </LabelPlacement>
                        <Halo>
                            <Radius>
                                <ogc:Literal>3</ogc:Literal>
                            </Radius>
                            <Fill>
                                <CssParameter name="fill">#FFFFFF</CssParameter>
                                <CssParameter name="fill-opacity">1</CssParameter>
                            </Fill>
                        </Halo>
                        <Fill>
                            <CssParameter name="fill">#FF0000</CssParameter>
                        </Fill>
                        <VendorOption name="followLine">true</VendorOption>
                        <VendorOption name="%VENDOR_KEY%">%VENDOR_VALUE%</VendorOption>
                    </TextSymbolizer>
                </Rule>
                <Rule>
                    <ogc:Filter>
                        <ogc:PropertyIsEqualTo>
                            <ogc:Function name="in2">
                                <ogc:Function name="id"/>
                                <ogc:Literal>Line.2</ogc:Literal>
                                <ogc:Literal>Line.7</ogc:Literal>
                            </ogc:Function>
                            <ogc:Literal>true</ogc:Literal>
                        </ogc:PropertyIsEqualTo>
                    </ogc:Filter>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>name</ogc:PropertyName>
                        </Label>
                        <Font>
                            <CssParameter name="font-family">Bitstream Vera Sans</CssParameter>
                            <CssParameter name="font-size">20</CssParameter>
                        </Font>
                        <LabelPlacement>
                            <PerpendicularOffset>10</PerpendicularOffset>
                        </LabelPlacement>
                        <Halo>
                            <Radius>
                                <ogc:Literal>3</ogc:Literal>
                            </Radius>
                            <Fill>
                                <CssParameter name="fill">#FFFFFF</CssParameter>
                                <CssParameter name="fill-opacity">1</CssParameter>
                            </Fill>
                        </Halo>
                        <Fill>
                            <CssParameter name="fill">#00FF00</CssParameter>
                        </Fill>
                        <VendorOption name="%VENDOR_KEY%">%VENDOR_VALUE%</VendorOption>
                    </TextSymbolizer>
                </Rule>
                <Rule>
                    <ogc:Filter>
                        <ogc:PropertyIsEqualTo>
                            <ogc:Function name="in2">
                                <ogc:Function name="id"/>
                                <ogc:Literal>Line.5</ogc:Literal>
                                <ogc:Literal>Line.8</ogc:Literal>
                            </ogc:Function>
                            <ogc:Literal>true</ogc:Literal>
                        </ogc:PropertyIsEqualTo>
                    </ogc:Filter>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>name</ogc:PropertyName>
                        </Label>
                        <Font>
                            <CssParameter name="font-family">Bitstream Vera Sans</CssParameter>
                            <CssParameter name="font-style">italic</CssParameter>
                            <CssParameter name="font-size">25</CssParameter>
                        </Font>
                        <LabelPlacement>
                            <LinePlacement/>
                        </LabelPlacement>
                        <Fill>
                            <CssParameter name="fill">#00FFFF</CssParameter>
                        </Fill>
                        <VendorOption name="followLine">true</VendorOption>
                        <VendorOption name="%VENDOR_KEY%">%VENDOR_VALUE%</VendorOption>
                    </TextSymbolizer>
                </Rule>
                <Rule>
                    <ogc:Filter>
                        <ogc:PropertyIsEqualTo>
                            <ogc:Function name="id"/>
                            <ogc:Literal>Line.9</ogc:Literal>
                        </ogc:PropertyIsEqualTo>
                    </ogc:Filter>
                    <TextSymbolizer>
                        <Label>
                            <ogc:PropertyName>name</ogc:PropertyName>
                        </Label>
                        <Font>
                            <CssParameter name="font-family">Bitstream Vera Sans</CssParameter>
                            <CssParameter name="font-size">30</CssParameter>
                        </Font>
                        <LabelPlacement>
                            <LinePlacement/>
                        </LabelPlacement>
                        <VendorOption name="autoWrap">3</VendorOption>
                        <VendorOption name="%VENDOR_KEY%">%VENDOR_VALUE%</VendorOption>
                    </TextSymbolizer>
                </Rule>
            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>
