Style
-----

GeoTools Rendering process is controlled styling information that you provide. The data structures we use to describe styling are based on the Style Layer Descriptor (SLD) and Symbology Encoding (SE) specifications provided by the OGC. The SLD specification defines an XML document you can use to save and load your styles.

This page is devoted to examples, to review the concepts consult the references below.

References:

* :doc:`gt-api style layer descriptor <../api/sld>` (interfaces)
* :doc:`gt-opengis symbology encoding <../opengis/se>` (interfaces)
* http://www.opengeospatial.org/standards/sld (style layer descriptor)
* http://www.opengeospatial.org/standards/symbol (symbology encoding)

StyleFactory
^^^^^^^^^^^^

We have three style factories offering various levels of standards compliance vs pragmatism.

========== ================ ================ ======== =======================================
Module     Class            Capability       Scope    Description
========== ================ ================ ======== =======================================
gt-opengis StyleFactory     get              se       Strictly limited to the SE standard
gt-api     StyleFactory     get/set          se / sld Supports GeoTools vendor extensions
gt-api     StyleFactory2    get/set          se / sld Supports text label graphics
gt-main    StyleBuilder     get/set/defaults se / sld Shorter methods, does not do everything
========== ================ ================ ======== =======================================

Here are some examples of these classes in action:

* StyleFactory
  
  The gt-opengis StyleFactory allows you to create read-only instances.
  
  Here is a quick example showing the creation of a PointSymbolizer:
  
  .. literalinclude:: /../src/main/java/org/geotools/opengis/StyleExamples.java
     :language: java
     :start-after: // styleFactoryExample start
     :end-before: // styleFactoryExample end
  
* StyleFactory2
  
  This gt-api interface allows one additional non standard trick; it allows us to place
  an icon behind text labels. This is a popular technique used for example to place a
  "label shield" behind hi-way signs.

* StyleFactory
  
  This gt-api interface allows access to all the GeoTools vendor specific options.
  
  It has a slightly different style of programming where mutable instances are creating allowing
  you to call both get and set methods.
  
  You are of course not advised to udpate a style while it is being used to draw.
  
  .. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
     :language: java
     :start-after: // styleFactoryExample start
     :end-before: // styleFactoryExample end

* StyleBuilder
  
  Since a Style is composed of a complex set of objects, a StyleBuilder object is provided for
  you to conveniently build simple styles without the need to build all of the style elements
  by hand.
  
  For example, you can create a PolygonSymbolizer and then create a Style out of it with a
  single method call: the builder will generate a default FeatureTypeStyle and the Rule for you.
  
  .. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
     :language: java
     :start-after: // styleBuilderExample start
     :end-before: // styleBuilderExample end
  
  StlyeBuilder also helps by filling in many defaults values. The use of defaults is
  less of an issue now as the rendering system is able to correctly handle null as a default
  for many cases such as default symbol size.

**What to use**

For working with symbology encoding StyleFactory is recommended as it defines a small
number of easy to use methods. There are however no helpful methods and shortcuts
but you have the advantage of less methods to trip over). Since everything is in plain
sight you may discover some tricky advanced abilities that may not obvious using
StyleBuilder.

For working with style layer descriptor use StyleBuilder to quickly create objects
with their default values filled in; and then configure them as needed using setters.



Internally we have:

* StyleFactoryImpl2 that creates the raw objects; this is an implementation
  of the simple **gt-opengis** StyleFactory.
* StyleFactoryImpl makes use of a aelegate to create the objects; and then allows for a wider
  range of create methods defined by **gt-api** StyleFactory
* StyleBuilder which as expected uses a FilterFactory and a StyleFactory in order to get the job done.

Style Layer Descriptor
^^^^^^^^^^^^^^^^^^^^^^

GeoTools styling is built on the style layer descriptor data model shown below (from :doc:`gt-api <../api/sld>`).


.. image:: /images/sld.PNG

GeoTools rendering tends to focus the "User Style" which we represent **Style** to let you control how
your Map is rendered.

