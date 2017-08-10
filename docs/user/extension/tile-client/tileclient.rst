Tile client
===========

The tile client module provides logic for retrieving images from tile oriented services.

There are a couple of services implemented: OSM and Bing.

The Bing tile service
---------------------

Bing requires a key, which you can generate `here <http://www.microsoft.com/maps/create-a-bing-maps-key.aspx>`_.

This service follows the documentation for the `Imagery API <https://msdn.microsoft.com/en-us/library/ff701721.aspx>`_.

In order for the BingService to work correctly, you must use the URL fragment
provided by the `Get Imagery Metadata <https://msdn.microsoft.com/en-us/library/ff701716.aspx>`_ page.
In particular, you need to instantiate a BingService with a URL
template such as ``http://ecn.subdomain.tiles.virtualearth.net/tiles/r${code}.jpeg?key=YOUR_BING_KEY&g=129&mkt={culture}&shading=hill&stl=H``:

.. code-block:: java

    String baseURL = "http://ecn.subdomain.tiles.virtualearth.net/tiles/r${code}.jpeg?key=YOUR_BING_KEY&g=129&mkt={culture}&shading=hill&stl=H";
    TileService service = new BingService("Road", baseURL);
 
    // you may add to a map:
    map.addLayer(new TileLayer(service));
 
    // or do some hard work to fetch the tiles
    Collection<Tile> tiles = service.findTilesInExtent(viewportExtent, scale, false, 128);

The ``${code}`` value will be substituted by the tile code (the quadkey) when the BingTile creates its URL.


OpenStreetMap
-------------

As for Bing, you can define an OSM service simply like this:

.. code-block:: java

    String baseURL = "http://tile.openstreetmap.org/";
    TileService service = new OSMService("OSM", baseURL);

    // you may add to a map:
    map.addLayer(new TileLayer(service));


If you need, you can retrieve single tiles:

.. code-block:: java

    Tile t = new OSMTile(
        new OSMTileIdentifier(38596, 49269, new WebMercatorZoomLevel(17), service.getName()),
        service);

    // retrieve the image!
    BufferedImage image = t.loadImageTileImage(t);
