package org.geotools.renderer.shape;

import org.geotools.filter.visitor.DefaultFilterVisitor;
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

public class GeometryFilterChecker extends DefaultFilterVisitor {
	
	boolean geometryFilterPresent = false;
	
	public boolean isGeometryFilterPresent() {
		return geometryFilterPresent;
	}

	@Override
	public Object visit(BBOX filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Beyond filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Contains filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Crosses filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Disjoint filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(DWithin filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Equals filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Intersects filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Overlaps filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Touches filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
	
	@Override
	public Object visit(Within filter, Object data) {
		geometryFilterPresent = true;
		return super.visit(filter, data);
	}
}
