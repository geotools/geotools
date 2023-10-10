HTTP Clients
------------

Supports calling external HTTP services by a common client API. The API is defined by two interfaces HTTPClient and HTTPResponse.

.. figure:: /images/gt-http.svg
   
   gt-http module

To create an implementation of the HTTPClient we're offering a factory through HTTPClientFinder.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-http</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**

.. sidebar:: Extra
   
   .. toctree::
      :maxdepth: 1
      
      logging

.. toctree::
   :maxdepth: 1
   
   httpclient
   httpresponse



HTTPClientFactory
^^^^^^^^^^^^^^^^^

Main approach to get a http client is through the client factory. That one can be retrived through HTTPClientFinder.
Here is the easiest way::

  HTTPClient client = HTTPClientFinder.createClient();

You could also get a specific behavior::

  HTTPClient client = HTTPClientFinder.createClient(HTTPConnectionPooling.class);


To get a specific client::

  HTTPClient client = HTTPClientFinder.createClient(new Hints(Hints.HTTP_CLIENT,
                                    CustomHttpClient.class));

For a test case it is possible to set a MockingHttpClientFactory to avoid network traffic.
The factory will present a client that is suitable for the specific need in the test.
The code could look like::

   @Test
    public void testThatCallsgetHttpClientFactory() throws Exception {

        Hints.putSystemDefault(Hints.HTTP_CLIENT_FACTORY, MockingHttpClientFactory.class);
        try {
        .... 
            HTTPClient client = HTTPClientFinder.createClient();
        ....   
        } finally {
            Hints.removeSystemDefault(Hints.HTTP_CLIENT_FACTORY);
        }
    }

  
System property
'''''''''''''''

The client can also be set through system property at the command line::

  java -Dorg.geotools.http.client=org.geotools.http.CustomHttpClient ....