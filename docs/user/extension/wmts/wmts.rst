WebMapTileServer
================

The client code is fairly slick in that it takes care of version negotiation (inheriting functionalities from AbstractOpenWebService),
and even a few server specific wrinkles for you.

This page contains examples how how to connect and use the GeoTools WebMapTileServer class. WebMapTileServer acts as a proxy
for a remote "Web Map Tile Server" and can be used to examine and retrieve published information in the forms of descriptive Java beans, and raw images.

* To begin communicating with a server, pass in a URL pointing to a WMTS Capabilities document.

  Constructing a WebMapTileServer object:

  .. code-block:: java

    URL url = null;
    try {
      url = new URL("https://gibs.earthdata.nasa.gov/wmts/epsg4326/best/wmts.cgi?VERSION=1.0.0&Request=GetCapabilities&Service=WMTS");
    } catch (MalformedURLException e) {
      // will not happen
    }

    WebMapTileServer wmts = null;
    try {
      wmts = new WebMapTileServer(url);
    } catch (IOException e) {
      // There was an error communicating with the server
      // For example, the server is down
    } catch (ServiceException e) {
      // The server returned a ServiceException (unusual in this case)
    } catch (SAXException e) {
      // Unable to parse the response from the server
      // For example, the capabilities it returned was not valid
    }

  Assuming nothing went wrong, we now have a WebMapTileServer object "wmts"
  that contains the Capabilities document of returned from the server. It
  can also provide further communication with the server.


WMTSCapabilities
----------------

WMTSCapabilities is used to describe the abilities and published information available via a Web Map Tile Service.

You can retrieve a WMTSCapabilities directly from your WebMapTileService:

.. code-block:: java

  WMTSCapabilities capabilities = wmts.getCapabilities();

From the capabilities you can retrieve info about the Service, the available Requests, the available Layers and the
configured MatrixSets.

Service
^^^^^^^

Service contains metadata information about the server:

.. code-block:: java

  WMTSCapabilities capabilities = wmts.getCapabilities();

  String serverName = capabilities.getService().getName();
  String serverTitle = capabilities.getService().getTitle();
  System.out.println("Capabilities retrieved from server: " + serverName + " (" + serverTitle + ")");

Request
^^^^^^^

The WMTS Operations describe the requests entrypoints:

.. code-block:: java

  URL requestUrl = wmts.getCapabilities().getRequest().getGetTile().getGet();

Layer
^^^^^

The Layer section contains detailed information about the layers on the server:

.. code-block:: java

  WMTSCapabilities capabilities = wmts.getCapabilities();

  // gets all the layers in a list, in the order they appear in
  // the capabilities document
  List<WMTSLayer> layers = capabilities.getLayerList();


Every layer has a list of related Formats::

  String layerName = ...

  // Get formats for a given layer
  List<String> formats = wmts.getCapabilities().getLayer(layerName).getFormats()

Styles
^^^^^^

For each layer you can get its available styles:

.. code-block:: java

  for (WMTSLayer layer : wmts.getCapabilities().getLayerList()) {
     System.out.println("Layer: " + layer.getName());
     System.out.println("       " + layer.getTitle());

     for (StyleImpl style : layer.getStyles()) {
        // Print style info
        System.out.println("Style:");
        System.out.println("  Name:  " + style.getName());
        System.out.println("  Title: " + style.getTitle());
     }
  }


MatrixSet
^^^^^^^^^

Each Layer is available in one or more MatrixSet.
Here's an example on how to loop in the matrices of the published layers:

.. code-block:: java

  for (WMTSLayer layer : wmts.getCapabilities().getLayerList()) {
     for (String tileMatrixId : layer.getTileMatrixLinks().keySet()) {
        List<TileMatrixLimits> limits = layer.getTileMatrixLinks().get(tileMatrixId).getLimits();
        TileMatrixSet matrixSet = wmts.getCapabilities().getMatrixSet(tileMatrixId);

        List<TileMatrix> matrices = matrixSet.getMatrices();

        for (TileMatrix matrix : matrices) {
           System.out.println("LAYER " + layer.getName()
                    + ", matrixSet id : " + tileMatrixId
                    + ", matrix " + matrix.getIdentifier()
                    + ", matrixGrid" + matrix.getMatrixWidth() + "x" + matrix.getMatrixHeight()
                    + ", tileSize" + matrix.getTileWidth() + "x" + matrix.getTileHeight());
        }
     }
  }


GetTileRequest
--------------

Making a GetTile request is more interesting than looking at WMSCapabilities.

1. We need to ask the client to create us a GetTileRequest object:

   .. code-block:: java

     GetTileRequest req = wmts.createGetTileRequest();

2. Using data from the capabilities document (or our own personal choices) we can configure the request object:

   .. code-block:: java

     req.setLayer(...);
     req.setStyle(...);

     // set any other required dimensions, as advertised by the layer - only time is supported at the moment
     // if time is need but is not provided, the default value will be used
     req.setRequestedTime();

     // set bbox - coords and CRS
     req.setRequestedBBox(...);

     // set the pixel size of the image to be created
     req.setRequestedHeight(h);
     req.setRequestedWidth(w);

4. Now we can issue our request and grab the Tiles:

   .. code-block:: java

     Set<Tile> responses = wmts.issueRequest(req);

   We are getting a Set of Tiles, which are all the Tiles needed to fill
   the bbox we requested.

5. In order to get the tile images, we'll have to use the info in the Tiles:

   .. code-block:: java

     for(Tile tile: responses) {
        BufferedImage image = ImageIO.read(tile.getInputStream());
        ... use the image (you may want to render it onto a Graphics2D) ...
     }
