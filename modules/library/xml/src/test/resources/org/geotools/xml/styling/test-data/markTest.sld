<StyledLayerDescriptor version="0.7.2">
<!-- a named layer is the basic building block of an sld document -->
<NamedLayer>
<Name>A Random Layer</Name>
<title>The title of the layer</title>
<abstract>
A longer and some would say less random peice of text
that allows you to describe the latyer in more detail
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
                <graphic>
                    <size><PropertyName>size</PropertyName></size>
                    <rotation><PropertyName>rotation</PropertyName></rotation>
                    <mark>
                        <wellknownname><PropertyName>name</PropertyName></wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#FF0000</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </PointSymbolizer>
        </rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
        <FeatureTypeName>labelPoint</FeatureTypeName>
        <rule>
            <TextSymbolizer>
                <Label><PropertyName>name</PropertyName></Label>
                <Font>
                    <CssParameter name="font-family">SansSerif</CssParameter>
                    <CssParameter name="font-Size">
                        <literal>10</literal>
                    </CssParameter>
                </Font>
                
                <LabelPlacement>
                    <PointPlacement>
                        <AnchorPoint>
                            <AnchorPointX><PropertyName>X</PropertyName> </AnchorPointX>
                            <AnchorPointY><PropertyName>Y</PropertyName> </AnchorPointY>
                        </AnchorPoint>
                    </PointPlacement>
                </LabelPlacement>
                <Fill>
                    <CssParameter name="fill">#000000</CssParameter>
                </Fill>
                <Halo/>
            </TextSymbolizer>
            <PointSymbolizer>
                <graphic>
                    <size>4</size>
                    <mark>
                        <wellknownname>circle</wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#FF0000</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </PointSymbolizer>
        </rule>
    </FeatureTypeStyle>
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>