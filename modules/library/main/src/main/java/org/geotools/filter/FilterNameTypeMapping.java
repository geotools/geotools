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
package org.geotools.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.expression.Function;

/**
 * A utility class for {@link FilterCapabilities} that assists in mapping between a filter or expression or
 * function name to the object that represents it's type.
 * @author Jesse
 */
class FilterNameTypeMapping {
    static Map spatialFiltersMap = loadSpatialFiltersMap();
    static Map comparisonsMap = loadComparisonFilterMap();
    static Map filterTypeToFilterCapabilitiesMap = loadFilterTypeToFilterCapabilitiesMap();
    static Map functionNameMap = loadFunctionNameMap();

    public static Map loadSpatialFiltersMap() {
        spatialFiltersMap = new HashMap();
        spatialFiltersMap.put("", NO_OP_CAPS);
        spatialFiltersMap.put("BBOX", new FilterCapabilities(FilterCapabilities.SPATIAL_BBOX));
        spatialFiltersMap.put("Equals", new FilterCapabilities(FilterCapabilities.SPATIAL_EQUALS));
        spatialFiltersMap.put("Disjoint", new FilterCapabilities(FilterCapabilities.SPATIAL_DISJOINT));
        spatialFiltersMap.put("Intersect", new FilterCapabilities(FilterCapabilities.SPATIAL_INTERSECT));
        spatialFiltersMap.put("Touches", new FilterCapabilities(FilterCapabilities.SPATIAL_TOUCHES));
        spatialFiltersMap.put("Crosses", new FilterCapabilities(FilterCapabilities.SPATIAL_CROSSES));
        spatialFiltersMap.put("Within", new FilterCapabilities(FilterCapabilities.SPATIAL_WITHIN));
        spatialFiltersMap.put("Contains", new FilterCapabilities(FilterCapabilities.SPATIAL_CONTAINS));
        spatialFiltersMap.put("Overlaps", new FilterCapabilities(FilterCapabilities.SPATIAL_OVERLAPS));
        spatialFiltersMap.put("Beyond", new FilterCapabilities(FilterCapabilities.SPATIAL_BEYOND));
        spatialFiltersMap.put("DWithin", new FilterCapabilities(FilterCapabilities.SPATIAL_DWITHIN));

        return spatialFiltersMap;
    }

	public static Map loadComparisonFilterMap() {
        comparisonsMap = new HashMap();
        comparisonsMap.put("", NO_OP_CAPS);
        comparisonsMap.put("Logical", new FilterCapabilities(FilterCapabilities.LOGICAL));
        comparisonsMap.put("Simple_Comparisons", new FilterCapabilities(FilterCapabilities.SIMPLE_COMPARISONS));
        comparisonsMap.put("Like", new FilterCapabilities(FilterCapabilities.LIKE));
        comparisonsMap.put("Between", new FilterCapabilities(FilterCapabilities.BETWEEN));
        comparisonsMap.put("NullCheck", new FilterCapabilities(FilterCapabilities.NULL_CHECK));
        comparisonsMap.put("Simple_Arithmetic", new FilterCapabilities(FilterCapabilities.SIMPLE_ARITHMETIC));
        comparisonsMap.put("Functions", new FilterCapabilities(FilterCapabilities.FUNCTIONS));

        return comparisonsMap;
    }
    
