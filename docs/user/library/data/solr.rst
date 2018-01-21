Solr Plugin
-----------

Solr is a popular documentation database.

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-solr</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

The following connection parameters are available:

+-------------------------+----------------------------------------------------+
| Param                   | Description                                        |
+=========================+====================================================+
| "solr_url"              | Url to a SOLR server CORE                          |
+-------------------------+----------------------------------------------------+
| "layer_mapper"          | Controls how documents are mapped to layers        |
+-------------------------+----------------------------------------------------+
| "layer_name_field"      | Field used in SOLR that holds the layer names      |
+-------------------------+----------------------------------------------------+
| "namespace"             | Namespace prefixs                                  |
+-------------------------+----------------------------------------------------+

Notes: "layer_mapper" and "layer_name_field" are deprecated, a single layer is exposed now by default, use 
the ``SolrLayerConfiguration`` class to configure more layers

Example use:

.. code-block:: java

        Map map = new HashMap();
        map.put("solr_url", new URL("http://localhost:8080/solr"));
        map.put(SolrDataStoreFactory.NAMESPACE.key, "namespace");