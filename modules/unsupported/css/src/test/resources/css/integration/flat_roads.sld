<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name/>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Rule>
          <sld:Title>Zoomed out highway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MinScaleDenominator>200000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1.5E7</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#ffffff</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Mul>
                  <ogc:Function name="Categorize">
                    <ogc:Function name="env">
                      <ogc:Literal>wms_scale_denominator</ogc:Literal>
                    </ogc:Function>
                    <ogc:Literal>1.8</ogc:Literal>
                    <ogc:Literal>400000</ogc:Literal>
                    <ogc:Literal>1.4</ogc:Literal>
                    <ogc:Literal>800000</ogc:Literal>
                    <ogc:Literal>0.8</ogc:Literal>
                    <ogc:Literal>1500000</ogc:Literal>
                    <ogc:Literal>0.4</ogc:Literal>
                    <ogc:Literal>3000000</ogc:Literal>
                    <ogc:Literal>0.2</ogc:Literal>
                    <ogc:Literal>6000000</ogc:Literal>
                    <ogc:Literal>0.1</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>1.8</ogc:Literal>
                </ogc:Mul>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed out highway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MinScaleDenominator>200000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1000000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#ffffff</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Mul>
                  <ogc:Function name="Categorize">
                    <ogc:Function name="env">
                      <ogc:Literal>wms_scale_denominator</ogc:Literal>
                    </ogc:Function>
                    <ogc:Literal>1.8</ogc:Literal>
                    <ogc:Literal>400000</ogc:Literal>
                    <ogc:Literal>1.4</ogc:Literal>
                    <ogc:Literal>800000</ogc:Literal>
                    <ogc:Literal>0.8</ogc:Literal>
                    <ogc:Literal>1500000</ogc:Literal>
                    <ogc:Literal>0.4</ogc:Literal>
                    <ogc:Literal>3000000</ogc:Literal>
                    <ogc:Literal>0.2</ogc:Literal>
                    <ogc:Literal>6000000</ogc:Literal>
                    <ogc:Literal>0.1</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>1.8</ogc:Literal>
                </ogc:Mul>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Mid zoom motorway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MinScaleDenominator>100000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>200000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#c24e6b</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>27</ogc:Literal>
                  <ogc:Literal>1500</ogc:Literal>
                  <ogc:Literal>21</ogc:Literal>
                  <ogc:Literal>3000</ogc:Literal>
                  <ogc:Literal>18</ogc:Literal>
                  <ogc:Literal>6000</ogc:Literal>
                  <ogc:Literal>10</ogc:Literal>
                  <ogc:Literal>25000</ogc:Literal>
                  <ogc:Literal>7</ogc:Literal>
                  <ogc:Literal>35000</ogc:Literal>
                  <ogc:Literal>7</ogc:Literal>
                  <ogc:Literal>50000</ogc:Literal>
                  <ogc:Literal>6</ogc:Literal>
                  <ogc:Literal>100000</ogc:Literal>
                  <ogc:Literal>3</ogc:Literal>
                  <ogc:Literal>200000</ogc:Literal>
                  <ogc:Literal>2</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed out motorway link</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MinScaleDenominator>100000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>200000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#c24e6b</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>16</ogc:Literal>
                  <ogc:Literal>1500</ogc:Literal>
                  <ogc:Literal>13</ogc:Literal>
                  <ogc:Literal>3000</ogc:Literal>
                  <ogc:Literal>12</ogc:Literal>
                  <ogc:Literal>6000</ogc:Literal>
                  <ogc:Literal>7.4</ogc:Literal>
                  <ogc:Literal>25000</ogc:Literal>
                  <ogc:Literal>3.8</ogc:Literal>
                  <ogc:Literal>35000</ogc:Literal>
                  <ogc:Literal>3.8</ogc:Literal>
                  <ogc:Literal>50000</ogc:Literal>
                  <ogc:Literal>3</ogc:Literal>
                  <ogc:Literal>100000</ogc:Literal>
                  <ogc:Literal>2</ogc:Literal>
                  <ogc:Literal>200000</ogc:Literal>
                  <ogc:Literal>1.2</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in motorway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsNotEqualTo>
                <ogc:PropertyName>bridge</ogc:PropertyName>
                <ogc:Literal>1</ogc:Literal>
              </ogc:PropertyIsNotEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#dc2a67</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>27</ogc:Literal>
                  <ogc:Literal>1500</ogc:Literal>
                  <ogc:Literal>21</ogc:Literal>
                  <ogc:Literal>3000</ogc:Literal>
                  <ogc:Literal>18</ogc:Literal>
                  <ogc:Literal>6000</ogc:Literal>
                  <ogc:Literal>10</ogc:Literal>
                  <ogc:Literal>25000</ogc:Literal>
                  <ogc:Literal>7</ogc:Literal>
                  <ogc:Literal>35000</ogc:Literal>
                  <ogc:Literal>7</ogc:Literal>
                  <ogc:Literal>50000</ogc:Literal>
                  <ogc:Literal>6</ogc:Literal>
                  <ogc:Literal>100000</ogc:Literal>
                  <ogc:Literal>3</ogc:Literal>
                  <ogc:Literal>200000</ogc:Literal>
                  <ogc:Literal>2</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in bridge</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>bridge</ogc:PropertyName>
                <ogc:Literal>1</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>27</ogc:Literal>
                  <ogc:Literal>1500</ogc:Literal>
                  <ogc:Literal>21</ogc:Literal>
                  <ogc:Literal>3000</ogc:Literal>
                  <ogc:Literal>18</ogc:Literal>
                  <ogc:Literal>6000</ogc:Literal>
                  <ogc:Literal>10</ogc:Literal>
                  <ogc:Literal>25000</ogc:Literal>
                  <ogc:Literal>7</ogc:Literal>
                  <ogc:Literal>35000</ogc:Literal>
                  <ogc:Literal>7</ogc:Literal>
                  <ogc:Literal>50000</ogc:Literal>
                  <ogc:Literal>6</ogc:Literal>
                  <ogc:Literal>100000</ogc:Literal>
                  <ogc:Literal>3</ogc:Literal>
                  <ogc:Literal>200000</ogc:Literal>
                  <ogc:Literal>2</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in motorway link</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsNotEqualTo>
                <ogc:PropertyName>bridge</ogc:PropertyName>
                <ogc:Literal>1</ogc:Literal>
              </ogc:PropertyIsNotEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#dc2a67</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>16</ogc:Literal>
                  <ogc:Literal>1500</ogc:Literal>
                  <ogc:Literal>13</ogc:Literal>
                  <ogc:Literal>3000</ogc:Literal>
                  <ogc:Literal>12</ogc:Literal>
                  <ogc:Literal>6000</ogc:Literal>
                  <ogc:Literal>7.4</ogc:Literal>
                  <ogc:Literal>25000</ogc:Literal>
                  <ogc:Literal>3.8</ogc:Literal>
                  <ogc:Literal>35000</ogc:Literal>
                  <ogc:Literal>3.8</ogc:Literal>
                  <ogc:Literal>50000</ogc:Literal>
                  <ogc:Literal>3</ogc:Literal>
                  <ogc:Literal>100000</ogc:Literal>
                  <ogc:Literal>2</ogc:Literal>
                  <ogc:Literal>200000</ogc:Literal>
                  <ogc:Literal>1.2</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in link bridge</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>bridge</ogc:PropertyName>
                <ogc:Literal>1</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>16</ogc:Literal>
                  <ogc:Literal>1500</ogc:Literal>
                  <ogc:Literal>13</ogc:Literal>
                  <ogc:Literal>3000</ogc:Literal>
                  <ogc:Literal>12</ogc:Literal>
                  <ogc:Literal>6000</ogc:Literal>
                  <ogc:Literal>7.4</ogc:Literal>
                  <ogc:Literal>25000</ogc:Literal>
                  <ogc:Literal>3.8</ogc:Literal>
                  <ogc:Literal>35000</ogc:Literal>
                  <ogc:Literal>3.8</ogc:Literal>
                  <ogc:Literal>50000</ogc:Literal>
                  <ogc:Literal>3</ogc:Literal>
                  <ogc:Literal>100000</ogc:Literal>
                  <ogc:Literal>2</ogc:Literal>
                  <ogc:Literal>200000</ogc:Literal>
                  <ogc:Literal>1.2</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
      <sld:FeatureTypeStyle>
        <sld:Rule>
          <sld:Title>Zoomed out highway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>1.5E7</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e66e89</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>1.8</ogc:Literal>
                  <ogc:Literal>400000</ogc:Literal>
                  <ogc:Literal>1.4</ogc:Literal>
                  <ogc:Literal>800000</ogc:Literal>
                  <ogc:Literal>0.8</ogc:Literal>
                  <ogc:Literal>1500000</ogc:Literal>
                  <ogc:Literal>0.4</ogc:Literal>
                  <ogc:Literal>3000000</ogc:Literal>
                  <ogc:Literal>0.2</ogc:Literal>
                  <ogc:Literal>6000000</ogc:Literal>
                  <ogc:Literal>0.1</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed out highway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>1000000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e66e89</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>1.8</ogc:Literal>
                  <ogc:Literal>400000</ogc:Literal>
                  <ogc:Literal>1.4</ogc:Literal>
                  <ogc:Literal>800000</ogc:Literal>
                  <ogc:Literal>0.8</ogc:Literal>
                  <ogc:Literal>1500000</ogc:Literal>
                  <ogc:Literal>0.4</ogc:Literal>
                  <ogc:Literal>3000000</ogc:Literal>
                  <ogc:Literal>0.2</ogc:Literal>
                  <ogc:Literal>6000000</ogc:Literal>
                  <ogc:Literal>0.1</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Mid zoom highway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MinScaleDenominator>100000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>200000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>1.8</ogc:Literal>
                  <ogc:Literal>400000</ogc:Literal>
                  <ogc:Literal>1.4</ogc:Literal>
                  <ogc:Literal>800000</ogc:Literal>
                  <ogc:Literal>0.8</ogc:Literal>
                  <ogc:Literal>1500000</ogc:Literal>
                  <ogc:Literal>0.4</ogc:Literal>
                  <ogc:Literal>3000000</ogc:Literal>
                  <ogc:Literal>0.2</ogc:Literal>
                  <ogc:Literal>6000000</ogc:Literal>
                  <ogc:Literal>0.1</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Mid zoom highway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MinScaleDenominator>100000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>200000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>1.8</ogc:Literal>
                  <ogc:Literal>400000</ogc:Literal>
                  <ogc:Literal>1.4</ogc:Literal>
                  <ogc:Literal>800000</ogc:Literal>
                  <ogc:Literal>0.8</ogc:Literal>
                  <ogc:Literal>1500000</ogc:Literal>
                  <ogc:Literal>0.4</ogc:Literal>
                  <ogc:Literal>3000000</ogc:Literal>
                  <ogc:Literal>0.2</ogc:Literal>
                  <ogc:Literal>6000000</ogc:Literal>
                  <ogc:Literal>0.1</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Mid zoom motorway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MinScaleDenominator>100000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>200000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Mul>
                  <ogc:Function name="Categorize">
                    <ogc:Function name="env">
                      <ogc:Literal>wms_scale_denominator</ogc:Literal>
                    </ogc:Function>
                    <ogc:Sub>
                      <ogc:Literal>27</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>1500</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>21</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>3000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>18</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>6000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>10</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>25000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>35000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>50000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>6</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>100000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>200000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                  </ogc:Function>
                  <ogc:Literal>0.9</ogc:Literal>
                </ogc:Mul>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed out motorway link</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MinScaleDenominator>100000.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>200000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Mul>
                  <ogc:Function name="Categorize">
                    <ogc:Function name="env">
                      <ogc:Literal>wms_scale_denominator</ogc:Literal>
                    </ogc:Function>
                    <ogc:Sub>
                      <ogc:Literal>16</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>1500</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>13</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>3000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>12</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>6000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7.4</ogc:Literal>
                      <ogc:Literal>1.4</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>25000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3.8</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>35000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3.8</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>50000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>100000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>200000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>1.2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                  </ogc:Function>
                  <ogc:Literal>0.9</ogc:Literal>
                </ogc:Mul>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in highway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>1.8</ogc:Literal>
                  <ogc:Literal>400000</ogc:Literal>
                  <ogc:Literal>1.4</ogc:Literal>
                  <ogc:Literal>800000</ogc:Literal>
                  <ogc:Literal>0.8</ogc:Literal>
                  <ogc:Literal>1500000</ogc:Literal>
                  <ogc:Literal>0.4</ogc:Literal>
                  <ogc:Literal>3000000</ogc:Literal>
                  <ogc:Literal>0.2</ogc:Literal>
                  <ogc:Literal>6000000</ogc:Literal>
                  <ogc:Literal>0.1</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in highway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Function name="Categorize">
                  <ogc:Function name="env">
                    <ogc:Literal>wms_scale_denominator</ogc:Literal>
                  </ogc:Function>
                  <ogc:Literal>1.8</ogc:Literal>
                  <ogc:Literal>400000</ogc:Literal>
                  <ogc:Literal>1.4</ogc:Literal>
                  <ogc:Literal>800000</ogc:Literal>
                  <ogc:Literal>0.8</ogc:Literal>
                  <ogc:Literal>1500000</ogc:Literal>
                  <ogc:Literal>0.4</ogc:Literal>
                  <ogc:Literal>3000000</ogc:Literal>
                  <ogc:Literal>0.2</ogc:Literal>
                  <ogc:Literal>6000000</ogc:Literal>
                  <ogc:Literal>0.1</ogc:Literal>
                </ogc:Function>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in motorway</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Mul>
                  <ogc:Function name="Categorize">
                    <ogc:Function name="env">
                      <ogc:Literal>wms_scale_denominator</ogc:Literal>
                    </ogc:Function>
                    <ogc:Sub>
                      <ogc:Literal>27</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>1500</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>21</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>3000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>18</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>6000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>10</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>25000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>35000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>50000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>6</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>100000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>200000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                  </ogc:Function>
                  <ogc:Literal>0.9</ogc:Literal>
                </ogc:Mul>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in bridge</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>bridge</ogc:PropertyName>
                <ogc:Literal>1</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Mul>
                  <ogc:Function name="Categorize">
                    <ogc:Function name="env">
                      <ogc:Literal>wms_scale_denominator</ogc:Literal>
                    </ogc:Function>
                    <ogc:Sub>
                      <ogc:Literal>27</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>1500</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>21</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>3000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>18</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>6000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>10</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>25000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>35000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>50000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>6</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>100000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>200000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                  </ogc:Function>
                  <ogc:Literal>0.9</ogc:Literal>
                </ogc:Mul>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in motorway link</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Mul>
                  <ogc:Function name="Categorize">
                    <ogc:Function name="env">
                      <ogc:Literal>wms_scale_denominator</ogc:Literal>
                    </ogc:Function>
                    <ogc:Sub>
                      <ogc:Literal>16</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>1500</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>13</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>3000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>12</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>6000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7.4</ogc:Literal>
                      <ogc:Literal>1.4</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>25000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3.8</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>35000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3.8</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>50000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>100000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>200000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>1.2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                  </ogc:Function>
                  <ogc:Literal>0.9</ogc:Literal>
                </ogc:Mul>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Zoomed in link bridge</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>class</ogc:PropertyName>
                <ogc:Literal>highway</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>bridge</ogc:PropertyName>
                <ogc:Literal>1</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>type</ogc:PropertyName>
                <ogc:Literal>motorway_link</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:MaxScaleDenominator>100000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#e892a2</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-width">
                <ogc:Mul>
                  <ogc:Function name="Categorize">
                    <ogc:Function name="env">
                      <ogc:Literal>wms_scale_denominator</ogc:Literal>
                    </ogc:Function>
                    <ogc:Sub>
                      <ogc:Literal>16</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>1500</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>13</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>3000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>12</ogc:Literal>
                      <ogc:Literal>2</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>6000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>7.4</ogc:Literal>
                      <ogc:Literal>1.4</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>25000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3.8</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>35000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3.8</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>50000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>3</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>100000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                    <ogc:Literal>200000</ogc:Literal>
                    <ogc:Sub>
                      <ogc:Literal>1.2</ogc:Literal>
                      <ogc:Literal>1</ogc:Literal>
                    </ogc:Sub>
                  </ogc:Function>
                  <ogc:Literal>0.9</ogc:Literal>
                </ogc:Mul>
              </sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>
