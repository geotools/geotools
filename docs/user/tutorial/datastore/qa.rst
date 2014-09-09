:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Quality Assurance
-----------------

Since this tutorial has been written the result has been broken out into a distinct **gt-csv** module. This work has also been forked into service as part of the GeoServer importer module.

Unsupported Module
^^^^^^^^^^^^^^^^^^

Good tutorials do not just teach, they get pressed into production. By popular request the CSVDataStore outlined in this tutorial is available as an "unsupported" plugin:

* :download:`CSVDataStore.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java>`
* :download:`CSVDataStoreFactory.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java>`
* :download:`CSVFeatureReader.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureReader.java>`
* :download:`CSVFeatureSource.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java>`
* :download:`META-INF/services/org.geotools.data.DataStoreFactorySpi </../../modules/unsupported/csv/src/main/resources/META-INF/services/org.geotools.data.DataStoreFactorySpi>`

To get an idea of what kind of "extra work" is required for a supported module:

#. Ask on the email list - a Project Steering Committee member can often reply with a +1 and go about getting you commit access on GitHub. The project is fairly relaxed with a safe "unsupported" area for new experiments.
#. Set up a pom.xml
#. Use a profile in unsupported/pom.xml to include your module in the build

Directory Support
^^^^^^^^^^^^^^^^^

Earlier copies of this tutorial would read an entire directory of files at a time. This functionality has been factored out into a support class and is used by implementations such as ShapefileDataStore.

The same steps can be taken to CSVDataStore - although it is a real trade off between the code being clear vs less typing.

GeoServer Fork
^^^^^^^^^^^^^^

This is more interesting, the code was forked in order to add new abilities:
   
* `org.geoserver.importer.csv <https://github.com/geoserver/geoserver/tree/master/src/extension/importer/core/src/main/java/org/geoserver/importer/csv>`_ (GitHub)

The implementation is strictly concerned with reading content (as part of an import process) and has added a nifty CSVStratagy (with implementations for CSVAttributesOnly, CSVLatLonStratagy, CSVSpecifiedLatLngStratagy and SpecifiedWKTStratagy).

Sounds like a sensible addition.
   
Info
^^^^

This is more a case of polish, you can fill in a "info" data strucutre to more accurately describe your information to the uDig or GeoServer catalog.

In our case we could pick up any comments at the top of the file and use them as the initial file description.