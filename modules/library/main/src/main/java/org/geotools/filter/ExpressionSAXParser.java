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


// Java Topology Suite dependencies
import java.util.ArrayList;
import java.util.logging.Logger;

import org.opengis.feature.simple.SimpleFeatureType;
import org.xml.sax.Attributes;

import com.vividsolutions.jts.geom.Geometry;


/**
 * DOCUMENT ME!
 *
 * @author Rob Hranac, TOPP<br>
 * @author Chris Holmes, TOPP
 *
 * @source $URL$
 * @version $Id$
 */
public class ExpressionSAXParser {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /** Factory to construct filters. */
    private FilterFactory ff;

    /** A nested expression parser for math sub expressions */
    private ExpressionSAXParser expFactory = null;

    /** The current expression being constructed */
    private Expression curExprssn = null;

    /**
     * The current state of the expression. Deterimines if a proper expression
     * can be made..
     */
    private String currentState = null; //DJB: appears this can be leftValue rightValue complete.
                                        //DJB: added "accumulate" for <Function> 
    
    private ArrayList accumalationOfExpressions = new ArrayList(); //DJB: keep a list of the expressions used in a <Function>

    /** The type of expression being constructed. */
    private String declaredType = null;

    /**
     * If a expression can be created.  Indicates if currentState equals
     * complete
     */
    private boolean readyFlag = false;

    /**
     * A schema to read the attributes against.  Currently not really
     * impelmented.
     */
    private SimpleFeatureType schema;

    /**
     * If the message from the SAX characters function should be read.   For
     * example when the expression is expecting character values.
     */
    private boolean readChars = false;

    public ExpressionSAXParser(){
    	this( FilterFactoryFinder.createFilterFactory() );    	
	}
    public ExpressionSAXParser(FilterFactory factory ){
        this( null, factory );    	
    }
    /**
     * Constructor with a schema to read the attribute againset.
     *
     * @param schema The schema for attributes (null is fine, as the code for
     *        this is not in place.
     */
    public ExpressionSAXParser(SimpleFeatureType schema) {
        this( schema, FilterFactoryFinder.createFilterFactory() );    	
    }
    /** Constructor injection */
    public ExpressionSAXParser( SimpleFeatureType schema, FilterFactory factory ){
    	this.schema = schema;
        ff = factory;    	
    }
    /** Setter injection */
    public void setFilterFactory( FilterFactory factory ){
    	ff = factory;
    }

    /**
     * Initializes the factory to create a new expression.  Called when the
     * filter handler reaches a new expression.
     *
     * @param declaredType The string representation of the expression type.
     *
     * @throws IllegalFilterException If there are problems creating
     *         expressions.
     */
    public void start(String declaredType,Attributes atts) throws IllegalFilterException {
        LOGGER.finer("incoming type: " + declaredType);
        LOGGER.finer("declared type: " + this.declaredType);
        LOGGER.finer("current state: " + currentState);

        if (expFactory == null) 
        {
            this.declaredType = declaredType;
            
            if (DefaultExpression.isFunctionExpression(convertType(declaredType)))
            {
            	 expFactory = new ExpressionSAXParser(schema);
                 curExprssn = ff.createFunctionExpression( getFunctionName(atts) );
                 LOGGER.finer("is <function> expression");
            }

            // if the expression is math, then create a factory for its
            // sub expressions, otherwise just instantiate the main expression
            if (DefaultExpression.isMathExpression(convertType(declaredType))) {
                expFactory = new ExpressionSAXParser(schema);
                curExprssn = ff.createMathExpression(convertType(
                            declaredType));
                LOGGER.finer("is math expression");
            } else if (DefaultExpression.isLiteralExpression(convertType(
                            declaredType))) {
                curExprssn = ff.createLiteralExpression();
                readChars = true;
                LOGGER.finer("is literal expression");
            } else if (DefaultExpression.isAttributeExpression(convertType(
                            declaredType))) {
                curExprssn = ff.createAttributeExpression(schema);
                readChars = true;
                LOGGER.finer("is attribute expression");
            }

            currentState = setInitialState(curExprssn);
            readyFlag = false;
        } else {
            expFactory.start(declaredType,atts);
        }
    }

