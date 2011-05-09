Swing FAQ
---------

Q: What is JMapPane for?
^^^^^^^^^^^^^^^^^^^^^^^^

The JMapPane class is primiarly used as a teaching aid for the
:doc:`tutorials </tutorial/index>` used to explore the GeoTools library.

It is developed in collaboration with the user list, and while not a intended as a
GIS application it is a good starting point for trying out your ideas.

Q: What is the best way to simulate a car moving on a map?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

There is a flying saucers demo you can review; but the real answer is to set up a second
raster to draw your moving car into; and draw that over top of the map rendered by gt-renderer.

Remember that JMapPane is just a demo showing how you can use StreamingRenderer to draw into a
BufferedImage. You can do the same thing in your own code; and have one buffered image for the
map and a second one for your "overlays" including the moving car.

Q: JMapPane is Slow how do I make it faster?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This really comes down to how you use the GeoTools renderer. Remember that the GeoTools renderer
is doing a lot of calculation and data access; not what you want in the middle of animation.

The gt-renderer is optimised for memory use; it does not loading your data into memory
(it is drawing from disk, or database, each time). You can experiment with loading your data
into memory (specifically into a spatial index) if you want faster performance out of it.

For raster rendering you have a great deal of control over performance using JAI TileCache settings
in addition to convering your rasters into an efficient format (anything is better than jpeg).

Referneces:

* `FeatureCollection Performance </library/main/collection>`_
