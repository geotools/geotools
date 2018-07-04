package org.geotools.filter.function;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeatureType;

/** Fixture capturing a lot of cut-and-paste data that has collected in our function testing */
public class FunctionTestFixture {

    static SimpleFeatureCollection polygons() throws SchemaException, ParseException {
        // Create SimpleFeatures
        SimpleFeatureType type = DataUtilities.createType("polygons", "id:int,geom:Polygon");
        ListFeatureCollection featureCollection = new ListFeatureCollection(type);
        String[] polygons = {
            "POLYGON ((1235702.2034807256 707935.1879023351, 1229587.156498981 671715.2942412316, 1242287.6386918353 688649.2704983709, 1245109.9680680253 677359.9529936113, 1247932.297444215 711227.9055078899, 1239935.6975450104 705583.2467555101, 1235702.2034807256 707935.1879023351))",
            "POLYGON ((1237113.3681688206 622324.5301579087, 1224883.274205331 586575.0247261701, 1258280.8384902447 589397.3541023601, 1237113.3681688206 622324.5301579087))",
            "POLYGON ((1131746.4047910655 718754.1171777296, 1115282.8167632914 681593.4470578962, 1139272.6164609052 679241.5059110713, 1147269.2163601099 707935.1879023351, 1131746.4047910655 718754.1171777296)))"
        };
        WKTReader reader = new WKTReader();
        for (int i = 0; i < polygons.length; i++) {
            Geometry polygon = reader.read(polygons[i]);
            featureCollection.add(
                    SimpleFeatureBuilder.build(type, new Object[] {i, polygon}, String.valueOf(i)));
        }
        return featureCollection;
    }
}
