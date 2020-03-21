WKT Marks
---------

The use of WKT marks (prefix with ``wkt://`` in your SLD) allows an easy way to add complex symbols to the rendering process. 
Also there is the option of rendering marks built from WKT geometries; moreover, some Java-generated 
shapes can be used to draw meteorological symbols. We hope the weathermen will find this useful.


Examples:

* A double dashed line with transparent background can be shown with this SLD fragment::

          <LineSymbolizer>
            <Stroke>
              <GraphicStroke>
                <Graphic>
                  <Mark>
                    <WellKnownName>wkt://MULTILINESTRING((-0.25 -0.25, -0.125 -0.25), (0.125 -0.25, 0.25 -0.25), (-0.25 0.25, -0.125 0.25), (0.125 0.25, 0.25 0.25))</WellKnownName>
                    <Fill>
                      <CssParameter name="fill">#0000ff</CssParameter>
                    </Fill>
                    <Stroke>
                      <CssParameter name="stroke">#0000ff</CssParameter>
                      <CssParameter name="stroke-width">1</CssParameter>
                    </Stroke>
                  </Mark>
                  <Size>6</Size>
                </Graphic>
              </GraphicStroke>
            </Stroke>
          </LineSymbolizer>
  
  Which gives this output: 
  
  
  .. image:: /images/double-dashed-line.png

* This module supports curves as well; for instance, this SLD fragment::
  
          <LineSymbolizer>
            <Stroke>
              <GraphicStroke>
                <Graphic>
                  <Mark>
                    <WellKnownName>wkt://COMPOUNDCURVE((0 0, 0.25 0), CIRCULARSTRING(0.25 0, 0.5 0.5, 0.75 0), (0.75 0, 1 0))</WellKnownName>
                    <Fill>
                      <CssParameter name="fill">#0000ff</CssParameter>
                    </Fill>
                    <Stroke>
                      <CssParameter name="stroke">#0000ff</CssParameter>
                      <CssParameter name="stroke-width">1</CssParameter>
                    </Stroke>
                  </Mark>
                  <Size>10</Size>
                </Graphic>
              </GraphicStroke>
            </Stroke>
          </LineSymbolizer>
  
  Produces this:
  
  .. image:: /images/emicircle-line.png

Property File Configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^^

WKT shapes may be pooled in properties files (this file must be packed in the same JAR of this module) and re-used across SLD files, becoming libraries of symbols.

For instance the WKT marks defined in this properties files (``wkt.properties``)::
  
  ls=LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)
  ...


