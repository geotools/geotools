Coverage FAQ
------------

Q: Relationship with ISO Coverage?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Within GeoTools, the coverage module exists below the main module (where Feature is defined) and therefore a GeoTools GridCoverage is not a feature.

This is in opposition to the ISO Coverage model (where Coverage extends Feature) provided by ISO 19123 specification.

GeoTools 'Coverage' objects emerged from an earlier specification published by the Open Geospatial Consortium (OGC) called "Grid Coverage Services Implementation" (OGC 01-004).

Q: How is raster data supported?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The design of the coverage modules follows closely the design of image handling in Java. Java provides three major subsystems in their Java media APIs which are used and extended in GeoTools.

`Image I/O API <https://docs.oracle.com/javase/1.5.0/docs/guide/imageio/>`__
  The Java Runtime include an Image I/O system to access files with image content. This provides code to access many
  standard image formats.
  
`ImageN <https://eclipse-imagen.github.io/imagen/>`__
  Initially developed by Sun Microsystems as "Java Advanced Imaging" this project was donated to the Eclipse Foundation
  for use by the geospatial community. This library provides a powerful image processing engine for performing operations on images.
  Not only does ImageN provide efficient code for performing lots of operations, but it creates a system through which developers can create
  chains of operations which can be applied repeatedly to an image or applied to a whole slew of images.

  ImageN now includes "JAI-Ext" and "JAI-Tools" functionality developed by the geospatial community
  including the powerful `Jiffle <https://eclipse-imagen.github.io/imagen/guide/jiffle/>`__ domain specifc language (offering a deferred loading raster calculator).

`ImageIO-Ext <https://github.com/geosolutions-it/imageio-ext>`__
  Extends ImageIO by treating image access as one of the ImageN operations allowing for differed access and enabling the treatment
  of file access as a standard step in an operation chain. The module also provides access to additional geospatial file formats.

When combined, these subsystems provide immense power to the GeoTools coverage module but this dependency does require that programmers who wish to use and extend the module must learn to use these other Java modules.

To get the most out of the ``gt-coverage`` module developers should be familiar with the underlying mathematical ideas. Knowledge of ideas such as an Affine Transforms (for rotating and scaling) is required to work effectively with the coverage module. Affines are explained any computer graphics textbooks, and on many web sites, if you need a refresher.

Q: How to make raster data work in a NetBeans based application?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Reportedly, the NetBeans class loader breaks ImageIO plugin mechanism which is used
to locate image format readers, image input and output streams, and the like.
To work around it, run the following line of code, only once, before any raster
access::
  
  IIORegistry.getDefaultInstance().registerApplicationClasspathSpis();
