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
package org.geotools.filter.spatial;

import org.geotools.filter.FilterFactory;
import org.geotools.filter.GeometryFilterImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.Disjoint;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class DisjointImpl extends AbstractPreparedGeometryFilter implements Disjoint {

	public DisjointImpl(org.opengis.filter.FilterFactory factory,Expression e1,Expression e2) {
		super(factory,e1,e2);
		
		//backwards compat with old type system
		this.filterType = GEOMETRY_DISJOINT;
	}
	
	public boolean evaluate(Object feature) {
		if (feature instanceof SimpleFeature && !validate((SimpleFeature)feature))
			return false;
		
		Geometry left;
        Geometry right;

        switch (literals) {
        case BOTH:
            return cacheValue;
        case RIGHT: {
            return rightPreppedGeom.disjoint(getLeftGeometry(feature));
        }
        case LEFT: {
            return leftPreppedGeom.disjoint(getRightGeometry(feature));
        }
        default: {
            left = getLeftGeometry(feature);
            right = getRightGeometry(feature);
            return basicEvaluate(left, right);
        }
        }
		
	}
	
	@Override
	protected boolean basicEvaluate(Geometry left, Geometry right) {
		Envelope envLeft = left.getEnvelopeInternal();
		Envelope envRight = right.getEnvelopeInternal();
		
		if(envRight.intersects(envLeft))
            return left.disjoint(right);
        
         return true;
     }
	
	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit(this,extraData);
	}

}