* Style
  
  The Style interface matches up with the "Style Layer Descriptor" 1.0 specification
  (so if you need explanations or examples please review the OGC documentation for more information).


Create
''''''

To create a StyleLayerDescriptor object using a StyleFactory:

.. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
   :language: java
   :start-after: // sldExample start
   :end-before: // sldExample end
  
This is the last time we will talk about StyleLayerDescriptor object - it is not really that useful in
controlling the rendering process.

Access
''''''

To go from a StyleLayerDescriptor object to something useful::
    
    FeatureTypeStyle useful[] = SLD.featureTypeStyles( sld );

Or find a style that is compatible with your feature type::
    
    FeatureTypeStyle applicable = SLD.featureTypeStyle( sld, schema );

Document
''''''''

The Styled Layer Descriptor Reference Document OpenGIS standard defines an XML document we use to persist our GeoTools Style objects. This standard is the authoritative definition as far as functionally goes, if you find any place where we are out of line please send us a bug report.

How to parse an SLD:

* You can create a Style using an SLD document (an XML file format defined by the Style Layer Descriptor 1.0 specification):
  
  .. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
     :language: java
     :start-after: // parseSLD start
     :end-before: // parseSLD end

* SAX StyleReader
  
  A SAX based StyleReader is also available for GeoTools 2.2 code::
    
    private Style loadStyleFromXml() throws Exception {
        java.net.URL base = getClass().getResource("rs-testData");
        
        StyleFactory factory = StyleFactory.createStyleFactory();
        java.net.URL surl = new java.net.URL(base + "/markTest.sld");
        
        //A class to read and parse an SLD file based on verion 0.7.2 of the OGC
        SLDStyle stylereader = new SLDStyle(factory, surl);
        Style[] style = stylereader.readXML();
        
        return style[0];
    }

How to write a SLD File:

* GeoTools has an XML Transfer written up allowing you to generate an SLD file::
    
    SLDTransformer styleTransform = new SLDTransformer();
    String xml = styleTransform.transform(sld);

* How to write an SLD file using only a Style
  
  The above code example requires a complete StyleLayerDescriptor document in order to make a valid sld file.
  
  Here is how you can wrap up your Style object for output::

    StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();        
    UserLayer layer = styleFactory.createUserLayer();
    layer.setLayerFeatureConstraints(new FeatureTypeConstraint[] {null});
    sld.addStyledLayer(layer);
    layer.addUserStyle(style);
    
    SLDTransformer styleTransform = new SLDTransformer();
    String xml = styleTransform.transform(sld);

XML:

* The file :download:`markTest.sld </../../modules/library/main/src/test/resources/org/geotools/styling/test-data/markTest.sld>`
  contains the following XML:
  
  .. literalinclude:: /../../modules/library/main/src/test/resources/org/geotools/styling/test-data/markTest.sld
  
* The same style can be created using the StyleBuilder:
  
  .. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
     :language: java
     :start-after: // markTestSLD start
     :end-before: // markTestSLD end

Symbology Encoding
^^^^^^^^^^^^^^^^^^

The feature type style data model captures the symbology encoding information describing how a feature should be drawn on the screen and will represent the bulk of our examples.


.. image:: /images/se.PNG


