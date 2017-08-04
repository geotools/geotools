Cartographic CSS Plugin
-----------------------

The **gt-css** module is a plugin used used to generate Style objects from a human readable CSS representation.

To use this module in your application:


Related Links:

* `CSS <http://docs.geoserver.org/latest/en/user/styling/css/index.html>`_ (GeoServer User Guide)
* `CSS Cookbook <http://docs.geoserver.org/latest/en/user/styling/css/cookbook/index.html>`_ (GeoServer User Guide)

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-css</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Cartographic CSS parser
'''''''''''''''''''''''

Here is an example Cartographic CSS style definition from the CSS Cookbook:

.. code-block:: css

    * {
       mark: symbol(circle);
       mark-size: 6px;
     }
    
     :mark {
       fill: red;
     }

The following code can be used to parse the above file and generate a GeoTools Style object:

.. code-block:: java

        Stylesheet ss = CssParser.parse(css);
        CssTranslator translator = new CssTranslator();
        Style style = translator.translate(ss);

Here is the SLD represenattion of the generated style:

.. code-block:: xml

    <?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
      <sld:NamedLayer>
        <sld:Name/>
        <sld:UserStyle>
          <sld:Name>Default Styler</sld:Name>
          <sld:FeatureTypeStyle>
            <sld:Rule>
              <sld:PointSymbolizer>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>circle</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#ff0000</sld:CssParameter>
                    </sld:Fill>
                  </sld:Mark>
                  <sld:Size>6</sld:Size>
                </sld:Graphic>
              </sld:PointSymbolizer>
            </sld:Rule>
            <sld:VendorOption name="ruleEvaluation">first</sld:VendorOption>
          </sld:FeatureTypeStyle>
        </sld:UserStyle>
      </sld:NamedLayer>
    </sld:StyledLayerDescriptor>


