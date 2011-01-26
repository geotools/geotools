package org.geotools.data.ingres;

import java.io.IOException;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.SQLDialect;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;

public class IngresFilterToSQL extends PreparedFilterToSQL {

	boolean looseBBOXEnabled;
	
    public IngresFilterToSQL(IngresDialect dialect) {

    }

    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        // evaluate the literal and store it for later
        Geometry geom  = (Geometry) evaluateLiteral(expression, Geometry.class);
        
        if ( geom instanceof LinearRing ) {
            //ingres does not handle linear rings, convert to just a line string
            geom = geom.getFactory().createLineString(((LinearRing) geom).getCoordinateSequence());
        }
        
        out.write("ST_GeomFromText('");
        out.write(geom.toText());
        if(currentSRID == null && currentGeometry  != null) {
            // if we don't know at all, use the srid of the geometry we're comparing against
            // (much slower since that has to be extracted record by record as opposed to 
            // being a constant)
            out.write("', ST_SRID(\"" + currentGeometry.getLocalName() + "\"))");
        } else {
            out.write("', " + currentSRID + ")");
        }
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);

        // adding the spatial filters support
//        caps.addType(BBOX.class);
//        caps.addType(Contains.class);
//      caps.addType(Within.class);
        caps.addType(Crosses.class);
        caps.addType(Disjoint.class);
        caps.addType(Equals.class);
        caps.addType(Intersects.class);
        caps.addType(Overlaps.class);
        caps.addType(Touches.class);
        caps.addType(Beyond.class);

        return caps;
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {
        try {
            if (filter instanceof DistanceBufferOperator) {
                visitDistanceSpatialOperator((DistanceBufferOperator) filter,
                        property, geometry, swapped, extraData);
            } else {
                visitComparisonSpatialOperator(filter, property, geometry,
                        swapped, extraData);
            }
        } catch (IOException e) {
            throw new RuntimeException(IO_ERROR, e);
        }
        return extraData;
    }
        
    void visitDistanceSpatialOperator(DistanceBufferOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) throws IOException {
        if ((filter instanceof DWithin && !swapped)
                || (filter instanceof Beyond && swapped)) {
            out.write("ST_DWithin(");
            property.accept(this, extraData);
            out.write(",");
            geometry.accept(this, extraData);
            out.write(",");
            out.write(Double.toString(filter.getDistance()));
            out.write(")");
        }
        if ((filter instanceof DWithin && swapped)
                || (filter instanceof Beyond && !swapped)) {
            out.write("ST_Distance(");
            property.accept(this, extraData);
            out.write(",");
            geometry.accept(this, extraData);
            out.write(") > ");
            out.write(Double.toString(filter.getDistance()));
        }
    }

    void visitComparisonSpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped, Object extraData)
            throws IOException {
        
        String closingParenthesis = ")";
//        if(!(filter instanceof Disjoint)) {
//            out.write("ST_Disjoint ");
//        }
        if (filter instanceof Equals) {
            out.write("ST_Equals");
        } else if (filter instanceof Disjoint) {
            out.write("NOT (ST_Intersects");
            closingParenthesis += ")";
        } else if (filter instanceof Intersects || filter instanceof BBOX) {
            out.write("ST_Intersects");
        } else if (filter instanceof Crosses) {
            out.write("ST_Crosses");
        } else if (filter instanceof Within) {
            if(swapped)
                out.write("ST_Within");
            else
                out.write("ST_Contains");
        } else if (filter instanceof Contains) {
            if(swapped)
                out.write("ST_Contains");
            else
                out.write("ST_Within");
        } else if (filter instanceof Overlaps) {
            out.write("ST_Overlaps");
        } else if (filter instanceof Touches) {
            out.write("ST_Touches");
        } else {
            throw new RuntimeException("Unsupported filter type " + filter.getClass());
        }
        out.write("(");

        property.accept(this, extraData);
        out.write(", GeomFromWKB(");
        geometry.accept(this, extraData);
        out.write(")");
        out.write(closingParenthesis);
        out.write(" = 1");
    }

}
