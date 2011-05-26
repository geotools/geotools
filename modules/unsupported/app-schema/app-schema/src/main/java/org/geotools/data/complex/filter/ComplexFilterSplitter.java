package org.geotools.data.complex.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.data.complex.filter.XPath.Step;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.Types;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.IsNullImpl;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.opengis.feature.type.Name;
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
        StepList exprSteps = XPath.steps(mappings.getTargetFeature(), expression.getPropertyName(), expression.getNamespaceContext());

        if (exprSteps.size() > 1) {
            List<Expression> matchingMappings = mappings.findMappingsFor(exprSteps);
    
            if (matchingMappings.isEmpty()) {
                postStack.push(expression);
                return null;
            } else {
                for (Expression expr : matchingMappings) {
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