FeatureTypeStyle
''''''''''''''''

A FeatureTypeStyle declares a part of a style that is specifically geared toward a FeatureType,
that is, features will be rendered according to this FeatureTypeStyle only if their FeatureType
is the same as the FeatureType declared in the FeatureTypeStyle or a descendent.

When defining a Style you will spend the majority of time working with FeatureTypeStyle.  A FeatureTypeStyle is specifically geared toward drawing features.

The level of detail is similar to CSS in that you need to define some Rules saying when to draw, and some symbolizers saying how to draw it. Individual symbolizers will use expressions to access feature content (as an example TextSymbolizer will use an expression you provide to construct the text to display).

* FeatureTypeStyle.getName(): machine readable name
* FeatureTypeStyle.getDescriptor(): human readable title and description
* FeatureTypeStyle.featureTypeNames(): the Name here is important; it must match the Features you want to draw.
  
  Features will be rendered according a FeatureTypeStyle only if their
  FeatureType name matches what is recorded in the FeatureTypeStyle or a
  descendant.
  
  For most practical purposes you will set featureTypeName to be "Feature" to act as a wild card.

* FeatureTypeStyle.semanticTypeIdentifiers(): used to quickly limit based on the kind of vector data (point, line or polygon)

Here is a quick example that will draw any "Feature" using a PointSymbolizer:

.. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
   :language: java
   :start-after: // featureTypeStyleExample start
   :end-before: // featureTypeStyleExample end

Of note is how the PointSymbolizer will first try and use C:\images\house.gif (ie an external graphic) and if that fails it will use a circle (ie a mark).

Notes on handling of features:

* Each FeatureTypeStyle that applies is used. That means, the layer will be drawn multiple times if the styles
  contain more than one FeatureTypeStyle that matches the FeatureType of the features in the layer.
* FeatureTypeStyles are painted in order: a FeatureTypeStyle is painted only once the previous one in the list
  has been completely painted
* Each feature is then passed to the rules and their list of symbolizers
* This means that a single feature can be painted more than once, if more than one rule matches it, or
  if the rules contain more than one symbolizer.
* The full set of rules and symbolizers in the current FeatureTypeStyle
  is applied to the current Feature before considering the next one.
  
  The last consideration is important when you need to draw, for example, roads with a double line
  such as a wide black line below a thin white line. This is possible using two FeatureTypeStyles,
  since using a Rule with a couple of symbolizers will generate a map that doesn't look good at
  road intersections.
  
  Example of style with two FeatureTypeStyles:
  
  .. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
     :language: java
     :start-after: // twoFeatureTypeStyles start
     :end-before: // twoFeatureTypeStyles end
  
* For an in depth discussion of the rendering process please refer to * :doc:`style </tutorial/map/style>` (tutorial)

Rule
''''
A FeatureTypeStyle contains one or more rules, these rules are considered in order with the possibility of an "else" Rule being
used to render any remaining features.

A rule is based on the following:

* minimum/maximum scale: if set and the current scale is outside the specified range, the rule won't apply and thus its symbolizers won't be used
* Filter: that is applied to the features, only the features matching the filter will be painted
  according to the Rule symbolizers.
  
  As an alternative, the rule can have an "else filter". This special kind of filter catches all of the
  features that still haven't been symbolized by previous rules with a regular filter.

FeatureTypeStyle used **featureTypename** to sort out what kind of features we are dealing with. Rules are used to refine this contents, possibly filtering according to feature attributes or scale, to determine specifically what we are going to draw.

Pay Attention to:

* minimum and maximum map scale, if set and the current scale is outside the specified range, the rule won't apply and thus its symbolizers won't be used
* Filter that is used to select features to draw, only the features matching the filter will be painted 
* A rule can have an "else filter". This special kind of filter catches all of the features that still haven't been symbolized by previous rules with a regular filter).

Once FeatureTypeStyle and Rules have determined that a Feature is going to be drawn; the Rule makes use of a list of of Symbolizers to define how the content is painted:

* A Symbolizer describes how to represent a feature on the screen based on the feature contents (geometry and attributes). 
* Each rule can have a list of Symbolizer attached to it.
* symbolizers are used like a display language to produce pixels on the display device. 

