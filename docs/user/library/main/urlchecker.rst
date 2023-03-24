``URLChecker``
--------------

``URLChecker`` is an SPI extension point allowing to plug-in a custom URL checker.
URL checks can be implemented whenever the code is going to access a location that
might be externally provided, either because the input is used provided, or because
it's part of remote access in a distributed system. 


.. literalinclude:: /../../modules/library/main/src/main/java/org/geotools/data/ows/URLChecker.java
   :language: java
   :start-after: doc-begin
   :end-before: doc-end


Examples of this situation are:

* Accessing a remote icon as part of a user provided style (e.g., SLD) or though a dynamic portion
  of a system provided style (e.g. dynamic symbolizers, general usage of ``env`` function in styles).
* Accessing a remote style (e.g., dynamic SLD in WMS GetMap).
* Accessing a dynamically provided WFS source in WMS "feature portrayal" mode.
* Accessing a remote input in a WPS process.

URL checks can be performed using the ``URLCheckers`` class, which will look up the plugged in
chekers and run them one by one, until one of them accepts the target location, or failing the
check if none of them does.

At the time of writing, inside GeoTools remote icon access is the only case where a URL check 
is performed, but more might be added in the future. More extensive checks are implemented
downstreams, in GeoServer, where most of the user provided remote locations are used.

Also, by default, GeoTools contains no URLChecker implementation, meaning that out of the
box it will accept any URL. This is done because each implementation will have its own
specific requirements to controlling remote access. The downstreams GeoServer project
provides an example of how a configurable ``URLChecker`` may be implemented.