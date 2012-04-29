<?xml version="1.0" encoding="UTF-8"?>
<sld:UserStyle xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
    <sld:Name>Default Styler</sld:Name>
    <sld:Title/>
    <sld:FeatureTypeStyle>
        <sld:Name>Buffer</sld:Name>
        <sld:Transformation>
            <ogc:Function name="BufferTest">
                <ogc:Literal>0.2</ogc:Literal>
            </ogc:Function>
        </sld:Transformation>
        <sld:Rule>
            <sld:PolygonSymbolizer>
                <sld:Fill>
                    <sld:CssParameter name="fill">#f5deb3</sld:CssParameter>
                </sld:Fill>
            </sld:PolygonSymbolizer>
        </sld:Rule>
    </sld:FeatureTypeStyle>
    <sld:FeatureTypeStyle>
        <sld:Name>Line</sld:Name>
        <sld:Rule>
            <sld:LineSymbolizer>
                <sld:Stroke>
                    <sld:CssParameter name="stroke">#000000</sld:CssParameter>
                    <sld:CssParameter name="stroke-width">1.0</sld:CssParameter>
                </sld:Stroke>
            </sld:LineSymbolizer>
        </sld:Rule>
    </sld:FeatureTypeStyle>
</sld:UserStyle>
