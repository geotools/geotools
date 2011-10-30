Image Pyramid Plugin
^^^^^^^^^^^^^^^^^^^^

There are a series of interesting raster "formats" that make use of a bunch of raster files and
present the result as a single image. This format uses a magic directory structure combined with
a property file describing what goes where.

**Reference**

* `ImagePyramidReader <http://docs.geotools.org/latest/javadocs/index.html?org/geotools/gce/imagemosaic/ImageMosaicFormat.html>`_
* :doc:`mosaic`
* http://docs.geoserver.org/stable/en/user/data/imagepyramid.html
* http://docs.geoserver.org/stable/en/user/tutorials/imagepyramid/imagepyramid.html?highlight=bluemarble

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-gt-imagepyramid</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Example
^^^^^^^

On disk an image pyramid is going to look a bit like the following (you can use any format for
the tiles from mrsid to tiff)::

  directory/
  directory/pyramid.properties
  directory/0/mosaic metadata files
  directory/0/mosaic_file_0.tiff
  directory/0/...
  directory/0/mosiac_file_n.tiff
  directory/...
  directory/0/32/mosaic metadata files
  directory/0/32/mosaic_file_0.tiff
  directory/0/32/...
  directory/0/32/mosiac_file_n.tiff

The format of that pyramid.properties file is magic, while we can look at the javadocs
(and the following example), you are going to have to read the source code on this one::
  
  # Pyramid Description
  #
  # Name of the coverage
  Name=ikonos
  
  #different resolution levels available
  Levels=1.2218682749859724E-5,9.220132503102996E-6 2.4428817977683634E-5,1.844026500620314E-5 4.8840552865873626E-5,3.686350299024973E-5 9.781791400307775E-5,7.372700598049946E-5 1.956358280061555E-4,1.4786360643866836E-4 3.901787184256844E-4,2.9572721287731037E-4
  
  #where all the levels reside
  LevelsDirs=0 2 4 8 16 32
  
  #number of levels availaible
  LevelsNum=6
  
  #envelope for this pyramid
  Envelope2D=13.398228477973406,43.591366397808976 13.537912459169803,43.67121274528585
  
