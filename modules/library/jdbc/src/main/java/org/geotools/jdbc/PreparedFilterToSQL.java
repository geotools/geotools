/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

/**
 * Extension of FilterToSQL intended for use with prepared statements.
 *
 * <p>Each time a {@link Literal} is visited, a '?' is encoded, and the value and type of the literal are stored,
 * available after the fact via {@link #getLiteralValues()} and {@link #getLiteralTypes()}.
 *
 * @author Justin Deoliveira, OpenGEO
 * @author Andrea Aime, OpenGEO
 */
public class PreparedFilterToSQL extends FilterToSQL {
    /** ordered list of literal values encountered and their types */
    protected List<Object> literalValues = new ArrayList<>();

    protected List<Class> literalTypes = new ArrayList<>();
    protected List<Integer> SRIDs = new ArrayList<>();
    protected List<Integer> dimensions = new ArrayList<>();
    protected List<AttributeDescriptor> descriptors = new ArrayList<>();
    protected PreparedStatementSQLDialect dialect;
    boolean prepareEnabled = true;

    /** Contructor taking a reference to the SQL dialect, will use it to encode geometry placeholders */
    public PreparedFilterToSQL(PreparedStatementSQLDialect dialect) {
        this.dialect = dialect;
    }

    /** If true (default) a sql statement with literal placemarks is created, otherwise a normal statement is created */
    public boolean isPrepareEnabled() {
        return prepareEnabled;
    }

    public void setPrepareEnabled(boolean prepareEnabled) {
        this.prepareEnabled = prepareEnabled;
    }

    public PreparedFilterToSQL(Writer out) {
        super(out);
    }

    @Override
    public Object visit(Literal expression, Object context) throws RuntimeException {
        if (!prepareEnabled) return super.visit(expression, context);

        Class clazz = getTargetClassFromContext(context);

        // evaluate the literal and store it for later
        Object literalValue = evaluateLiteral(expression, clazz);

        // bbox filters have a right side expression that's a ReferencedEnvelope,
        // but SQL dialects use/want polygons instead
        if (literalValue instanceof Envelope && convertEnvelopeToPolygon()) {
            clazz = Polygon.class;
            literalValue = JTS.toGeometry((Envelope) literalValue);
        }

        if (clazz == null && literalValue != null) {
            clazz = literalValue.getClass();
        }

        literalValues.add(literalValue);
        SRIDs.add(currentSRID);
        dimensions.add(currentDimension);
        descriptors.add(context instanceof AttributeDescriptor ? (AttributeDescriptor) context : null);
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

    /**
     * When returning true, the {@link Literal} visit will turn {@link Envelope} objects (typically coming from
     * {@link org.geotools.api.filter.spatial.BBOX} filters) into {@link Polygon}. Defaults to true, subclasses can
     * override.
     */
    protected boolean convertEnvelopeToPolygon() {
        return true;
    }

    private Class getTargetClassFromContext(Object context) {
        if (context instanceof Class) {
            return (Class) context;
        } else if (context instanceof AttributeDescriptor) {
            return ((AttributeDescriptor) context).getType().getBinding();
        }
        return null;
    }

    /**
     * Encodes an Id filter
     *
     * @param filter the
     * @throws RuntimeException If there's a problem writing output
     */
    @Override
    public Object visit(Id filter, Object extraData) {

        if (primaryKey == null) {
            throw new RuntimeException("Must set a primary key before trying to encode FIDFilters");
        }

        Set ids = filter.getIdentifiers();

        List<PrimaryKeyColumn> columns = primaryKey.getColumns();
        for (Iterator i = ids.iterator(); i.hasNext(); ) {
            try {
                Identifier id = (Identifier) i.next();
                List<Object> attValues = JDBCDataStore.decodeFID(primaryKey, id.toString(), false);

                out.write("(");

                for (int j = 0; j < attValues.size(); j++) {
                    // in case of join the pk columns need to be qualified with alias
                    if (filter instanceof JoinId) {
                        out.write(escapeName(((JoinId) filter).getAlias()));
                        out.write(".");
                    }
                    out.write(escapeName(columns.get(j).getName()));
                    out.write(" = ");
                    out.write('?');

                    // store the value for later usage
                    literalValues.add(attValues.get(j));
                    // no srid, pk are not formed with geometry values
                    SRIDs.add(-1);
                    dimensions.add(-1);
                    // if it's not null, we can also infer the type
                    literalTypes.add(attValues.get(j) != null ? attValues.get(j).getClass() : null);
                    descriptors.add(null);

                    if (j < attValues.size() - 1) {
                        out.write(" AND ");
                    }
                }

                out.write(")");

                if (i.hasNext()) {
                    out.write(" OR ");
                }
            } catch (java.io.IOException e) {
                throw new RuntimeException(IO_ERROR, e);
            }
        }

        return extraData;
    }

    public List<Object> getLiteralValues() {
        return literalValues;
    }

    public List<Class> getLiteralTypes() {
        return literalTypes;
    }

    /** Returns the list of native SRID for each literal that happens to be a geometry, or null otherwise */
    public List<Integer> getSRIDs() {
        return SRIDs;
    }

    /** Returns the list of dimensions for each literal tha happens to be a geometry, or null otherwise */
    public List<Integer> getDimensions() {
        return dimensions;
    }

    /**
     * Returns the attribute descriptors compared to a given literal (if any, not always available, normally only needed
     * if arrays are involved)
     */
    public List<AttributeDescriptor> getDescriptors() {
        return descriptors;
    }
}
