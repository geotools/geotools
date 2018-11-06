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

package org.geotools.appschema.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.appschema.filter.NestedAttributeExpression;
import org.geotools.appschema.jdbc.JoiningJDBCFeatureSource.JoiningFieldEncoder;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.config.JdbcMultipleValue;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor.FeatureChainLink;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor.FeatureChainedAttributeDescriptor;
import org.geotools.data.complex.filter.UnmappingFilterVisitor;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.filter.XPathUtil.StepList;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.jdbc.*;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * {@link FilterToSQL} decorator capable of encoding filters on nested attributes.
 *
 * <p>Currently, the filters that can be translated to SQL are:
 *
 * <ul>
 *   <li>BBOX
 *   <li>Contains
 *   <li>Touches
 *   <li>Intersects
 *   <li>Overlaps
 *   <li>Within
 *   <li>Crosses
 *   <li>Disjoint
 *   <li>Equals
 *   <li>Beyond
 *   <li>DWithin
 *   <li>PropertyIsEqualTo
 *   <li>PropertyIsNotEqualTo
 *   <li>PropertyIsLessThan
 *   <li>PropertyIsLessThanOrEqualTo
 *   <li>PropertyIsGreaterThan
 *   <li>PropertyIsGreaterThanOrEqualTo
 *   <li>PropertyIsLike
 *   <li>PropertyIsNull
 *   <li>PropertyIsBetween
 * </ul>
 *
 * <p>Note that, in order to be successfully encoded, the filter must not involve more than one
 * nested attribute (i.e. comparing nested attributes is not supported), nor attributes that are
 * chained via polymorphic mappings.
 *
 * <p>If the visited filter does not involve nested attributes, its encoding is delegated to the
 * wrapped {@link FilterToSQL} instance.
 *
 * @author Mauro Bartolomeoli, GeoSolutions
 * @author Stefano Costa, GeoSolutions
 */
public class NestedFilterToSQL extends FilterToSQL {
    FeatureTypeMapping rootMapping;

    FilterToSQL original;
    FilterFactory ff;

    /**
     * Constructor.
     *
     * @param rootMapping the feature type being queried
     * @param original the wrapped filter-to-SQL encoder
     */
    public NestedFilterToSQL(FeatureTypeMapping rootMapping, FilterToSQL original) {
        super();
        this.rootMapping = rootMapping;
        this.original = original;
        this.ff = new FilterFactoryImplNamespaceAware(rootMapping.getNamespaces());
    }

    public void encode(Filter filter) throws FilterToSQLException {
        if (out == null) throw new FilterToSQLException("Can't encode to a null writer.");
        original.setWriter(out);
        JdbcMultipleValueEncoder encoder = new JdbcMultipleValueEncoder(rootMapping, out);
        filter = (Filter) filter.accept(encoder, null);
        if (original.getCapabilities().fullySupports(filter)) {

            try {
                if (!inline) {
                    out.write("WHERE ");
                }

                filter.accept(this, null);

                // out.write(";");
            } catch (java.io.IOException ioe) {
                throw new FilterToSQLException("Problem writing filter: ", ioe);
            }
        } else {
            throw new FilterToSQLException("Filter type not supported");
        }
    }

