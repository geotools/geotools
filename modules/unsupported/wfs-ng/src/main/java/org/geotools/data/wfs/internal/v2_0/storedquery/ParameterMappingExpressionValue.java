package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.io.Serializable;

import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.expression.Expression;

public class ParameterMappingExpressionValue implements ParameterMapping, Serializable {
	private String parameterName;
	private String expressionLanguage;
	private String expression;
	
	private transient Expression cqlExpression;
	
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	@Override
	public String getParameterName() {
		return parameterName;
	}
	
	public void setExpressionLanguage(String expressionLanguage) {
		this.expressionLanguage = expressionLanguage;
	}

	public String getExpressionLanguage() {
		return expressionLanguage;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public String getExpression() {
		return expression;
	}

	private Expression ensureExpression() {
		if (cqlExpression != null) return cqlExpression;
		
		try {
			cqlExpression = CQL.toExpression(expression,
					new ParameterCQLExpressionFilterFactoryImpl());
		} catch(CQLException ce) {
			throw new IllegalArgumentException("Illegal CQL expression", ce);
		}
		
		return cqlExpression;
	}
	
	public String evaluate(ParameterMappingContext mappingContext) {
	
		Object obj = ensureExpression().evaluate(mappingContext);
		
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
