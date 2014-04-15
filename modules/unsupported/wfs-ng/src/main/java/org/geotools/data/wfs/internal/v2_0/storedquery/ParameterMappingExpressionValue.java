package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.util.Map;

import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

import com.vividsolutions.jts.geom.Envelope;

public class ParameterMappingExpressionValue extends ParameterMapping {
	private final String expressionLanguage;
	private final String expression;
	
	private Expression cqlExpression;
	
	public ParameterMappingExpressionValue(String parameterName, String expressionLanguage, 
			String expression) {
		super(parameterName);
		this.expressionLanguage = expressionLanguage;
		this.expression = expression;
		
		if (!this.expressionLanguage.equals("CQL")) {
			throw new IllegalArgumentException("Expression language "+expressionLanguage+
					" is not supported!");
		}
		
		try {
			cqlExpression = CQL.toExpression(expression,
					new ParameterCQLExpressionFilterFactoryImpl());
		} catch(CQLException ce) {
			throw new IllegalArgumentException("Illegal CQL experssion", ce);
		}
	}
	
	public String getExpression() {
		return expression;
	}
	
	public String getExpressionLanguage() {
		return expressionLanguage;
	}

	public String evaluate(ParameterMappingContext mappingContext) {
	
		Object obj = cqlExpression.evaluate(mappingContext);
		
		String ret;
		
		if (obj == null) {
			ret = null;
		} else if (obj instanceof String) {
			ret = (String)obj;
		} else {
			ret = obj.toString();
		}
		return ret;
	}

}
