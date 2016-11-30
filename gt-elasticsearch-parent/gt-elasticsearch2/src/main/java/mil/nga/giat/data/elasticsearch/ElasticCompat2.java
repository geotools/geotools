/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import com.spatial4j.core.io.GeohashUtils;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.geotools.util.logging.Logging;
import org.joda.time.format.DateTimeFormatter;

public class ElasticCompat2 implements ElasticCompat {

    private final static Logger LOGGER = Logging.getLogger(ElasticCompat2.class);

    @Override
    public FilterToElastic newFilterToElastic() {
        return new FilterToElastic2();
    }

    @Override
    public Settings createSettings(Object... params) {
        return Settings.builder().put(params).build();
    }

    @Override
    public String encodeGeohash(double lon, double lat, int level) {
        return GeohashUtils.encodeLatLon(lat, lon, level);
    }

    @Override
    public GeoPoint decodeGeohash(String geohash) {
        return GeoPoint.fromGeohash(geohash);
    }

    @Override
    public ElasticClient createClient(String host, int port, String clusterName) throws IOException {
        final TransportAddress address;
        address = new InetSocketTransportAddress(InetAddress.getByName(host), port);
        final Settings settings = createSettings("cluster.name", clusterName);
        final Client client = TransportClient.builder().settings(settings).build().addTransportAddresses(address);
        LOGGER.info("Created Elasticsearch Transport client");
        return new TransportElasticClient(client);
    }

    @Override
    public Date parseDateTime(String datestring, String format) {
        final DateTimeFormatter dateFormatter = Joda.forPattern(format).parser();
        return dateFormatter.parseDateTime((String) datestring).toDate();
    }

    @Override
    public boolean isAnalyzed(Map<String, Object> map) {
        final String index = (String) map.get("index");
        return index == null || index.equals("analyzed");
    }

    @Override
    public void addField(SearchRequestBuilder builder, String name) {
        builder.addField(name);
    }

}
