<StyledLayerDescriptor version="0.7.2">
<NamedLayer>
<Name>Star</Name>
<title>Start</title>
<abstract>
SLD submitted via from email - thanks Tomas
</abstract>
<UserStyle xmlns:ogc="http://www.opengis.net">
    <!-- again they have names, titles and abstracts -->
    <Name>TestStyle</Name>
    <Title>Transparency style</Title>
    <Abstract>A style to test transparency provider</Abstract>
    <FeatureTypeStyle>
        <Name>Points</Name>
        <Title>Points Feature Type Style</Title>
        <Abstract>Draw the provided feature as a star</Abstract>
        <FeatureTypeName>Feature</FeatureTypeName>
       
        <rule>
            <Name>Point</Name>
            <Title>Point</Title>
           
            <PointSymbolizer>
                <graphic>
                    <geometry><propertyname>the_geom</propertyname></geometry>
                    <mark>
                        <wellknownname>star</wellknownname>
                        <Fill>
                            <CssParameter name="fill">#FF0000</CssParameter>
                            <CssParameter name="fill-opacity">0.3</CssParameter>
                        </Fill>
                        <Stroke>   
                            <CssParameter name="stroke">#FFC800</CssParameter>
                            <CssParameter name="stroke-linecap">butt</CssParameter>
                            <CssParameter name="stroke-linejoin">round</CssParameter>
                            <CssParameter name="stroke-opacity">1.0</CssParameter>
                            <CssParameter name="stroke-width">1.0</CssParameter>
                            <CssParameter name="stroke-dashoffset">0.0</CssParameter>
                        </Stroke>
                    </mark>
                    <Size>80</Size>
                    <Rotation>20.0</Rotation>
                </graphic>
            </PointSymbolizer>
        </rule>
    </FeatureTypeStyle>
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>