package org.geotools.main;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.measure.unit.Unit;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.FunctionFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.event.MapMouseEvent;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This class gathers up the filter examples shown in the sphinx documentation for Filters.
 * 
 * @author Jody Garnett
 */
public class FilterExamples {
SimpleFeatureSource featureSource;

/**
 * How to find Features using IDs?
 * 
 * Each Feature has a FeatureID; you can use these FeatureIDs to request the feature again later.
 * 
 * If you have a Set<String> of feature IDs, which you would like to query from a shapefile:
 * 
 * @param selection
 *            Set of FeatureIDs identifying requested content
 * @return Selected Features
 * @throws IOException
 */
// grabSelectedIds start
SimpleFeatureCollection grabSelectedIds(Set<String> selection) throws IOException {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
    Set<FeatureId> fids = new HashSet<FeatureId>();
    for (String id : selection) {
        FeatureId fid = ff.featureId(id);
        fids.add(fid);
    }
    Filter filter = ff.id(fids);
    return featureSource.getFeatures(filter);
}

// grabSelectedIds end

/**
 * How to find a Feature by Name?
 * 
 * CQL is very good for one off queries like this:
 * 
 * @param name
 * @return
 * @throws CQLException
 */
// grabSelectedName start
FeatureCollection grabSelectedName(String name) throws Exception {
    return featureSource.getFeatures(CQL.toFilter("Name = '" + name + "'"));
}

// grabSelectedName end

/**
 * To select this feature while ignoring case we are going to have to use the FilterFactory (rather
 * than CQL):
 */
// grabSelectedNameIgnoreCase start
SimpleFeatureCollection grabSelectedNameIgnoreCase(String name) throws Exception {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
    Filter filter = ff.equal(ff.property("Name"), ff.literal(name), false);
    return featureSource.getFeatures(filter);
}

// grabSelectedNameIgnoreCase end

/**
 * How to find Features using a Set of Names?
 * 
 * If you have a Set<String> of "names" which you would like to query from PostGIS. In this case we
 * are doing a check for an attribute called "Name".
 */
// grabSelectedNames start
SimpleFeatureCollection grabSelectedNames(Set<String> selectedNames) throws Exception {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
    List<Filter> match = new ArrayList<Filter>();
    for (String name : selectedNames) {
        Filter aMatch = ff.equals(ff.property("Name"), ff.literal(name));
        match.add(aMatch);
    }
    Filter filter = ff.or(match);
    return featureSource.getFeatures(filter);
}

// grabSelectedNames end
/**
 * What features on in this bounding Box?
 * 
 * You can make a bounding box query as shown below:
 * 
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 * @return
 * @throws Exception
 */
// grabFeaturesInBoundingBox start
SimpleFeatureCollection grabFeaturesInBoundingBox(double x1, double y1, double x2, double y2)
        throws Exception {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    FeatureType schema = featureSource.getSchema();
    
    // usually "THE_GEOM" for shapefiles
    String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
    CoordinateReferenceSystem targetCRS = schema.getGeometryDescriptor()
            .getCoordinateReferenceSystem();
    
    ReferencedEnvelope bbox = new ReferencedEnvelope(x1, y1, x2, y2, targetCRS);
    
    Filter filter = ff.bbox(ff.property(geometryPropertyName), bbox);
    return featureSource.getFeatures(filter);
}

// grabFeaturesInBoundingBox end

// grabFeaturesInPolygon start
SimpleFeatureCollection grabFeaturesInPolygon(double x1, double y1, double x2, double y2)
        throws Exception {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    FeatureType schema = featureSource.getSchema();
    CoordinateReferenceSystem worldCRS = DefaultGeographicCRS.WGS84;
    
    // usually "THE_GEOM" for shapefiles
    String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
    CoordinateReferenceSystem targetCRS = schema.getGeometryDescriptor()
            .getCoordinateReferenceSystem();
    
    ReferencedEnvelope click = new ReferencedEnvelope(x1, y1, x2, y2, worldCRS);
    
    // will result in a slight larger BBOX then the original click
    ReferencedEnvelope bbox = click.transform(targetCRS, true);
    
    // will result in a polygon matching the original click
    Polygon clickPolygon = JTS.toGeometry(bbox, null, 10);
    MathTransform transform = CRS.findMathTransform(worldCRS, targetCRS);
    Polygon polygon = (Polygon) JTS.transform(clickPolygon, transform);
    
    Filter filter = ff.intersects(ff.property(geometryPropertyName), ff.literal(polygon));
    
    return featureSource.getFeatures(filter);
}

// grabFeaturesInPolygon end

// grabFeaturesOnScreen start
SimpleFeatureCollection grabFeaturesOnScreen(ReferencedEnvelope screen) throws Exception {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    FeatureType schema = featureSource.getSchema();
    
    // usually "THE_GEOM" for shapefiles
    String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
    CoordinateReferenceSystem targetCRS = schema.getGeometryDescriptor()
            .getCoordinateReferenceSystem();
    CoordinateReferenceSystem worldCRS = screen.getCoordinateReferenceSystem();
    
    // will result in a slight larger BBOX then the original click
    ReferencedEnvelope bbox = screen.transform(targetCRS, true);
    
    // will result in a polygon matching the original click
    Polygon clickPolygon = JTS.toGeometry(bbox, null, 10);
    MathTransform transform = CRS.findMathTransform(worldCRS, targetCRS);
    Polygon polygon = (Polygon) JTS.transform(clickPolygon, transform);
    
    Filter filter1 = ff.bbox(ff.property(geometryPropertyName), bbox);
    Filter filter2 = ff.intersects(ff.property(geometryPropertyName), ff.literal(polygon));
    
    Filter filter = ff.and(filter1, filter2);
    
    return featureSource.getFeatures(filter);
}

// grabFeaturesOnScreen end

private JMapFrame mapFrame;

// click1 start
SimpleFeatureCollection click1(MapMouseEvent ev) throws Exception {
    // Construct a 3x3 pixel rectangle centred on the mouse click position
    java.awt.Point screenPos = ev.getPoint();
    
    Rectangle screenRect = new Rectangle(screenPos.x - 1, screenPos.y - 1, 3, 3);
    CoordinateReferenceSystem worldCRS = mapFrame.getMapContext().getCoordinateReferenceSystem();
    // Transform the screen rectangle into bounding box in the coordinate reference system of our
    // map context.
    AffineTransform screenToWorld = mapFrame.getMapPane().getScreenToWorldTransform();
    Rectangle2D worldRect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
    ReferencedEnvelope worldBBox = new ReferencedEnvelope(worldRect, worldCRS);
    
    // transform from world to target CRS
    SimpleFeatureType schema = featureSource.getSchema();
    CoordinateReferenceSystem targetCRS = schema.getCoordinateReferenceSystem();
    String geometryAttributeName = schema.getGeometryDescriptor().getLocalName();
    
    ReferencedEnvelope bbox = worldBBox.transform(targetCRS, true, 10);
    
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
    // Option 1 BBOX
    Filter filter = ff.bbox(ff.property(geometryAttributeName), bbox);
    
    // Option 2 Intersects
    // Filter filter = ff.intersects(ff.property(geometryAttributeName), ff.literal(bbox));
    
    return featureSource.getFeatures(filter);
}

// click1 end

// distance start
SimpleFeatureCollection distance(MapMouseEvent ev) throws Exception {
    DirectPosition2D worldPosition = ev.getMapPosition();
    
    // get the unit of measurement
    SimpleFeatureType schema = featureSource.getSchema();
    CoordinateReferenceSystem crs = schema.getGeometryDescriptor().getCoordinateReferenceSystem();
    Unit<?> uom = crs.getCoordinateSystem().getAxis(0).getUnit();
    
    MathTransform transform = CRS.findMathTransform(worldPosition.getCoordinateReferenceSystem(),
            crs, true);
    
    DirectPosition dataPosition = transform.transform(worldPosition, null);
    
    Point point = JTS.toGeometry(dataPosition);
    
    // threshold distance
    double distance = 10.0d;
    
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter = ff.dwithin(ff.property("POLYGON"), ff.literal(point), distance, uom.toString());
    
    return featureSource.getFeatures(filter);
}

// distance end

// polygonInteraction start
private void polygonInteraction() {
    SimpleFeatureCollection polygonCollection = null;
    SimpleFeatureCollection fcResult = null;
    final SimpleFeatureCollection found = FeatureCollections.newCollection();
    
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    SimpleFeature feature = null;
    
    Filter polyCheck = null;
    Filter andFil = null;
    Filter boundsCheck = null;
    
    String qryStr = null;
    
    SimpleFeatureIterator it = polygonCollection.features();
    try {
        while (it.hasNext()) {
            feature = it.next();
            BoundingBox bounds = feature.getBounds();
            boundsCheck = ff.bbox(ff.property("the_geom"), bounds);
            
            Geometry geom = (Geometry) feature.getDefaultGeometry();
            polyCheck = ff.intersects(ff.property("the_geom"), ff.literal(geom));
            
            andFil = ff.and(boundsCheck, polyCheck);
            
            try {
                fcResult = featureSource.getFeatures(andFil);
                // go through results and copy out the found features
                fcResult.accepts(new FeatureVisitor() {
                    public void visit(Feature feature) {
                        found.add((SimpleFeature) feature);
                    }
                }, null);
            } catch (IOException e1) {
                System.out.println("Unable to run filter for " + feature.getID() + ":" + e1);
                continue;
            }
            
        }
    } finally {
        it.close();
    }
}

// polygonInteraction end

private void expressionExamples() {
    Geometry geometry = null;
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    // expressionExamples start
    Expression propertyAccess = ff.property("THE_GEOM");
    Expression literal = ff.literal(geometry);
    Expression math = ff.add(ff.literal(1), ff.literal(2));
    Expression function = ff.function("length", ff.property("CITY_NAME"));
    // expressionExamples end
}

private static void functionList() {
    // functionList start
    Set<FunctionFactory> functionFactories = CommonFactoryFinder.getFunctionFactories(null);
    
    for (FunctionFactory factory : functionFactories) {
        System.out.println( factory.getClass().getName() );
        for (FunctionName functionName : factory.getFunctionNames()) {
            System.out.print("    ");
            System.out.print(functionName.getName());
            System.out.print("(");
            int count = functionName.getArgumentCount();
            if( count < 0 ){
                count = functionName.getArgumentNames().size();
            }
            for (int i = 0; i < count; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                String arg = null;
                if (functionName.getArgumentNames() != null
                        && i < functionName.getArgumentNames().size()) {
                    arg = functionName.getArgumentNames().get(i);
                }
                if (arg == null) {
                    arg = "arg" + (i + 1); // arg1, arg2, etc...
                }
                System.out.print(arg);
            }
            System.out.println(")");
        }
    }
    // functionList end
}

public static void main(String args[]) {
    functionList();
}

}
