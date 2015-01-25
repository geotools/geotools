<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name/>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:FeatureTypeName>tasmania_roads</sld:FeatureTypeName>
        <sld:Rule>
          <sld:Title>r1</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>TYPE</ogc:PropertyName>
              <ogc:Literal>road</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>4000000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1.0E7</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#6699FF</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">8</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>r2</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>TYPE</ogc:PropertyName>
              <ogc:Literal>road</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>4000000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#aaaaaa</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:VendorOption name="ruleEvaluation">first</sld:VendorOption>
      </sld:FeatureTypeStyle>
      <sld:FeatureTypeStyle>
        <sld:FeatureTypeName>tasmania_roads</sld:FeatureTypeName>
        <sld:Rule>
          <sld:Title>r2</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>TYPE</ogc:PropertyName>
              <ogc:Literal>road</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>4000000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#FF99aa</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">3</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:VendorOption name="ruleEvaluation">first</sld:VendorOption>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>

