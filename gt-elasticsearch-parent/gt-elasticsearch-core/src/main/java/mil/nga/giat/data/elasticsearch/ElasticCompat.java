package mil.nga.giat.data.elasticsearch;

import java.util.Date;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;

public interface ElasticCompat {

    public FilterToElastic newFilterToElastic();
    
    public Settings createSettings(Object... params);
    
    public String encodeGeohash(double lon, double lat, int level);
    
    public GeoPoint decodeGeohash(String geohash);
    
    public Client createClient(Settings settings, TransportAddress... addresses);
    
    public Date parseDateTime(String datestring, String format);

}
