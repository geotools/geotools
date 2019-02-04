/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import java.util.Map;

public class BasicGeoHashGrid extends GeoHashGrid {

    @Override
    public Number computeCellValue(Map<String,Object> bucket) {
        return super.pluckDocCount(bucket);
    }

}
