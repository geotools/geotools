/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.solr;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

/** Encodes a OGC filter into a SOLR query syntax */
public class FilterToSolr implements FilterVisitor {

    /* Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FilterToSolr.class);

    /* Lucene characters to escape on filter expressions */
    private static final String[] LUCENE_SPECIAL_CHARACTERS =
            new String[] {
                "+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~", ":"
            };

    /** Filter factory */
    protected static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /** Where to write the constructed string from visiting the filters. */
    protected Writer out;

    /** The filter types that this class can encode */
    protected FilterCapabilities capabilities = null;

    /* Store SOLR identifier attribute */
    private SolrAttribute primaryKey;

    /* Store SOLR attribute used as layer name filter */
    private String featureTypeName;

    /** the feature type */
    private SimpleFeatureType featureType;

    public FilterToSolr(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }

    /**
     * A single call method to encode filter to SOLR query
     *
     * @return a string representing the filter encoded to SOLR.
     */
    public String encodeToString(Filter filter) throws Exception {
        StringWriter out = new StringWriter();
        this.out = out;
        this.encode(filter);
        return out.getBuffer().toString();
    }

    /**
     * Performs the encoding, sends the encoded SOLR string to the writer passed in.
     *
     * @param filter the Filter to be encoded.
     * @throws Exception if there were io problems or unsupported filter operation
     */
    public void encode(Filter filter) throws Exception {
        if (out == null) throw new Exception("Can't encode to a null writer.");
        if (getCapabilities().fullySupports(filter)) {
            try {
                filter.accept(this, out);
            } catch (Exception ioe) {
                LOGGER.warning("Unable to export filter" + ioe);
                throw new Exception("Problem writing filter: ", ioe);
            }
        } else {
            throw new Exception("Filter type not supported");
        }
    }

