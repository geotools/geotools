/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.io.IOException;
import java.io.Writer;
import java.sql.Time;
import java.sql.Timestamp;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/** @author ian */
public class GeoPkgFilterToSQL extends PreparedFilterToSQL {

    GeoPkgDialect dialect;

    /** @param dialect */
    public GeoPkgFilterToSQL(GeoPkgDialect dialect) {
        super(dialect);
        this.dialect = dialect;
    }

    /**
     * cf. visit(Literal expression,...) When doing temporal queries (like "time BETWEEN t1 AND t2")
     *
     * <p>The encoding of the column name ("time") and the literals must be the same!
     *
     * <p>There is different handling for Date (DATE) and Timestamp (DATETIME).
     *
     * <p>For Timestamp (DATETIME), we use the datetime(XYZ, 'utc'):
     *
     * <p>datetime("Time",'utc') BETWEEN datetime(?,'utc') AND datetime(?,'utc')
     *
     * <p>For Date (DATE), we do no conversion in the sql lite:
     *
     * <p>datetime("Date") BETWEEN datetime(?) AND datetime(?)
     *
     * <p>For non-time columns, this just relegates to the superclass
     *
     * <p>For GeoPKG, the time column is actually stored as a STRING.
     */
    @Override
    public String escapeName(String name) {
        String super_result = super.escapeName(name);
        AttributeDescriptor desc = featureType.getDescriptor(name);
        // desc might be null in the case of joins et al
        if (desc != null && desc.getType().getBinding() != null) {
            Class<?> binding = desc.getType().getBinding();
            // utc -- everything must be consistent -- see literal visitor
            if (Time.class.isAssignableFrom(binding)) {
                return "time(" + super_result + ")";
            } else if (Timestamp.class.isAssignableFrom(binding)) {
                return "datetime(" + super_result + ",'utc' )"; // utc -- everything must be consistent -- see
                // literal visitor
            } else if (java.sql.Date.class.isAssignableFrom(binding)) {
                return "date(" + super_result + ")";
            }
        }
        return super_result;
    }
    /** Override done to ensure we don't complain if there is a BBOX filter, even if we claim not to support it */
    @Override
    public void encode(Filter filter) throws FilterToSQLException {
        if (out == null) throw new FilterToSQLException("Can't encode to a null writer.");
        // hack, we lied about being able to support BBOX, because the implementation is
        // just partial, so here don't complain if we got one, it means the dialect already
        // set the filter correctly
        FilterCapabilities caps = createFilterCapabilities();
        caps.addType(BBOX.class);
        if (caps.fullySupports(filter)) {

            try {
                if (!inline) {
                    out.write("WHERE ");
                }

                filter.accept(this, null);
            } catch (java.io.IOException ioe) {
                LOGGER.warning("Unable to export filter" + ioe);
                throw new FilterToSQLException("Problem writing filter: ", ioe);
            }
        } else {
            throw new FilterToSQLException("Filter type not supported");
        }
    }

    /** @param out */
    public GeoPkgFilterToSQL(Writer out) {
        super(out);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Object visit(Literal expression, Object context) throws RuntimeException {
        if (!isPrepareEnabled()) return super.visit(expression, context);

        // evaluate the literal and store it for later
        Object literalValue = evaluateLiteral(expression, context instanceof Class ? (Class) context : null);
        literalValues.add(literalValue);
        SRIDs.add(currentSRID);
        dimensions.add(currentDimension);
        descriptors.add(context instanceof AttributeDescriptor ? (AttributeDescriptor) context : null);

        Class clazz = null;
        if (context instanceof Class) clazz = (Class) context;
        else if (literalValue != null) clazz = literalValue.getClass();
        literalTypes.add(clazz);

        try {
            if (literalValue == null || dialect == null) {
                out.write("?");
            } else {
                StringBuffer sb = new StringBuffer();
                if (Geometry.class.isAssignableFrom(literalValue.getClass())) {
                    int srid = currentSRID != null ? currentSRID : -1;
                    int dimension = currentDimension != null ? currentDimension : -1;
                    dialect.prepareGeometryValue((Geometry) literalValue, dimension, srid, Geometry.class, sb);
                } else if (Time.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("time(?)");
                } else if (Timestamp.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("datetime(?,'utc' )");
                } else if (java.sql.Date.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("date(?)");
                } else if (encodingFunction) {
                    dialect.prepareFunctionArgument(clazz, sb);
                } else {
                    sb.append("?");
                }
                out.write(sb.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return context;
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) {

        // get the attribute, it will expand the default geom name if necessary and give access to
        // the user data
        AttributeDescriptor attribute = property.evaluate(featureType, AttributeDescriptor.class);
        if (attribute == null) {
            throw new IllegalArgumentException("Could not find attribute referenced as " + property);
        }
        // should be ever called only with a bbox filter
        Geometry reference = geometry.evaluate(null, Geometry.class);
        Envelope envelope = reference.getEnvelopeInternal();
        if (envelope == null) {
            throw new IllegalArgumentException(
                    "Invalid BBOX filter specification, it's defined against a NULL envelope");
        }
        envelope.expandBy(1e-11);

        // can we use the spatial index?
        try {
            if (primaryKey != null
                    && Boolean.TRUE.equals(attribute.getUserData().get(GeoPkgDialect.HAS_SPATIAL_INDEX))) {
                // encode the primary key
                PrimaryKeyColumn pk = primaryKey.getColumns().get(0);
                String pkName = pk.getName();
                filterFactory.property(pkName).accept(this, null);
                // Make Sure the table name is escaped - GEOT-5852
                StringBuffer sb = new StringBuffer();
                dialect.encodeTableName("rtree_" + featureType.getTypeName() + "_" + attribute.getLocalName(), sb);
                String spatial_index = sb.toString();

                out.write(" IN (SELECT id FROM " + spatial_index + " r WHERE");
                out.write(" r.maxx >= " + envelope.getMinX());
                out.write(" AND r.minx <= " + envelope.getMaxX());
                out.write(" AND r.maxy >= " + envelope.getMinY());
                out.write(" AND r.miny <= " + envelope.getMaxY());
                out.write(")");
            } else {
                // fall back on direct BBOX tests, it's still faster than loading the whole geometry
                // and to do BBOX in memory
                StringBuffer sb = new StringBuffer();
                dialect.encodeColumnName(null, attribute.getLocalName(), sb);
                String encodedPropertyName = sb.toString();
                out.write("(ST_MaxX(" + encodedPropertyName + ") >= " + envelope.getMinX() + " AND\n");
                out.write("ST_MinX(" + encodedPropertyName + ") <= " + envelope.getMaxX() + " AND\n");
                out.write("ST_MaxY(" + encodedPropertyName + ") >= " + envelope.getMinY() + " AND\n");
                out.write("ST_MinY(" + encodedPropertyName + ") <= " + envelope.getMaxY() + ")\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failure encoding the SQL equivalent for a spatial filter", e);
        }

        return extraData;
    }
}
