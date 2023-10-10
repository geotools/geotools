Style Layer Descriptor
----------------------

The ``StyleLayerDescriptor`` concept comes to us from  the SLD specification. It is aimed at defining how a
web map server can draw an entire map (with all the layers included in one gulp.)

References:

* :doc:`style </tutorial/map/style>` (tutorial)
* :doc:`gt-api se <se>`
* :doc:`gt-render style <../render/style>`
* http://www.opengeospatial.org/standards/sld (style layer descriptor)

Style Layer Descriptor API
^^^^^^^^^^^^^^^^^^^^^^^^^^

The Style Layer Descriptor API defines an enter map, as a series of layers to display.

.. image:: /images/sld.PNG

Since many of the elements referenced above assume we are operating inside a WMS this class is not used
frequently during day to day use of the GeoTools library. 

If your application allows user managed styles these interface allow names and titles to be recorded, along with the order in which layers are intended to be drawn.

Styling for a map is captured with three initial classes:

* ``StyledLayerDescriptor`` representing the styling information for an entire map
* ``NamedLayer`` defines ``FeatureTypeConstraints`` to test if the style is applicable to your data, and a list of ``Style``
* ``Style`` defines how ``features`` or ``Rasters`` are to be drawn

