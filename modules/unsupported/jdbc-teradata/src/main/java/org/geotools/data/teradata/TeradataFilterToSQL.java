/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.teradata;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.DistanceBufferOperator;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

public class TeradataFilterToSQL extends PreparedFilterToSQL {

    static final Logger LOGGER = Logging.getLogger(TeradataFilterToSQL.class);

    boolean looseBBOX;

    public TeradataFilterToSQL(TeradataDialect dialect) {
        super(dialect);
        looseBBOX = dialect.isLooseBBOXEnabled();
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);

        // adding the spatial filters support
        caps.addType(BBOX.class);
        caps.addType(Contains.class);
        caps.addType(Crosses.class);
        caps.addType(Disjoint.class);
        caps.addType(Equals.class);
        caps.addType(Intersects.class);
        caps.addType(Overlaps.class);
        caps.addType(Touches.class);
        caps.addType(Within.class);
        caps.addType(DWithin.class);
        caps.addType(Beyond.class);

        return caps;
    }

    //    protected void visitLiteralGeometry(Literal expression) throws IOException {
    //        // evaluate the literal and store it for later
    //        Geometry geom = (Geometry) evaluateLiteral(expression, Geometry.class);
    //
    //        if (geom instanceof LinearRing) {
    //            // teradata does not handle linear rings, convert to just a line
    //            // string
    //            geom = geom.getFactory().createLineString(((LinearRing)
    // geom).getCoordinateSequence());
    //        }
    //
    //        out.write("'");
    //        out.write(geom.toText());
    //        out.write("'");
    //    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) {
        try {
            if (filter instanceof DistanceBufferOperator) {
                visitDistanceSpatialOperator((DistanceBufferOperator) filter, property, geometry, swapped, extraData);
            } else {
                visitComparisonSpatialOperator(filter, property, geometry, swapped, extraData);
            }
        } catch (IOException e) {
            throw new RuntimeException(IO_ERROR, e);
        }
        return extraData;
    }

    void visitDistanceSpatialOperator(
            DistanceBufferOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData)
            throws IOException {

        if (filter instanceof DWithin && !swapped || filter instanceof Beyond && swapped) {
            encodeIndexPredicate(property, geometry);

            property.accept(this, extraData);
            out.write(".ST_DWithin(new ST_Geometry(");
            geometry.accept(this, extraData);
            out.write("),");
            out.write(Double.toString(filter.getDistance()));
            out.write(")");
        }
        if (filter instanceof DWithin && swapped || filter instanceof Beyond && !swapped) {
            encodeIndexPredicate(property, geometry);

            property.accept(this, extraData);
            out.write(".ST_Distance(SYSSPATIAL.ST_Geometry(");
            geometry.accept(this, extraData);
            out.write(")) > ");
            out.write(Double.toString(filter.getDistance()));
        }
    }

    void visitComparisonSpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData)
            throws IOException {

        if (!(filter instanceof Disjoint)) {
            if (encodeIndexPredicate(property, geometry)) {
                out.write(" AND ");
            }
        }

        property.accept(this, extraData);
        out.write(".");

        if (filter instanceof Equals) {
            out.write("ST_Equals");
        } else if (filter instanceof Disjoint) {
            out.write("ST_Disjoint ");
        } else if (filter instanceof Intersects || filter instanceof BBOX) {
            out.write("ST_Intersects");
        } else if (filter instanceof Crosses) {
            out.write("ST_Crosses");
        } else if (filter instanceof Within) {
            if (swapped) out.write("ST_Contains");
            else out.write("ST_Within");
        } else if (filter instanceof Contains) {
            if (swapped) out.write("ST_Within");
            else out.write("ST_Contains");
        } else if (filter instanceof Overlaps) {
            out.write("ST_Overlaps");
        } else if (filter instanceof Touches) {
            out.write("ST_Touches");
        } else {
            throw new RuntimeException("Unsupported filter type " + filter.getClass());
        }

        int tdVersion = ((TeradataDialect) dialect).getTdVersion();
        if (tdVersion > 12) {
            out.write("(new ST_Geometry(");
            geometry.accept(this, extraData);
            out.write(")) = 1");
        } else {
            out.write("(CAST (");
            geometry.accept(this, extraData);
            out.write(" AS ST_Geometry)) = 1");
        }
    }

    boolean encodeIndexPredicate(PropertyName property, Literal geometry) throws IOException {

        TessellationInfo tinfo =
                (TessellationInfo) currentGeometry.getUserData().get(TessellationInfo.KEY);
        if (tinfo == null) {
            LOGGER.info("Tessellation info not available for "
                    + currentGeometry.getLocalName()
                    + ", unable to perform spatially indexed query");
            return false;
        }

        if (tinfo.getIndexTableName() == null) {
            LOGGER.info("Tessellation info available for "
                    + currentGeometry.getLocalName()
                    + ", but index table does not exist, unable to perform spatially index query.");
            return false;
        }

        if (primaryKey == null || primaryKey.getColumns().isEmpty()) {
            LOGGER.info(
                    "No primary key for " + featureType.getTypeName() + ", unable to perform spatially indexed query");
            return false;
        }

        Geometry g = (Geometry) geometry.getValue();

        // check the geomtry envelope vs the world envelope, if it falls outside of the world bounds
        // we can't use the index
        Envelope oenv = g.getEnvelopeInternal();
        Envelope uenv = tinfo.getUBounds();

        if (!uenv.contains(oenv)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Bounds:"
                        + oenv
                        + " falls outside of univerise bounds: "
                        + uenv
                        + ". Unable to perform index query.");
            }
            return false;
        }

        // check coverage... if the geometry bounds covers most of the the index universe don't use
        // the index since the join query is expensive... it will be more efficient to just do the
        // direct intersection
        // TODO: make this threshhold configurable
        double coverage = uenv.intersection(oenv).getArea() / uenv.getArea();
        if (coverage > 0.5) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Bounds:"
                        + oenv
                        + " covers "
                        + coverage * 100
                        + " of universe bounds: "
                        + uenv
                        + ". Forgoing index query.");
            }
            return false;
        }

        out.write('(');
        for (Iterator<PrimaryKeyColumn> it = primaryKey.getColumns().iterator(); it.hasNext(); ) {
            out.write(it.next().getName());
            if (it.hasNext()) {
                out.write(", ");
            }
        }
        out.write(')');

        out.write(" IN (SELECT DISTINCT ");
        for (Iterator<PrimaryKeyColumn> it = primaryKey.getColumns().iterator(); it.hasNext(); ) {
            out.write("t." + it.next().getName());
            if (it.hasNext()) {
                out.write(", ");
            }
        }

        out.write(" FROM ");
        if (tinfo.getSchemaName() != null) {
            out.write(escapeName(tinfo.getSchemaName()));
            out.write(".");
        }
        out.write(escapeName(tinfo.getIndexTableName()));
        out.write(" t, TABLE (SYSSPATIAL.tessellate_search(1,");

        out.write(String.format(
                "%f, %f, %f, %f, %f, %f, %f, %f, %d, %d, %d, %f, %d)) AS i ",
                oenv.getMinX(),
                oenv.getMinY(),
                oenv.getMaxX(),
                oenv.getMaxY(),
                uenv.getMinX(),
                uenv.getMinY(),
                uenv.getMaxX(),
                uenv.getMaxY(),
                tinfo.getNx(),
                tinfo.getNy(),
                tinfo.getLevels(),
                tinfo.getScale(),
                tinfo.getShift()));

        out.write(" WHERE t.cellid = i.cellid)");
        return true;
    }
}
