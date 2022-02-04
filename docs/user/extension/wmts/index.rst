====
WMTS
====

The ``gt-wmts`` module constitutes functionality to work with a OGC Web Map Tile server. It contains the class WebMapTileServer that extends the AbstractOpenWebService, for making direct calls to the server.
It has also the class WMTSTileService that extends the functionality within :doc:`gt-tile-client <../tile-client/index>` module to offer functionality for a specific tile matrix structure.
At last it has two classes that is useful for accessing a WMTS layer within your application: WMTSCoverageReader and WMTSMapLayer.


**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-wmts</artifactId>
      <version>${geotools.version}</version>
    </dependency>

WebMapTileServer
----------------

The client code takes care of version negotiation, and even a few server specific wrinkles for you. It is based on the functionality of AbstractOpenWebService, and handles the requests: GetCapabilities and GetTile.

This page contains examples how how to connect and use the GeoTools WebMapTileServer class. WebMapTileServer acts as a proxy
for a remote "Web Map Tile Server" and can be used to examine and retrieve published information in the forms of descriptive Java beans, and raw images.

* To begin communicating with a server, pass in a URL pointing to a WMTS.

  Constructing a WebMapTileServer object:

  .. code-block:: java

    URL url = null;
    try {
      url = new URL("https://gibs.earthdata.nasa.gov/wmts/epsg4326/best/wmts.cgi");
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

  Assuming nothing went wrong, we now have a WebMapTileServer object ``wmts`` 
  that contains the Capabilities document returned from the server. It
  can also provide further communication with the server.


WMTSCapabilities
^^^^^^^^^^^^^^^^

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

The WMTS Operations describe the requests entry points:

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

Each Layer could use one or more matrix set's, and for each matrix it can restrict which tiles that are available.
This depends on the extent of the layer compared to the global extent of the matrix set.
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
^^^^^^^^^^^^^^

Making a GetTile request is more interesting than looking at WMTS capabilities.

1. We need to ask the client to create us a GetTileRequest object:

   .. code-block:: java

     GetTileRequest req = wmts.createGetTileRequest();

2. Using data from the capabilities document (or our own personal choices) we can configure the request object:

   .. code-block:: java

     req.setLayer(layer);
     req.setStyle(...);
     req.setFormat("image/png");
     req.setTileMatrixSet(...);
     req.setTileMatrix(...);
     req.setTileRow(9);
     req.setTileCol(12);

3. The GetTile supports headers for the http calls.

  .. code-block:: java

    req.getHeaders().add("Authentication", "Bearer uyhhkhasghbasgy9ji62gddkjady3y");

4. Then we would issue the request, and get the image.
   
   .. code-block:: java

    GetTileResponse response = server.issueRequest(req);
    BufferedImage image = response.getTileImage();


WMTSTileService
---------------

This class builds on the functionality of TileService. The main difference between a WMTS and a TileService is that a given WMTS layer might not cover all of the matrix set.
This is handled through matrix set links given by the configuration of each layer.

.. literalinclude:: /../src/main/java/org/geotools/wmts/WMTSDownloader.java
  :language: java
  :start-after: // start wmtsTileService example
  :end-before: // end wmtsTileService example


WMTSCoverageReader
------------------

These classes is based on a WebMapTileServer and is useful for exporting or displaying maps from a WMTS server. 
For export we treat the tiles as a coverage with the WMTSCoverageReader, and use ImageIO to write the rendered image.

.. literalinclude:: /../src/main/java/org/geotools/wmts/WMTSCoverage.java
  :language: java
  :start-after: // start wmtsCoverage example
  :end-before: // end wmtsCoverage example	

WMTSMapLayer
------------

To display the WMTS within a map we would use WMTSMapLayer in a similar way.

.. literalinclude:: /../src/main/java/org/geotools/wmts/WMTSMapViewer.java
  :language: java
  :start-after: // start wmtsMapViewer example
  :end-before: // end wmtsMapViewer example


In addition it's possible to use the TileLayer class of gt-tile-client with a WMTSTileService as input.
Be aware that TileLayer is a subclass of DirectLayer which uses the Viewport to specify dimensions.

References:
^^^^^^^^^^^

* http://www.opengeospatial.org/standards/wmts (OGC standard)
* http://www.geowebcache.org/ (WMTS server)
* :doc:`gt-tile-client <../tile-client/index>`


