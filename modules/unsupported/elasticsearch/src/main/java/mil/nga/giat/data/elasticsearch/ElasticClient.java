/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

interface ElasticClient extends Closeable {

    String RUN_AS = "es-security-runas-user";

    double getVersion();

    List<String> getTypes(String indexName) throws IOException;

    Map<String,Object> getMapping(String indexName, String type) throws IOException;

    ElasticResponse search(String searchIndices, String type, ElasticRequest request) throws IOException;

    ElasticResponse scroll(String scrollId, Integer scrollTime) throws IOException;

    @Override
    void close() throws IOException;

    void clearScroll(Set<String> scrollIds) throws IOException;

}
