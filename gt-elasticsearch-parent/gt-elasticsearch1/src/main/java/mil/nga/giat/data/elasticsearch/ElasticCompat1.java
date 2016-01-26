package mil.nga.giat.data.elasticsearch;

import java.util.Date;

import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;

public class ElasticCompat1 implements ElasticCompat {

    @Override
    public FilterToElastic newFilterToElastic() {
        return new FilterToElastic1();
    }

    @Override
    public Settings createSettings(Object... params) {
        return ImmutableSettings.builder().put(params).build();
    }

    @Override
    public String encodeGeohash(double lon, double lat, int level) {
        return GeoHashUtils.encode(lon, lat, level);
    }

    @Override
    public GeoPoint decodeGeohash(String geohash) {
        GeoPoint geoPoint = null;
        try {
            geoPoint = GeoHashUtils.decode(geohash);
        } catch (ElasticsearchIllegalArgumentException e) {
            // not a geohash
        }
        return geoPoint;
    }

    @Override
    public Client createClient(Settings settings, TransportAddress... addresses) {
        return (new TransportClient(settings)).addTransportAddresses(addresses);
    }
    
    @Override
    public Date parseDateTime(String datestring, String format) {
        final DateTimeFormatter dateFormatter = Joda.forPattern(format).parser();
        return dateFormatter.parseDateTime((String) datestring).toDate();
    }

}
