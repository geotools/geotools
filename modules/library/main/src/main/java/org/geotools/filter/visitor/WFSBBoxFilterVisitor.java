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
package org.geotools.filter.visitor;

import java.util.Iterator;
import java.util.logging.Logger;

import org.geotools.filter.AttributeExpression;
import org.geotools.filter.BetweenFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.ExpressionType;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterType;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LikeFilter;
import org.geotools.filter.LiteralExpression;
import org.geotools.filter.LogicFilter;
import org.geotools.filter.MathExpression;
import org.geotools.filter.NullFilter;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.IncludeFilter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Visit the BBOX filter elements and make sure they are valid.
 * <p>
 * Any BBOX filter using a literal geometry will be changed to be a literal envelope
 * based on the geometry internal envelope. If a max bounding box has been provided
 * it will be used to clip this request envelope.
 * <p> 
 * @author Jody
 *
 *
 * @source $URL$
 */
public class WFSBBoxFilterVisitor implements org.geotools.filter.FilterVisitor2 {
    private static final Logger logger=org.geotools.util.logging.Logging.getLogger("org.geotools.filter");                
    Envelope maxbbox;
    public WFSBBoxFilterVisitor() {
        this( null );
    }
    public WFSBBoxFilterVisitor(Envelope fsd) {
        maxbbox = fsd;
    }
    public void visit(org.geotools.filter.Filter filter) {
        if (org.geotools.filter.Filter.NONE == filter) {
            return;
        }
            switch (((org.geotools.filter.Filter)filter).getFilterType()) {
            case FilterType.BETWEEN:
                visit((BetweenFilter) filter);

                break;

            case FilterType.COMPARE_EQUALS:
            case FilterType.COMPARE_GREATER_THAN:
            case FilterType.COMPARE_GREATER_THAN_EQUAL:
            case FilterType.COMPARE_LESS_THAN:
            case FilterType.COMPARE_LESS_THAN_EQUAL:
            case FilterType.COMPARE_NOT_EQUALS:
                visit((BetweenFilter) filter);

                break;

            case FilterType.FID:
                visit((BetweenFilter) filter);

                break;

            case FilterType.GEOMETRY_BBOX:
            case FilterType.GEOMETRY_BEYOND:
            case FilterType.GEOMETRY_CONTAINS:
            case FilterType.GEOMETRY_CROSSES:
            case FilterType.GEOMETRY_DISJOINT:
            case FilterType.GEOMETRY_DWITHIN:
            case FilterType.GEOMETRY_EQUALS:
            case FilterType.GEOMETRY_INTERSECTS:
            case FilterType.GEOMETRY_OVERLAPS:
            case FilterType.GEOMETRY_TOUCHES:
            case FilterType.GEOMETRY_WITHIN:
                visit((GeometryFilter) filter);

                break;

            case FilterType.LIKE:
                visit((LikeFilter) filter);

                break;

            case FilterType.LOGIC_AND:
            case FilterType.LOGIC_NOT:
            case FilterType.LOGIC_OR:
                visit((LogicFilter) filter);

                break;

            case FilterType.NULL:
                visit((NullFilter) filter);

                break;

            default:
        }
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.BetweenFilter)
     */
    public void visit( BetweenFilter filter ) {
        if(filter!=null){
            if(filter.getLeftValue()!=null)
                    filter.getLeftValue().accept(this);
            if(filter.getRightValue()!=null)
                filter.getRightValue().accept(this);
            if(filter.getMiddleValue()!=null)
                filter.getMiddleValue().accept(this);
        }
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.CompareFilter)
     */
    public void visit( CompareFilter filter ) {
        if(filter!=null){
            if(filter.getLeftValue()!=null)
                    filter.getLeftValue().accept(this);
            if(filter.getRightValue()!=null)
                filter.getRightValue().accept(this);
        }
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.GeometryFilter)
     */
    public void visit( GeometryFilter filter ) {
        if(filter!=null){
            org.geotools.filter.Expression leftGeometry = filter.getLeftGeometry();
            org.geotools.filter.Expression rightGeometry = filter.getRightGeometry();
			switch (filter.getFilterType()) {
            
                        case FilterType.GEOMETRY_BBOX:
                            // find literal side and deal ...
                            Envelope bbox = null;
                            LiteralExpression le = null;
                            if (leftGeometry!=null && leftGeometry.getType() == ExpressionType.LITERAL_GEOMETRY) {
                                le = (LiteralExpression) leftGeometry;
                                if(le != null &&  le.getLiteral() != null && le.getLiteral() instanceof Geometry){                
                                    bbox = ((Geometry) le.getLiteral()).getEnvelopeInternal();
                                }
                            } else {
                                if (rightGeometry!=null && rightGeometry.getType() == ExpressionType.LITERAL_GEOMETRY) {
                                    le = (LiteralExpression) rightGeometry;
                                    if(le != null &&  le.getLiteral() != null && le.getLiteral() instanceof Geometry){
                                        Geometry g = (Geometry) le.getLiteral();
                                        bbox = g.getEnvelopeInternal();
                                    }
                                }
                            }
                            if(bbox!=null){
                                boolean changed = false;
                                double minx,miny,maxx,maxy;
                                minx = bbox.getMinX();
                                miny = bbox.getMinY();
                                maxx = bbox.getMaxX();
                                maxy = bbox.getMaxY();
                                if( maxbbox != null ){
                                    if(minx < maxbbox.getMinX()){
                                        minx = maxbbox.getMinX();
                                        changed = true;
                                    }
                                    if(maxx > maxbbox.getMaxX()){
                                        maxx = maxbbox.getMaxX();
                                        changed = true;
                                    }
                                    if(miny < maxbbox.getMinY()){
                                        miny = maxbbox.getMinY();
                                        changed = true;
                                    }
                                    if(maxy > maxbbox.getMaxY()){
                                        maxy = maxbbox.getMaxY();
                                        changed = true;
                                    }
                                }
                                if(changed){
                                    Envelope tmp = new Envelope(minx,maxx,miny,maxy);
                                    try {
                                        le.setLiteral((new GeometryFactory()).toGeometry(tmp));
                                    } catch (IllegalFilterException e) {
                                        logger.warning(e.toString());
                                    }
                                }
                            }
                            return;
                        case FilterType.GEOMETRY_BEYOND:
                        case FilterType.GEOMETRY_CONTAINS:
                        case FilterType.GEOMETRY_CROSSES:
                        case FilterType.GEOMETRY_DISJOINT:
                        case FilterType.GEOMETRY_DWITHIN:
                        case FilterType.GEOMETRY_EQUALS:
                        case FilterType.GEOMETRY_INTERSECTS:
                        case FilterType.GEOMETRY_OVERLAPS:
                        case FilterType.GEOMETRY_TOUCHES:
                        case FilterType.GEOMETRY_WITHIN:
                        default:
                            if(leftGeometry!=null)
                                leftGeometry.accept(this);
                            if(rightGeometry!=null)
                                rightGeometry.accept(this);
                            
            }
        }
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LikeFilter)
     */
    public void visit( LikeFilter filter ) {
        if(filter!=null){
            if(filter.getValue()!=null)
                filter.getValue().accept(this);
        }
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LogicFilter)
     */
    public void visit( LogicFilter filter ) {
        if(filter!=null){
            Iterator i = filter.getFilterIterator();
            while(i.hasNext()){
            	org.opengis.filter.Filter child = (org.opengis.filter.Filter) i.next();
            	if( child instanceof org.geotools.filter.Filter){
            		org.geotools.filter.Filter tmp = (org.geotools.filter.Filter) child;
            		tmp.accept(this);
            	}
            	else if (child instanceof IncludeFilter){
            		IncludeFilter include = (IncludeFilter) child;
            		visit( include );
            	}
            	else if (child instanceof ExcludeFilter){
            		ExcludeFilter exclude = (ExcludeFilter) child;
            		visit( exclude );
            	}
            	else {
            		logger.warning("Unnown filter:"+child);
            	}
            }
        }
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.NullFilter)
     */
    public void visit( NullFilter filter ) {
        if(filter!=null){
            if(filter.getNullCheckValue()!=null)
                filter.getNullCheckValue().accept(this);
        }
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.FidFilter)
     */
    public void visit( FidFilter filter ) {
        // do nothing
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.AttributeExpression)
     */
    public void visit( AttributeExpression expression ) {
        // do nothing
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.Expression)
     */
    public void visit( org.geotools.filter.Expression expression ) {
        // do nothing
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LiteralExpression)
     */
    public void visit( LiteralExpression expression ) {
        // do nothing
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.MathExpression)
     */
    public void visit( MathExpression expression ) {
        // do nothing
    }
    /*
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.FunctionExpression)
     */
    public void visit( FunctionExpression expression ) {
        // do nothing
    }
	public void visit(IncludeFilter filter) {
		// TODO Auto-generated method stub
		
	}
	public void visit(ExcludeFilter filter) {
		// TODO Auto-generated method stub
		
	}
}