    private void encodeMultipleValueJoin(
            FeatureChainedAttributeDescriptor nestedAttribute,
            JDBCDataStore store,
            StringBuffer sql) {
        FeatureTypeMapping featureMapping = nestedAttribute.getFeatureTypeOwningAttribute();
        AttributeMapping mapping =
                featureMapping.getAttributeMapping(nestedAttribute.getAttributePath());
        if (mapping == null
                || !mapping.isMultiValued()
                || !(mapping.getMultipleValue() instanceof JdbcMultipleValue)) {
            return;
        }
        JdbcMultipleValue multipleValue = (JdbcMultipleValue) mapping.getMultipleValue();
        sql.append(" LEFT JOIN ");
        String alias = String.valueOf(multipleValue.getId());
        try {
            store.encodeAliasedTableName(multipleValue.getTargetTable(), sql, null, alias);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql.append(" ON ");
        store.dialect.encodeColumnName(nestedAttribute.getLastLink().getAlias(), sql);
        sql.append(".");
        store.dialect.encodeColumnName(multipleValue.getSourceColumn(), sql);
        sql.append(" = ");
        store.dialect.encodeTableName(alias, sql);
        sql.append(".");
        store.dialect.encodeColumnName(multipleValue.getTargetColumn(), sql);
        sql.append(" ");
    }

    protected Object visitNestedFilter(Filter filter, Object extraData, String xpath) {
        try {

            FeatureChainedAttributeVisitor nestedMappingsExtractor =
                    new FeatureChainedAttributeVisitor(rootMapping);
            nestedMappingsExtractor.visit(ff.property(xpath), null);
            List<FeatureChainedAttributeDescriptor> attributes =
                    nestedMappingsExtractor.getFeatureChainedAttributes();
            // encoding of filters on multiple nested attributes is not (yet) supported
            if (attributes.size() == 1) {
                FeatureChainedAttributeDescriptor nestedAttrDescr = attributes.get(0);

                int numMappings = nestedAttrDescr.chainSize();
                if (numMappings > 0 && nestedAttrDescr.isJoiningEnabled()) {
                    out.write("EXISTS (");

                    FeatureChainLink lastMappingStep = nestedAttrDescr.getLastLink();
                    StringBuffer sql = encodeSelectKeyFrom(lastMappingStep);
                    JDBCDataStore store =
                            (JDBCDataStore)
                                    lastMappingStep
                                            .getFeatureTypeMapping()
                                            .getSource()
                                            .getDataStore();
                    encodeMultipleValueJoin(nestedAttrDescr, store, sql);

                    for (int i = numMappings - 2; i > 0; i--) {
                        FeatureChainLink mappingStep = nestedAttrDescr.getLink(i);
                        if (mappingStep.hasNestedFeature()) {
                            FeatureTypeMapping parentFeature = mappingStep.getFeatureTypeMapping();
                            store = (JDBCDataStore) parentFeature.getSource().getDataStore();
                            String parentTableName =
                                    parentFeature.getSource().getSchema().getName().getLocalPart();

                            sql.append(" INNER JOIN ");
                            store.encodeTableName(parentTableName, sql, null);
                            sql.append(" ");
                            store.dialect.encodeTableName(mappingStep.getAlias(), sql);
                            sql.append(" ON ");
                            encodeJoinCondition(nestedAttrDescr, i, sql);
                        }
                    }

                    if (nestedAttrDescr.getAttributePath() != null) {
                        createWhereClause(filter, xpath, nestedAttrDescr, sql);

                        sql.append(" AND ");
                    } else {
                        sql.append(" WHERE ");
                    }

                    // join with root table
                    encodeJoinCondition(nestedAttrDescr, 0, sql);

                    out.write(sql.toString());
                    out.write(")");
                }
            }

            return extraData;

        } catch (java.io.IOException ioe) {
            throw new RuntimeException("Problem writing filter: ", ioe);
        } catch (SQLException e) {
            throw new RuntimeException("Problem writing filter: ", e);
        } catch (FilterToSQLException e) {
            throw new RuntimeException("Problem writing filter: ", e);
        }
    }

    private void encodeJoinCondition(
            FeatureChainedAttributeDescriptor nestedAttrDescr, int stepIdx, StringBuffer sql)
            throws SQLException, IOException, FilterToSQLException {
        FeatureChainLink parentStep = nestedAttrDescr.getLink(stepIdx);
        FeatureChainLink nestedStep = nestedAttrDescr.getLink(stepIdx + 1);
        FeatureTypeMapping parentFeature = parentStep.getFeatureTypeMapping();
        JDBCDataStore store = (JDBCDataStore) parentFeature.getSource().getDataStore();
        NestedAttributeMapping nestedFeatureAttr = parentStep.getNestedFeatureAttribute();
        FeatureTypeMapping nestedFeature = nestedFeatureAttr.getFeatureTypeMapping(null);

        SimpleFeatureType parentSource = (SimpleFeatureType) parentFeature.getSource().getSchema();
        String parentTableName = parentFeature.getSource().getSchema().getName().getLocalPart();
        String parentTableAlias = parentStep.getAlias();
        WrappedFilterToSql parentToSQL = createFilterToSQL(parentFeature);
        // don't escape, as it will be done by the encodeColumn methods
        parentToSQL.setSqlNameEscape("");
        Expression parentExpression = nestedFeatureAttr.getSourceExpression();
        String parentTableColumn = parentToSQL.encodeToString(parentExpression);

        SimpleFeatureType nestedSource = (SimpleFeatureType) parentFeature.getSource().getSchema();
        String nestedTableAlias = nestedStep.getAlias();
        WrappedFilterToSql nestedFilterToSQL = createFilterToSQL(parentFeature);
        // don't escape, as it will be done by the encodeColumn methods
        nestedFilterToSQL.setSqlNameEscape("");
        Expression nestedExpr = nestedFeatureAttr.getMapping(nestedFeature).getSourceExpression();
        String nestedTableColumn = nestedFilterToSQL.encodeToString(nestedExpr);

        if (stepIdx == 0) {
            encodeColumnName(store, parentTableColumn, parentTableName, sql, null);
        } else {
            encodeAliasedColumnName(store, parentTableColumn, parentTableAlias, sql, null);
        }
        sql.append(" = ");
        encodeAliasedColumnName(store, nestedTableColumn, nestedTableAlias, sql, null);
    }

    private StringBuffer encodeSelectKeyFrom(FeatureChainLink lastMappingStep) throws SQLException {
        FeatureTypeMapping lastTypeMapping = lastMappingStep.getFeatureTypeMapping();
        JDBCDataStore store = (JDBCDataStore) lastTypeMapping.getSource().getDataStore();
        SimpleFeatureType lastType = (SimpleFeatureType) lastTypeMapping.getSource().getSchema();

        // primary key
        PrimaryKey key = null;

        try {
            key = store.getPrimaryKey(lastType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        StringBuffer sqlKeys = new StringBuffer();
        String colName;
        for (PrimaryKeyColumn col : key.getColumns()) {
            colName = col.getName();
            sqlKeys.append(",");
            encodeAliasedColumnName(store, colName, lastMappingStep.getAlias(), sqlKeys, null);
        }
        if (sqlKeys.length() <= 0) {
            sql.append("*");
        } else {
            sql.append(sqlKeys.substring(1));
        }
        sql.append(" FROM ");
        store.encodeTableName(lastType.getTypeName(), sql, null);
        sql.append(" ");
        store.dialect.encodeTableName(lastMappingStep.getAlias(), sql);
        return sql;
    }

    private void createWhereClause(
            Filter filter,
            String nestedProperty,
            FeatureChainedAttributeDescriptor nestedAttrDescr,
            StringBuffer sql)
            throws FilterToSQLException {
        FeatureChainLink lastLink = nestedAttrDescr.getLastLink();
        String simpleProperty = nestedAttrDescr.getAttributePath().toString();
        FeatureTypeMapping featureMapping = lastLink.getFeatureTypeMapping();
        JDBCDataStore store = (JDBCDataStore) featureMapping.getSource().getDataStore();
        FeatureTypeMapping featureMappingForUnrolling =
                nestedAttrDescr.getFeatureTypeOwningAttribute();
        SimpleFeatureType sourceType = (SimpleFeatureType) featureMapping.getSource().getSchema();

        NamespaceAwareAttributeRenameVisitor duplicate =
                new NamespaceAwareAttributeRenameVisitor(nestedProperty, simpleProperty);
        Filter duplicated = (Filter) filter.accept(duplicate, null);
        Filter unrolled = unrollFilter(duplicated, featureMappingForUnrolling);

        JoiningFieldEncoder fieldEncoder = new JoiningFieldEncoder(lastLink.getAlias(), store);
        WrappedFilterToSql filterToSQL = createFilterToSQL(featureMapping);
        filterToSQL.setFieldEncoder(fieldEncoder);
        String encodedFilter = filterToSQL.encodeToString(unrolled);
        sql.append(" ").append(encodedFilter);
    }

    private WrappedFilterToSql createFilterToSQL(FeatureTypeMapping featureMapping) {
        JDBCDataStore store = (JDBCDataStore) featureMapping.getSource().getDataStore();
        SimpleFeatureType sourceType = (SimpleFeatureType) featureMapping.getSource().getSchema();
        FilterToSQL filterToSQL = null;
        if (store.getSQLDialect() instanceof PreparedStatementSQLDialect) {
            PreparedFilterToSQL preparedFilterToSQL = store.createPreparedFilterToSQL(sourceType);
            // disable prepared statements to have literals actually encoded in the SQL
            preparedFilterToSQL.setPrepareEnabled(false);
            filterToSQL = preparedFilterToSQL;
        } else {
            filterToSQL = store.createFilterToSQL(sourceType);
        }
        return new WrappedFilterToSql(featureMapping, filterToSQL);
    }

    private void encodeColumnName(
            JDBCDataStore store, String colName, String typeName, StringBuffer sql, Hints hints)
            throws SQLException {
        store.encodeTableName(typeName, sql, hints);
        sql.append(".");
        store.dialect.encodeColumnName(colName, sql);
    }

    private void encodeAliasedColumnName(
            JDBCDataStore store, String colName, String typeName, StringBuffer sql, Hints hints)
            throws SQLException {
        store.dialect.encodeTableName(typeName, sql);
        sql.append(".");
        store.dialect.encodeColumnName(colName, sql);
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) throws RuntimeException {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) throws RuntimeException {
        NestedAttributeExpression nestedAttr = getNestedAttributeExpression(filter);
        if (nestedAttr == null) {
            return original.visit(filter, extraData);
        }
        return visitNestedFilter(filter, extraData, nestedAttr.getPropertyName());
    }

    /**
     * A filter is considered <em>nested</em> if it operates on at least one nested attribute.
     *
     * <p>Technically, this means that at least one of the expressions in it is an instance of
     * {@link NestedAttributeExpression}.
     *
     * @param filter the filter to test
     * @return <code>true</code> if the filter involves at least one nested attribute, <code>false
     *     </code> otherwise
     */
    public static boolean isNestedFilter(Filter filter) {
        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        filter.accept(extractor, null);
        return hasNestedAttributes(extractor.getPropertyNameSet());
    }

    private static boolean hasNestedAttributes(Set<PropertyName> propertyNames) {
        for (PropertyName property : propertyNames) {
            if (property instanceof NestedAttributeExpression) {
                return true;
            }
        }
        return false;
    }

    private <T extends Filter> NestedAttributeExpression getNestedAttributeExpression(T filter) {
        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        filter.accept(extractor, null);
        Set<PropertyName> propertyNames = extractor.getPropertyNameSet();
        for (PropertyName property : propertyNames) {
            if (property instanceof NestedAttributeExpression) {
                return (NestedAttributeExpression) property;
            }
        }
        return null;
    }

    private Filter unrollFilter(Filter complexFilter, FeatureTypeMapping mappings) {
        UnmappingFilterVisitorExcludingNestedMappings visitor =
                new UnmappingFilterVisitorExcludingNestedMappings(mappings);
        Filter unrolledFilter = (Filter) complexFilter.accept(visitor, null);
        return unrolledFilter;
    }

    private class UnmappingFilterVisitorExcludingNestedMappings extends UnmappingFilterVisitor {

        public UnmappingFilterVisitorExcludingNestedMappings(FeatureTypeMapping mappings) {
            super(mappings);
        }

        @Override
        public List<Expression> visit(PropertyName expr, Object arg1) {
            String targetXPath = expr.getPropertyName();
            NamespaceSupport namespaces = mappings.getNamespaces();
            AttributeDescriptor root = mappings.getTargetFeature();

            // break into single steps
            StepList simplifiedSteps = XPath.steps(root, targetXPath, namespaces);

            List<Expression> matchingMappings = mappings.findMappingsFor(simplifiedSteps, false);

            if (matchingMappings.size() == 0) {
                throw new IllegalArgumentException(
                        "Can't find source expression for: " + targetXPath);
            }

            return matchingMappings;
        }
    }
}