To reference the above property file in your SLD, prefixed like this (note the protocol changed to ``wktlib://``)::
  
  <WellKnownName>wktlib://wkt.properties#ls</WellKnownName>

The location of the property file is either to be provided as an absolute path, or should be relative to a search root which can be programmatically set using ``WKTMarkFactory.setRoot(URL)``
  
Weather symbols
^^^^^^^^^^^^^^^

The weather symbols, which are create using Java code, should be prefixed by the ``extshape://`` protocol in the SLD,  are, for the time being, limited to the following

* ``Triangle``::
     
     <WellKnownName>extshape://triangle</WellKnownName> 
  
  Produces:
  
  .. image:: /images/triangle.png
* ``Emicircle``::
    
     <WellKnownName>extshape://emicircle</WellKnownName> 
   
  Produces:
  
  .. image:: /images/emicircle.png
* ``Triangle Emicircle``::
     
     <WellKnownName>extshape://triangleemicircle</WellKnownName> 
  
  Produces:
  
  .. image:: /images/triangleemicircle.png
* ``North Arrow``::
     
     <WellKnownName>extshape://narrow</WellKnownName> 
  
  Produces:
  
  .. image:: /images/narrow.png
* ``South Arrow``::
     
     <WellKnownName>extshape://sarrow</WellKnownName> 
  
  Produces:
  
  .. image:: /images/sarrow.png
  

A customizable arrow shape is also available, that can generate different arrows based on different values
of three optional parameters:

  * ``t``: thickness of the arrow base, between 0 and 1, default is 0.2
  * ``hr``: height over width ratio, a positive number, default is 2
  * ``ab``: arrow head base ration, between 0 and 1, default is 0.5 

The general syntax is::
     
     <WellKnownName>extshape://arrow?hr=[hrValue]&t=[tValue]&ab=[abValue]</WellKnownName> 
  
Changing the thickness between 0 and 1 generates:
  
  .. image:: /images/arrowThickness.png
  
Changing the height ratio between 0.2 and 2 generates:
  
  .. image:: /images/arrowHeight.png
  
Finally, changing the arrow base between 0 and 1 generates:

  .. image:: /images/arrowBase.png
  
All of the above pictures have been taken using an anchor point of (0.5,0), that is, anchoring
the arrow to its base, instead of its center, which would be its natural position. 

More complex symbols like Wind Barbs can be created with the ``windbarbs://`` prefix. This is an example::

	 <WellKnownName>windbarbs://default(15)[kts]</WellKnownName>
		
Where *15* indicates the wind intensity and *[kts]* indicates its unit of measure.

And it produces:

  .. image:: /images/barbs.png
  
WKT Marks as Strokes
--------------------

The WKT marks are normally converted into images and then repeated along the line geometry,
in case of continue shapes (e.g a Wave like Shape), the bends across different line segments
will break the continuity. If the goal is to have the wkt shape repeated and connected
irrespective of angles between line segments then it is possible to convert the WKT shape
into a stroke instead of image. 

To enable this make the following vendor option child of <LineSymbolizer> ::
  
  <VendorOption name="markAlongLine">true</VendorOption>					
  ...

(Optional) `markAlongLineScaleLimit` is used define the scale limit. Marks can be stretched a bit to have an exact number of them fit a straight segment in the line being symbolized. This value is a percentage.

 Valid values are between 0 and 1
 
 0 = Always Scale
 
 1 = Never Scale
 
 Default value = 0.9
 
 This value is used by Stroke to adjust the Shape width to its respective line segment.
 
 0.9 means that Shape cannot shrink beyond this scale limit to fit on the underlying line segment ::
 
  
  <VendorOption name="markAlongLineScaleLimit">0.9</VendorOption>
  ...
  
(Optional) `markAlongLineSimplify` is used to calculate simplification factor by scaling WKT shape height.

 Valid values are between 0 and 1
 
 0 = Never simplify
 
 1 = Use complete height of WKT Shape
 
 Default value = 0.5
 
 This value represents a multiplier that will be multiplied with Wkt shape height and used as pixel distance to simplify line geometry
 
 0.5 means that use half the height of Wkt shape. ::
 
  
  <VendorOption name="markAlongLineSimplify">0.5</VendorOption>
  ...

Examples:

* A red square wave following the line geometry as its stroke ::

          <LineSymbolizer>
            <Stroke>
              <GraphicStroke>
                <Graphic>
                  <Mark>                    					
					<WellKnownName>wkt://LINESTRING (0 0, 0 -0.5, 0.5 -0.5, 0.5 0.5, 1 0.5, 1 0)</WellKnownName>
                    <Stroke>
                      <CssParameter name="stroke">0xFF0000</CssParameter>
                      <CssParameter name="stroke-width">2</CssParameter>
                      <CssParameter name="stroke-linecap">round</CssParameter>
                    </Stroke>															
                  </Mark>				  
                  <Size>20</Size>  				  				  
                </Graphic>                
              </GraphicStroke>			  
            </Stroke>			
            <VendorOption name="markAlongLine">true</VendorOption>							
            <VendorOption name="markAlongLineScaleLimit">0.9</VendorOption> <!--Optional-->
            <VendorOption name="markAlongLineSimplify">0.4</VendorOption> <!--Optional-->
          </LineSymbolizer>
  
And it produces:  
  
  .. image:: /images/mark_along_line.png
  
Note : The blue line under the red square wave is a basic stroke shown here as a reference to actual line geometry

