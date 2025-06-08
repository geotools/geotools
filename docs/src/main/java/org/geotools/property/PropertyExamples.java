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
package org.geotools.property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

public class PropertyExamples {

    static File directory;

    public static void main(String[] args) {
        File tmp = null;
        try {
            tmp = File.createTempFile("example", "");
            boolean exists = tmp.exists();
            if (exists) {
                System.err.println("Removing tempfile " + tmp);
                tmp.delete();
            }
            boolean created = tmp.mkdirs();
            if (!created) {
                System.err.println("Could not create " + tmp);
                System.exit(1);
            }
            File example = new File(tmp, "example.properties");

            BufferedWriter writer = new BufferedWriter(new FileWriter(example, StandardCharsets.UTF_8));
            writer.write("_=id:Integer,name:String,geom:Point");
            writer.newLine();
            writer.write("fid1=1|jody garnett|POINT(0 0)");
            writer.newLine();
            writer.write("fid2=2|brent|POINT(10 10)");
            writer.newLine();
            writer.write("fid3=3|dave|POINT(20 20)");
            writer.newLine();
            writer.write("fid4=4|justin deolivera|POINT(30 30)");
            writer.newLine();
            writer.close();

            directory = tmp;
            try {
                example3();
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tmp != null) {
                File[] list = tmp.listFiles();
                if (list != null) {
                    for (File file : list) {
                        file.delete();
                    }
                }
                tmp.delete();
            }
        }
    }

    private static void example3() throws IOException {
        System.out.println("example3 start\n");
        // example3 start
        Map<String, Serializable> params = new HashMap<>();
        params.put("directory", directory);
        DataStore datastore = DataStoreFinder.getDataStore(params);

        Query query = new Query("example");
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                datastore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                System.out.println("feature " + count + ": " + feature.getID());
                count++;
            }
            System.out.println("read in " + count + " features");
        }
        // example3 end
        System.out.println("\nexample3 end\n");
    }
}
