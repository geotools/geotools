Coverage FAQ
------------

Q: Relationship with ISO Coverage?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Within GeoTools, the coverage module exists below the main module (where Feature is defined) and therefore a GeoTools GridCoverage is not a feature.

This is in opposition to the ISO Coverage model (where Coverage extends Feature) provided by ISO 19123 specification.

GeoTools 'Coverage' objects emerged from an earlier specification published by the OpenGeospatial Consortium (OGC) called "Grid Coverage Services Implementation" (OGC 01-004).

Q: How is raster data supported?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The design of the coverage modules follows closely the design of image handling in Java. Java provides three major subsystems in their Java media API's which are used and extended in GeoTools.

Image I/O system
  To access files with image content. This provides code to access many
  standard image formats.
  
Java Advanced Imaging (JAI) system
  which provides a powerful approach for performing operations on images.
  Not only does JAI provide efficient code for performing lots of
  operations, but it creates a system through which a user can create
  chains of operations which can be applied repeatedly to an image or
  applied to a whole slew of images.

JAI Image I/O system
  Combines the two other systems by treating image access as one of the
  JAI operations allowing for differed access and enabling the treatment
  of file access as a standard step in an operation chain. The module
  also provides access to additional file formats.

When combined, these subsystems provide immense power to the GeoTools coverage module but this dependency does require that programmers who wish to use and extend the module must learn to use these other Java modules.

To get the most out of the gt-coverage module developers should be familiar with the underlying mathematical ideas. Knowledge of ideas such as an Affine Transforms (for rotating and scaling) is required to work effectively with the coverage module. Affines are explained any computer graphics textbooks, and on many web sites, if you need a refresher.