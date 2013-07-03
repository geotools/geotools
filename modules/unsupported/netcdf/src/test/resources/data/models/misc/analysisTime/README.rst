The NetCDF data files in the misc directory (and its subdirectories analysisTime and ncmlAgg) are sample test data files created by Geocent and are approved for release.

You can confirm this in each of the NetCDF data file (*.nc) by opening them and you will see two global attributes:
	:classification_level = "unclassified" ;
    :distribution_statement = "Approved for public release.  Distribution unlimited." ;
	
In the ncmlAgg directory there are two additional configuration files (*.ncml).  These two files do not introduce any new data, they serve to aggregate the data in the specified data files (*.nc).
	


Suggested ways to view data in .nc files:
a. ncdump
http://www.unidata.ucar.edu/software/netcdf/docs/netcdf.html#NetCDF-Utilities

b. ncBrowse
http://www.epic.noaa.gov/java/ncBrowse/

c. toolsUI
http://www.unidata.ucar.edu/software/netcdf-java/documentation.htm

Generic link to software re: netcdf file access:
http://www.unidata.ucar.edu/software/netcdf/software.html	