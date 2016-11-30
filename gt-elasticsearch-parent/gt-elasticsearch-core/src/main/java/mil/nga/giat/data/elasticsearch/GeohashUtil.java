/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.util.Map;

import com.github.davidmoten.geo.GeoHash;
import com.vividsolutions.jts.geom.Envelope;

public class GeohashUtil {

    public static int computePrecision(Envelope envelope, long size, double threshold) {
        return computePrecision(envelope, size, threshold, 1);
    }

    private static int computePrecision(Envelope envelope, long size, double threshold, int n) {
        return computeSize(envelope, n)/size > threshold ? n : computePrecision(envelope, size, threshold, n+1);
    }

    private static double computeSize(Envelope envelope, int n) {
        final double area = Math.min(360*180, envelope.getArea());
        return area/(GeoHash.widthDegrees(n)*GeoHash.heightDegrees(n));
    }

    public static void updateGridAggregationPrecision(Map<String,Map<String,Map<String,Object>>> aggregations, int precision) {
        aggregations.values().stream().filter(a->a.containsKey("geohash_grid")).forEach(a -> {
            a.get("geohash_grid").put("precision", precision);
        });
    }

}
