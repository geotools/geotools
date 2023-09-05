/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.api;

import java.net.URI;
import java.util.Set;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.DataUtilities;
import org.geotools.feature.NameImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.util.SuppressFBWarnings;

@SuppressFBWarnings("UWF_NULL_FIELD")
public class DataStoreExamples {

    @SuppressFBWarnings("NP_UNWRITTEN_FIELD")
    DataStore dataStore = null;

    @SuppressFBWarnings("NP_UNWRITTEN_FIELD")
    SimpleFeatureSource featureSource = null;

    void exampleInfo() {
        // exampleInfo start
        ServiceInfo info = dataStore.getInfo();

        // Human readable name and description
        String title = info.getTitle();
        String text = info.getDescription();

        // keywords (dublin core keywords like a web page)
        Set<String> keywords = info.getKeywords();

        // formal metadata
        URI publisher = info.getPublisher(); // authority publishing data
        URI schema = info.getSchema(); // used for data conforming to a standard
        URI source = info.getSource(); // location where information is published from

        // exampleInfo end
    }

    void exampleCreateSchema() throws Exception {
        // exampleCreateSchema start
        SimpleFeatureType schema =
                DataUtilities.createType("LINE", "centerline:LineString,name:\"\",id:0");

        dataStore.createSchema(schema);
        // exampleCreateSchema end

    }

    void exampleRemoveSchema() throws Exception {
        // exampleRemoveSchema start
        Name schemaName = new NameImpl("myTable");
        dataStore.removeSchema(schemaName);
        // exampleRemoveSchema end

    }

    void exampleAllCount() throws Exception {
        // all start
        int count = featureSource.getCount(Query.ALL);
        if (count == -1) {
            count = featureSource.getFeatures().size();
        }
        // all end
    }

    void exampleQueryCount() throws Exception {
        // count start
        Query query = new Query("typeName", CQL.toFilter("REGION = 3"));
        int count = featureSource.getCount(query);
        if (count == -1) {
            count = featureSource.getFeatures(query).size();
        }
        // count end
    }
}
