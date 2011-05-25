/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.util;

import java.util.Iterator;
import java.util.Stack;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.And;
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
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
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

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/** The purpose of this class is to split any Filter into two filters :
 *  <ol><ul> a SpatialRestriction
 *      <ul> and an OtherAttributesRestriction
 *  <ol>
 *  so we have :
 *  OriginalFilter = SpatialRestriction && OtherAttributeRestriction
 *
 *  SpatialRestriction may actually be a rough approximation of OtherAttributeRestriction
 *
 * @author Christophe Rousson, SoC 2007, CRG-ULAVAL
 *
 *
 *
 * @source $URL$
 */
public class BBoxFilterSplitter implements FilterVisitor {
    private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory2(null);
    private static final Envelope UNIVERSE_ENVELOPE = new Envelope(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    private static final Envelope EMPTY_ENVELOPE = new Envelope();
    
    //envelopes that can be used to limit the 
    //bounds of the data collected; if empty then equivalent to excludes filter
    private Stack<Envelope> envelopes = new Stack<Envelope>();
    
    private Stack<Filter> otherRestrictions = new Stack<Filter>();
    private String geom = null;
    private String srs = null;

    //private Stack notEnvelopes = new Stack() ;
    public Object visit(ExcludeFilter f, Object arg1) {
        envelopes.push(new Envelope(EMPTY_ENVELOPE));
        return null;
    }

    public Object visit(IncludeFilter f, Object arg1) {
        envelopes.push(new Envelope(UNIVERSE_ENVELOPE));
        return null;
    }
    
    public Object visit(Id f, Object arg1) {
        otherRestrictions.push(f);
        return null;
    }

    public Object visit(Not f, Object arg1) {
        //visit child
        f.getFilter().accept(this, arg1);
        
        //deal with envelope
        if (envelopes.size() > 0){
            //universe - envelope = universe 
            envelopes.pop();
            envelopes.push(new Envelope(UNIVERSE_ENVELOPE));
        }
        otherRestrictions.push(f);
        return null;
    }
    
    public Object visit(And f, Object arg1) {
        int envSize = envelopes.size();
        int othSize = otherRestrictions.size();

        for (Iterator<Filter> it = f.getChildren().iterator(); it.hasNext();) {
            Filter child = (Filter) it.next();
            child.accept(this, arg1);
        }

        if (envelopes.size() >= (envSize + 2)) {
            Envelope e = (Envelope) envelopes.pop();
            for (int i = envelopes.size(); i > envSize; i--) {
                Envelope curr = (Envelope) envelopes.pop();
                if (curr.equals(EMPTY_ENVELOPE) || e.equals(EMPTY_ENVELOPE)){
                    e = new Envelope(EMPTY_ENVELOPE);
                }else if (curr.equals(UNIVERSE_ENVELOPE)){
                    //do nothing leave e alone
                    //universe & envelope = enevelope
                }else if (e.equals(UNIVERSE_ENVELOPE )){
                    e = curr;
                }else{
                    //must expand to include instead of intersects
                    //because two bounding boxes may be disjoint
                    //but a geometry may still intersect both of the
                    //bounding boxes
                    e.expandToInclude(curr);
                }
            }
            envelopes.push(e);
        }

        // in all case, we'll need original filter as computed SpatialRestriction is a rough approximation       
        multiplePop(otherRestrictions, othSize);
        Envelope top = envelopes.peek();
        if (!(top.equals(EMPTY_ENVELOPE))){
        	otherRestrictions.push(f);
        }

        return null;
    }
    
    public Object visit(Or f, Object arg1) {
        int envSize = envelopes.size();
        int othSize = otherRestrictions.size();

        for (Iterator<Filter> it = f.getChildren().iterator(); it.hasNext();) {
            Filter child = (Filter) it.next();
            child.accept(this, arg1);
        }

        if (envelopes.size() > (envSize + 1)) {
            Envelope e = (Envelope) envelopes.pop();

            for (int i = envelopes.size(); i > envSize; i--) {
                e.expandToInclude((Envelope) envelopes.pop());
            }

            envelopes.push(e);
        } else if (envelopes.size() == (envSize + 1)) {
            // the trick is we cannot separate this filter in the form of SpatialRestriction && OtherRestriction
            // so we add this part to OtherRestriction
            envelopes.pop();
            envelopes.push(new Envelope(UNIVERSE_ENVELOPE));
        }

        // in all case, we'll need original filter as computed SpatialRestriction is a rough approximation
        int size = otherRestrictions.size();
        multiplePop(otherRestrictions, othSize);
        if (size > othSize){
            otherRestrictions.push(f);
        }

        return null;
    }

    public Object visit(PropertyIsBetween f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(PropertyIsEqualTo f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(PropertyIsNotEqualTo f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(PropertyIsGreaterThan f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(PropertyIsGreaterThanOrEqualTo f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(PropertyIsLessThan f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(PropertyIsLessThanOrEqualTo f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(PropertyIsLike f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(PropertyIsNull f, Object arg1) {
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(BBOX f, Object arg1) {
        if (geom == null) {
            if (f.getExpression1() instanceof PropertyName){
                geom = ((PropertyName)f.getExpression1()).getPropertyName();
            }
            srs = f.getSRS();
                        
        }else{
            String newgeom = f.getExpression1() instanceof PropertyName ? ((PropertyName)f.getExpression1()).getPropertyName() : null;
            String newsrs = f.getSRS();
        
            if ((geom != newgeom) ||  srs != srs  ) {
                throw new UnsupportedOperationException(
                    "This splitter can not be used against a filter where different BBOX filters refer to different Geometry attributes.");
            }
        }
       
        Envelope e = new Envelope(f.getMinX(), f.getMaxX(), f.getMinY(), f.getMaxY());
        envelopes.push(e);
        
        return null;
    }

    public Object visit(Beyond f, Object arg1) {
        // we don't know how to handle this geometric restriction as a BBox
        // so we treat this as an attribute filter
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(Contains f, Object arg1) {
        // we don't know how to handle this geometric restriction as a BBox
        // so we treat this as an attribute filter
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(Crosses f, Object arg1) {
        // we don't know how to handle this geometric restriction as a BBox
        // so we treat this as an attribute filter
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(Disjoint f, Object arg1) {
        // we don't know how to handle this geometric restriction as a BBox
        // so we treat this as an attribute filter
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(DWithin f, Object arg1) {
        // we don't know how to handle this geometric restriction as a BBox
        // so we treat this as an attribute filter
        otherRestrictions.push(f);

        return null;
    }

    public Object visit(Equals f, Object arg1) {
        //       we don't know how to handle this geometric restriction as a BBox
        // so we treat this as an attribute filter
        otherRestrictions.push(f);

        return null;
    }

    protected void traverse(BinarySpatialOperator f) {
        if (f.getExpression1() instanceof Literal) {
            Literal l = (Literal) f.getExpression1();
            Geometry g = (Geometry) l.getValue();
            envelopes.push(g.getEnvelopeInternal());
        } else if (f.getExpression2() instanceof Literal) {
            Literal l = (Literal) f.getExpression2();
            Geometry g = (Geometry) l.getValue();
            envelopes.push(g.getEnvelopeInternal());
        }
        
        if (f.getExpression1() instanceof PropertyName){
            geom = ((PropertyName)f.getExpression1()).getPropertyName();
        }else if (f.getExpression2() instanceof PropertyName){
            geom = ((PropertyName)f.getExpression2()).getPropertyName();
        }
        
        otherRestrictions.push(f);
    }

    public Object visit(Intersects f, Object arg1) {
        traverse(f);

        return null;
    }

    public Object visit(Overlaps f, Object arg1) {
        traverse(f);

        return null;
    }

    public Object visit(Touches f, Object arg1) {
        traverse(f);

        return null;
    }

    public Object visit(Within f, Object arg1) {
        traverse(f);

        return null;
    }

    public Object visitNullFilter(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Envelope getEnvelope() {
        assert (envelopes.size() < 2);

        if (envelopes.isEmpty()) {
            return null;
        } else {
            return (Envelope) envelopes.peek();
        }
    }

    /** Return the bbox part of original filter :
     *  filter == (1) AND (2), where
     *  (1) = BBOXImpl
     *  (2) = other filter
     *
     * @return filter part (1)
     */
    public Filter getFilterPre() {
        Envelope e = getEnvelope();

        if (e == null || e.isNull()) {
            return Filter.EXCLUDE;
            //return Filter.INCLUDE;
        } else if (e.equals(UNIVERSE_ENVELOPE)){
            return Filter.INCLUDE;
        } else {
            Filter myfilter = filterFactory.bbox(geom, e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY(), srs);
            return myfilter;
        }
    }

    /** Return the non bbox part (2) of original filter :
     *  filter == (1) AND (2), where
     *  (1) = BBOXImpl
     *  (2) = other filter
     *
     * @return filter part (2)
     */
    public Filter getFilterPost() {
        if (otherRestrictions.isEmpty()) {
            return Filter.INCLUDE;
        } else if (otherRestrictions.size() == 1) {
            return (Filter) otherRestrictions.peek();
        } else {
            return filterFactory.and(otherRestrictions.subList(0,
                    otherRestrictions.size() - 1));
        }
    }

    private void multiplePop(Stack<Filter> s, int downsize) {
        for (int i = s.size(); i > downsize; i--) {
            s.pop();
        }
    }
}
