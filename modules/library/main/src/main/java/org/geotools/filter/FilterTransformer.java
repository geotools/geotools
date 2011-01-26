/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.geotools.gml.producer.GeometryTransformer;
import org.geotools.xml.transform.TransformerBase;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
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
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.identity.Identifier;
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
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * An XMLEncoder for Filters and Expressions.
 *
 * @source $URL$
 * @version $Id$
 * @author Ian Schneider
 *
 */
public class FilterTransformer extends TransformerBase {
    
    /** The namespace to use if none is provided. */
    private static String defaultNamespace = "http://www.opengis.net/ogc";

    /** Map of comparison types to sql representation */
    private static Map comparisions = new HashMap();
    
    /** Map of spatial types to sql representation */
    private static Map spatial = new HashMap();
    
    /** Map of logical types to sql representation */
    private static Map logical = new HashMap();
    
    /**
     * A typed convenience method for converting a Filter into XML.
     */
    public String transform(Filter f) throws TransformerException {
        return super.transform(f);
    }
    
    public org.geotools.xml.transform.Translator createTranslator(ContentHandler handler) {
        return new FilterTranslator(handler);
    }    
    public static class FilterTranslator extends TranslatorSupport implements org.opengis.filter.FilterVisitor, org.opengis.filter.expression.ExpressionVisitor {
        GeometryTransformer.GeometryTranslator geometryEncoder;
        
        public FilterTranslator(ContentHandler handler) {
            super(handler, "ogc" ,defaultNamespace);
 
            geometryEncoder = new GeometryTransformer.GeometryTranslator(handler);
            
            addNamespaceDeclarations(geometryEncoder);
        }
        
        public Object visit(ExcludeFilter filter, Object extraData) {
            // Exclude filter represents "null" when the default action is to not accept any content
            // the code calling the FilterTransformer should of checked for this case
            // and taken appropriate action            
            return extraData; // should we consider throwing an illegal state exception?
        }

        public Object visit(IncludeFilter filter, Object extraData) {
            // Include filter represents "null" when the default action is to include all content
            // the code calling the FilterTransformer should of checked for this case
            // and taken appropriate action            
            return extraData; // should we consider throwing an illegal state exception?
        }

        public Object visit(And filter, Object extraData) {
            start("And");
            for( org.opengis.filter.Filter child : filter.getChildren() ){
                child.accept( this, extraData );
            }
            end("And");
            return extraData;
        }

        public Object visit(Id filter, Object extraData) {
            Set<Identifier> fids = filter.getIdentifiers();
            for (Identifier fid : fids ) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute( null, "fid", "fid",  null, fid.toString() );
                element("FeatureId", null, atts );
            }
            return extraData;
        }

        public Object visit(Not filter, Object extraData) {
            start("Not");
            filter.getFilter().accept( this, extraData );
            end("Not");
            return extraData;
 
        }

        public Object visit(Or filter, Object extraData) {
            start("Or");
            for( org.opengis.filter.Filter child : filter.getChildren() ){
                child.accept( this, extraData );
            }
            end("Or");
            return extraData; 
        }

        public Object visit(PropertyIsBetween filter, Object extraData) {
            Expression left = (Expression) filter.getLowerBoundary();
            Expression mid = (Expression) filter.getExpression();
            Expression right = (Expression) filter.getUpperBoundary();
            
            String type = "PropertyIsBetween";
            
            start(type);
            mid.accept(this, extraData);
            start("LowerBoundary");
            left.accept(this, extraData);
            end("LowerBoundary");
            start("UpperBoundary");
            right.accept(this, extraData);
            end("UpperBoundary");
            end(type);
            
            return extraData;
        }

