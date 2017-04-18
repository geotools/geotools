package org.geotools.mbstyle.function;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.Parameter;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.text.Text;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Generate an output by interpolating between stops just less than and just
 * greater than the function input. The domain must be numeric.
 *
 * @author Jody Garnett (Boundless)
 */
public class ExponentialFunction extends FunctionImpl {
    public static final FunctionName NAME;
    static {
        Parameter<Object> input = new Parameter<Object>("input",Object.class,1,1);
        Parameter<Double> base = new Parameter<Double>(
                "base", Double.class,
                Text.text("Base"),
                Text.text("Exponential base of the interpolation curve controlling rate at which function output increases."),
                true,0,1,
                1.0,
                null
        );
        Parameter<Object> stops= new Parameter<Object>("stops",Object.class,4,-1);
        NAME = new FunctionNameImpl("Exponential", input, base, stops);
    }
    private static class Stop {
        Expression stop;
        Expression value;
        public Stop(Expression stop, Expression value ){
            this.stop = stop;
            this.value = value;
        }
        @Override
        public String toString() {
            return "Stop "+stop+": "+value;
        }
    }
    public ExponentialFunction() {
        this.functionName = NAME;
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        List<Expression> parameters = getParameters();
        List<Stop> stops = new ArrayList<>();
        
        Expression input = parameters.get(0);
        Expression base = parameters.get(1);
        
        for (int i = 2; i < parameters.size(); i++) {
            Stop stop = new Stop(parameters.get(i), parameters.get(i + 1));
            stops.add(stop);
        }
        Double inputValue = input.evaluate(object, Double.class);
        Double baseValue = base.evaluate(object, Double.class);
        if( stops.size()==1){
            // single stop
            Stop single = stops.get(0);
            return single.value.evaluate(object, context);
        }
        int find = find(object, inputValue, stops);
        
        if( find <= 0 ){
            // data is below stop range, use min
            Stop min = stops.get(0);
            return min.value.evaluate(object, context);
        }
        else if (find >= stops.size()){
            // data is above the stop range, use max
            Stop max = stops.get(stops.size()-1);
            return max.value.evaluate(object, context);
        }
        Stop lower = stops.get(find-1);
        Stop upper = stops.get(find);
        double exponential = exponential(object, inputValue, baseValue, lower, upper);
        
        return context.cast(exponential);
    }

    private double exponential(Object object, double inputValue, double base, Stop lower, Stop upper) {
        
        double stop1 = lower.stop.evaluate(object,Double.class);
        double value1 = lower.value.evaluate(object,Double.class);
        double stop2 = upper.stop.evaluate(object,Double.class);
        double value2 = upper.value.evaluate(object,Double.class); 
        
        double scale = (value2-value1)/(Math.pow(stop2, base) - Math.pow(stop1, base));
        double offset = value1-scale*Math.pow(stop1, base);
        
        return offset + scale*Math.pow(inputValue, base);
    }
    
    /**
     * Find the stop containing the input value. The value returned is the index, in the stops list,
     * of the higher point of the segment between two stops.
     *
     * @return stop index; or 0 if input is below the range of the stops; or
     *         {@code max stop index + 1} if it is above the range
     */
    private int find(Object object, Double input, List<Stop> stops) {
        int find = stops.size();
        for (int i = 0; i < stops.size(); i++) {
            Double stop = stops.get(i).stop.evaluate(object, Double.class);
            if (input <= stop) {
                find = i;
                break;
            }
        }
        return find;
    }
    
}
