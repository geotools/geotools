package org.geotools.demo.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class refers to the the wiki <a
 * href="http://docs.codehaus.org/display/GEOTDOC/Filter+Examples"
 * >FilterExamples</a>.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class FilterExamples {
    SimpleFeatureSource featureSource;

    /**
     * How to find Features using IDs?
     * 
     * Each Feature has a FeatureID; you can use these FeatureIDs to request the
     * feature again later.
     * 
     * If you have a Set<String> of feature IDs, which you would like to query
     * from a shapefile:
     * 
     * @param selection
     *            Set of FeatureIDs identifying requested content
     * @return Selected Features
     * @throws IOException
     */
    SimpleFeatureCollection grabSelectedIds(
            Set<String> selection) throws IOException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        Set<FeatureId> fids = new HashSet<FeatureId>();
        for (String id : selection) {
            FeatureId fid = ff.featureId(id);
            fids.add(fid);
        }
        Filter filter = ff.id(fids);
        return featureSource.getFeatures(filter);
    }

    /**
     * How to find a Feature by Name?
     * 
     * CQL is very good for one off queries like this:
     * 
     * @param name
     * @return
     * @throws CQLException 
     */
    FeatureCollection grabSelectedName(String name) throws Exception {
        return featureSource.getFeatures(CQL.toFilter("Name = '" + name + "'"));
    }
    
    /** To select this feature while ignoring case we are going to have to use the FilterFactory (rather than CQL):
     */
    FeatureCollection grabSelectedNameIgnoreCase( String name ) throws Exception {
       FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( null );

       Filter filter = ff.equal( ff.property( "Name"), ff.literal( name ), false );
       return featureSource.getFeatures( filter );
    }
    /**
     * How to find Features using a Set of Names?
     * 
     * If you have a Set<String> of "names" which you would like to query from PostGIS. In this case we are doing a check for an attribute called "Name".
     */
    FeatureCollection grabSelectedNames( Set<String> selectedNames ) throws Exception {
       FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( null );
    
       List<Filter> match = new ArrayList<Filter>();
       for( String name : selectedNames ){
          Filter aMatch = ff.equals( ff.property( "Name"), ff.literal( name ) );
          match.add( aMatch );
       }
       Filter filter = ff.or( match );
       return featureSource.getFeatures( filter );
    }
    
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
FeatureCollection grabFeaturesInBoundingBox( double x1, double y1, double x2, double y2) throws Exception {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( null );
    FeatureType schema = featureSource.getSchema();
    
    // usually "THE_GEOM" for shapefiles
    String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
    CoordinateReferenceSystem crs = schema.getGeometryDescriptor().getCoordinateReferenceSystem();
    
    ReferencedEnvelope bbox = new ReferencedEnvelope( x1,y1, x2, y2, crs );
    
    Filter filter = ff.bbox( ff.property( geometryPropertyName ), bbox );
    return featureSource.getFeatures( filter );
}

}
