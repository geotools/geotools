Checking user provided location
-------------------------------

GeoTools provides a ``URLChecker`` Service Provider Interface that library users can implement
to assert control over user provided locations (remote or otherwise) before they are actually
accessed.

Code implementing access to such resources can use the ``URLCheckers`` utility class to verify
that the user provided location is valid before attempting to access it. An ``URLCheckerException`` 
will be thrown in case access to such location is denied. For example:

.. code-block:: java

    URL url = new URL("http://www.acme.com/myfile.txt");
    URLCheckers.checkURL(url);

    String path = "/home/john/myfile.txt";
    URLCheckers.checkFile(path);

At the time of writing, GeoTools provides no default implementation of the ``URLChecker`` SPI, 
which means that all user provided locations are considered valid. However, downstream
projects may implement their own, for example, GeoServer does.

Any place in the code that accesses potentially user provided locations should use the 
``URLCheckers`` utility to validate the location before attempting to access it.
At the time of writing, implementation of ``MarkFactory`` and ``ExternalGraphicFactory`` are
using ``URLCheckers``, as dynamic, remote icons are often used. 

In time, more GeoTools classes will start implementing this pattern (e..g, WMS and WFS
cascading might use it to validate the backlinks provided in capabilities documents).