/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
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
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.SpatialOperator;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

/**
 * Wraps an instanceof of {@link org.geotools.filter.FilterVisitor} in an 
 * {@link org.opengis.filter.FilterVisitorWrapper}.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 * @source $URL$
 */
public class FilterVisitorFilterWrapper implements FilterVisitor {

	org.geotools.filter.FilterVisitor delegate;
	
	public FilterVisitorFilterWrapper(org.geotools.filter.FilterVisitor delegate) {
		this.delegate = delegate;
	}
	    
	protected void visitLogicFilter(org.opengis.filter.Filter filter) {
		if (filter instanceof LogicFilter) {
			delegate.visit((LogicFilter)filter);
		}
	}
	
	protected void visitCompareFilter(org.opengis.filter.Filter filter) {
		if (filter instanceof BetweenFilter) {
			delegate.visit((BetweenFilter)filter);
			return;
		}
		
		if (filter instanceof NullFilter) {
			delegate.visit((NullFilter)filter);
			return;
		}
		
		if (filter instanceof LikeFilter) {
			delegate.visit((LikeFilter)filter);
		}
	
			
		if (filter instanceof CompareFilter) {
			delegate.visit((CompareFilter)filter);
		}
	}
	
	protected void visitGeometryFilter(SpatialOperator filter) {
		if (filter instanceof GeometryFilter) {
			delegate.visit((GeometryFilter)filter);
		}
	}
	
	public Object visit(And filter, Object extraData) {
		visitLogicFilter(filter);
		return extraData;
	}

	public Object visit( Id filter, Object extraData) {
		if (filter instanceof FidFilter) {
			delegate.visit((FidFilter)filter);
		}
		
		return extraData;
	}

    public Object visitNullFilter( Object extraData) {        
        return extraData;
    }
    public Object visit( IncludeFilter filter, Object extraData) {
        if(delegate instanceof FilterVisitor2)
            ((FilterVisitor2) delegate).visit(filter);
        return extraData;
    }
    public Object visit( ExcludeFilter filter, Object extraData) {   
        if(delegate instanceof FilterVisitor2)
            ((FilterVisitor2) delegate).visit(filter);
        return extraData;
    }
	public Object visit(Not filter, Object extraData) {
		visitLogicFilter(filter);
		return extraData;
	}

	public Object visit(Or filter, Object extraData) {
		visitLogicFilter(filter);
		return extraData;
	}

	public Object visit(PropertyIsBetween filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}

	public Object visit(PropertyIsEqualTo filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}

	public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}
	
	public Object visit(PropertyIsGreaterThan filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}

	public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}

	public Object visit(PropertyIsLessThan filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}

	public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}

	public Object visit(PropertyIsLike filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}

	public Object visit(PropertyIsNull filter, Object extraData) {
		visitCompareFilter(filter);
		return extraData;
	}

	public Object visit(BBOX filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Beyond filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Contains filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Crosses filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Disjoint filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(DWithin filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Equals filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Intersects filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Overlaps filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Touches filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}

	public Object visit(Within filter, Object extraData) {
		visitGeometryFilter(filter);
		return extraData;
	}
}
