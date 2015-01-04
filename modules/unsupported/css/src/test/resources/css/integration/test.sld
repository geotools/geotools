<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name/>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:FeatureTypeName>Layer</sld:FeatureTypeName>
        <sld:Rule>
          <ogc:Filter>
            <ogc:Or>
              <ogc:And>
                <ogc:PropertyIsEqualTo>
                  <ogc:PropertyName>name</ogc:PropertyName>
                  <ogc:Literal>bob</ogc:Literal>
                </ogc:PropertyIsEqualTo>
                <ogc:PropertyIsEqualTo>
                  <ogc:PropertyName>gender</ogc:PropertyName>
                  <ogc:Literal>male</ogc:Literal>
                </ogc:PropertyIsEqualTo>
              </ogc:And>
              <ogc:PropertyIsGreaterThan>
                <ogc:PropertyName>foo</ogc:PropertyName>
                <ogc:PropertyName>bar</ogc:PropertyName>
              </ogc:PropertyIsGreaterThan>
            </ogc:Or>
          </ogc:Filter>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#00FF00</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">round</sld:CssParameter>
              <sld:CssParameter name="stroke-opacity">0.9</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Div>
                  <ogc:PropertyName>magnitude</ogc:PropertyName>
                  <ogc:Literal>1000</ogc:Literal>
                </ogc:Div>
              </sld:CssParameter>
              <sld:CssParameter name="stroke-dashoffset">2.0</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">1.0 3.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:VendorOption name="ruleEvaluation">first</sld:VendorOption>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>

