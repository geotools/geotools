Style Layer Descriptor
----------------------

The ``gt-main`` module provides interfaces for the Style Layer Descriptor data structure and
offers GeoTools specific extensions to the Symbology Encoding data structure.

References:

* :doc:`style </tutorial/map/style>` (tutorial)
* :doc:`gt-api symbology encoding<../api/se>`
* :doc:`gt-render style <../render/style>` (code examples)
* http://www.opengeospatial.org/standards/sld (style layer descriptor)
* http://www.opengeospatial.org/standards/symbol (symbology encoding)

SLD Utility Class
^^^^^^^^^^^^^^^^^

The SLD utility class is used to perform common operations on ``Style`` objects. This is the class you can used when you "just" want to hack away at an existing ``Style`` object for a test case.

* Changing the Colors
  
  Utility methods exits to change the "first" symbolizer found in the ``Style``:
  
  .. code-block:: java
    
     SLD.setLineColour(style, Color.BLUE );
     SLD.setPolyColour(style, Color.RED );
  
* You can also perform these kinds of changes on individual symbolizers:

  .. code-block:: java
  
     SLD.setLineColour( lineSymbolizer, Color.BLUE );
     SLD.setPolyColour( polygonSymbolizer, Color.RED );

* You can combine this approach with a ``StyleVisitor`` to edit the colors for specific rules:

  .. code-block:: java
    
     DuplicatingStyleVisitor repaint = new DuplicatingStyleVisitor(){
        boolean flag=false;
        public void visit(Rule rule){
             flag=rule.getName().equals("fred");
   
             super.visit( rule ); // makes a copy
             flag=false;        
        }
        public void visit(PolygonSymbolizer polygonSymbolizer){
             super.visit( rule ); // makes a copy
             if( flag ){
                 PolygonSymbolizer copy = getObject(); // the copy just made
                 SLD.setPolyColour( copy, Color.RED );
             }
        }
     };
     style.accepts( repaint ):
     Style modified = (Style) repaint.getObject();

.. note:: This utility class originated as part of the uDig application.