	public static Map loadFilterTypeToFilterCapabilitiesMap(){
    	Map conversionMap=new HashMap();
    	conversionMap.put(new Short(FilterType.BETWEEN), new FilterCapabilities(FilterCapabilities.BETWEEN));
    	conversionMap.put(new Short(FilterType.COMPARE_EQUALS), new FilterCapabilities(FilterCapabilities.COMPARE_EQUALS));
    	conversionMap.put(new Short(FilterType.COMPARE_GREATER_THAN), new FilterCapabilities(FilterCapabilities.COMPARE_GREATER_THAN));
    	conversionMap.put(new Short(FilterType.COMPARE_GREATER_THAN_EQUAL), new FilterCapabilities(FilterCapabilities.COMPARE_GREATER_THAN_EQUAL));
    	conversionMap.put(new Short(FilterType.COMPARE_LESS_THAN), new FilterCapabilities(FilterCapabilities.COMPARE_LESS_THAN));
    	conversionMap.put(new Short(FilterType.COMPARE_LESS_THAN_EQUAL), new FilterCapabilities(FilterCapabilities.COMPARE_LESS_THAN_EQUAL));
    	conversionMap.put(new Short(FilterType.COMPARE_NOT_EQUALS), new FilterCapabilities(FilterCapabilities.COMPARE_NOT_EQUALS));
    	conversionMap.put(new Short(FilterType.FID), new FilterCapabilities(FilterCapabilities.FID));
    	conversionMap.put(new Short(FilterType.GEOMETRY_BBOX), new FilterCapabilities(FilterCapabilities.SPATIAL_BBOX));
    	conversionMap.put(new Short(FilterType.GEOMETRY_BEYOND), new FilterCapabilities(FilterCapabilities.SPATIAL_BEYOND));
    	conversionMap.put(new Short(FilterType.GEOMETRY_CONTAINS), new FilterCapabilities(FilterCapabilities.SPATIAL_CONTAINS));
    	conversionMap.put(new Short(FilterType.GEOMETRY_CROSSES), new FilterCapabilities(FilterCapabilities.SPATIAL_CROSSES));
    	conversionMap.put(new Short(FilterType.GEOMETRY_DISJOINT), new FilterCapabilities(FilterCapabilities.SPATIAL_DISJOINT));
    	conversionMap.put(new Short(FilterType.GEOMETRY_DWITHIN), new FilterCapabilities(FilterCapabilities.SPATIAL_DWITHIN));
    	conversionMap.put(new Short(FilterType.GEOMETRY_EQUALS), new FilterCapabilities(FilterCapabilities.SPATIAL_EQUALS));
    	conversionMap.put(new Short(FilterType.GEOMETRY_INTERSECTS), new FilterCapabilities(FilterCapabilities.SPATIAL_INTERSECT));
    	conversionMap.put(new Short(FilterType.GEOMETRY_OVERLAPS), new FilterCapabilities(FilterCapabilities.SPATIAL_OVERLAPS));
    	conversionMap.put(new Short(FilterType.GEOMETRY_TOUCHES), new FilterCapabilities(FilterCapabilities.SPATIAL_TOUCHES));
    	conversionMap.put(new Short(FilterType.GEOMETRY_WITHIN), new FilterCapabilities(FilterCapabilities.SPATIAL_WITHIN));
    	conversionMap.put(new Short(FilterType.LIKE), new FilterCapabilities(FilterCapabilities.LIKE));
    	conversionMap.put(new Short(FilterType.LOGIC_AND), new FilterCapabilities(FilterCapabilities.LOGIC_AND));
    	conversionMap.put(new Short(FilterType.LOGIC_NOT), new FilterCapabilities(FilterCapabilities.LOGIC_NOT));
    	conversionMap.put(new Short(FilterType.LOGIC_OR), new FilterCapabilities(FilterCapabilities.LOGIC_OR));
    	conversionMap.put(new Short(FilterType.NULL), new FilterCapabilities(FilterCapabilities.NULL_CHECK));
    	return conversionMap;
    }


