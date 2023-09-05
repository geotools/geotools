package org.geotools.data.transform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;
import org.junit.BeforeClass;

public abstract class AbstractTransformTest {

    static SimpleFeatureSource STATES;

    static SimpleFeatureSource STATES2;

    static ReferencedEnvelope DELAWARE_BOUNDS;

    static CoordinateReferenceSystem WGS84;

    static FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @BeforeClass
    public static void setup() throws Exception {
        // just to make sure the loggin is not going to cause exceptions when turned on
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setLevel(java.util.logging.Level.FINE);
        Logging.getLogger(AbstractTransformTest.class).setLevel(java.util.logging.Level.FINE);

        WGS84 = CRS.decode("EPSG:4326");
        DELAWARE_BOUNDS =
                new ReferencedEnvelope(-75.791435, -75.045998, 38.44949, 39.826435000000004, WGS84);

        PropertyDataStore pds =
                new PropertyDataStore(new File("./src/test/resources/org/geotools/data/transform"));
        STATES = pds.getFeatureSource("states");
        STATES2 = pds.getFeatureSource("states");
    }

    SimpleFeatureSource transformWithSelection() throws IOException {
        return transformWithSelection(STATES);
    }

    SimpleFeatureSource transformWithSelection(SimpleFeatureSource states) throws IOException {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("the_geom"));
        definitions.add(new Definition("state_name"));
        definitions.add(new Definition("persons"));

        SimpleFeatureSource transformed =
                TransformFactory.transform(states, "states_mini", definitions);
        return transformed;
    }

    SimpleFeatureSource transformWithSelectionAndDescription(SimpleFeatureSource states)
            throws IOException {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("the_geom", new SimpleInternationalString("the geometry")));
        definitions.add(
                new Definition("state_name", new SimpleInternationalString("the state name")));
        definitions.add(
                new Definition("persons", new SimpleInternationalString("the number of persons")));

        SimpleFeatureSource transformed =
                TransformFactory.transform(states, "states_described", definitions);
        return transformed;
    }

    SimpleFeatureSource transformWithSelectionAndDescription() throws IOException {
        return transformWithSelectionAndDescription(STATES);
    }

    SimpleFeatureSource transformWithRename() throws Exception {
        return transformWithRename(STATES);
    }

    SimpleFeatureSource transformWithRename(SimpleFeatureSource states)
            throws CQLException, IOException {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("geom", ECQL.toExpression("the_geom")));
        definitions.add(new Definition("name", ECQL.toExpression("state_name")));
        definitions.add(new Definition("people", ECQL.toExpression("persons")));

        SimpleFeatureSource transformed = TransformFactory.transform(states, "usa", definitions);
        return transformed;
    }

    SimpleFeatureSource transformWithExpressions() throws Exception {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(the_geom, 1)")));
        definitions.add(new Definition("name", ECQL.toExpression("strToLowercase(state_name)")));
        definitions.add(new Definition("total", ECQL.toExpression("male + female")));
        definitions.add(new Definition("logp", ECQL.toExpression("log(persons)")));

        SimpleFeatureSource transformed =
                TransformFactory.transform(STATES, "bstates", definitions);
        return transformed;
    }

    SimpleFeatureSource transformWithExpressionsWithEmptySource() throws Exception {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(the_geom, 1)")));
        definitions.add(new Definition("name", ECQL.toExpression("strToLowercase(state_name)")));
        definitions.add(new Definition("total", ECQL.toExpression("male + female")));
        definitions.add(new Definition("logp", ECQL.toExpression("log(persons)")));

        ListFeatureCollection fc = new ListFeatureCollection(STATES.getSchema());
        SimpleFeatureSource emptySource = DataUtilities.source(fc);

        SimpleFeatureSource transformed =
                TransformFactory.transform(emptySource, "bstates", definitions);
        return transformed;
    }
}
