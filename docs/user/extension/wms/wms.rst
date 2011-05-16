WebMapServer
------------

The client code is fairly slick in that it takes care of version negotiation, and even a few server specific wrinkles for you.

This page contains examples how how to connect and use the GeoTools WebMapServer class. WebMapServer acts as a proxy for a remote "Web Map Server" and can be used to examine and retrieve published information in the forms of descriptive java beans, and raw images.

* To begin communicating with a server, simply pass in a URL pointing to 
  a WMS Capabilities document.
  
  Constructing a WebMapServer object::
    
    URL url = null;
    try {
      url = new URL("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?VERSION=1.1.0&REQUEST=GetCapabilities");
    } catch (MalformedURLException e) {
      //will not happen
    }
    
    WebMapServer wms = null;
    try {
      wms = new WebMapServer(url);
    } catch (IOException e) {
      //There was an error communicating with the server
      //For example, the server is down
    } catch (ServiceException e) {
      //The server returned a ServiceException (unusual in this case)
    } catch (SAXException e) {
      //Unable to parse the response from the server
      //For example, the capabilities it returned was not valid
    }
  
  Assuming nothing went wrong, we now have a WebMapServer object "wms"
  that contains the Capabilities document of returned from the server. It
  can also provide further communication with the server.

WMSCapabilities
^^^^^^^^^^^^^^^

WMSCapabilities is used to describe the abilities and published information available via a Web Map Service.

You can retrive a WMSCapabilities directly from your WebMapService::
  
  WMSCapabilities capabilities = wms.getCapabilities();

This capabilities bean is split into three sections: Service, Request, and Layer.

Service
'''''''

Service contains metadata information about the server.::
  
  WMSCapabilities capabilities = wms.getCapabilities();
  
  String serverName = capabilities.getService().getName();
  String serverTitle = capabilities.getService().getTitle();
  System.out.println("Capabilities retrieved from server: " + serverName + " (" + serverTitle + ")");

Request
'''''''

Request contains information about what the server is capable of.::
  
  WMSCapabilities capabilities = wms.getCapabilities();
  
  if (capabilities.getRequest().getGetFeatureInfo() != null) {
    //This server supports GetFeatureInfo requests!
    // We could make one if we wanted to.
  }

Use GetMap to list Valid Formats. Retrieving valid formats for the GetMap operation::
  
  URL serverURL = new URL(...);
  
  WebMapServer wms = new WebMapServer(serverURL);
  
  WMSCapabilities capabilities = wms.getCapabilities();
  
  //Get formats for GetMap operation
  String[] formats = wms.getCapabilities().getRequest().getGetMap().getFormatStrings()

Layer
'''''

The Layer section contains detailed information about the layers on the server.::
  
  WMSCapabilities capabilities = wms.getCapabilities();
  
  //gets the top most layer, which will contain all the others
  Layer rootLayer = capabilities.getLayer();
  
  //gets all the layers in a flat list, in the order they appear in
  //the capabilities document (so the rootLayer is at index 0)
  List layers = capabilities.getLayerList();

**WMSUtils**

The concept of a Layer in WMS is a bit strange. Not all layers are useful (some are abstract and cannot be used, some are used as folders, and some do not even bother to have names - perhaps they are used for documentation?).

Rather than figure all this out yourself we recommend using the WMSUtils utility class.

To retrieve all the layers that can be requested (ie they have a name) try the following::
  
  Layer[] layers = WMSUtils.getNamedLayers(capabilities);

Note that each Layer maintains references to its children, so even though they are returned in flat arrays, the hierarchy can be reconstructed.

Styles
^^^^^^

For each layer you can get its available styles::
  
  for(int i=0; i< layers.length; i++){
    // Print layer info
    System.out.println("Layer: ("+i+")"+layers[i].getName());
    System.out.println("       "+layers[i].getTitle());
    System.out.println("       "+layers[i].getChildren().length);
    System.out.println("       "+layers[i].getBoundingBoxes());
    CRSEnvelope env = layers[i].getLatLonBoundingBox();
    System.out.println("       "+env.getLowerCorner()+" x "+env.getUpperCorner());       
 
    // Get layer styles
    List styles = layers[i].getStyles();
    for (Iterator it = styles.iterator(); it.hasNext();) {
        StyleImpl elem = (StyleImpl) it.next();                 

        // Print style info
        System.out.println("Style:");
        System.out.println("  Name:"+elem.getName());
        System.out.println("  Title:"+elem.getTitle());
    }                  
  }

GetMapRequest
^^^^^^^^^^^^^

Making a GetMap request is more interesting then looking at WMSCapabilities.

1. We need to ask the client to create us a GetMapRequest object::
     
     GetMapRequest request = wms.createGetMapRequest();

2. Using data from the capabilities document (or our own personal choices) we can configure the request object::
     
     request.setFormat("image/png");
     request.setDimensions("583", "420"); //sets the dimensions of the image to be returned from the server
     request.setTransparent(true);
     request.setSRS("EPSG:4326");
     request.setBBox("-131.13151509433965,46.60532747661736,-117.61620566037737,56.34191403281659");
     //Note: you can look at the layer metadata to determine a layer's bounding box for a SRS
   
   Here is a method to get all the useful "Named" layers in a WMS service::
     
     for ( Layer layer : WMSUtils.getNamedLayers(capabilities) ) {
       request.addLayer(layer);
     }

4. Now we can issue our request and grab our image::
     
     GetMapResponse response = (GetMapResponse) wms.issueRequest(request);
     BufferedImage image = ImageIO.read(response.getInputStream());

Other common activites:

* Requesting with a style
  
  If you want to request the map with some of its available styles use the appropriate addLayer method::
     
     Layer[] layers = WMSUtils.getNamedLayers(capabilities);
     List styles = layers[2].getStyles();
     Style style = styles.get(2);
     
     request.addLayer(layers[2]);
     request.addLayer(layers[2], style);
     request.addLayer(layers[2], style.getName());
     request.addLayer(layers[2].getName(), style);

* List of Valid Formats
  
  Retrieving valid formats for the GetMap operation::
     
     URL serverURL = new URL(...);
     
     WebMapServer wms = new WebMapServer(serverURL);
     
     WMSCapabilities capabilities = wms.getCapabilities();
     
     //Get formats for GetMap operation
     String[] formats = wms.getCapabilities().getRequest().getGetMap().getFormatStrings()
