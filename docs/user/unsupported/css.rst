Cartographic CSS Plugin
-----------------------

The ``gt-css`` module is a plugin used used to generate Style objects from a human readable CSS representation.

To use this module in your application:


Related Links:

* `CSS <http://docs.geoserver.org/latest/en/user/styling/css/index.html>`_ (GeoServer User Guide)
* `CSS Cookbook <http://docs.geoserver.org/latest/en/user/styling/css/cookbook/index.html>`_ (GeoServer User Guide)

``Maven``::
   
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

Here is the SLD representation of the generated style:

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


Rule metadata i18n
''''''''''''''''''

GeoTools CSS supports internationalized rule metadata in rule comments for ``@title`` and ``@abstract``.
Localized variants are provided with a language suffix:

.. code-block:: css

    /*
     * @title Default title
     * @title[en] English title
     * @title[it] Titolo italiano
     * @abstract Default abstract
     * @abstract[en_US] English abstract
     */
    * {
      stroke: #444;
    }

When translated to SLD, localized values are emitted as ``<Localized>`` entries under ``<Title>`` and ``<Abstract>``.

In cascaded styles where multiple CSS rules combine into one SLD rule:

* default ``@title`` values are concatenated using ``", "``
* default ``@abstract`` values are concatenated using newlines
* localized values are concatenated per language independently

Language tags accept both ``-`` and ``_`` separators (for example ``en-US`` and ``en_US``).
Malformed localized tags that match the localized syntax but are not valid language tags cause translation to fail.

Note:

* i18n support is currently for rule metadata comments only (``@title[...]``, ``@abstract[...]``)
* style directives such as ``@styleTitle`` and ``@styleAbstract`` are not localized in GeoTools CSS
