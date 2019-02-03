/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ElasticClient extends Closeable {

    static final String RUN_AS = "es-security-runas-user";

    public double getVersion();

    public List<String> getTypes(String indexName) throws IOException;

    public Map<String,Object> getMapping(String indexName, String type) throws IOException;

    public ElasticResponse search(String searchIndices, String type, ElasticRequest request) throws IOException;

    public ElasticResponse scroll(String scrollId, Integer scrollTime) throws IOException;

    @Override
    public void close() throws IOException;

    public void clearScroll(Set<String> scrollIds) throws IOException;

}
