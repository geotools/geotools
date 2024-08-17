Zoom to Scale denominators
==========================

The classes found in the ``org.geotools.styling.zoom`` package provide a way to convert zoom levels into scale denominators. In particular:
 
 * ``ZoomContextFactory`` is a factory class that provides a way to create a ``ZoomContext`` object for a given Tile Matrix Set, by name.
 * ``ZoomContext`` is an interface that provides a way to convert zoom levels into scale denominators.
 * ``ScalerRange`` is an interface that describe a range of scale denominators.

The ``WellKnownZoomContextFactory`` class provides way to create a ``ZoomContext`` object for well
zoom Tile Matrix Sets such as ``WebMercatorQuad`` and ``WGS84``. Other projects, such as GeoServer,
can provide their own implementations of ``ZoomContextFinder`` that matches configured Tile Matrix Sets,
e.g. ``GWCZoomContextFactory`` provides access to all Tile Matrix Sets defined in the GeoServer configuration.

Here is an example of how to use these facilities:

.. code-block:: java

    ZoomContextFactory zoomContextFactory = new WellKnownZoomContextFactory();
    ZoomContext zoomContext = zoomContextFactory.createContext("WebMercatorQuad");
    double scaleDenominator = zoomContext.getScaleDenominator(10);
    ScaleRange scaleRange = zoomContext.getScaleRange(10, 12);
