package org.geotools.tutorial.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.filter.FunctionFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class ExampleFunctionFactory implements FunctionFactory {    
    public List<FunctionName> getFunctionNames() {
        List<FunctionName> functionList = new ArrayList<FunctionName>();
        functionList.add(SnapFunction.NAME);        
        return Collections.unmodifiableList( functionList );
    }    
    public Function function(String name, List<Expression> args, Literal fallback) {
        if( SnapFunction.NAME.equals(name)){
            return new SnapFunction( args, fallback );
        }
        return null; // we do not implement that function
    }
}
