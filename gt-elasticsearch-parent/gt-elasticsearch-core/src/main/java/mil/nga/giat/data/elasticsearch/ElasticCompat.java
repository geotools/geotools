/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.settings.Settings;

public interface ElasticCompat {

    public FilterToElastic newFilterToElastic();

    public Settings createSettings(Object... params);

    public String encodeGeohash(double lon, double lat, int level);

    public GeoPoint decodeGeohash(String geohash);

    public ElasticClient createClient(String host, int port, String clusterName) throws IOException;

    public Date parseDateTime(String datestring, String format);

    public boolean isAnalyzed(Map<String,Object> map);

    public void addField(SearchRequestBuilder builder, String name);

}
