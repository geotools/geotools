Icon
----

The gt-render module supplies two factories that can be used to teach the rendering system about your custom icons.

Notes: This is considered Advanced material because you need to register the factory you have created.

MarkFactory
^^^^^^^^^^^

The job of this class is to evaluate the provided expression (it should evaluate into a String) and construct a good Java Shape out of it that we can draw on the map.:

.. literalinclude:: /../src/main/java/org/geotools/render/SplatMarkFactory.java
   :language: java

To use this class we are going to need to define a style that refers to this new mark:

.. literalinclude:: /../src/main/java/org/geotools/render/StyleExamples.java
   :language: java
   :start-after: // splatExample start
   :end-before: // splatExample end

To register your class you will need to:
  
1. Create the following folder: META_INF/services/
2. In this folder create a file: org.geotools.renderer.style.MarkFactory
   
   The contents of this file should list your implementation::
      
      org.geotools.demo.render.SplatMarkFactory

ExternalGraphicsFactory
^^^^^^^^^^^^^^^^^^^^^^^

This factory is much more capable, as it is not restricted to creating a simple shape.

This class is responsible for:

* evaluate the provided expression (it should evaluate into a URL)
  
  A feature is allowing the expression to refer to attributes if needed.

* construct a Java Icon
* both a format and a size are provided for reference

Your implementation can:

* implement a dynamic Icon directly (useful for symbols that need to be composed based on feature attributes)
* or as simple as a straight SwingIcon backed by a PNG file contained in your jar.
