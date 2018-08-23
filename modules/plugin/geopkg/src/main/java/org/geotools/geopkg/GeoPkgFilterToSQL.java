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
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;

/** @author ian */
public class GeoPkgFilterToSQL extends PreparedFilterToSQL {

    GeoPkgDialect dialect;

    /** @param dialect */
    public GeoPkgFilterToSQL(GeoPkgDialect dialect) {
        super(dialect);
        this.dialect = dialect;
    }

    /**
     * Override done to ensure we don't complain if there is a BBOX filter, even if we claim not to
     * support it
     */
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
        Object literalValue =
                evaluateLiteral(expression, (context instanceof Class ? (Class) context : null));
        literalValues.add(literalValue);
        SRIDs.add(currentSRID);
        dimensions.add(currentDimension);
        descriptors.add(
                context instanceof AttributeDescriptor ? (AttributeDescriptor) context : null);

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
                    dialect.prepareGeometryValue(
                            (Geometry) literalValue, dimension, srid, Geometry.class, sb);
                } else if (Time.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("time(?,'localtime')");
                } else if (Timestamp.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("datetime(?,'localtime')");
                } else if (java.sql.Date.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("date(?,'localtime')");
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
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {

        // get the attribute, it will expand the default geom name if necessary and give access to
        // the user data
        AttributeDescriptor attribute = property.evaluate(featureType, AttributeDescriptor.class);
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
                    && attribute != null
                    && Boolean.TRUE.equals(
                            attribute.getUserData().get(GeoPkgDialect.HAS_SPATIAL_INDEX))) {
                // encode the primary key
                PrimaryKeyColumn pk = primaryKey.getColumns().get(0);
                String pkName = pk.getName();
                filterFactory.property(pkName).accept(this, null);
                // Make Sure the table name is escaped - GEOT-5852
                StringBuffer sb = new StringBuffer();
                dialect.encodeTableName(
                        "rtree_" + featureType.getTypeName() + "_" + attribute.getLocalName(), sb);
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
                out.write(
                        "(ST_MaxX("
                                + encodedPropertyName
                                + ") >= "
                                + envelope.getMinX()
                                + " AND\n");
                out.write(
                        "ST_MinX(" + encodedPropertyName + ") <= " + envelope.getMaxX() + " AND\n");
                out.write(
                        "ST_MaxY(" + encodedPropertyName + ") >= " + envelope.getMinY() + " AND\n");
                out.write("ST_MinY(" + encodedPropertyName + ") <= " + envelope.getMaxY() + ")\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failure encoding the SQL equivalent for a spatial filter", e);
        }

        return extraData;
    }
}
