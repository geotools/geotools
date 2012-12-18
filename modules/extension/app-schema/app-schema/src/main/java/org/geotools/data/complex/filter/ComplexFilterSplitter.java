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

import java.util.List;

import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.filter.XPathUtil.StepList;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.opengis.filter.Id;
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

/**
 * @author Niels Charlier (Curtin University of Technology)
 *
 * @source $URL$
 */
public class ComplexFilterSplitter extends PostPreProcessFilterSplittingVisitor {
    
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
    
    public Object visit(PropertyName expression, Object notUsed) {
        
        // break into single steps
        StepList exprSteps = XPath.steps(mappings.getTargetFeature(), expression.getPropertyName(), this.mappings.getNamespaces());

        if (exprSteps.containsPredicate()) {
            postStack.push(expression);
            return null;
        }
        
        List<Expression> matchingMappings = mappings.findMappingsFor(exprSteps);

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