Symbolizer
''''''''''

A symbolizer describes how a feature should appear on a map.  Each rule has a list of symbolizers which it applies in order.


.. image:: /images/symbolizer3.PNG

As you can see, there are many kind of symbolizers, for points, lines, polygons, labels and raster data.

You don't need to match the symbolizer with the specific geometry contained in the feature, the renderer will try to do the most appropriate thing on a case by case basis. For example, TextSymbolizer applies to all kinds of geometries, and will generate labels on the map. If you apply a PolygonSymbolizer to a line, the line will be closed to form a polygon, and then the polygon symbolizer will be applied.


.. image:: /images/symbolizer2.PNG

The GeoTools Symbolizer interface offers a couple of advantages over the base standard:

* getGeometry()
* setGeometry( Expression )
  
  The ability to define a geometry using an expression allows the use of a function to "preprocess" your geometry
  prior to it being considered for rendering.
  
  This is a little bit tricky (as functions like buffer will make your geometry bigger) but the result is worthwhile
  in the amount of flexibility it offers.

Notes on the use of symbolizers:

* The symbolizer describes not just the shape that should appear but also such graphical properties as color and opacity
* Symbolizers do have a default behaviour, after creating a Symbolizer you should supplying parameters to overide the default settings
* The original details of this object are taken from the OGC Styled-Layer Descriptor Report (OGC 01-077) version 0.7.2.
* Renderers can use this information when displaying styled features.
  Though it must be remembered that not all renderers will be able to fully represent
  strokes as set out by this interface. For example, opacity may not be supported.
* The graphical parameters and their values are derived from SVG/CSS2 standards with
  names and semantics which are as close as possible.
* The most important thing to note here is that symbolizer component objects are
  composed of Expression objects, which means that they may be made dependent on
  Feature attributes.
  
  For example, you can create a mathematical expression that links some Feature
  attribute to the line width.
  
  Thus, you have two ways to symbolize different features with different styles:
  
  * By using more than one rule with different filters, and then building symbolizers with literal expressions.
    This is a good way to create a classified map, in which colours, line styles and so on depend on the range the attribute value
    falls into.
  * By directly linking a symbolizer property to an attribute value;


Point Symbolizer
''''''''''''''''

Used to draw a point location, the actual graphic drawn is referred to as a Mark with the option to use
some well known marks (circle, square etc..) or your own external graphics such as PNG icons.

Examples: 

* GeoServer SLD cookbook
  
  `Points <http://docs.geoserver.org/stable/en/user/styling/sld-cookbook/points.html>`_
  
* Quick example creating a PointSymbolizer using StyleBuilder:
  
  .. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
     :language: java
     :start-after: // quickPointSymbolizer start
     :end-before: // quickPointSymbolizer end
  
  Here is the same style as an xml fragments::
  
            <PointSymbolizer>
                <graphic>
                    <size><PropertyName>size</PropertyName></size>
                    <rotation><PropertyName>rotation</PropertyName></rotation>
                    <mark>
                        <wellknownname><PropertyName>name</PropertyName></wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill 
                            (the color) and fill-opacity -->
                            <CssParameter name="fill">#FF0000</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </PointSymbolizer>

LineSymbolizer
''''''''''''''

Used to control how lines (or edges) are drawn.

Examples:

* GeoServer SLD cookbook
  
  `Lines <http://docs.geoserver.org/stable/en/user/styling/sld-cookbook/lines.html>`_ .

PolygonSymbolizer
'''''''''''''''''

Used to control how solid shapes are drawn.

Examples:

* GeoServer SLD cookbook
  
  `Polygons <http://docs.geoserver.org/stable/en/user/styling/sld-cookbook/polygons.html>`_ .
  
* Quick example using StyleBuilder to create a PolygonSymbolizer:

.. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
   :language: java
   :start-after: // quickPolygonSymbolizer start
   :end-before: // quickPolygonSymbolizer end
  
TextSymbolizer
''''''''''''''

Used to control the labelling system; labels are generated by TextSymbolizers and
thrown into the rendering engine which detect overlaps, sorts things out according
to priorities you have defined and decides on a final label placement.

This mays TextSymbolizer a little bit odd in that it does not get the final say on how labels are rendered on a pixel by pixel basis.

Examples:

* GeoServer SLD cookbook
  
  * `Point Labels <http://docs.geoserver.org/stable/en/user/styling/sld-cookbook/points.html#point-with-default-label>`_
  * `Line Labels <http://docs.geoserver.org/stable/en/user/styling/sld-cookbook/lines.html#line-with-default-label>`_
  * `Polygon Labels <http://docs.geoserver.org/stable/en/user/styling/sld-cookbook/polygons.html#polygon-with-default-label>`_

* Here is a quick example of creating a TextSymbolizer with StyleBuilder:
  
  .. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
     :language: java
     :start-after: // quickTextSymbolizer start
     :end-before: // quickTextSymbolizer end
  
  Here is the same example as an xml fragment::
  
            <TextSymbolizer>
                <Label><PropertyName>name</PropertyName></Label>
                <Font>
                    <CssParameter name="font-family">Lucida Sans</CssParameter>
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
                            <!-- CssParameters allowed are fill 
                            (the color) and fill-opacity -->
                            <CssParameter name="fill">#FF0000</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </PointSymbolizer>

Raster Symbolizer
'''''''''''''''''

