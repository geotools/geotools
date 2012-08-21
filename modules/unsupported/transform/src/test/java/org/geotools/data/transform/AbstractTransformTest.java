package org.geotools.data.transform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.transform.Definition;
import org.geotools.data.transform.TransformFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.BeforeClass;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class AbstractTransformTest {

    static SimpleFeatureSource STATES;

    static ReferencedEnvelope DELAWARE_BOUNDS;

    static CoordinateReferenceSystem WGS84;
    
    static FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    @BeforeClass
    public static void setup() throws Exception {
        // just to make sure the loggin is not going to cause exceptions when turned on
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setLevel(java.util.logging.Level.FINE);
        Logging.getLogger("org.geotools.data.transform").setLevel(java.util.logging.Level.FINE);

        WGS84 = CRS.decode("EPSG:4326");
        DELAWARE_BOUNDS = new ReferencedEnvelope(-75.791435, -75.045998, 38.44949,
                39.826435000000004, WGS84);

        PropertyDataStore pds = new PropertyDataStore(new File(
                "./src/test/resources/org/geotools/data/transform"));
        STATES = pds.getFeatureSource("states");
    }

    SimpleFeatureSource transformWithSelection() throws IOException {
        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("the_geom"));
        definitions.add(new Definition("state_name"));
        definitions.add(new Definition("persons"));

        SimpleFeatureSource transformed = TransformFactory.transform(STATES, "states_mini", definitions);
        return transformed;
    }

    SimpleFeatureSource transformWithRename() throws Exception {
        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("geom", ECQL.toExpression("the_geom")));
        definitions.add(new Definition("name", ECQL.toExpression("state_name")));
        definitions.add(new Definition("people", ECQL.toExpression("persons")));

        SimpleFeatureSource transformed = TransformFactory.transform(STATES, "usa", definitions);
        return transformed;
    }

    SimpleFeatureSource transformWithExpressions() throws Exception {
        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(the_geom, 1)")));
        definitions.add(new Definition("name", ECQL.toExpression("strToLowercase(state_name)")));
        definitions.add(new Definition("total", ECQL.toExpression("male + female")));
        definitions.add(new Definition("logp", ECQL.toExpression("log(persons)")));

        SimpleFeatureSource transformed = TransformFactory.transform(STATES, "bstates", definitions);
        return transformed;
    }

}