    /**
     * Called when the filter handler has reached the end of an expression
     *
     * @param message the expression to end.
     *
     * @throws IllegalFilterException If there are problems creating
     *         exceptions.
     */
    public void end(String message) throws IllegalFilterException {
        LOGGER.finer("declared type: " + declaredType);
        LOGGER.finer("end message: " + message);
        LOGGER.finer("current state: " + currentState);
        LOGGER.finest("expression factory: " + expFactory);

        // first, check to see if there are internal (nested) expressions
        //  note that this is identical to checking if the curExprssn
        //  is a math expression
        // if this internal expression exists, send its factory an end message
        if (expFactory != null) {
            expFactory.end(message);

            // if the factory is ready to be returned:
            //  (1) add its expression to the current expression, as determined
            //      by the current state
            //  (2) increment the current state
            //  (3) set the factory to null to indicate that it is now done
            // if in a bad state, throw exception
            if (expFactory.isReady()) {
                if (currentState.equals("leftValue")) {
                    ((MathExpression) curExprssn).addLeftValue(expFactory
                        .create());
                    currentState = "rightValue";
                    expFactory = new ExpressionSAXParser(schema);
                    LOGGER.finer("just added left value: " + currentState);
                } else if (currentState.equals("rightValue")) {
                    ((MathExpression) curExprssn).addRightValue(expFactory
                        .create());
                    currentState = "complete";
                    expFactory = null;
                    LOGGER.finer("just added right value: " + currentState);
                } else if (currentState.equals("accumulate")) {
                        accumalationOfExpressions.add(expFactory.create());
                        expFactory = null;
                        // currentState = "accumulate";  //leave unchanged
                        LOGGER.finer("just added a parameter for a function: " + currentState);
                                  
                        if (  ((FunctionExpression) curExprssn).getArgCount() ==   accumalationOfExpressions.size())
                        {
                        	//hay, we've parsed all the arguments!
                        	currentState = "complete";
                        	((FunctionExpression) curExprssn).setArgs( (Expression[]) accumalationOfExpressions.toArray( new Expression[0] ));
                        }
                        else
                        {
                        	expFactory = new ExpressionSAXParser(schema); // we're gonna get more expressions
                        }
                    } else {
                    throw new IllegalFilterException(
                        "Attempted to add sub expression in a bad state: "
                        + currentState);
                }
            }
        } else if (declaredType.equals(message)
                && currentState.equals("complete")) {
            // if there are no nested expressions here,
            //  determine if this expression is ready and set flag appropriately
            readChars = false;
            readyFlag = true;
        } else { // otherwise, throw exception
            throw new IllegalFilterException(
                "Reached end of unready, non-nested expression: "
                + currentState);
        }
    }

    /**
     * Checks to see if this expression is ready to be returned.
     *
     * @return <tt>true</tt> if the expression is ready to be returned,
     *         <tt>false</tt> otherwise.
     */
    public boolean isReady() {
        return readyFlag;
    }

    /**
     * Handles incoming characters.
     *
     * @param message the incoming chars from the SAX handler.
     *
     * @throws IllegalFilterException If there are problems with filter
     *         constrcution.
     *
     * @task TODO: this function is a mess, but it's mostly due to filters
     *       being loosely coupled with schemas, so we have to make a lot of
     *       guesses.
     * @task TODO: Revisit stripping leading characters.  Needed now to get
     *       things working, and may be the best choice in the end, but it
     *       should be thought through more.
     */
    public void message(String message, boolean convertToNumber) throws IllegalFilterException {
        // TODO 2:
        // AT SOME POINT MUST MAKE THIS HANDLE A TYPED FEATURE
        // BY PASSING IT A FEATURE AND CHECKING ITS TYPE HERE
        LOGGER.finer("incoming message: " + message);
        LOGGER.finer("should read chars: " + readChars);

        if (readChars) {
            // If an attribute path, set it.  Assumes undeclared type.
            if (curExprssn instanceof AttributeExpression) {
                LOGGER.finer("...");

                //HACK: this code is to get rid of the leading junk that can
                //occur in a filter encoding.  The '.' is from the .14 wfs spec
                //when the style was typeName.propName, such as road.nlanes, 
                //The ':' is from wfs 1.0 xml request, such as myns:nlanes, 
                //and the '/' is from wfs 1.0 kvp style: road/nlanes.  
                //We're not currently checking to see if the typename matches,
                // or if the namespace is right, which isn't the best, 
                //so that should be fixed.
                String[] splitName = message.split("[.:/]");
                String newAttName = message;

                if (splitName.length == 1) {
                    newAttName = splitName[0];
                } else {
                    //REVISIT: not sure what to do if there are multiple
                    //delimiters.  
                    //REVISIT: should we examine the first value?  See
                    //if the namespace or typename matches up right?
                    //this is currently very permissive, just grabs
                    //the value of the end.
                    newAttName = splitName[splitName.length - 1];
                }

                LOGGER.finer("setting attribute expression: " + newAttName);
                ((AttributeExpression) curExprssn).setAttributePath(newAttName);
                LOGGER.finer("...");
                currentState = "complete";
                LOGGER.finer("...");
            } else if (curExprssn instanceof LiteralExpression) {
                // This is a relatively loose assignment routine, which uses
                //  the fact that the three allowed literal types have a strict
                //  instatiation hierarchy (ie. double can be an int can be a 
                //  string, but not the other way around).
                // A better routine would consider the use of this expression
                //  (ie. will it be compared to a double or searched with a
                //  like filter?)
                //HACK: This should also not use exception catching, it's 
                //expensive and bad code practice.
                if (convertToNumber){
	            	try {
	                    Object temp = new Integer(message);
	                    ((LiteralExpression) curExprssn).setLiteral(temp);
	                    currentState = "complete";
	                } catch (NumberFormatException nfe1) {
	                    try {
	                        Object temp = new Double(message);
	                        ((LiteralExpression) curExprssn).setLiteral(temp);
	                        currentState = "complete";
	                    } catch (NumberFormatException nfe2) {
	                        Object temp = message;
	                        ((LiteralExpression) curExprssn).setLiteral(temp);
	                        currentState = "complete";
	                    }
	                }
                }else{
                    Object temp = message;
                    ((LiteralExpression) curExprssn).setLiteral(temp);
                    currentState = "complete";
                }
            } else if (expFactory != null) {
                expFactory.message(message,convertToNumber);
            }
        } else if (expFactory != null) {
            expFactory.message(message,convertToNumber);
        }
    }

