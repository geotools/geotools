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
import org.geotools.data.jdbc.FilterToSQL;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.identity.Identifier;

/**
 * Extension of FilterToSQL intended for use with prepared statements.
 *
 * <p>Each time a {@link Literal} is visited, a '?' is encoded, and the value and type of the
 * literal are stored, available after the fact via {@link #getLiteralValues()} and {@link
 * #getLiteralTypes()}.
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

    /**
     * Default constructor
     *
     * @deprecated Use {@link PreparedFilterToSQL(PreparedStatementSQLDialect)} instead
     */
    public PreparedFilterToSQL() {
        this.dialect = null;
    }

    /**
     * Contructor taking a reference to the SQL dialect, will use it to encode geometry placeholders
     *
     * @param dialect
     */
    public PreparedFilterToSQL(PreparedStatementSQLDialect dialect) {
        this.dialect = dialect;
    }

    /**
     * If true (default) a sql statement with literal placemarks is created, otherwise a normal
     * statement is created
     *
     * @return
     */
    public boolean isPrepareEnabled() {
        return prepareEnabled;
    }

    public void setPrepareEnabled(boolean prepareEnabled) {
        this.prepareEnabled = prepareEnabled;
    }

    public PreparedFilterToSQL(Writer out) {
        super(out);
    }

    public Object visit(Literal expression, Object context) throws RuntimeException {
        if (!prepareEnabled) return super.visit(expression, context);

        Class clazz = getTargetClassFromContext(context);

        // evaluate the literal and store it for later
        Object literalValue = evaluateLiteral(expression, clazz);
        literalValues.add(literalValue);
        SRIDs.add(currentSRID);
        dimensions.add(currentDimension);
        descriptors.add(
                context instanceof AttributeDescriptor ? (AttributeDescriptor) context : null);

        if (clazz == null && literalValue != null) {
            clazz = literalValue.getClass();
        }
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
    public Object visit(Id filter, Object extraData) {
        if (mapper == null) {
            throw new RuntimeException("Must set a fid mapper before trying to encode FIDFilters");
        }

        Set ids = filter.getIdentifiers();

        // prepare column name array
        String[] colNames = new String[mapper.getColumnCount()];

        for (int i = 0; i < colNames.length; i++) {
            colNames[i] = mapper.getColumnName(i);
        }

        for (Iterator i = ids.iterator(); i.hasNext(); ) {
            try {
                Identifier id = (Identifier) i.next();
                Object[] attValues = mapper.getPKAttributes(id.toString());

                out.write("(");

                for (int j = 0; j < attValues.length; j++) {
                    out.write(escapeName(colNames[j]));
                    out.write(" = ");
                    out.write('?');

                    // store the value for later usage
                    literalValues.add(attValues[j]);
                    // no srid, pk are not formed with geometry values
                    SRIDs.add(-1);
                    dimensions.add(-1);
                    // if it's not null, we can also infer the type
                    literalTypes.add(attValues[j] != null ? attValues[j].getClass() : null);
                    descriptors.add(null);

                    if (j < (attValues.length - 1)) {
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

    /**
     * Returns the list of native SRID for each literal that happens to be a geometry, or null
     * otherwise
     *
     * @return
     */
    public List<Integer> getSRIDs() {
        return SRIDs;
    }

    /**
     * Returns the list of dimensions for each literal tha happens to be a geometry, or null
     * otherwise
     *
     * @return
     */
    public List<Integer> getDimensions() {
        return dimensions;
    }

    /**
     * Returns the attribute descriptors compared to a given literal (if any, not always available,
     * normally only needed if arrays are involved)
     *
     * @return
     */
    public List<AttributeDescriptor> getDescriptors() {
        return descriptors;
    }
}