        public Object visit(PropertyIsEqualTo filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "PropertyIsEqualTo";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "PropertyIsNotEqualTo";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(PropertyIsGreaterThan filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "PropertyIsGreaterThan";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "PropertyIsGreaterThanOrEqualTo";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(PropertyIsLessThan filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "PropertyIsLessThan";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "PropertyIsLessThanOrEqualTo";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(PropertyIsLike filter, Object extraData) {
            String wcm = filter.getWildCard();
            String wcs = filter.getSingleChar();
            String esc = filter.getEscape();
            Expression expression = filter.getExpression();
            
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "wildCard", "wildCard", "", wcm);
            atts.addAttribute("", "singleChar", "singleChar", "", wcs);
            atts.addAttribute("", "escape", "escape", "", esc);
            
            start("PropertyIsLike",atts);

            expression.accept( this, extraData);
            
            element("Literal", filter.getLiteral() );
            
            end("PropertyIsLike");
            return extraData;
        }

        public Object visit(PropertyIsNull filter, Object extraData) {
            Expression expr = (Expression) filter.getExpression();
            
            String type = "PropertyIsNull";            
            start(type);
            expr.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(BBOX filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "BBOX";            
            start(type);
            left.accept(this,extraData);
            if( right instanceof Literal){
                Literal literal = (Literal) right;
                Envelope bbox = literal.evaluate(null, Envelope.class);                
                if( bbox != null ){
                    encode( bbox );
                }
                else {
                    right.accept(this,extraData);
                }
            }
            else {
                right.accept(this,extraData);
            }
            end(type);
            return extraData;
        }

        public Object visit(Beyond filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Beyond";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            element("Distance", String.valueOf(filter.getDistance()) );
            element("DistanceUnits", String.valueOf(filter.getDistanceUnits()) );
            end(type);
            return extraData;
        }

        public Object visit(Contains filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Contains";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(Crosses filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Crosses";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(Disjoint filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Disjoint";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(DWithin filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            final String type = "DWithin";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            element("Distance", String.valueOf(filter.getDistance()) );
            element("DistanceUnits", String.valueOf(filter.getDistanceUnits()) );
            end(type);
            return extraData;
        }

        public Object visit(Equals filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Equals";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(Intersects filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Intersects";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(Overlaps filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Overlaps";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(Touches filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Touches";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visit(Within filter, Object extraData) {
            Expression left = filter.getExpression1();
            Expression right = filter.getExpression2();
            
            final String type = "Within";
            
            start(type);
            left.accept(this,extraData);
            right.accept(this,extraData);
            end(type);
            return extraData;
        }

        public Object visitNullFilter(Object extraData) {
            // We do not have an expression? how to represent? 
            return extraData;            
        }

        public void encode(Object o) throws IllegalArgumentException {
            if (o instanceof Filter) {
                Filter filter = (Filter) o;
                filter.accept( this, null);
            }
            else if (o instanceof Expression) {
                Expression expression = (Expression) o;
                expression.accept( this, null );
            }
            else { 
                throw new IllegalArgumentException("Cannot encode " + (o == null ? "null" : o.getClass().getName())+" should be Filter or Expression");
            }
        }

        public Object visit(NilExpression expression, Object extraData) {
            // We do not have an expression? how to represent? <Literal></Literal>?
            element("Literal","");
            return extraData;
        }

        public Object visit(Add expression, Object extraData) {
            String type = "Add";
            start(type);
            expression.getExpression1().accept(this, extraData);
            expression.getExpression2().accept(this, extraData);
            end(type);
            return extraData;
        }

        public Object visit(Divide expression, Object extraData) {
            String type = "Div";
            start(type);
            expression.getExpression1().accept(this, extraData);
            expression.getExpression2().accept(this, extraData);
            end(type);
            return extraData;
        }

        public Object visit(Function expression, Object extraData) {
            String type = (String) "Function";

            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "name", "name", "", expression.getName());
            start(type,atts);

            for( org.opengis.filter.expression.Expression parameter : expression.getParameters() ){
                parameter.accept(this, extraData);
            }
            end(type);
            return extraData;
        }

        public Object visit(Literal expression, Object extraData) {
            Object value = expression.getValue();
            if( value == null ){
                element("Literal", "");
            }
            else if (value instanceof Geometry) {
                geometryEncoder.encode( (Geometry) value );
            }
            else {
                String txt = expression.evaluate(null, String.class );
                if( txt == null ){
                    txt = value.toString();
                }
                element("Literal",txt);
            }
            return extraData;
        }

        public Object visit(Multiply expression, Object extraData) {
            String type = "Mul";
            start(type);
            expression.getExpression1().accept(this, extraData);
            expression.getExpression2().accept(this, extraData);
            end(type);
            return extraData;
        }

        public Object visit(PropertyName expression, Object extraData) {
            element("PropertyName",expression.getPropertyName());
            return extraData;
        }

        public Object visit(Subtract expression, Object extraData) {
            String type = "Sub";
            start(type);
            expression.getExpression1().accept(this, extraData);
            expression.getExpression2().accept(this, extraData);
            end(type);
            return extraData;
        }
        
    }
    
}
