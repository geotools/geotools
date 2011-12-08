MongoDB DataStore
=================

Installing MongoDB
-------------------------

Install MongoDB using the appropriate package from:
http://www.mongodb.org/downloads

The MongoDB plugin depends on a JavaScript MapReduce script to calculate schema. The script (MetaDataCompute.js), and installation specifics (INSTALL.txt), can be found in modules/unsupported/mongodb/src/main/javascript/. Install this script on your MongoDB server.

Functionality
-------------

The MongoDataStore supports the use of MongoDB as a data store. This should be considered a beta version.

The current 'design document' in use for the test cases is not necessarily the
final solution and is acknowledged to have shortcomings.
