package org.geotools.data.shapefile;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import org.geotools.TestData;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.URLs;
import org.junit.Test;

public class DuplicateColumnNameReaderTest extends TestCaseSupport {

    public final String SHPFILE = "dup-column/dup_column.shp";

    // TODO The name "TestColumn1" for the duplicate column "TestColumn" is
    // based on the current implementation of ShapefileDataStore.
    public final String testColumn = "TestColumn1";

    public final Integer expectedValue = Integer.valueOf(20);

    @Test
    public void testAttributeReader() throws IOException {
        URL u = TestData.url(TestCaseSupport.class, SHPFILE);
        File shpFile = URLs.urlToFile(u);

        // open the test shapefile
        ShapefileDataStore store = new ShapefileDataStore(shpFile.toURI().toURL());
        SimpleFeatureSource source = store.getFeatureSource();

        // read the first feature
        try (SimpleFeatureIterator iter = source.getFeatures().features()) {
            SimpleFeature feature = iter.next();

            // get the value of the duplicate column & compare it against expectation
            assertEquals(expectedValue, feature.getAttribute(testColumn));
        }

        // cleanup
        store.dispose();
    }

    @Test
    public void testAttributeReaderIndexed() throws IOException {
        URL u = TestData.url(TestCaseSupport.class, SHPFILE);
        File shpFile = URLs.urlToFile(u);

        // open the test shapefile
        // creates both indexed and regular shapefile data store
        ShapefileDataStore indexedstore = new ShapefileDataStore(shpFile.toURI().toURL());

        // get a random feature id from one of the stores
        try (SimpleFeatureIterator it =
                indexedstore.getFeatureSource().getFeatures().features()) {
            FeatureId fid = it.next().getIdentifier();

            // query the datastore
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            Filter idFilter = ff.id(Collections.singleton(fid));
            final Query query =
                    new Query(indexedstore.getSchema().getName().getLocalPart(), idFilter, new String[] {testColumn});
            final SimpleFeatureCollection indexedfeatures =
                    indexedstore.getFeatureSource().getFeatures(query);

            // compare the results
            try (SimpleFeatureIterator indexIterator = indexedfeatures.features()) {
                SimpleFeature indexedFeature = indexIterator.next();

                // get the value of the duplicate column & compare it against expectation
                assertEquals(expectedValue, indexedFeature.getAttribute(testColumn));
            }
        }

        // cleanup
        indexedstore.dispose();
    }
}
