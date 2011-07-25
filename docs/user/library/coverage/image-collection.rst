Image collection plug-in
------------------------

An ``ImageReader`` and ``Format`` implementation working against a directory tree of non georeferenced images (scanned maps or plain photos). The directory tree location is specified while building the reader (it's the location being read).

The plug-in will normally return images in the ``EPSG:404000`` wild-card Cartesian 2D reference system. The image to be returned is chosen randomly among the available ones on start up unless a filter is specified to choose a particular one. 

The filter has to follow the ``PATH=relative/path/to/image.jpeg`` pattern and be provided to the reader as a ``ParameterValue`` of type ``org.opengis.filter.Filter`` and name ``Filter``.

The plugin is meant to be used in an environment where the underlying image collection is very large and varies through time, and assumes the caller has some external knowledge about what images are available in the directory being published. 