Used to control the rendering of raster data with full "color map" control.

* GeoServer SLD cookbook
  
  * `Rasters <http://docs.geoserver.org/stable/en/user/styling/sld-cookbook/rasters.html>`_
  
StyleVisitor
^^^^^^^^^^^^

Just like with the FilterVisitor interface we are going to use these implementation to navigate over a nested data structure and either copy what we see, or modify it as we go.

While the StyleVisitor interface will let modify a style in place we have never found that to be a good idea (at best opening your code up to magic threading issues with what is probably a very active rendering thread).

The generic StyleVisitor interface is everything you would expect from the Gang of Four Visitor pattern, it has a visit methods one for each significant interface in a Style object.

To use a StyleVisitor pass it to a Style (or any style object) using the accepts method::
  
  style.accepts( styleVisitor );

You will find that not all Style objects accept a StyleVisitor; as an example Font does not. This is not really a problem - but it is something to keep in mind when writing your own visitor.

**Ready to Use Implementations**

There are a number of ready to use implementations; while we have provided some examples on this page please explore what is available in the library - you can do this quikly by checking the javadocs.

* StyleAttributeExtractor - return all the attributes mentioned by this style; used by the renderer when constructing a Query
* DuplicatingStyleVisitor - return a copy of the style
* RescaleStyleVisitor - return a copy of the style modified for display at a different scale.

**Implementation Tips**

If you are used to simple visitors on list like data structures you are in for a surprise - StyleVisitor does not navigate the Style object structure on its own you are going to have to do the work.::
  
  class YourStyleVisitor implements StyleVisitor {
      ...
      public void visit(Halo halo) {
          // do your work here
  
          // make sure you visit the "child" objects
          if( halo.getFill() != null ) halo.getFill().accepts( this );
          if( halo.getRadius() != null ) halo.getRadius().accepts( this );
      }
      ...
  }

We should have an AbstractStyleVisitor for you to start from; perhaps you would like to write it for us?

DuplicatingStyleVisitor
'''''''''''''''''''''''

DuplicatingStyleVisitor will copy any style object; it keeps track of what is copied using an internal stack (this means it is not thread safe!).::
  
  DuplicatingStyleVisitor xerox = new DuplicatingStyleVisitor();
  style.accepts( xerox );
  Style copy = (Style) xerox.getCopy();

Please note this works for everything::
  
  DuplicatingStyleVisitor xerox = new DuplicatingStyleVisitor();
  lineSymbolizer.accepts( xerox );
  LineSymbolizer copy = (LineSymbolizer) xerox.getCopy();

RescaleStyleVisitor
'''''''''''''''''''

RescaleStyleVisitor can be used to scale up a provided style; something that is useful when printing. The SLD specification is pretty careful about working with pixels at all times (this is annoying when you switch to 300 DPI).::
  
  RescaleStyleVisitor scale = new RescaleStyleVisitor(5.0);
  style.accepts( scale );
  Style bigger = (Style) scale.getCopy();

Please note that this also returns a copy; while you could modify a style in place using a visitor we find that life is too short for threading issues.::
  
  RescaleStyleVisitor scale = new RescaleStyleVisitor(5.0);
  lineSymbolizer.accepts( scale );
  LineSymbolizer bigger = (LineSymbolizer) scale.getCopy();