	public static Map loadFunctionNameMap() {
		functionNameMap = new HashMap();
		functionNameMap.put("", NO_OP_CAPS);
//		functionNameMap.put("distance", new FilterCapabilities(FilterFunction_distance.class));
//		FilterCapabilities tmp = new FilterCapabilities(FilterFunction_abs.class);
//		tmp.addType(FilterFunction_abs_2.class);
//		tmp.addType(FilterFunction_abs_3.class);
//		tmp.addType(FilterFunction_abs_4.class);
//		functionNameMap.put("abs", tmp);
//		functionNameMap.put("acos", new FilterCapabilities(FilterFunction_acos.class));
//		functionNameMap.put("area", new FilterCapabilities(FilterFunction_area.class));
//		functionNameMap.put("asin", new FilterCapabilities(FilterFunction_asin.class));
//		functionNameMap.put("atan", new FilterCapabilities(FilterFunction_atan.class));
//		functionNameMap.put("atan2", new FilterCapabilities(FilterFunction_atan2.class));
//		functionNameMap.put("between", new FilterCapabilities(FilterFunction_between.class));
//		functionNameMap.put("boundary", new FilterCapabilities(FilterFunction_boundary.class));
//		functionNameMap.put("boundarydimension", new FilterCapabilities(FilterFunction_boundaryDimension.class));
//		functionNameMap.put("buffer", new FilterCapabilities(FilterFunction_buffer.class));
//		functionNameMap.put("bufferwithsegments", new FilterCapabilities(FilterFunction_bufferWithSegments.class));
//		tmp = new FilterCapabilities(FilterFunction_ceil.class);
//		functionNameMap.put("ceil", tmp);
//		functionNameMap.put("ceiling", tmp);
//		functionNameMap.put("centroid", new FilterCapabilities(FilterFunction_centroid.class));
//
//		functionNameMap.put("collection_average", new FilterCapabilities(Collection_AverageFunction.class));
//		functionNameMap.put("collection_bounds", new FilterCapabilities(Collection_BoundsFunction.class));
//		functionNameMap.put("collection_count", new FilterCapabilities(Collection_CountFunction.class));
//		functionNameMap.put("collection_max", new FilterCapabilities(Collection_MaxFunction.class));
//		functionNameMap.put("collection_median", new FilterCapabilities(Collection_MedianFunction.class));
//		functionNameMap.put("collection_min", new FilterCapabilities(Collection_MinFunction.class));
//		functionNameMap.put("collection_sum", new FilterCapabilities(Collection_SumFunction.class));
//		
//		functionNameMap.put("contains", new FilterCapabilities(FilterFunction_contains.class));
//		functionNameMap.put("convexhull", new FilterCapabilities(FilterFunction_convexHull.class));
//		functionNameMap.put("cos", new FilterCapabilities(FilterFunction_cos.class));
//		functionNameMap.put("crosses", new FilterCapabilities(FilterFunction_crosses.class));
//		functionNameMap.put("difference", new FilterCapabilities(FilterFunction_difference.class));
//		functionNameMap.put("dimension", new FilterCapabilities(FilterFunction_dimension.class));
//		functionNameMap.put("disjoint", new FilterCapabilities(FilterFunction_disjoint.class));
//		functionNameMap.put("double2bool", new FilterCapabilities(FilterFunction_double2bool.class));
//		functionNameMap.put("endpoint", new FilterCapabilities(FilterFunction_endPoint.class));
//		functionNameMap.put("envelope", new FilterCapabilities(FilterFunction_envelope.class));
//		functionNameMap.put("equalinterval", new FilterCapabilities(EqualIntervalFunction.class));
//		functionNameMap.put("equalsexact", new FilterCapabilities(FilterFunction_.class));
//		functionNameMap.put("equalsexacttolerance", new FilterCapabilities(FilterFunction_buffer.class));
//		functionNameMap.put("equalto", new FilterCapabilities(FilterFunction_buffer.class));
//		functionNameMap.put("exp", new FilterCapabilities(FilterFunction_buffer.class));
//		functionNameMap.put("exteriorring", new FilterCapabilities(FilterFunction_buffer.class));
//		functionNameMap.put("floor", new FilterCapabilities(FilterFunction_buffer.class));
//		functionNameMap.put("geometrytype", new FilterCapabilities(FilterFunction_buffer.class));
//		functionNameMap.put("geomfromtwkt", new FilterCapabilities(FilterFunction_buffer.class));
//		functionNameMap.put("geomlength", new FilterCapabilities(FilterFunction_buffer.class));
		
		Iterator expressions = CommonFactoryFinder.getFunctionExpressions( null ).iterator();
		while ( expressions.hasNext() ){
			FunctionExpression exp=(FunctionExpression) expressions.next();
			functionNameMap.put(exp.getName().toLowerCase(), new FilterCapabilities(exp.getClass()));
		}
		Iterator functions = CommonFactoryFinder.getFunctions(null ).iterator();
        while ( functions.hasNext() ){
            Function exp=(Function) functions.next();
            functionNameMap.put(exp.getName().toLowerCase(), new FilterCapabilities(exp.getClass()));
        }
        return functionNameMap;
    }

