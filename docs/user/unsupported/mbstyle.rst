Mapbox Styles Module
--------------------

The **gt-mbstyle** module is an unsupported module that provides a parser/encoder to convert between Mapbox Styles and GeoTools style objects. These docs are under active development, along with the module itself.

References:

* https://www.mapbox.com/mapbox-gl-style-spec

Code Example
^^^^^^^^^^^^

Quick example of module usage (API change):

.. code-block:: java

    StyledLayerDescriptor style = MapBoxStyle.parse( reader );

Internally the extension provides greater access to the parsed style:

.. code-block:: java
  
    MBStyle mbstyle = MapBoxStyleParser.parse( reader );

    // pull back all layers for a provided source
    List<MBLayer> layers = mbstyle.layers( "mapbox://mapbox.mapbox-streets-v6" );

    // pull back selected layers for a provided source
    List<MBLayer> layers = mbstyle.layers( "mapbox://mapbox.mapbox-streets-v6", "water" ); 

    //Which can be used to Generate Styler Layer Descriptor:
    StyleLayerDescriptor sld = MBStyleTransformer.transform( layers );