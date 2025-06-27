/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import static org.geotools.data.complex.util.ComplexFeatureConstants.DEFAULT_GEOMETRY_LOCAL_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.BinaryExpression;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.expression.FeaturePropertyAccessorFactory;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor.FeatureChainLink;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor.FeatureChainedAttributeDescriptor;
import org.geotools.data.complex.util.XPathUtil.Step;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.PropertyAccessorFactory;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/** @author Niels Charlier (Curtin University of Technology) */
public class ComplexFilterSplitter extends PostPreProcessFilterSplittingVisitor {

    private static final Logger LOGGER = Logging.getLogger(ComplexFilterSplitter.class);

    List<FeatureChainedAttributeDescriptor> nestedAttributes = new ArrayList<>();

    public class CapabilitiesExpressionVisitor implements ExpressionVisitor {

        protected boolean capable = true;

        public boolean isCapable() {
            return capable;
        }

        @Override
        public Object visit(NilExpression expr, Object extraData) {
            return null;
        }

        @Override
        public Object visit(Add expr, Object extraData) {
            visitMathExpression(expr);
            return null;
        }

        @Override
        public Object visit(Subtract expr, Object extraData) {
            visitMathExpression(expr);
            return null;
        }

        @Override
        public Object visit(Divide expr, Object extraData) {
            visitMathExpression(expr);
            return null;
        }

        @Override
        public Object visit(Multiply expr, Object extraData) {
            visitMathExpression(expr);
            return null;
        }

        @Override
        public Object visit(Function expr, Object extraData) {
            for (int i = 0; i < expr.getParameters().size(); i++) {
                expr.getParameters().get(i).accept(this, null);
            }

            capable = capable && fcs.supports(expr.getClass());
            return null;
        }

        @Override
        public Object visit(Literal expr, Object extraData) {
            return null;
        }

        @Override
        public Object visit(PropertyName expr, Object extraData) {
            return null;
        }

        private void visitMathExpression(BinaryExpression expression) {
            expression.getExpression1().accept(this, null);
            expression.getExpression2().accept(this, null);

            capable = capable && fcs.supports(expression.getClass());
        }
    }

    private FeatureTypeMapping mappings;

    public ComplexFilterSplitter(FilterCapabilities fcs, FeatureTypeMapping mappings) {
        super(fcs, null, null);
        this.mappings = mappings;
    }

    @Override
    public Object visit(Id filter, Object notUsed) {
        CapabilitiesExpressionVisitor visitor = new CapabilitiesExpressionVisitor();
        mappings.getFeatureIdExpression().accept(visitor, null);

        if (visitor.isCapable()) {
            super.visit(filter, notUsed);
        } else {
            postStack.push(filter);
        }

        return null;
    }