	public static FilterCapabilities findFunction(String name) {
		FilterCapabilities filterCapabilities = (FilterCapabilities) functionNameMap.get(name);
		if( filterCapabilities!=null )
			return filterCapabilities;
		return NO_OP_CAPS;
	}
    
    /**
     * Translates a String into an long mask for the operation
     * 
     * @param s String, operation name
     * @return one of the filter constants
     */
    public static FilterCapabilities findOperation(String s) {
    	
        if (spatialFiltersMap.containsKey(s)) {
            return (FilterCapabilities) spatialFiltersMap.get(s);
        }

        if (comparisonsMap.containsKey(s)) {
            return (FilterCapabilities) comparisonsMap.get(s);
        }

        return FilterNameTypeMapping.NO_OP_CAPS;
    }

    /**
     * Converts a singular mask to the appropriate string as a Spatial Op
     * 
     * @param i The long constant
     * @return The String representation of the long as a FilterType
     */
    public static String writeSpatialOperation(long i) {
        if ( i == FilterCapabilities.SPATIAL_BBOX )
            return "BBOX";

        if ( i == FilterCapabilities.SPATIAL_EQUALS )
            return "Equals";

        if ( i == FilterCapabilities.SPATIAL_DISJOINT )
            return "Disjoint";

        if ( i == FilterCapabilities.SPATIAL_INTERSECT )
            return "Intersect";

        if ( i == FilterCapabilities.SPATIAL_TOUCHES )
            return "Touches";

        if ( i == FilterCapabilities.SPATIAL_CROSSES )
            return "Crosses";

        if ( i == FilterCapabilities.SPATIAL_WITHIN )
            return "Within";

        if ( i == FilterCapabilities.SPATIAL_CONTAINS )
            return "Contains";

        if ( i == FilterCapabilities.SPATIAL_OVERLAPS )
            return "Overlaps";

        if ( i == FilterCapabilities.SPATIAL_BEYOND )
            return "Beyond";

        if ( i == FilterCapabilities.SPATIAL_DWITHIN )
            return "DWithin";

        return "";
    }

    /**
     * Converts a singular mask to the appropriate string as a Scalar Op
     * 
     * @param i The long constant
     * @return The String representation of the long as a FilterType
     */
    public static String writeScalarOperation(long i) {
        if( i == FilterCapabilities.LOGICAL) 
            return "Logical";

        if( i == FilterCapabilities.SIMPLE_COMPARISONS)
            return "Simple_Comparisons";

        if( i ==FilterCapabilities.LIKE )
            return "Like";

        if( i == FilterCapabilities.BETWEEN )
            return "Between";

        if( i == FilterCapabilities.NULL_CHECK )
            return "NullCheck";

        if( i == FilterCapabilities.SIMPLE_ARITHMETIC )
            return "Simple_Arithmetic";

        if( i == FilterCapabilities.FUNCTIONS )
            return "Functions";

        if( i == FilterCapabilities.FID )
            return "FeatureID";
        
        if( i == FilterCapabilities.COMPARE_EQUALS )
            return "Compare_Equals";
        
        if( i == FilterCapabilities.COMPARE_GREATER_THAN )
            return "Compare_Greater_Than";
        
        if( i == FilterCapabilities.COMPARE_GREATER_THAN_EQUAL )
            return "Compare_Greater_Than_Equal";
        
        if( i == FilterCapabilities.COMPARE_LESS_THAN )
            return "Compare_Less_Than";
        
        if( i == FilterCapabilities.COMPARE_LESS_THAN_EQUAL )
            return "Compare_Less_Than_Equal";
        
        if( i == FilterCapabilities.COMPARE_NOT_EQUALS )
            return "Compare_Not_Equals";
        
        return "";
    }

	static final FilterCapabilities NO_OP_CAPS=new FilterCapabilities(FilterCapabilities.NO_OP);
	public static final FilterCapabilities ALL_CAPS = new FilterCapabilities(){
		public boolean supports(Class type) {
			// TODO Auto-generated method stub
			return super.supports(type);
		}
		
		public boolean supports(Filter filter) {
			return true;
		}
		
		public boolean supports(FilterCapabilities type) {
			return true;
		}
		
		public boolean supports(long type) {
			return true;
		}
		
		public boolean supports(short type) {
			return true;
		}
		
	};
    
}
