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
package org.geotools.renderer.shape;

import java.util.Stack;

import org.geotools.filter.visitor.AbstractFilterVisitor;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Visits a filter and extracts the minimum bounds that the filter requires.
 * 
 * @author jones
 *
 *
 * @source $URL$
 */
public class BoundsExtractor extends AbstractFilterVisitor{
    private Stack/*<Envelope>*/ envelopeStack=new Stack/*<Envelope>*/();

    private final Envelope original;

    private Envelope notEnvelope;

    private final static GeometryFactory factory=new GeometryFactory();

    public BoundsExtractor(Envelope bbox) {
        original=bbox;
    }

    public BoundsExtractor(int minx, int maxx, int miny, int maxy) {
        this(new Envelope(minx, maxx, miny, maxy));
    }

    /**
     *  @return the intersecton of the new bbox and the original
     */ 
    public Envelope getIntersection() {
        Envelope bbox=null;
        if( !envelopeStack.isEmpty())
            bbox=(Envelope) envelopeStack.peek();
        if( original==null ){
            return bbox==null?new Envelope():bbox;
        }
        if( bbox!=null )
            return bbox.intersection(original);
        if( notEnvelope!=null ){
            return intersectionWithNotEnvelope(original);
        }
        return original;
    }

    /**
     *  @return the intersecton of the new bbox and the original
     */ 
    public Envelope getFilterEnvelope() {
        if( envelopeStack.isEmpty() )
            return new Envelope();
        return (Envelope) envelopeStack.peek();
    }
    
    public Envelope getNotEnvelope() {
        return notEnvelope==null?new Envelope():notEnvelope;
    }
//    /*
//     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LogicFilter)
//     */
//    public void visit(LogicFilter filter) {
//
//        if (filter != null) {
//            switch (filter.getFilterType()) {
//            case FilterType.LOGIC_OR: {
//                Iterator i = filter.getFilterIterator();
//                while (i.hasNext()) {
//                    Filter tmp = (Filter) i.next();
//                    tmp.accept(this);
//                }
//                Envelope bbox=new Envelope();
//                while( !envelopeStack.isEmpty() ){
//                    Envelope env = (Envelope) envelopeStack.pop();
//                    bbox.expandToInclude(env);
//                }
//                if( notEnvelope!=null ){
//                    if( bbox.contains(notEnvelope) ){
//                        // or contains all of notEnvelope so notEnvelope is meaningless
//                        notEnvelope=null;
//                    }else{
//                        // lets err on the side of caution and we can safely ignore the or... This will
//                        // be a little big but that's ok.
//                        bbox=new Envelope();
//                    }
//                }
//
//                if( !bbox.isNull() )
//                    envelopeStack.push(bbox);
//                break;
//            }
//            case FilterType.LOGIC_AND: {
//                Iterator i = filter.getFilterIterator();
//                while (i.hasNext()) {
//                    Filter tmp = (Filter) i.next();
//                    tmp.accept(this);
//                }
//                if( !envelopeStack.isEmpty() ){
//                    Envelope bbox = null;
//                    while( !envelopeStack.isEmpty() ){
//                        Envelope env = (Envelope) envelopeStack.pop();
//                        if( bbox==null ){
//                            bbox=env;
//                        }else{
//                            bbox=bbox.intersection(env);
//                        }
//                    }
//                    if( notEnvelope!=null && bbox!=null){
//                        if( notEnvelope.contains(bbox) ){
//                            // this mean that nothing valid since we are ANDING
//                            // and area with an area that is guaranteed to be empty
//                            // Erring on the side of caution for now
//                            notEnvelope=bbox;
//                            bbox=null;
//                        }else{
//                            bbox = intersectionWithNotEnvelope(bbox);
//                            notEnvelope=null;
//                        }
//                    }
//                    if( bbox!=null && !bbox.isNull() )
//                        envelopeStack.push(bbox);
//                }
//                
//                break;
//            }
//            case FilterType.LOGIC_NOT:
//                Iterator i = filter.getFilterIterator();
//                Filter tmp = (Filter) i.next();
//                tmp.accept(this);
//                if( !envelopeStack.isEmpty() ){
//                    notEnvelope=(Envelope) envelopeStack.pop();
//                    assert envelopeStack.isEmpty();
//                }else if( notEnvelope!=null || !notEnvelope.isNull()){
//                    envelopeStack.push(notEnvelope);
//                    notEnvelope=null;
//                }
//            default:
//                break;
//            }
//
//        }
//    }
//
    private Envelope intersectionWithNotEnvelope(Envelope bbox) {
        Geometry notGeom = factory.toGeometry(notEnvelope);
        Geometry andGeom = factory.toGeometry(bbox);
        
        Envelope envelopeInternal = andGeom.difference(notGeom).getEnvelopeInternal();
        bbox = envelopeInternal;
        return bbox;
    }
//    
//    public void visit(LiteralExpression expression) {
//        Object literal = expression.getLiteral();
//        if (literal instanceof Geometry) {
//            Geometry geom = (Geometry) literal;
//            envelopeStack.push(geom.getEnvelopeInternal());
//        }
//    }
}
