package org.geotools.mbstyle;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Function;

public class MBFunction {
    final protected MBObjectParser parse;
    final protected JSONObject json;

    public MBFunction(JSONObject json) {
        this(new MBObjectParser(), json );
        
    }

    public MBFunction(MBObjectParser parse, JSONObject json) {
        this.parse = parse;
        this.json = json;
    }

    /** Optional type, one of identity, exponential, interval, categorical. */
    public static enum FunctionType {
        /** Functions return their input as their output. */
        IDENITY,
        /**
         * Functions generate an output by interpolating between stops just less than and just
         * greater than the function input. The domain must be numeric. This is the default for
         * properties marked with "exponential" symbol.
         * <p>
         * This is the default function type for continuous value (such as color or line width).
         */
        EXPONENTIAL,
        /**
         * Functions return the output value of the stop just less than the function input. The
         * domain must be numeric. This is the default for properties marked with thie "interval"
         * symbol.
         * <p>
         * This is the default function type for enums values (such as line_cap).
         */
        INTERVAL,

        /** Functions return the output value of the stop equal to the function input. */
        CATEGORICAL
    }

    public FunctionType getType(){
        String type = parse.get(json, "type", "exponential");
        switch (type) {
        case "identiy":
            return FunctionType.IDENITY;
        case "exponential":
            return FunctionType.EXPONENTIAL;
        case "internval":
            return FunctionType.INTERVAL;
        case "categorical":
            return FunctionType.CATEGORICAL;
        default:
            throw new MBFormatException(
                    "Function type \"" + type
                            + "\" invalid - expected identiy, expontential, interval, categorical");
        }
    }
    
    /**
     * If specified, the function will take the specified feature property as an input. See Zoom
     * Functions and Property Functions for more information.
     * 
     * @return property evaluated by function, optional (may be null for zoom functions).
     */
    public String getProperty(){
        return parse.optional(String.class, json, "property", null);
    }

    /**
     * Programmatically look at the structure of the function and determine if it is a Zoom function, Property function or Zoom-and-property functions. 
     * @return
     */
    public String getKind(){
        String property = getProperty();
        JSONArray stops = getStops();
        JSONArray first = parse.jsonArray(stops.get(0));
        
       if( property == null ){
           return "zoom"; // no property defined, zoom function
       }
       else if ( property != null && first.get(0) instanceof JSONObject){
           return "zoom and property";
       }
       else {
           return "property";
       }
    }
    /**
     * Functions are defined in terms of input and output values. A set of one input value and one
     * output value is known as a "stop."
     * 
     * @return stops definition, optional may be null.
     */
    public JSONArray getStops(){
        return parse.getJSONArray(json, "stops", null);
    }
    /**
     * Function as defined by json.
     * <p>
     * The value for any layout or paint property may be specified as a function. Functions allow
     * you to make the appearance of a map feature change with the current zoom level and/or the
     * feature's properties.
     * </p>
     * <p>
     * Function types:
     * </p>
     * <p>
     * <em>
     * 
     * @param json JSONOBject definition of Function
     * @return Function as defined by json
     */
    public static MBFunction create(JSONObject json) {
        return null;
    }
    public Function expression(JSONObject json){
        MBFunction function = create(json);
        return function.expression();
        
    }

    /**
     * Generate GeoTools {@link Function} from json definition.
     * <p>
     * This only works for concrete functions, that have been resolved to the current zoom level.
     * 
     * @return GeoTools Function or the provided json
     */
    public Function expression() {
        // TODO Auto-generated method stub
        return null;
    }
}