    /**
     * Gets geometry.
     *
     * @param geometry The geometry from the filter.
     *
     * @throws IllegalFilterException If there are problems creating
     *         expression.
     */
    public void geometry(Geometry geometry) throws IllegalFilterException {
        // Sets the geometry for the expression, as appropriate
        LOGGER.finer("got geometry: " + geometry.toString());

        //if(curExprssn.getType()==ExpressionDefault.LITERAL_GEOMETRY){
        //LOGGER.finer("got geometry: ");
        curExprssn = ff.createLiteralExpression();
        ((LiteralExpression) curExprssn).setLiteral(geometry);
        LOGGER.finer("set expression: " + curExprssn.toString());
        currentState = "complete";
        LOGGER.finer("set current state: " + currentState);

        //        }
    }

    /**
     * Creates and returns the expression.
     *
     * @return The expression currently held by this parser.
     *
     * @task REVISIT: shouldn't this check the readyFlag?
     */
    public Expression create() {
        LOGGER.finer("about to create expression: "
            + curExprssn.toString());

        return curExprssn;
    }

    /**
     * Sets the appropriate state.
     *
     * @param expression the expression being evaluated.
     *
     * @return <tt>leftValue</tt> if curExprssn is a mathExpression, an
     *         empty string if a literal or attribute, illegal expression
     *         thrown otherwise.
     *
     * @throws IllegalFilterException if the current expression is not math,
     *         attribute, or literal.
     */
    private static String setInitialState(Expression expression)
        throws IllegalFilterException {
        if (expression instanceof MathExpression) {
            return "leftValue";
        } else if ((expression instanceof AttributeExpression)
                || (expression instanceof LiteralExpression)) {
            return "";
        }else if(expression instanceof FunctionExpression)
        {
        	return "accumulate";  // start storing values!
        }
		else {
            throw new IllegalFilterException("Created illegal expression: "
                + expression.getClass().toString());
        }
    }

    /**
     * Converts the string representation of the expression to the
     * DefaultExpression short type.
     *
     * @param expType Type of filter for check.
     *
     * @return the short representation of the expression.
     */
    protected static short convertType(String expType) {
        // matches all filter types to the default logic type
        if (expType.equals("Add")) {
            return DefaultExpression.MATH_ADD;
        } else if (expType.equals("Sub")) {
            return DefaultExpression.MATH_SUBTRACT;
        } else if (expType.equals("Mul")) {
            return DefaultExpression.MATH_MULTIPLY;
        } else if (expType.equals("Div")) {
            return DefaultExpression.MATH_DIVIDE;
        } else if (expType.equals("PropertyName")) {
            return DefaultExpression.ATTRIBUTE_DOUBLE;
        } else if (expType.equals("Literal")) {
            return DefaultExpression.LITERAL_DOUBLE;
        }
        else if (expType.equals("Function")) {
            return DefaultExpression.FUNCTION;
        }

        return DefaultExpression.ATTRIBUTE_UNDECLARED;
    }
    
    /**
     * stolen from the DOM parser -- for a list of attributes, find the "name"
     * ie. for <Function name="geomLength"> return "geomLength"
     * NOTE: if someone uses <Function name="geomLength">  or <Function ogc:name="geomLength"> this will work,
     * if they use a different prefix, it will not. 
     * @param map
     */
    public String getFunctionName(Attributes map)
    {
	   String result = map.getValue("name");
	   if (result == null)
	   {
	   	   result = map.getValue("ogc:name"); // highly unlikely for this to happen.  But, it might...  
	   }
	   if (result == null)
	   {
	   	   result = map.getValue("ows:name"); // highly unlikely for this to happen.  But, it might...  
	   }
	   return result;
    }
}

