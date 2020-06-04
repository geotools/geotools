ArcfGIS ReST DataStore
======================


Overview
--------

Proof of concept of an ArcGIS ReST API datastore.


Unit tests
------------

mvn test


System tests
------------

Make sure to have all the necessary servwer certificates in a truststore.

mvn integration-test -Psystemtest -Djavax.net.ssl.trustStorePassword=<truststore pasword>

