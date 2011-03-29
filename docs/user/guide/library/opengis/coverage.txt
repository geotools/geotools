GridCoverage
------------

Before tackling the details of how to create and use **GridCoverage2D** instances, the semantics of the information contents of a GridCoverage must be well understood.

At the most basic level, a GridCoverage is a matrix of numeric values with information about the meaning of the numeric values and about the geographic position of the values. So for each 'GridCoverage' there will be one data structure, such as an image file, which contains all of the numeric values of interest. There will also be some data structure containing the meaning of the data and another data structure containing the georeferencing information for the data.

Example:

* A Digital Elevation Model (DEM) file
  
  Consider an image of 1000 pixels in width and of 600 pixels in height
  with one floating point value per pixel. The data source will include
  some way to access this matrix of values, however, we also need more
  information to understand what the values mean.

Categories
^^^^^^^^^^

Images used in geospatial science have highly structured values. For example, the image could have one value, like -9999, used to indicate that the data were not available for that location. The image might have another value, such as -8888, used to indicate cloud cover. Then we might have the actual measurements in a range, possibly from 0 to 5000 but these actually map to 5000 levels between -99 to 400 of some unit of measure which interests the user, say US feet above a given vertical datum.

In GeoTools, we treat the different kinds of values with different categories. That is we create a Category for the -9999 value, another for the -8888 value and a third for the range from 0 to 5000. The categories define how we want to use the values, what they will map to and provide a default color to use for the data when presented in an image. So we have:

========== ==================== ============== ======================
RAW Data   Category             Value          Colour
========== ==================== ============== ======================
-9999      QualitativeCategory  NaN            new Color(0,0,0,0)
-8888      QualitativeCategory  NaN            Color.WHITE
0:5000     QuantitativeCategory -99:400 foot   Color.BLUE:Color.GREEN
========== ==================== ============== ======================

If the file contains a value which is not defined in one category or another, the behaviour of how that value will be handled is undefined.

SampleDimensions
^^^^^^^^^^^^^^^^

A sample dimension establishes the mapping for each band of the resulting GridCoverage. Typically we map each band in the original data to one SampleDimension in the GridCoverage so the number of bands in the original data is the same as the number of bands in the resulting GridCoverage. In this example we have only one band so we only create a single SampleDimension.

The 'sample dimension' will be built out of the three category objects so we have a complete mapping from the data values in the file to some numeric value. To build the sample dimension, we would create an array of the Category instances and define the Unit in which the values -99 to 400 are defined.

Georeferencing
^^^^^^^^^^^^^^

To build a GeoTools GridCoverage we also need information about the mapping between coordinate space in the data matrix (row/column) and the real world coordinate space in some geographically meaningful Coordinate Reference System, projected or not.

The quick way to build this information is to build an Envelope from both the CoordinateReferenceSystem of the resulting GridCoverage and the coordinates in that CRS of the lower and upper corners of the original data matrix (the outer corners not the pixel center). The coverage module can draw on this Envelope to build the MathTransform which it will use. While this strategy is provided for convenience, it does not allow for much flexibility: the grid must be perfectly axis aligned in the resulting CRS and the axes must be in the same order in the original data matrix and in the resulting CRS.

A more complex but fully powerful approach to building the georeferencing information involves building oneself the MathTransform which maps from the coordinate space of the data matrix to the georeferenced space of the real world for the resultant GridCoverage. This approach allows for rotated imagery, axis order switching and other more complex transforms.

GridCoverage views
^^^^^^^^^^^^^^^^^^

With the original data matrix, an array of all the SampleDimension instances and the georeferencing information, we can then create the GridCoverage instance.

On the resulting GridCoverage objects, we will be able to obtain different 'views' of the data values. We can work against either of the original 'native' data, the transformed 'geophysical' data in the given unit, or the colour values.

Example: An RGB file
