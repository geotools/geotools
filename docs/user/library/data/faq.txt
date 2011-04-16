Data FAQ
--------

Q: How do I get a FeatureStore?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

FeatureStore is an extension of FeatureSource that is only available if your information can be modified. 

With this in mind you can use an instanceof check, and then cast to a FeatureStore::

    SimpleFeatureSource source = dataStore.getFeatureSource( typeName );
    if( source instanceof SimpleFeatureStore ){
       // you have write access
       SimpleFeatureStore store = (SimpleFeatureStore) source;
    }
    else {
     // read-only
    }

Q: My FeatureSource is not an instance of FeatureStore?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Please check that write support is available. 

Common solutions:

* ShapeFile: the file is not writable (or is located on a DVD).
* WFS: We do not support read/write for WFS 1.1.0 at this time; please use VERSION=1.0.0 when connecting.
* Database: Please check the permissions of the username and password used to connect to the database.
