Elasticsearch Plugin
--------------------

Elasticsearch is a popular search and analytics engine.

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-elasticsearch</artifactId>
      <version>${geotools.version}</version>
    </dependency>

.. note: Community Experiment
   
   This is an :doc:`unsupported <../../unsupport>` community plugin to work with `Elasticsearch <https://www.elastic.co/elasticsearch/>`__ search and analytics engine.
   
   This is a plugin was added in 2020, gathered from public domain implementations. If you are interested in this functionality contact the author ant help make this plugin part of GeoTools!

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

Available data store connection parameters are summarized in the following table:

.. list-table::
   :widths: 20 80

   * - Parameter
     - Description
   * - elasticsearch_host
     - Host (IP) for connecting to Elasticsearch. HTTP scheme and port can optionally be included to override the defaults. Multiple hosts can be provided. Examples::

         localhost
         localhost:9200
         http://localhost
         http://localhost:9200
         https://localhost:9200
         https://somehost.somedomain:9200,https://anotherhost.somedomain:9200
   * - elasticsearch_port
     - Default HTTP port for connecting to Elasticsearch. Ignored if the hostname includes the port.
   * - user
     - Elasticsearch user. Must have superuser privilege on index.
   * - passwd
     - Elasticsearch user password
   * - runas_geoserver_user
     - Whether to submit requests on behalf of the authenticated GeoServer user
   * - proxy_user
     - Elasticsearch user for document queries. If not provided then admin user credentials are used for all requests.
   * - proxy_passwd
     - Elasticsearch proxy user password
   * - index_name
     - Index name or alias (wildcards supported)
   * - reject_unauthorized
     - Whether to validate the server certificate during the SSL handshake for https connections
   * - default_max_features
     - Default used when maxFeatures is unlimited
   * - response_buffer_limit
     - Maximum number of bytes to buffer in memory when reading responses from Elasticsearch
   * - source_filtering_enabled
     - Whether to enable filtering of the _source field
   * - scroll_enabled
     - Enable the Elasticsearch scan and scroll API
   * - scroll_size
     - Number of documents per shard when using the scroll API
   * - scroll_time
     - Search context timeout when using the scroll API
   * - array_encoding
     - Array encoding strategy. Allowed values are ``JSON`` (keep arrays) and ``CSV`` (keep first array element).
   * - grid_size 
     - Hint for Geohash grid size (numRows*numCols)
   * - grid_threshold
     - Geohash grid aggregation precision will be the minimum necessary so that actual_grid_size/grid_size > grid_threshold

Example use:

.. code-block:: java

        Map map = new HashMap();
        map.put(ElasticDataStoreFactory.HOSTNAME.key, "http://localhost:9200");
        map.put(ElasticDataStoreFactory.INDEX_NAME.key, "status_s");
