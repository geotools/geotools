Coverage MultiDimensional
-------------------------

Module looking into coverage "ND" (for N-Dimensional).

The following Module contains two new plugins for reading the following raster formats:

* NetCDF
* Grib

Also the module contains a sub-module called coverage-api containing a few classes than can be used for creating a new plugin 
for another multidimensional raster format.

It should be pointed out that the two modules internally use the `Unidata JAVA Libraries <http://www.unidata.ucar.edu/software/thredds/current/netcdf-java/>`_
for accessing the data.

NetCDF
++++++

The NetCDF plugin gives the ability to access to a NetCDF file. The Maven dependency associated is::

	<dependency>
		<groupId>org.geotools</groupId>
		<artifactId>gt-netcdf</artifactId>
		<version>${geotools.version}</version>
	</dependency>

For reading a NetCDF file we must follow these simple steps:

* Create a new instance of NetCDFReader;
* Get the name of all the input coverages;
* Select one of them and use it;

Here is described an example::

	public static void test(){
		
		// Selection of the NetCDF file
		File file = new File("path/to/file.nc");
		
		// Creation of the NetCDF reader
		final NetCDFReader reader = new NetCDFReader(file, null);
		
		// It is better to surround this part of code with a try-finally construct
		// in order to avoid to leave the reader unclosed.
		try {
			// Getting the coverage names
			String[] names = reader.getGridCoverageNames();

			// Selection of the first coverage name
			String first = names[0];
			
			// Selection of the coverage associated to the name
			GridCoverage2D grid = reader.read(first, null);
			
			// Example: Get the value for the following position.
			float[] value = grid.evaluate((DirectPosition) new
				DirectPosition2D(grid.getCoordinateReferenceSystem(), 5, 45 ), new float[1]);

		} finally {
			// Closure of the Reader
			if (reader != null) {
				try {
					reader.dispose();
				} catch (Throwable t) {
					// Log the exception
				}
			}
		}
	}

Multiple bands dimensions are supported, although some configuration needs to be provided through the ancillary file.
Let's consider that we have a dimension called data that contains three values quality, pressure and temperature. An
ancillary file similar to this one needs to be provided::

	<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	<Indexer>
	    <coverages>
	        <coverage>
	            <schema name="station">
	                <attributes>the_geom:Polygon,imageindex:Integer,time:java.util.Date</attributes>
	            </schema>
	            <origName>station</origName>
	            <name>station</name>
	        </coverage>
	    </coverages>
	    <multipleBandsDimensions>
			<multipleBandsDimension>
				<name>data</name>
				<bandsNames>quality,pressure,temperature</bandsNames>
			</multipleBandsDimension>
		</multipleBandsDimensions>
	</Indexer>

The data dimension values will be mapped to three bands called quality, pressure and temperature.

Default Enhance Mode
++++++++++++++++++++
The default `Enhance <https://www.unidata.ucar.edu/software/thredds/v4.3/netcdf-java/v4.3/javadoc/ucar/nc2/dataset/NetcdfDataset.Enhance.html>`_ mode used
by GeoTools is `Enhance.CoordSys`, in order to change the enhance mode use the following system properties:

- org.geotools.coverage.io.netcdf.enhance.CoordSystems
- org.geotools.coverage.io.netcdf.enhance.ScaleMissing
- org.geotools.coverage.io.netcdf.enhance.ConvertEnums
- org.geotools.coverage.io.netcdf.enhance.ScaleMissingDefer

These can be set individually. According to the NetCDF-Java docs it is not safe to use ScaleMissing and ScaleMissingDefer
simultaneously.
	
GRIB
++++

The GRIB plugin does the same operations on the GRIB files. The Maven dependency associated is::

	<dependency>
		<groupId>org.geotools</groupId>
		<artifactId>gt-grib</artifactId>
		<version>${geotools.version}</version>
	</dependency>
	
The GRIB module only loads the Unidata libraries associated with the GRIB format and it internally calls the NetCDF reader for 
accessing the data. For this reason the example above can also be used for GRIB data.
