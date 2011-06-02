CouchDB DataStore
=================

Installing Couch/Geocouch
-------------------------

The easiest way to install couch is to obtain the installer from couchbase at:
http://www.couchbase.com/downloads/couchbase-server/community

Functionality
-------------

The CouchDBDataStore supports reading and writing (add only) with native couch 
spatial bbox queries. This should be considered a alpha version with minimal
tests and error handling.

The current 'design document' in use for the test cases is not necessarily the
final solution and is acknowledged to have shortcomings. This is one aspect for
future discussion and work - coming up with a set of supported couch designs
and branching internally on detection/recognition of a design.