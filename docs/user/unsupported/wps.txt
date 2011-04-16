WPS
---

The WPS module provides a "WPS client" API so programmers can easily build Web Process
Service requests and parse the responses. This module allows developers to
create getCapabilities, describeProcess and Execute requests for WPS servers
send requests and parse responses into objects

A main design goal of this module is to make the process of building a request and handling the
response quick and easy for programmers. By using Process Plugin wrapper classes, developers
can easily build and create process requests with just a few lines of code.

The following sections will show the design of this module and some examples of how to use it.

Internally the design of this module is similar to the WMS module with the client code 
taking care of creating requests, sending requests, and parsing responses.

The following are some of the core objects for this module:

* WebProcessingService
  
  The main object of the design is the WebProcessingService class. WebProcessingService acts as a
  proxy for a remote WPS Server and can be used to examine and retrieve information from the server,
  and to execute processes the server provides. To begin communicating with a server, simply pass
  in a URL pointing to a WPS Capabilities document (view examples below).

* WPSFactory
  
  This class wraps around an AbstractProcessFactory from the Process Plugin. By providing it a 
  ProcessDescriptionType bean (which can be fetched from a WebProcessingService describeProcess 
  request) it will build a Process Factory based on that process definition.
  
  This factory can then build processes which can be executed (view examples below).
  
* WPSProcess
  
  After creating a WPSFactory, you can build processes from it. This class wraps around a
  AbstractProcess from the Process Plugin. By calling its execute method, it will build up
  a request object, send it, then parse and return the results.
  
  By using these wrapper classes, a programmer can quickly build process requests and get results
  with only a few lines of code (view examples below).

WPS getCapabilties Example
^^^^^^^^^^^^^^^^^^^^^^^^^^

The following example shows how to create a WebProcessingService and use it to retrieve
a getCapabilities document.::

    URL url = new URL("http://localhost:8080/geoserver/ows?service=WPS&request=GetCapabilities");
    WebProcessingService wps = new WebProcessingService(url);
    WPSCapabilitiesType capabilities = wps.getCapabilities();
    
    // view a list of processes offered by the server
    ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
    EList processes = processOfferings.getProcess();

You can now iterate over the list of processes the server offers.

WPS describeProcess Example
^^^^^^^^^^^^^^^^^^^^^^^^^^^

This example shows how to do a full describeProcess request from a WebProcessingService.::

    // create a WebProcessingService as shown above, then do a full describeprocess on my process
    DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
    descRequest.setIdentifier("DoubleAddition"); // describe the double addition process
    
    // send the request and get the ProcessDescriptionType bean to create a WPSFactory
    DescribeProcessResponse descResponse = wps.issueRequest(descRequest);
    ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
    ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc.getProcessDescription().get(0);
    WPSFactory wpsfactory = new WPSFactory(pdt, url);
    
    // create a process 
    Process process = wpsfactory.create();

You now have a process built from the describeProcess description, which can be executed
as shown below.

WPS Execute Example
^^^^^^^^^^^^^^^^^^^

This example builds from the previous ones and shows how to send a request to execute a
simple "double addition" process.::

    // create a WebProcessingService, WPSFactory and WPSProcess as shown above and execute it 
    Process process = wpsfactory.create();
    
    // setup the inputs		
    Map<String, Object> map = new TreeMap<String, Object>();
    Double d1 = 77.5;
    Double d2 = 22.3;		
    map.put("input_a", d1);
    map.put("input_b", d2);
    
    // you could validate your inputs against what the process expected by checking
    // your map against the Parameters in wpsfactory.getParameterInfo(), but
    // to keep this simple let's just try sending the request without validation
    Map<String, Object> results = process.execute(map, null);
    
    Double result = (Double) results.get("result");

Now you you have a result that was calculated on the WPS server.