Data and Resources
------------------

As a geospatial library we distribute some data resources alongside and to support executable code.

We do out best not to commit large sample data as part of the GeoTools repository (in order to keep checkout times down).

For more information on handling of sample data for tests see :doc:`test/data`.

Maven Repo for stable schemas and datasets
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

There are number of modules which have the responsibility of packaging data to be managed in maven repository.

* :file:`modules/epsg-postgresql`, :file:`modules/epsg-hsql`, and :file:`modules/epsg-wkt`: Contain a format shifted distribution of the EPSG database. 
  
  Released as part of the GeoTools release cycle.

* :file:`modules/ogc`: modules combining schema definitions with data model.

  Released as part of the GeoTools release cycle.

* :file:`extension/app-schema/app-schema-packages`: modules packaging schema definitions only.
   
  Independent modules each with their own release cycle and version information.

* :file:`extension/app-schema/app-schema-data`: sample data for integration testing.
  
  Independent modules each with their own release cycle and version information.