    /**
     * Describes the capabilities of this encoder.
     *
     * <p>Performs lazy creation of capabilities. If you're subclassing this class, override
     * createFilterCapabilities to declare which filtercapabilities you support. Don't use this
     * method.
     *
     * @return The capabilities supported by this encoder.
     */
    public FilterCapabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = createFilterCapabilities();
        }
        return capabilities; // maybe clone? Make immutable somehow
    }

    /**
     * Sets the capabilities of this filter.
     *
     * @return FilterCapabilities for this Filter
     */
    protected FilterCapabilities createFilterCapabilities() {
        capabilities = new FilterCapabilities();
        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(FilterCapabilities.FID);
        capabilities.addType(FilterCapabilities.BETWEEN);
        capabilities.addType(FilterCapabilities.LIKE);
        capabilities.addType(FilterCapabilities.NULL_CHECK);
        capabilities.addType(FilterCapabilities.SPATIAL_BBOX);
        // JD: as of solr 5 the disjoint filter no longer works directly so we disable it
        // See https://issues.apache.org/jira/browse/LUCENE-5692
        // capabilities.addType(FilterCapabilities.SPATIAL_DISJOINT);
        capabilities.addType(FilterCapabilities.SPATIAL_WITHIN);
        capabilities.addType(FilterCapabilities.SPATIAL_INTERSECT);
        capabilities.addType(FilterCapabilities.SPATIAL_CONTAINS);
        // temporal filters
        capabilities.addType(After.class);
        capabilities.addType(Before.class);
        capabilities.addType(Begins.class);
        capabilities.addType(BegunBy.class);
        capabilities.addType(During.class);
        capabilities.addType(Ends.class);
        capabilities.addType(EndedBy.class);
        capabilities.addType(TContains.class);
        capabilities.addType(TEquals.class);

        return capabilities;
    }

    /*
     * The current implementation does exactly nothing
     */

    @Override
    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    /*
     * Writes the query for the IncludeFilter by writing "FALSE".
     */
    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        StringWriter output = asStringWriter(extraData);
        output.append("-*:*");
        return output;
    }

    /*
     * Writes the query for the IncludeFilter by writing "TRUE".
     */
    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        StringWriter output = asStringWriter(extraData);
        output.append("*:*");
        return output;
    }

    @Override
    public Object visit(And filter, Object extraData) {
        return buildBinaryLogicalOperator("AND", this, filter, extraData);
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        StringWriter output = asStringWriter(extraData);
        Set<Identifier> ids = filter.getIdentifiers();
        output.append(" (");
        for (Iterator<Identifier> i = ids.iterator(); i.hasNext(); ) {
            Identifier id = i.next();
            String fid = decodeFID(id.toString());
            output.write(primaryKey.getName() + ":" + "\"" + fid + "\"");
            if (i.hasNext()) {
                output.write(" OR ");
            }
        }
        output.append(") ");
        return output;
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        StringWriter output = asStringWriter(extraData);
        output.append("NOT (");
        filter.getFilter().accept(this, output);
        output.append(")");
        return output;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        return buildBinaryLogicalOperator("OR", this, filter, extraData);
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        checkExpressionIsProperty(filter.getExpression());
        StringWriter output = asStringWriter(extraData);
        ExpressionToSolr visitor = new ExpressionToSolr();
        PropertyName propertyName = (PropertyName) filter.getExpression();
        propertyName.accept(visitor, output);
        output.append(":[");
        filter.getLowerBoundary().accept(visitor, output);
        output.append(" TO ");
        filter.getUpperBoundary().accept(visitor, output);
        output.append("]");
        return output;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return buildComparison(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return buildComparison(filter, extraData);
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        Expression[] expr =
                binaryFilterVisitorNormalizer(filter.getExpression1(), filter.getExpression2());
        StringWriter output = asStringWriter(extraData);
        ExpressionToSolr visitor = new ExpressionToSolr();
        PropertyName propertyName = (PropertyName) expr[0];
        propertyName.accept(visitor, output);
        output.append(":{");
        expr[1].accept(visitor, output);
        output.append(" TO *}");
        return output;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        Expression[] expr =
                binaryFilterVisitorNormalizer(filter.getExpression1(), filter.getExpression2());
        StringWriter output = asStringWriter(extraData);
        ExpressionToSolr visitor = new ExpressionToSolr();
        PropertyName propertyName = (PropertyName) expr[0];
        propertyName.accept(visitor, output);
        output.append(":[");
        expr[1].accept(visitor, output);
        output.append(" TO *]");
        return output;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        Expression[] expr =
                binaryFilterVisitorNormalizer(filter.getExpression1(), filter.getExpression2());
        checkExpressionIsProperty(filter.getExpression1());
        StringWriter output = asStringWriter(extraData);
        ExpressionToSolr visitor = new ExpressionToSolr();
        PropertyName propertyName = (PropertyName) expr[0];
        propertyName.accept(visitor, output);
        output.append(":{* TO ");
        expr[1].accept(visitor, output);
        output.append("}");
        return output;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        Expression[] expr =
                binaryFilterVisitorNormalizer(filter.getExpression1(), filter.getExpression2());
        StringWriter output = asStringWriter(extraData);
        ExpressionToSolr visitor = new ExpressionToSolr();
        PropertyName propertyName = (PropertyName) expr[0];
        propertyName.accept(visitor, output);
        output.append(":[* TO ");
        expr[1].accept(visitor, output);
        output.append("]");
        return output;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        checkExpressionIsProperty(filter.getExpression());
        StringWriter output = asStringWriter(extraData);
        String pattern = escapeSpecialCharacters(filter.getLiteral(), filter.getEscape());
        pattern = pattern.replace(filter.getWildCard(), ".*");
        pattern = pattern.replace(filter.getSingleChar(), ".{1,1}");
        Expression expr = filter.getExpression();
        ExpressionToSolr visitor = new ExpressionToSolr();
        expr.accept(visitor, output);
        output.append(":/");
        output.append(pattern);
        output.append("/ ");
        return output;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        checkExpressionIsProperty(filter.getExpression());
        StringWriter output = asStringWriter(extraData);
        ExpressionToSolr visitor = new ExpressionToSolr();
        PropertyName propertyName = (PropertyName) filter.getExpression();
        output.append("-");
        propertyName.accept(visitor, output);
        output.append(":[* TO *]");
        return output;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("PropertyIsNil filter not supported");
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(After after, Object extraData) {
        return visitBinaryTemporalOperator(after, extraData);
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return visitBinaryTemporalOperator(before, extraData);
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return visitBinaryTemporalOperator(begins, extraData);
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return visitBinaryTemporalOperator(begunBy, extraData);
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return visitBinaryTemporalOperator(ends, extraData);
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return visitBinaryTemporalOperator(endedBy, extraData);
    }

    @Override
    public Object visit(During during, Object extraData) {
        return visitBinaryTemporalOperator(during, extraData);
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return visitBinaryTemporalOperator(contains, extraData);
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return visitBinaryTemporalOperator(equals, extraData);
    }

    /* UNSUPPORTED */

    @Override
    public Object visit(Beyond filter, Object extraData) {
        throw new UnsupportedOperationException("Beyond filter not supported");
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        throw new UnsupportedOperationException("Crosses filter not supported");
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        throw new UnsupportedOperationException("DWithin filter not supported");
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        throw new UnsupportedOperationException("Overlaps filter not supported");
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        throw new UnsupportedOperationException("Touches filter not supported");
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw new UnsupportedOperationException("AnyInteracts filter not supported");
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        throw new UnsupportedOperationException("Meets filter not supported");
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        throw new UnsupportedOperationException("MetBy filter not supported");
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw new UnsupportedOperationException("OverlappedBy filter not supported");
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        throw new UnsupportedOperationException("TOverlaps filter not supported");
    }

    /** Sets the {@link FilterToSolr#featureTypeName} */
    public void setFeatureTypeName(String featureTypeName) {
        this.featureTypeName = featureTypeName;
    }

    /** Sets the {@link FilterToSolr#primaryKey} */
    public void setPrimaryKey(SolrAttribute primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * Convert extraData parameter to StringWriter or create new one if not exists This method is
     * called at the start of each visit method to obtain output to write in
     *
     * @param extraData output to write in
     */
    protected static StringWriter asStringWriter(Object extraData) {
        if (extraData instanceof StringWriter) {
            return (StringWriter) extraData;
        }
        return new StringWriter();
    }

    /**
     * Escape with "\\" the phrase according to Lucene special characters and other characters
     * passed as input
     *
     * @see {@link FilterToSolr#LUCENE_SPECIAL_CHARACTERS}
     * @param searchPhrase the phrase to escape
     * @param otherEscapes additional parameters to escape other than {@link
     *     FilterToSolr#LUCENE_SPECIAL_CHARACTERS}
     * @return the escaped string
     */
    protected static String escapeSpecialCharacters(String searchPhrase, String... otherEscapes) {
        for (int i = 0; i < LUCENE_SPECIAL_CHARACTERS.length; i++) {
            searchPhrase =
                    searchPhrase.replace(
                            LUCENE_SPECIAL_CHARACTERS[i], "\\" + LUCENE_SPECIAL_CHARACTERS[i]);
        }
        for (String e : otherEscapes) {
            searchPhrase = searchPhrase.replace(e, "\\" + e);
        }
        return searchPhrase;
    }

    /*
     * Check if Expression is a Property
     */
    private void checkExpressionIsProperty(Expression expr) {
        if (!(expr instanceof PropertyName)) {
            throw new RuntimeException("SOLR requires a PropertyName");
        }
    }

    /*
     * Check if Expression is a Literal
     */
    private void checkExpressionIsLiteral(Expression expr) {
        if (!(expr instanceof Literal)) {
            throw new RuntimeException("SOLR requires a Literal");
        }
    }

    /*
     * Writes the SOLR query for binary comparison operator : EQUAL, NOT EQUAL
     *
     * @param filter binary comparison operator to encode
     */
    private Object buildComparison(BinaryComparisonOperator filter, Object extraData) {
        StringWriter output = asStringWriter(extraData);
        if (filter instanceof PropertyIsNotEqualTo) {
            output.append("-");
        }
        Expression[] expr =
                binaryFilterVisitorNormalizer(filter.getExpression1(), filter.getExpression2());
        ExpressionToSolr visitor = new ExpressionToSolr();
        expr[0].accept(visitor, output);
        output.append(":");
        expr[1].accept(visitor, output);
        return output;
    }

    /*
     * Writes the SOLR query for binary logical operator : AND, OR
     *
     * @param operator the operator to use in encode
     *
     * @param visitor encoder to manage nested inner expressions
     *
     * @param filter binary logical operator to encode
     */

    private Object buildBinaryLogicalOperator(
            final String operator,
            FilterVisitor visitor,
            BinaryLogicOperator filter,
            Object extraData) {
        StringWriter output = asStringWriter(extraData);
        List<Filter> children = filter.getChildren();
        if (children != null) {
            for (Iterator<Filter> i = children.iterator(); i.hasNext(); ) {
                Filter child = i.next();
                if (child instanceof BinaryLogicOperator) {
                    output.append("(");
                }
                child.accept(visitor, output);
                if (child instanceof BinaryLogicOperator) {
                    output.append(")");
                }
                if (i.hasNext()) {
                    output.append(" ").append(operator).append(" ");
                }
            }
        }
        return output;
    }

    /*
     * Writes the SOLR query for temporal operator : After, Before, Begins, Ends, TEquals, BegunBy,
     * EndedBy, During, TContains
     *
     * @param filter temporal operator to encode
     */
    private Object visitBinaryTemporalOperator(BinaryTemporalOperator filter, Object extraData) {
        StringWriter output = asStringWriter(extraData);
        Expression e1 = filter.getExpression1();
        Expression e2 = filter.getExpression2();
        ExpressionToSolr visitor = new ExpressionToSolr(filter);
        if (filter instanceof After) {
            checkExpressionIsProperty(e1);
            checkExpressionIsLiteral(e2);
            PropertyName propertyName = (PropertyName) e1;
            propertyName.accept(visitor, output);
            output.append(":{");
            e2.accept(visitor, output);
            output.append(" TO *}");
        }
        if (filter instanceof Before) {
            checkExpressionIsProperty(e1);
            checkExpressionIsLiteral(e2);
            PropertyName propertyName = (PropertyName) e1;
            propertyName.accept(visitor, output);
            output.append(":{* TO ");
            e2.accept(visitor, output);
            output.append("}");
        }
        if (filter instanceof Begins || filter instanceof Ends || filter instanceof TEquals) {
            checkExpressionIsProperty(e1);
            checkExpressionIsLiteral(e2);
            PropertyName propertyName = (PropertyName) e1;
            propertyName.accept(visitor, output);
            output.append(":");
            e2.accept(visitor, output);
        }
        if (filter instanceof BegunBy || filter instanceof EndedBy) {
            checkExpressionIsProperty(e2);
            checkExpressionIsLiteral(e1);
            PropertyName propertyName = (PropertyName) e2;
            propertyName.accept(visitor, output);
            output.append(":");
            e1.accept(visitor, output);
        }
        if (filter instanceof During) {
            checkExpressionIsProperty(e1);
            checkExpressionIsLiteral(e2);
            PropertyName propertyName = (PropertyName) e1;
            propertyName.accept(visitor, output);
            output.append(":{");
            e2.accept(visitor, output);
            output.append("}");
        }
        if (filter instanceof TContains) {
            checkExpressionIsProperty(e2);
            checkExpressionIsLiteral(e1);
            PropertyName propertyName = (PropertyName) e2;
            propertyName.accept(visitor, output);
            output.append(":{");
            e1.accept(visitor, output);
            output.append("}");
        }
        return output;
    }

    /*
     * Writes the SOLR query for spatial operator : BBOX, IsWithin, IsDisjointTo, IsWithin,
     * Intersects, Contains
     *
     * @param filter spatial operator to encode
     */
    private Object visitBinarySpatialOperator(BinarySpatialOperator filter, Object extraData) {
        StringWriter output = asStringWriter(extraData);
        Expression e1 = filter.getExpression1();
        checkExpressionIsProperty(e1);
        Expression e2 = filter.getExpression2();
        checkExpressionIsLiteral(e2);

        ExpressionToSolr visitor = new ExpressionToSolr();
        // let's set the feature type so property names can be encoded based on it
        visitor.setFeatureType(featureType);

        // initialize spatial strategy
        if (filter instanceof BBOX) visitor.setSpatialStrategy(SolrSpatialStrategy.BBOX);
        AttributeDescriptor spatialAtt = (AttributeDescriptor) e1.evaluate(featureType);
        if (spatialAtt != null && spatialAtt instanceof GeometryDescriptor) {
            visitor.setSpatialStrategy(
                    SolrSpatialStrategy.createStrategy((GeometryDescriptor) spatialAtt));
        } else {
            LOGGER.warning("Spatial field: " + e1.toString() + " resolved to null or non-spatial");
        }

        e1.accept(visitor, extraData);

        if (filter instanceof BBOX) {
            output.append(":\"Intersects(");
            e2.accept(visitor, extraData);
            output.append(")\"");
        } else if (filter instanceof Disjoint) {
            output.append(":\"IsDisjointTo(");
            e2.accept(visitor, extraData);
            output.append(")\"");
        } else if (filter instanceof Within) {
            output.append(":\"IsWithin(");
            e2.accept(visitor, extraData);
            output.append(")\"");
        } else if (filter instanceof Intersects) {
            output.append(":\"Intersects(");
            e2.accept(visitor, extraData);
            output.append(")\"");
        } else if (filter instanceof Contains) {
            output.append(":\"Contains(");
            e2.accept(visitor, extraData);
            output.append(")\"");
        } else {
            throw new RuntimeException("Unsupported filter type " + filter.getClass());
        }

        return output;
    }

    /*
     * Decodes a fid into its components based on a primary key.
     */
    private String decodeFID(String FID) {
        if (FID.startsWith(this.featureTypeName + ".")) {
            FID = FID.substring(this.featureTypeName.length() + 1);
        }

        try {
            FID = URLDecoder.decode(FID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return FID;
    }

    /*
     * Swap the PropertyName / Literal for binary filtering to obtain the correct order: first
     * PropertyName next Literal
     *
     * @return an array with PropertyName as first element and Literal as second element
     *
     * @throws UnsupportedOperationException if the arguments are both Literal or PropertyName
     */
    private Expression[] binaryFilterVisitorNormalizer(Expression expr1, Expression expr2) {
        Expression e1 = null;
        Expression e2 = null;
        if (expr1 instanceof PropertyName && expr2 instanceof Literal) {
            e1 = expr1;
            e2 = expr2;
        } else if (expr2 instanceof Literal && expr2 instanceof PropertyName) {
            e1 = expr2;
            e2 = expr1;
        } else {
            throw new UnsupportedOperationException(
                    "Expressions must be one PropertyName and one Literal");
        }
        return new Expression[] {e1, e2};
    }
}
