LoggingHttpClient
-----------------

You can wrap a HTTPClient with the LoggingHttpClient to log all requests generated.
This can be accomplished by::

  HTTPClient client = HTTPFactoryFinder.createClient(new Hints(Hints.HTTP_LOGGING,
                              "True"));

Or as a system propety::

  java -Dorg.geotools.http.logging=True ....


All messages goes through the regular channel, and you will get the url requested. And if you turn on FINEST you can get the content sent in a POST or the content of the response.