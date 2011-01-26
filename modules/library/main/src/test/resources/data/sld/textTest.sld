<StyledLayerDescriptor version="0.7.2">
<!-- a named layer is the basic building block of an sld document -->
<NamedLayer>
<Name>A Test Layer</Name>
<title>The title of the layer</title>
<abstract>
A styling layer used for the unit tests of sldstyler
</abstract>
<!-- with in a layer you have Named Styles -->
<UserStyle>
    <!-- again they have names, titles and abstracts -->
  <Name>MyStyle</Name>
    <!-- FeatureTypeStyles describe how to render different features -->
        <FeatureTypeStyle>
        <FeatureTypeName>testPoint</FeatureTypeName>
        <rule>
            <PointSymbolizer>
                <geometry>
                   <PropertyName>the_geom</PropertyName>
                </geometry>
                <graphic>
                    <mark>
                    <!-- since cross is not implemented yet should draw next mark -->
                        <wellknownname>triangle</wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#FF00FF</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </PointSymbolizer>
            <TextSymbolizer>
                <Label><Literal>Point Label</Literal></Label>
                <Font>
                    <CssParameter name="font-family">Times New Roman</CssParameter>
                    <CssParameter name="font-Size">
                        <PropertyName>size</PropertyName>
                    </CssParameter>
                </Font>
                <Font>
                    <CssParameter name="font-family">Arial</CssParameter>
                    <CssParameter name="font-Size">
                        <PropertyName>size</PropertyName>
                    </CssParameter>
                </Font>
                <LabelPlacement>
                    <PointPlacement>
                        <Displacement>
                            <DisplacementX><Literal>5</Literal></DisplacementX>
                            <DisplacementY><Literal>0</Literal></DisplacementY>
                        </Displacement>
                        <Rotation>
                            <PropertyName>rotation</PropertyName>
                        </Rotation>
                    </PointPlacement>
                </LabelPlacement>
                <Fill>
                    <CssParameter name="fill">#AAAA00</CssParameter>
                </Fill>
                <Halo/>
            </TextSymbolizer>
                    
        </rule>
    </FeatureTypeStyle>
            <FeatureTypeStyle>
        <FeatureTypeName>testLine</FeatureTypeName>
        <rule>
            <LineSymbolizer>
                <Stroke>
                <CssParameter name="stroke">#0000FF</CssParameter>
                <CssParameter name="width">
                    <literal>1</literal>
                </CssParameter>
            </Stroke>
            </LineSymbolizer>

            <TextSymbolizer>
                <Label><Literal>Line Label</Literal></Label>
                <Font>
                    <CssParameter name="font-family">Times New Roman</CssParameter>
                    <CssParameter name="font-Size">
                        <PropertyName>size</PropertyName>
                    </CssParameter>
                </Font>
                <Font>
                    <CssParameter name="font-family">Arial</CssParameter>
                    <CssParameter name="font-Size">
                        <PropertyName>size</PropertyName>
                    </CssParameter>
                </Font>
                <LabelPlacement>
                    <LinePlacement>
                        <PerpendicularOffset>
                            <PropertyName>perpendicularoffset</PropertyName>
                        </PerpendicularOffset>
                    </LinePlacement>
                </LabelPlacement>
                <Fill>
                    <CssParameter name="fill">#FFAA00</CssParameter>
                </Fill>
                <Halo>
                    <Radius><Literal>3</Literal></Radius>
                    <fill>
                        <CssParameter name="fill"><literal>#FF0000</literal></CssParameter>
                    </fill>
                </Halo>
            </TextSymbolizer>
                    
        </rule>
    </FeatureTypeStyle>
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>
