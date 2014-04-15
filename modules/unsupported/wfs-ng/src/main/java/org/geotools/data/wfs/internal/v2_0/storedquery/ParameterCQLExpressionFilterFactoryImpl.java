package org.geotools.data.wfs.internal.v2_0.storedquery;

import org.geotools.filter.FilterFactoryImpl;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.ReferenceIdentifier;

/**
 * Stored Query parameters may be configured as CQL expressions. The following properties
 * are supported:
 * 
 * <pre>
 *  bboxMinX       Filter envelope bounds
 *  bboxMaxX
 *  bboxMinY
 *  bboxMaxY
 *  defaultSRS	   The defaultSRS of the Feature Type in question
 *  viewparam:name View parameter used in original request ('name' is the name of the parameter) 
 * </pre>
 * 
 * @author Sampo Savolainen
 *
 */
public class ParameterCQLExpressionFilterFactoryImpl extends
		FilterFactoryImpl {

	@Override
	public PropertyName property(String name) {
		if (name.equals("bboxMinX")) {
			return new ParameterCQLExpressionPropertyName(name) {
				@Override
				protected Object get(ParameterMappingContext context) {
					return context.getBBOX().getMinX();
				}
			};
		} else if (name.equals("bboxMaxX")) {
			return new ParameterCQLExpressionPropertyName(name) {
				@Override
				protected Object get(ParameterMappingContext context) {
					return context.getBBOX().getMaxX();
				}
			};
		} else if (name.equals("bboxMinY")) {
			return new ParameterCQLExpressionPropertyName(name) {
				@Override
				protected Object get(ParameterMappingContext context) {
					return context.getBBOX().getMinY();
				}
			};
		} else if (name.equals("bboxMaxY")) {
			return new ParameterCQLExpressionPropertyName(name) {
				@Override
				protected Object get(ParameterMappingContext context) {
					return context.getBBOX().getMaxY();
				}
			};
		} else if (name.equals("defaultSRS")) {
			return new ParameterCQLExpressionPropertyName(name) {
				@Override
				protected Object get(ParameterMappingContext context) {
					ReferenceIdentifier ret = context.getFeatureTypeInfo().getCRS()
							.getIdentifiers().iterator().next();
					
					return ret.toString();
				}
			};
		} else if (name.startsWith("viewparam:")) {
			final String paramName = name.substring(10);
			return new ParameterCQLExpressionPropertyName(name) {
				@Override
				protected Object get(ParameterMappingContext context) {
					return context.getViewParams().get(paramName);
				}
			};
		}
		
		return super.property(name);
	}

}
