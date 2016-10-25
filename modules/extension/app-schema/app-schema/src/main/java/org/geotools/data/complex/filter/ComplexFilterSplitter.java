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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.NestedAttributeMapping;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor.FeatureChainLink;
import org.geotools.data.complex.filter.FeatureChainedAttributeVisitor.FeatureChainedAttributeDescriptor;
import org.geotools.data.complex.filter.XPathUtil.StepList;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.util.logging.Logging;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;

/**
 * @author Niels Charlier (Curtin University of Technology)
 *
 * @source $URL$
 */
public class ComplexFilterSplitter extends PostPreProcessFilterSplittingVisitor {

    private static final Logger LOGGER = Logging.getLogger(ComplexFilterSplitter.class);

    private int nestedAttributes = 0;

    public class CapabilitiesExpressionVisitor implements ExpressionVisitor {
        
        protected boolean capable = true;
        
        public boolean isCapable(){
            return capable;
        }

        public Object visit(NilExpression expr, Object extraData) {
            return null;
        }

        public Object visit(Add expr, Object extraData) {
            visitMathExpression(expr);
            return null;
        }        

        public Object visit(Subtract expr, Object extraData) {
            visitMathExpression(expr);
            return null;
        }

        public Object visit(Divide expr, Object extraData) {
            visitMathExpression(expr);
            return null;
        }

        public Object visit(Multiply expr, Object extraData) {
            visitMathExpression(expr);
            return null;
        }
        
        public Object visit(Function expr, Object extraData) {
            for (int i = 0; i < expr.getParameters().size(); i++) {
                ((Expression)expr.getParameters().get(i)).accept(this, null);
            }
            
            capable = capable && fcs.supports(expr.getClass());
            return null;
        }

        public Object visit(Literal expr, Object extraData) {
            return null;
        }

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
        nestedAttributes = 0;
        int i = preStack.size();
        Object data = super.visit(expression, notUsed);
        // encoding of functions with nested attributes as  arguments is not supported
        if (nestedAttributes > 0 && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
        return data;
    }

    @Override
    protected void visitBinarySpatialOperator(BinarySpatialOperator filter) {
        nestedAttributes = 0;
        int i = preStack.size();
        super.visitBinarySpatialOperator(filter);
        // encoding of binary spatial operators operating on nested attributes is not supported
        if (nestedAttributes > 0 && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
    }

    @Override
    protected Object visit(BinaryTemporalOperator filter, Object data) {
        nestedAttributes = 0;
        int i = preStack.size();
        Object ret = super.visit(filter, data);
        // encoding of temporal operators involving nested attributes is not supported
        if (nestedAttributes > 0 && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
        return ret;
    }

    @Override
    protected void visitMathExpression(BinaryExpression expression) {
        nestedAttributes = 0;
        int i = preStack.size();
        super.visitMathExpression(expression);
        // encoding of math expressions involving nested attributes is not supported
        if (nestedAttributes > 0 && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
    }

    @Override
    public Object visit(BBOX filter, Object notUsed) {
        nestedAttributes = 0;
        int i = preStack.size();
        if (filter.getExpression1() instanceof PropertyName) {
            PropertyName bboxProperty = (PropertyName)filter.getExpression1();
            if (!bboxProperty.getPropertyName().isEmpty()) {
                Object ret = this.visit(bboxProperty, notUsed);
                if (preStack.size() == i+1) {
                    preStack.pop();
                }
                // encoding bbox on nested geometry is not supported
                if (nestedAttributes > 0) {
                    postStack.push(filter);
                    return ret;
                }
            }
        }
        return super.visit(filter, notUsed);
    }

    @Override
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter) {
        nestedAttributes = 0;
        int i = preStack.size();
        super.visitBinaryComparisonOperator(filter);
        // encoding a comparison between multiple nested attributes is not supported
        if (nestedAttributes > 1 && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extradata) {
        nestedAttributes = 0;
        int i = preStack.size();
        Object ret = super.visit(filter, extradata);
        // encoding a comparison between multiple nested attributes is not supported
        if (nestedAttributes > 1 && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
        return ret;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object notUsed) {
        nestedAttributes = 0;
        int i = preStack.size();
        Object ret = super.visit(filter, notUsed);
        // encoding a comparison between multiple nested attributes is not supported
        if (nestedAttributes > 1 && preStack.size() == i + 1) {
            Object o = preStack.pop();
            postStack.push(o);
        }
        return ret;
    }

    public Object visit(PropertyName expression, Object notUsed) {
        
        // break into single steps
        StepList exprSteps = XPath.steps(mappings.getTargetFeature(), expression.getPropertyName(), this.mappings.getNamespaces());

        if (exprSteps.containsPredicate()) {
            postStack.push(expression);
            return null;
        }

        List<Expression> matchingMappings = mappings.findMappingsFor(exprSteps, false);

        if (AppSchemaDataAccessConfigurator.shouldEncodeNestedFilters()) {
            // check nested mappings
            FeatureChainedAttributeVisitor nestedAttrExtractor = new FeatureChainedAttributeVisitor(
                    mappings);
            nestedAttrExtractor.visit(expression, null);
            List<FeatureChainedAttributeDescriptor> attributes = nestedAttrExtractor
                    .getFeatureChainedAttributes();
            // encoding of filters on multiple nested attributes is not (yet) supported
            if (attributes.size() == 1) {
                FeatureChainedAttributeDescriptor nestedAttrDescr = attributes.get(0);
                if (nestedAttrDescr.chainSize() > 1 && nestedAttrDescr.isJoiningEnabled()) {
                    nestedAttributes++;

                    FeatureTypeMapping featureMapping = nestedAttrDescr.getFeatureTypeOwningAttribute();

                    // add source expressions for target attribute
                    List<Expression> nestedMappings = featureMapping.findMappingsFor(
                            nestedAttrDescr.getAttributePath(), false);
                    Iterator<Expression> it = matchingMappings.iterator();
                    while (it.hasNext()) {
                        if (it.next() == null) {
                            // necessary to enable encoding of nested filters when joining simple content
                            it.remove();
                        }
                    }
                    matchingMappings.addAll(nestedMappings);

                    // add source expressions for mappings used in join conditions, as they too must be encoded
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
                                        + "will be disabled: " + e.getMessage());
                                postStack.push(expression);
                                return null;
                            }

                            Expression nestedExpr = nestedAttr.getMapping(nestedFeature).getSourceExpression();
                            matchingMappings.add(nestedExpr);
                        }
                    }
                }
            }
        }

        if (matchingMappings.isEmpty()) {
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

}
