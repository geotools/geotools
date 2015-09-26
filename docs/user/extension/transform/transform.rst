Transform plugin
----------------

The *gt-transform* module allows to wrap ``SimpleFeatureSource`` or ``SimpleFeatureStore`` objects and transform their features and feature types. The transformation abilities include:

  * renaming existing attributes
  * convert an attribute type to a different type (via Converters api)
  * create a new attribute as an Expression of existing attributes (the module will try to figure out the target type of the new attribute if none is provided)
  * remove attributes

The transformed feature source will make sure all ``Filter`` and ``Query`` are translated back to the original feature type when accessing data, and will apply the transformations on the fly when returning data. If the original source was writeable, the transformed version will still work as writeable, with back transformations of renamed/retyped attributes.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-transform<artifactId>
      <version>${geotools.version}</version>
    </dependency>

While ``TransformFeatureSource`` and ``TransformFeatureStore`` can be created directly, it is advised to use the ``TransformFactory`` to build the transformed sources.

Selecting attributes
^^^^^^^^^^^^^^^^^^^^

The following example shows how to set up a transformation that merely selects a subset of attributes from an original feature type ``states`` into a reduced version called ``states_mini``: 

.. code-block:: java

      List<Definition> definitions = new ArrayList<Definition>();
      definitions.add(new Definition("the_geom"));
      definitions.add(new Definition("state_name"));
      definitions.add(new Definition("persons"));

      SimpleFeatureSource transformed = TransformFactory.transform(STATES, "states_mini", definitions);

Renaming attributes
^^^^^^^^^^^^^^^^^^^

Same as above, but some of the attributes are getting renamed:

.. code-block:: java
  
        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("geom", ECQL.toExpression("the_geom")));
        definitions.add(new Definition("name", ECQL.toExpression("state_name")));
        definitions.add(new Definition("people", ECQL.toExpression("persons")));

        SimpleFeatureSource transformed = TransformFactory.transform(STATES, "usa", definitions);

Creating new attributes with expressions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In this case the attributes are all defined in terms of expressions using source attributes. Notice how none of the attributes contains a target type definition, the module will perform a static analysis of the expressions and determine the target type automatically:

.. code-block:: java

        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(the_geom, 1)")));
        definitions.add(new Definition("name", ECQL.toExpression("strToLowercase(state_name)")));
        definitions.add(new Definition("total", ECQL.toExpression("male + female")));
        definitions.add(new Definition("logp", ECQL.toExpression("log(persons)")));

        SimpleFeatureSource transformed = TransformFactory.transform(STATES, "bstates", definitions);
 