    @Override
    public Object visit(Function expression, Object notUsed) {
        nestedAttributes.clear();
        int i = preStack.size();
        Object data = super.visit(expression, notUsed);
        // encoding of functions with nested attributes as  arguments is not supported
        if (!nestedAttributes.isEmpty() && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
        return data;
    }

    @Override
    protected Object visit(BinaryTemporalOperator filter, Object data) {
        nestedAttributes.clear();
        int i = preStack.size();
        Object ret = super.visit(filter, data);
        // encoding of temporal operators involving nested attributes is not supported
        if (!nestedAttributes.isEmpty() && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
        return ret;
    }

    @Override
    protected void visitMathExpression(BinaryExpression expression) {
        nestedAttributes.clear();
        int i = preStack.size();
        super.visitMathExpression(expression);
        // encoding of math expressions involving nested attributes is not supported
        if (!nestedAttributes.isEmpty() && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
    }

    @Override
    protected void visitBinarySpatialOperator(BinarySpatialOperator filter) {
        nestedAttributes.clear();
        int i = preStack.size();
        super.visitBinarySpatialOperator(filter);
        if (preStack.size() == i + 1) {
            if (nestedAttributes.size() == 1) {
                nestedAttributeSanityCheck(filter);
            } else if (nestedAttributes.size() > 1) {
                // encoding a spatial comparison between multiple nested attributes is not
                // supported)
                Object o = preStack.pop();
                postStack.push(o);
            }
        }
    }

    @Override
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter) {
        nestedAttributes.clear();
        int i = preStack.size();
        super.visitBinaryComparisonOperator(filter);
        if (preStack.size() == i + 1) {
            if (nestedAttributes.size() == 1) {
                nestedAttributeSanityCheck(filter);
            } else if (nestedAttributes.size() > 1) {
                // encoding a comparison between multiple nested attributes is not supported
                Object o = preStack.pop();
                postStack.push(o);
            }
        }
    }

    @Override
    public Object visit(BBOX filter, Object notUsed) {
        nestedAttributes.clear();
        int i = preStack.size();
        if (filter.getExpression1() instanceof PropertyName) {
            PropertyName bboxProperty = (PropertyName) filter.getExpression1();
            if (!bboxProperty.getPropertyName().isEmpty()) {
                Object ret = this.visit(bboxProperty, notUsed);
                if (preStack.size() == i + 1) {
                    preStack.pop();
                }
                if (nestedAttributes.size() == 1) {
                    nestedAttributeSanityCheck(filter);
                } else if (nestedAttributes.size() > 1) {
                    // encoding bbox on multiple nested attributes is not supported
                    postStack.push(filter);
                    return ret;
                }
            }
        }
        return super.visit(filter, notUsed);
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extradata) {
        nestedAttributes.clear();
        int i = preStack.size();
        Object ret = super.visit(filter, extradata);
        if (preStack.size() == i + 1) {
            if (nestedAttributes.size() == 1) {
                nestedAttributeSanityCheck(filter);
            } else if (nestedAttributes.size() > 1) {
                // encoding a comparison between multiple nested attributes is not supported
                Object o = preStack.pop();
                postStack.push(o);
            }
        }
        return ret;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object notUsed) {
        nestedAttributes.clear();
        int i = preStack.size();
        Object ret = super.visit(filter, notUsed);
        if (preStack.size() == i + 1) {
            if (nestedAttributes.size() == 1) {
                nestedAttributeSanityCheck(filter);
            } else if (nestedAttributes.size() > 1) {
                // encoding a comparison between multiple nested attributes is not supported
                Object o = preStack.pop();
                postStack.push(o);
            }
        }
        return ret;
    }

    @Override
    public Object visit(PropertyName expression, Object notUsed) {

        // replace the artificial DEFAULT_GEOMETRY property with the actual one
        if (DEFAULT_GEOMETRY_LOCAL_NAME.equals(expression.getPropertyName())) {
            String defGeomPath = mappings.getDefaultGeometryXPath();
            FilterFactory ff = new FilterFactoryImplNamespaceAware(mappings.getNamespaces());
            expression = ff.property(defGeomPath);
        }

        // break into single steps
        StepList exprSteps =
                XPath.steps(mappings.getTargetFeature(), expression.getPropertyName(), this.mappings.getNamespaces());

        if (exprSteps.containsPredicate()) {
            postStack.push(expression);
            return null;
        }

        List<Expression> matchingMappings = mappings.findMappingsFor(exprSteps, false);

        if (AppSchemaDataAccessConfigurator.shouldEncodeNestedFilters()) {
            // check nested mappings
            FeatureChainedAttributeVisitor nestedAttrExtractor = new FeatureChainedAttributeVisitor(mappings);
            nestedAttrExtractor.visit(expression, null);
            // check expression exists
            FeatureChainedAttributeVisitor existsAttrExtractor = existsExtractorVisitor();
            existsAttrExtractor.visit(expression, null);

            List<FeatureChainedAttributeDescriptor> fcAttrs = nestedAttrExtractor.getFeatureChainedAttributes();
            // error on attribute check
            checkAttributeFound(expression, exprSteps, nestedAttrExtractor, existsAttrExtractor, fcAttrs);
            // encoding of filters on multiple nested attributes is not (yet) supported
            if (fcAttrs.size() == 1 || !fcAttrs.isEmpty() && validateNoClientProperties(fcAttrs)) {
                FeatureChainedAttributeDescriptor nestedAttrDescr = fcAttrs.get(0);
                if (nestedAttrDescr.chainSize() > 1 && nestedAttrDescr.isJoiningEnabled()) {
                    FeatureTypeMapping featureMapping = nestedAttrDescr.getFeatureTypeOwningAttribute();
                    nestedAttributes.add(nestedAttrDescr);

                    // add source expressions for target attribute
                    List<Expression> nestedMappings =
                            featureMapping.findMappingsFor(nestedAttrDescr.getAttributePath(), false);
                    Iterator<Expression> it = matchingMappings.iterator();
                    while (it.hasNext()) {
                        if (it.next() == null) {
                            // necessary to enable encoding of nested filters when joining simple
                            // content
                            it.remove();
                        }
                    }
                    matchingMappings.addAll(nestedMappings);

                    // add source expressions for mappings used in join conditions, as they too must
                    // be encoded
                    for (int i = nestedAttrDescr.chainSize() - 2; i > 0; i--) {
                        FeatureChainLink mappingStep = nestedAttrDescr.getLink(i);
                        if (mappingStep.hasNestedFeature()) {
                            FeatureChainLink parentStep = nestedAttrDescr.getLink(i);

                            NestedAttributeMapping nestedAttr = parentStep.getNestedFeatureAttribute();
                            FeatureTypeMapping nestedFeature = null;
                            try {
                                nestedFeature = nestedAttr.getFeatureTypeMapping(null);
                            } catch (IOException e) {
                                LOGGER.warning("Exception occurred processing nested filter, encoding"
                                        + "will be disabled: "
                                        + e.getMessage());
                                postStack.push(expression);
                                return null;
                            }

                            Expression nestedExpr =
                                    nestedAttr.getMapping(nestedFeature).getSourceExpression();
                            matchingMappings.add(nestedExpr);
                        }
                    }
                }
            } else {
                // we may have a direct match, so lets push this filter to the post stack right away
                postStack.push(expression);
                return null;
            }
        }

        if (matchingMappings.isEmpty()) {
            // handle multi value
            AttributeMapping candidate = mappings.getAttributeMapping(exprSteps);
            if (candidate != null && candidate.isMultiValued() && candidate.getMultipleValue() != null) {
                return super.visit(expression, notUsed);
            }
            postStack.push(expression);
            return null;
        } else {
            for (Expression expr : matchingMappings) {
                if (expr == null) {
                    // this is joining for simple content
                    // has to go to post stack because it comes from another table
                    postStack.push(expression);
                    return null;
                } else {
                    CapabilitiesExpressionVisitor visitor = new CapabilitiesExpressionVisitor();
                    expr.accept(visitor, null);
                    if (!visitor.isCapable()) {
                        postStack.push(expression);
                        return null;
                    }
                }
            }
        }

        return super.visit(expression, notUsed);
    }

    private boolean validateNoClientProperties(List<FeatureChainedAttributeDescriptor> fcAttrs) {
        for (FeatureChainedAttributeDescriptor ad : fcAttrs) {
            if (ad.getFeatureChain() == null) continue;
            for (FeatureChainLink clink : ad.getFeatureChain()) {
                if (clink.getNestedFeatureAttribute() == null) continue;
                if (clink.getNestedFeatureAttribute().getClientProperties() != null
                        && !clink.getNestedFeatureAttribute()
                                .getClientProperties()
                                .isEmpty()) return false;
            }
        }
        return true;
    }

    /** Attribute error check */
    protected void checkAttributeFound(
            PropertyName expression,
            StepList exprSteps,
            FeatureChainedAttributeVisitor nestedAttrExtractor,
            FeatureChainedAttributeVisitor existsAttrExtractor,
            List<FeatureChainedAttributeDescriptor> fcAttrs) {
        if (fcAttrs.isEmpty()
                && !nestedAttrExtractor.conditionalMappingWasFound()
                && !isXlinkHRef(exprSteps)
                && !existsAttrExtractor.isUnboundedNestedElementFound()
                && existsAttrExtractor.getFeatureChainedAttributes().isEmpty()) {
            throw new IllegalArgumentException(String.format(
                    "Attribute \"%s\" not found in type \"%s\"",
                    expression, mappings.getTargetFeature().getName().toString()));
        }
    }

    protected boolean isXlinkHRef(StepList exprSteps) {
        return FeatureChainedAttributeVisitor.isXlinkHref(exprSteps);
    }

    private FeatureChainedAttributeVisitor existsExtractorVisitor() {
        return new FeatureChainedAttributeVisitor(mappings) {
            @Override
            protected boolean startsWith(StepList one, StepList other) {
                if (other.size() > one.size()) {
                    return false;
                }
                boolean result = true;
                for (int i = 0; i < other.size(); i++) {
                    Step thisStep = one.get(i);
                    Step otherStep = other.get(i);
                    if (thisStep.isIndexed() && otherStep.isIndexed()) {
                        result = result && thisStep.equals(otherStep);
                    } else {
                        result = result && thisStep.equalsIgnoreIndex(otherStep);
                    }
                }
                return result;
            }
        };
    }

    private void nestedAttributeSanityCheck(Filter filter) {
        if (nestedAttributes != null) {
            for (FeatureChainedAttributeDescriptor descr : nestedAttributes) {
                FeatureTypeMapping ownerType = descr.getFeatureTypeOwningAttribute();
                StepList attrPath = descr.getAttributePath();
                StepList xpathSteps = attrPath.clone();
                if (Types.isSimpleContentType(ownerType.getTargetFeature().getType())) {
                    // if chaining a simple content type, XPath expression may point directly to the
                    // type,
                    // and not to one of its attributes: if this is the case, the XPath expression
                    // should
                    // be tested against the container type, i.e. the previous link in the chain
                    String ownerTypeName =
                            Types.toPrefixedName(ownerType.getTargetFeature().getName(), ownerType.getNamespaces());
                    boolean chainingSimpleType =
                            ownerTypeName.equals(descr.getAttributePath().toString());
                    FeatureChainLink lastLink = descr.getLastLink();
                    if (chainingSimpleType && lastLink.previous() != null) {
                        ownerType = lastLink.previous().getFeatureTypeMapping();
                        StepList prevXPathSteps =
                                lastLink.previous().getNestedFeatureAttribute().getTargetXPath();
                        Step prevLastStep = prevXPathSteps.get(prevXPathSteps.size() - 1);
                        if (!prevLastStep.equalsIgnoreIndex(attrPath.get(0))) {
                            xpathSteps = prevXPathSteps.clone();
                            xpathSteps.add(attrPath.get(0));
                        }
                    } else {
                        LOGGER.warning(String.format(
                                "Cound not run sanity check for nested attribute \"%s\" of type \"%s\"",
                                descr.getAttributePath(),
                                ownerType.getTargetFeature().getName()));
                    }
                }
                xpathSteps = removeIndexesAndPredicates(xpathSteps);
                if (isXlinkHRef(xpathSteps) || FeatureChainedAttributeVisitor.isFid(xpathSteps)) {
                    // if XPath expression points to xlink:href or to a FID, the only reliable thing
                    // to is to check the existence of its parent attribute
                    xpathSteps.remove(xpathSteps.size() - 1);
                }
                if (!xpathSteps.isEmpty()) {
                    Class<?> expectedType = determineExpectedType(filter, xpathSteps);
                    checkPropetyExistenceAndType(ownerType, xpathSteps.toString(), expectedType);
                }
            }
        }
    }

    private StepList removeIndexesAndPredicates(StepList steps) {
        StepList newSteps = steps.clone();
        newSteps.clear();

        for (Step step : steps) {
            newSteps.add(new Step(step.getName(), step.isXmlAttribute(), null));
        }

        return newSteps;
    }

    private Class<?> determineExpectedType(Filter filter, StepList xpath) {
        Class<?> expectedType = AttributeDescriptor.class;

        if (filter instanceof BinarySpatialOperator) {
            expectedType = GeometryDescriptor.class;
        } else {
            if (!xpath.isEmpty()) {
                boolean isXmlAttr = xpath.get(xpath.size() - 1).isXmlAttribute();
                expectedType = isXmlAttr ? Name.class : AttributeDescriptor.class;
            }
        }

        return expectedType;
    }

    private void checkPropetyExistenceAndType(FeatureTypeMapping mapping, String xpath, Class<?> expectedType) {
        FeatureType featureType = (FeatureType) mapping.getTargetFeature().getType();
        FeaturePropertyAccessorFactory accessorFactory = new FeaturePropertyAccessorFactory();
        Hints hints = new Hints(PropertyAccessorFactory.NAMESPACE_CONTEXT, mapping.getNamespaces());
        PropertyAccessor accessor =
                accessorFactory.createPropertyAccessor(featureType.getClass(), xpath, Object.class, hints);
        if (accessor != null) {
            Object descr = null;
            try {
                descr = accessor.get(featureType, xpath, Object.class);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        String.format(
                                "Attribute \"%s\" not found in type \"%s\"",
                                xpath, mappings.getTargetFeature().getName().toString()),
                        e);
            }
            if (!expectedType.isAssignableFrom(descr.getClass())) {
                throw new IllegalArgumentException(String.format(
                        "Attribute descriptor for \"%s\" if of type \"%s\", but it should be of type \"%s\"",
                        xpath, descr.getClass().getName(), expectedType.getName()));
            }
        }
    }
}
