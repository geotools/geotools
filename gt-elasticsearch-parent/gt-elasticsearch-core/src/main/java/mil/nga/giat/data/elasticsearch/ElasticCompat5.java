/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.geotools.util.logging.Logging;
import org.joda.time.format.DateTimeFormatter;

public class ElasticCompat5 implements ElasticCompat {

    private final static Logger LOGGER = Logging.getLogger(ElasticCompat5.class);

    @Override
    public FilterToElastic newFilterToElastic() {
        return new FilterToElastic5();
    }

    @Override
    public Settings createSettings(Object... params) {
        return Settings.builder().put(params).build();
    }

    @Override
    public String encodeGeohash(double lon, double lat, int level) {
        return GeoHashUtils.stringEncode(lon, lat, level);
    }

    @Override
    public GeoPoint decodeGeohash(String geohash) {
        return GeoPoint.fromGeohash(geohash);
    }

    @Override
    public ElasticClient createClient(String host, int port, String clusterName) throws IOException {
        ElasticClient elasticClient = null;
        try {
            final RestClient client = RestClient.builder(new HttpHost(host, port, "http")).build();
            final Response response = client.performRequest("GET", "/", Collections.<String, String>emptyMap());
            if (response.getStatusLine().getStatusCode() >= 400) {
                throw new IOException();
            }
            elasticClient = new RestElasticClient(client);
            LOGGER.info("Created REST client");
        } catch (Exception e) {
            try {
                final TransportAddress address;
                address = new InetSocketTransportAddress(InetAddress.getByName(host), port);
                final Settings settings = createSettings("cluster.name", clusterName);
                final TransportClient client = new PreBuiltTransportClient(settings);
                client.addTransportAddresses(address);
                elasticClient = new TransportElasticClient(client);
                LOGGER.info("Created Transport client");
            } catch (Exception e2) {
                throw new IOException("Unable to create a REST or Transport client", e2);
            }
        }
        return elasticClient;
    }

    @Override
    public Date parseDateTime(String datestring, String format) {
        final DateTimeFormatter dateFormatter = Joda.forPattern(format).parser();
        return dateFormatter.parseDateTime((String) datestring).toDate();
    }

    @Override
    public boolean isAnalyzed(Map<String, Object> map) {
        boolean analyzed = false;
        Object value = map.get("type");
        if (value != null && value instanceof String && ((String) value).equals("text")) {
            analyzed = true;
        }
        return analyzed;
    }

    @Override
    public void addField(SearchRequestBuilder builder, String name) {
        builder.addStoredField(name);
    }

}
