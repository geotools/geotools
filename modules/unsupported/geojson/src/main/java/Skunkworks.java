import java.io.FileInputStream;

import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;


public class Skunkworks {

    public static void main(String[] args) throws Exception {
        FeatureJSON json = new FeatureJSON();
        FeatureCollection fcol = json.readFeatureCollection(new FileInputStream("/Users/jdeolive/states.json"));

        System.out.println(fcol.getSchema().getCoordinateReferenceSystem());
    }
}
