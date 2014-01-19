import java.io.File;
import java.io.IOException;

import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.visitor.CountVisitor;


public class ReadShapefile {

    public static void main(String[] args) throws IOException {
        ShapefileDataStore store = new ShapefileDataStore(DataUtilities.fileToURL(new File("/tmp/IN_ForcedFeederCables.shp")));
        ContentFeatureSource fs = store.getFeatureSource();
        System.out.println(fs.getCount(Query.ALL));
        CountVisitor cv = new CountVisitor();
        fs.getFeatures().accepts(cv, null);
        System.out.println(cv.getCount());
    }
}
