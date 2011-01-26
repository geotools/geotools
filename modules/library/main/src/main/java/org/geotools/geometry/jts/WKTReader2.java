/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *   (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *   (C) 2001, Vivid Solutions
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
 *    
 *    This is a port of the JTS WKTReader to handle SQL MM types such as Curve.
 *    We have subclassed so that our implementation can be used anywhere 
 *    a WKTReader is needed. We would of tried for more code reuse  except
 *    the base class has reduced everything to privatee methods.
 *    
 *    This class also contains code written by Mark Leslie for PostGIS while working
 *    at Refractions Reserach with whom we have a code contribution agreement.
 */
package org.geotools.geometry.jts;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.util.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import static java.lang.Math.*;
/**
 * Create a geometry from SQL Multi-Media Extension Well-Known Text which allows curves.
 * <p>
 * 
 * 
 *
 * @source $URL$
 * @version 1.7
 * @see WKTWriter
 */
public class WKTReader2 extends WKTReader {
    private static final String EMPTY = "EMPTY";

    private static final String COMMA = ",";

    private static final String L_PAREN = "(";

    private static final String R_PAREN = ")";

    private static final String NAN_SYMBOL = "NaN";

    private static final double EPSILON_SQLMM = 1.0e-8;

    private static final double M_PI = PI;
    private static final double M_PI_2 = PI/2.0;
    
    private GeometryFactory geometryFactory;

    private PrecisionModel precisionModel;

    private StreamTokenizer tokenizer;

    /**
     * Creates a reader that creates objects using the default {@link GeometryFactory}.
     */
    public WKTReader2() {
        this(JTSFactoryFinder.getGeometryFactory( null ));
    }

    /**
     * Creates a reader that creates objects using the given {@link GeometryFactory}.
     * 
     *@param geometryFactory
     *            the factory used to create <code>Geometry</code>s.
     */
    public WKTReader2(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
        precisionModel = geometryFactory.getPrecisionModel();
    }

    /**
     * Reads a Well-Known Text representation of a {@link Geometry} from a {@link String}.
     * 
     * @param wellKnownText
     *            one or more <Geometry Tagged Text>strings (see the OpenGIS Simple Features
     *            Specification) separated by whitespace
     * @return a <code>Geometry</code> specified by <code>wellKnownText</code>
     * @throws ParseException
     *             if a parsing problem occurs
     */
    public Geometry read(String wellKnownText) throws ParseException {
        StringReader reader = new StringReader(wellKnownText);
        try {
            return read(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * Reads a Well-Known Text representation of a {@link Geometry} from a {@link Reader}.
     * 
     *@param reader
     *            a Reader which will return a <Geometry Tagged Text> string (see the OpenGIS Simple
     *            Features Specification)
     *@return a <code>Geometry</code> read from <code>reader</code>
     *@throws ParseException
     *             if a parsing problem occurs
     */
    public Geometry read(Reader reader) throws ParseException {
        tokenizer = new StreamTokenizer(reader);
        // set tokenizer to NOT parse numbers
        tokenizer.resetSyntax();
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('A', 'Z');
        tokenizer.wordChars(128 + 32, 255);
        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('-', '-');
        tokenizer.wordChars('+', '+');
        tokenizer.wordChars('.', '.');
        tokenizer.whitespaceChars(0, ' ');
        tokenizer.commentChar('#');

        try {
            return readGeometryTaggedText();
        } catch (IOException e) {
            throw new ParseException(e.toString());
        }
    }

    /**
     * Returns the next array of <code>Coordinate</code>s in the stream.
     * 
     *@return the next array of <code>Coordinate</code>s in the stream, or an empty array if EMPTY
     *         is the next element returned by the stream.
     *@throws IOException
     *             if an I/O error occurs
     *@throws ParseException
     *             if an unexpected token was encountered
     */
    private Coordinate[] getCoordinates() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return new Coordinate[] {};
        }
        ArrayList coordinates = new ArrayList();
        coordinates.add(getPreciseCoordinate());
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            coordinates.add(getPreciseCoordinate());
            nextToken = getNextCloserOrComma();
        }
        Coordinate[] array = new Coordinate[coordinates.size()];
        return (Coordinate[]) coordinates.toArray(array);
    }

    private List<Coordinate> getCoordinateList(boolean openExpected) throws IOException, ParseException {
        String nextToken;
        if( openExpected ){
            nextToken = getNextEmptyOrOpener();
            if (nextToken.equals(EMPTY)) {
                return Collections.emptyList();
            }
        }        
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        coordinates.add(getPreciseCoordinate());
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            coordinates.add(getPreciseCoordinate());
            nextToken = getNextCloserOrComma();
        }
        Coordinate[] array = new Coordinate[coordinates.size()];
        return coordinates;
    }

    private Coordinate getPreciseCoordinate() throws IOException, ParseException {
        Coordinate coord = new Coordinate();
        coord.x = getNextNumber();
        coord.y = getNextNumber();
        if (isNumberNext()) {
            coord.z = getNextNumber();
        }
        precisionModel.makePrecise(coord);
        return coord;
    }

    private boolean isNumberNext() throws IOException {
        int type = tokenizer.nextToken();
        tokenizer.pushBack();
        return type == StreamTokenizer.TT_WORD;
    }

    /**
     * Parses the next number in the stream. Numbers with exponents are handled. <tt>NaN</tt> values
     * are handled correctly, and the case of the "NaN" token is not significant.
     * 
     *@param tokenizer
     *            tokenizer over a stream of text in Well-known Text format. The next token must be
     *            a number.
     *@return the next number in the stream
     *@throws ParseException
     *             if the next token is not a valid number
     *@throws IOException
     *             if an I/O error occurs
     */
    private double getNextNumber() throws IOException, ParseException {
        int type = tokenizer.nextToken();
        switch (type) {
        case StreamTokenizer.TT_WORD: {
            if (tokenizer.sval.equalsIgnoreCase(NAN_SYMBOL)) {
                return Double.NaN;
            } else {
                try {
                    return Double.parseDouble(tokenizer.sval);
                } catch (NumberFormatException ex) {
                    throw new ParseException("Invalid number: " + tokenizer.sval);
                }
            }
        }
        }
        parseError("number");
        return 0.0;
    }

    /**
     * Returns the next EMPTY or L_PAREN in the stream as uppercase text.
     * 
     *@return the next EMPTY or L_PAREN in the stream as uppercase text.
     *@throws ParseException
     *             if the next token is not EMPTY or L_PAREN
     *@throws IOException
     *             if an I/O error occurs
     */
    private String getNextEmptyOrOpener() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(EMPTY) || nextWord.equals(L_PAREN)) {
            return nextWord;
        }
        parseError(EMPTY + " or " + L_PAREN);
        return null;
    }

    /**
     * Returns the next R_PAREN or COMMA in the stream.
     * 
     *@return the next R_PAREN or COMMA in the stream
     *@throws ParseException
     *             if the next token is not R_PAREN or COMMA
     *@throws IOException
     *             if an I/O error occurs
     */
    private String getNextCloserOrComma() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(COMMA) || nextWord.equals(R_PAREN)) {
            return nextWord;
        }
        parseError(COMMA + " or " + R_PAREN);
        return null;
    }

    /**
     * Returns the next R_PAREN in the stream.
     * 
     *@return the next R_PAREN in the stream
     *@throws ParseException
     *             if the next token is not R_PAREN
     *@throws IOException
     *             if an I/O error occurs
     */
    private String getNextCloser() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(R_PAREN)) {
            return nextWord;
        }
        parseError(R_PAREN);
        return null;
    }

    /**
     * Returns the next word in the stream.
     * 
     *@return the next word in the stream as uppercase text
     *@throws ParseException
     *             if the next token is not a word
     *@throws IOException
     *             if an I/O error occurs
     */
    private String getNextWord() throws IOException, ParseException {
        int type = tokenizer.nextToken();
        switch (type) {
        case StreamTokenizer.TT_WORD:

            String word = tokenizer.sval;
            if (word.equalsIgnoreCase(EMPTY))
                return EMPTY;
            return word;

        case '(':
            return L_PAREN;
        case ')':
            return R_PAREN;
        case ',':
            return COMMA;
        }
        parseError("word");
        return null;
    }

    /**
     * Throws a formatted ParseException for the current token.
     * 
     * @param expected
     *            a description of what was expected
     * @throws ParseException
     * @throws AssertionFailedException
     *             if an invalid token is encountered
     */
    private void parseError(String expected) throws ParseException {
        // throws Asserts for tokens that should never be seen
        if (tokenizer.ttype == StreamTokenizer.TT_NUMBER)
            Assert.shouldNeverReachHere("Unexpected NUMBER token");
        if (tokenizer.ttype == StreamTokenizer.TT_EOL)
            Assert.shouldNeverReachHere("Unexpected EOL token");

        String tokenStr = tokenString();
        throw new ParseException("Expected " + expected + " but found " + tokenStr);
    }

    /**
     * Gets a description of the current token
     * 
     * @return a description of the current token
     */
    private String tokenString() {
        switch (tokenizer.ttype) {
        case StreamTokenizer.TT_NUMBER:
            return "<NUMBER>";
        case StreamTokenizer.TT_EOL:
            return "End-of-Line";
        case StreamTokenizer.TT_EOF:
            return "End-of-Stream";
        case StreamTokenizer.TT_WORD:
            return "'" + tokenizer.sval + "'";
        }
        return "'" + (char) tokenizer.ttype + "'";
    }

    /**
     * Creates a <code>Geometry</code> using the next token in the stream.
     * 
     *@return a <code>Geometry</code> specified by the next token in the stream
     *@throws ParseException
     *             if the coordinates used to create a <code>Polygon</code> shell and holes do not
     *             form closed linestrings, or if an unexpected token was encountered
     *@throws IOException
     *             if an I/O error occurs
     */
    private Geometry readGeometryTaggedText() throws IOException, ParseException {
        String type = null;

        try {
            type = getNextWord();
        } catch (IOException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }

        if (type.equals("POINT")) {
            return readPointText();
        } else if (type.equalsIgnoreCase("LINESTRING")) {
            return readLineStringText();
        } else if (type.equalsIgnoreCase("LINEARRING")) {
            return readLinearRingText();
        } else if (type.equalsIgnoreCase("POLYGON")) {
            return readPolygonText();
        } else if (type.equalsIgnoreCase("MULTIPOINT")) {
            return readMultiPointText();
        } else if (type.equalsIgnoreCase("MULTILINESTRING")) {
            return readMultiLineStringText();
        } else if (type.equalsIgnoreCase("MULTIPOLYGON")) {
            return readMultiPolygonText();
        } else if (type.equalsIgnoreCase("GEOMETRYCOLLECTION")) {
            return readGeometryCollectionText();
        } else if (type.equalsIgnoreCase("CIRCULARSTRING")) {
            return readCircularStringText();
        } else if (type.equalsIgnoreCase("COMPOUNDCURVE")) {
            return readCompoundCurveText();
        } else if (type.equalsIgnoreCase("CURVEPOLYGON")) {
            return readCurvePolygonText();
        }
        throw new ParseException("Unknown geometry type: " + type);
    }

    /**
     * Creates a <code>Point</code> using the next token in the stream.
     * 
     *@return a <code>Point</code> specified by the next token in the stream
     *@throws IOException
     *             if an I/O error occurs
     *@throws ParseException
     *             if an unexpected token was encountered
     */
    private Point readPointText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createPoint((Coordinate) null);
        }
        Point point = geometryFactory.createPoint(getPreciseCoordinate());
        getNextCloser();
        return point;
    }

    /**
     * Creates a <code>LineString</code> using the next token in the stream.
     * 
     *@return a <code>LineString</code> specified by the next token in the stream
     *@throws IOException
     *             if an I/O error occurs
     *@throws ParseException
     *             if an unexpected token was encountered
     */
    private LineString readLineStringText() throws IOException, ParseException {
        return geometryFactory.createLineString(getCoordinates());
    }

    /**
     * Creates a <code>LineString</code> using the next token in the stream.
     * 
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private LineString readCircularStringText() throws IOException, ParseException {
        List<Coordinate> coordinates = getCoordinateList( true );
        List<Coordinate> segmentized;
        if (coordinates.size() < 3) {
            segmentized = coordinates;
        } else {
            segmentized = new ArrayList<Coordinate>();
            for (int i = 0; i < coordinates.size() - 1; i += 2) {
                Coordinate p1 = coordinates.get(i);
                Coordinate p2 = coordinates.get(i + 1);
                Coordinate p3 = coordinates.get(i + 2);

                List<Coordinate> segments = circularSegmentize(p1, p2, p3);
                segmentized.addAll(segments.subList(0, segments.size() - 1));
            }
            segmentized.add(coordinates.get(coordinates.size() - 1));
        }
        // we can now process these coordinates based on the current precision model
        Coordinate array[] = segmentized.toArray(new Coordinate[segmentized.size()]);

        return geometryFactory.createLineString(array);

    }

    /**
     * Constructs a series of segments based on the provided three points. The routine is based on
     * the JTS Buffer code which devides a circle into 128 segments.
     * 
     * @param p1
     * @param p2
     * @param p3
     * @return List of Coordinates forming a set number of segments
     */
    /*
    private List<Coordinate> circularSegmentizeSimple(Coordinate p1, Coordinate p2, Coordinate p3) {
        List<Coordinate> curve = new ArrayList<Coordinate>();
        curve.add(p1);
        curve.add(p2);
        curve.add(p3);

        return curve;
    }
    */

    private List<Coordinate> circularSegmentize(Coordinate p1, Coordinate p2, Coordinate p3) {
        Coordinate center;

        double centerX, centerY, radius;
        double temp, bc, cd, determinate;
        
        /* Closed circle */
        if (abs(p1.x - p3.x) < EPSILON_SQLMM && abs(p1.y - p3.y) < EPSILON_SQLMM) {
            centerX = p1.x + (p2.x - p1.x) / 2.0;
            centerY = p1.y + (p2.y - p1.y) / 2.0;
            center = new Coordinate();
            center.x = centerX;
            center.y = centerY;

            radius = sqrt((centerX - p1.x) * (centerX - p1.x) + (centerY - p1.y) * (centerY - p1.y));
        } else {
            temp = p2.x * p2.x + p2.y * p2.y;
            bc = (p1.x * p1.x + p1.y * p1.y - temp) / 2.0;
            cd = (temp - p3.x * p3.x - p3.y * p3.y) / 2.0;
            determinate = (p1.x - p2.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p2.y);

            /* Check colinearity */
            if (abs(determinate) < EPSILON_SQLMM) {
                List<Coordinate> curve = new ArrayList<Coordinate>();
                curve.add(p1);
                // curve.add( p1 ); // forms a straight line not needed
                curve.add(p3);

                return curve;
            }
            determinate = 1.0 / determinate;
            centerX = (bc * (p2.y - p3.y) - cd * (p1.y - p2.y)) * determinate;
            centerY = ((p1.x - p2.x) * cd - (p2.x - p3.x) * bc) * determinate;
            center = new Coordinate();
            center.x = centerX;
            center.y = centerY;

            radius = sqrt((centerX - p1.x) * (centerX - p1.x) + (centerY - p1.y) * (centerY - p1.y));
        }
        return circularSegmentize( p1, p2, p3, center, radius, 32 );
    }

    private List<Coordinate> circularSegmentize(Coordinate p1, Coordinate p2, Coordinate p3, Coordinate center,
            double radius, int perQuad) {
        List<Coordinate> result;

        Coordinate pbuf = new Coordinate();
        int ptcount;
        
        Coordinate pt;
        
        double sweep = 0.0,
               angle = 0.0,
               increment = 0.0;
        double a1, a2, a3, i;
        
        if(radius < 0)
        {
            // does not form a circle
            result = new ArrayList<Coordinate>();
            result.add(p1);
            result.add(p2);
            return result;
        }
 
        a1 = atan2(p1.y - center.y, p1.x - center.x);
        a2 = atan2(p2.y - center.y, p2.x - center.x);
        a3 = atan2(p3.y - center.y, p3.x - center.x);
  
        if(abs(p1.x - p3.x) < EPSILON_SQLMM
                        && abs(p1.y - p3.y) < EPSILON_SQLMM)
        {
                sweep = 2*M_PI;
        }
        /* Clockwise */
        else if(a1 > a2 && a2 > a3)
        {
                sweep = a3 - a1;
        }
        /* Counter-clockwise */
        else if(a1 < a2 && a2 < a3)
        {
                sweep = a3 - a1;
        }
        /* Clockwise, wrap */
        else if((a1 < a2 && a1 > a3) || (a2 < a3 && a1 > a3))
        {
                sweep = a3 - a1 + 2*M_PI;
        }
        /* Counter-clockwise, wrap */
        else if((a1 > a2 && a1 < a3) || (a2 > a3 && a1 < a3))
        {
                sweep = a3 - a1 - 2*M_PI;
        }
        else
        {
                sweep = 0.0;
        }
         
        ptcount = (int) ceil(abs(perQuad * sweep / M_PI_2));
       
        result = new ArrayList<Coordinate>( ptcount );
 
        increment = M_PI_2 / perQuad;
        if(sweep < 0) increment *= -1.0;
        angle = a1;

        result.add( p1 ); // start with first point
        
        for(i = 0; i < ptcount - 1; i++)
        {
            pt = new Coordinate();
            result.add( pt );
            
            angle += increment;
            if(increment > 0.0 && angle > M_PI) angle -= 2*M_PI;
            if(increment < 0.0 && angle < -1*M_PI) angle -= 2*M_PI;
            
            pt.x = center.x + radius*cos(angle);
            pt.y = center.y + radius*sin(angle);
            /*
             //
             // update this code to handle interopolation of z and m values
             //
            if((sweep > 0 && angle < a2) || (sweep < 0 && angle > a2))
            {
                if((sweep > 0 && a1 < a2) || (sweep < 0 && a1 > a2))
                {
                        pbuf.z = interpolate_arc(angle, p1->z, a1, p2->z, a2);
                        pbuf.m = interpolate_arc(angle, p1->m, a1, p2->m, a2);
                }
                else
                {
                    if(sweep > 0)
                    {
                        pbuf.z = interpolate_arc(angle, p1->z, a1-(2*M_PI), p2->z, a2);
                        pbuf.m = interpolate_arc(angle, p1->m, a1-(2*M_PI), p2->m, a2);
                    }
                    else
                    {
                        pbuf.z = interpolate_arc(angle, p1->z, a1+(2*M_PI), p2->z, a2);
                        pbuf.m = interpolate_arc(angle, p1->m, a1+(2*M_PI), p2->m, a2);
                    }
                }
            }
            else
            {
                if((sweep > 0 && a2 < a3) || (sweep < 0 && a2 > a3))
                {
                    pbuf.z = interpolate_arc(angle, p2->z, a2, p3->z, a3);
                    pbuf.m = interpolate_arc(angle, p2->m, a2, p3->m, a3);
                }
                else
                {
                    if(sweep > 0)
                    {
                        pbuf.z = interpolate_arc(angle, p2->z, a2-(2*M_PI), p3->z, a3);
                        pbuf.m = interpolate_arc(angle, p2->m, a2-(2*M_PI), p3->m, a3);
                    }
                    else
                    {
                        pbuf.z = interpolate_arc(angle, p2->z, a2+(2*M_PI), p3->z, a3);
                        pbuf.m = interpolate_arc(angle, p2->m, a2+(2*M_PI), p3->m, a3);
                    }
                }
            }
            */
        }
        result.add( p3 );
 
        return result;
    }

    private LineString readCompoundCurveText()
            throws IOException, ParseException {
        List<LineString> lineStrings = getLineStrings();
        
        if( lineStrings.isEmpty() ){
            // return an empty lineString?
            return geometryFactory.createLineString(new Coordinate[] {});
        }
        if( lineStrings.size() == 1 ){
            return lineStrings.get(0);
        }
        // we must gather these all into one - removing duplicates!
        List<Coordinate> coords = new ArrayList<Coordinate>();
        for( LineString segment : lineStrings ){
            List<Coordinate> segmentCoordinates = Arrays.asList(segment.getCoordinates());
            coords.addAll( segmentCoordinates.subList(0, segmentCoordinates.size()-1 ));            
        }
        LineString last = lineStrings.get( lineStrings.size()-1);
        Coordinate end = last.getCoordinateN( last.getNumPoints()-1);
        coords.add( end );
        
        return geometryFactory.createLineString( coords.toArray( new Coordinate[ coords.size()]));
    }
    
    /**
     * Handles mixed line string notation - either LineString (the default) or CircularCurve.
     * Isolated as a seperate method as I think we will need to call this from the polygon code.
     * 
     * @return List of LineString (defined in a mixed format)
     * @throws IOException
     * @throws ParseException
     */
    List<LineString> getLineStrings() throws IOException, ParseException {
        ArrayList<LineString> lineStrings = new ArrayList<LineString>();
        String nextWord = getNextEmptyOrOpener();
        if (nextWord.equals(EMPTY)) {
            return lineStrings;
        }
        // must be an opener!
        nextWord = COMMA;
        while( nextWord.equals( COMMA )){
            nextWord = getNextWord();
            if( nextWord.equals(L_PAREN) ){
                List<Coordinate> coords = getCoordinateList(false);                
                LineString lineString  = geometryFactory.createLineString( coords.toArray( new Coordinate[coords.size()]));
                lineStrings.add(lineString);
            }
            else if( nextWord.equalsIgnoreCase("CIRCULARSTRING")){
                LineString circularString = readCircularStringText();
                lineStrings.add(circularString);
            }
            nextWord = getNextCloserOrComma();
        }
        return lineStrings;
    }
    /**
     * This method will read a LineString, CircularString or CompoundCurve and return the result as a LinearRing.
     * @return LinearRing
     * <p>
     * This method expects either "EMPTY", "(", "CIRCULARSTRING", or "COMPOIUNDCURVE" to start out with.
     * 
     * @throws IOException
     * @throws ParseException
     */
    private LinearRing readCurvedLinearRingText() throws IOException, ParseException {
        Coordinate ring[] = null;
        
        String nextWord = getNextWord();        
        if( nextWord.equals(L_PAREN) ){
            List<Coordinate> coords = getCoordinateList(false);
            ring = coords.toArray( new Coordinate[coords.size()]);            
        }
        else if( nextWord.equalsIgnoreCase("CIRCULARSTRING")){
            LineString circularString = readCircularStringText();
            ring = circularString.getCoordinates();
        }
        else if( nextWord.equalsIgnoreCase("COMPOUNDCURVE")){
            LineString circularString = readCompoundCurveText();
            ring = circularString.getCoordinates();
        }
        else {
            parseError(L_PAREN + ", CIRCULARSTRING or COMPOUNDCURVE");
        }        
        return geometryFactory.createLinearRing( ring );
    }
    
    /**
     * Creates a <code>LinearRing</code> using the next token in the stream.
     * 
     *@return a <code>LinearRing</code> specified by the next token in the stream
     *@throws IOException
     *             if an I/O error occurs
     *@throws ParseException
     *             if the coordinates used to create the <code>LinearRing</code> do not form a
     *             closed linestring, or if an unexpected token was encountered
     */
    private LinearRing readLinearRingText() throws IOException, ParseException {
        return geometryFactory.createLinearRing(getCoordinates());
    }

    /**
     * Creates a <code>MultiPoint</code> using the next token in the stream.
     * 
     *@return a <code>MultiPoint</code> specified by the next token in the stream
     *@throws IOException
     *             if an I/O error occurs
     *@throws ParseException
     *             if an unexpected token was encountered
     */
    private MultiPoint readMultiPointText() throws IOException, ParseException {
        return geometryFactory.createMultiPoint(toPoints(getCoordinates()));
    }

    /**
     * Creates an array of <code>Point</code>s having the given <code>Coordinate</code> s.
     * 
     *@param coordinates
     *            the <code>Coordinate</code>s with which to create the <code>Point</code>s
     *@return <code>Point</code>s created using this <code>WKTReader</code> s
     *         <code>GeometryFactory</code>
     */
    private Point[] toPoints(Coordinate[] coordinates) {
        ArrayList points = new ArrayList();
        for (int i = 0; i < coordinates.length; i++) {
            points.add(geometryFactory.createPoint(coordinates[i]));
        }
        return (Point[]) points.toArray(new Point[] {});
    }

    /**
     * Creates a <code>Polygon</code> using the next token in the stream.
     * 
     *@return a <code>Polygon</code> specified by the next token in the stream
     *@throws ParseException
     *             if the coordinates used to create the <code>Polygon</code> shell and holes do not
     *             form closed linestrings, or if an unexpected token was encountered.
     *@throws IOException
     *             if an I/O error occurs
     */
    private Polygon readPolygonText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createPolygon(geometryFactory
                    .createLinearRing(new Coordinate[] {}), new LinearRing[] {});
        }
        ArrayList holes = new ArrayList();
        LinearRing shell = readLinearRingText();
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            LinearRing hole = readLinearRingText();
            holes.add(hole);
            nextToken = getNextCloserOrComma();
        }
        LinearRing[] array = new LinearRing[holes.size()];
        return geometryFactory.createPolygon(shell, (LinearRing[]) holes.toArray(array));
    }
    
    private Polygon readCurvePolygonText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createPolygon(geometryFactory
                    .createLinearRing(new Coordinate[] {}), new LinearRing[] {});
        }
        if( !nextToken.equals( L_PAREN )){
            parseError("Ring expected");
        }
        LinearRing shell = readCurvedLinearRingText();
        ArrayList holes = new ArrayList();       
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            LinearRing hole = readCurvedLinearRingText();
            holes.add(hole);    
            nextToken = getNextCloserOrComma();
        }
        LinearRing[] array = new LinearRing[holes.size()];
        return geometryFactory.createPolygon(shell, (LinearRing[]) holes.toArray(array));
    }
    /**
     * Creates a <code>MultiLineString</code> using the next token in the stream.
     * 
     *@return a <code>MultiLineString</code> specified by the next token in the stream
     *@throws IOException
     *             if an I/O error occurs
     *@throws ParseException
     *             if an unexpected token was encountered
     */
    private com.vividsolutions.jts.geom.MultiLineString readMultiLineStringText()
            throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createMultiLineString(new LineString[] {});
        }
        ArrayList lineStrings = new ArrayList();
        LineString lineString = readLineStringText();
        lineStrings.add(lineString);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            lineString = readLineStringText();
            lineStrings.add(lineString);
            nextToken = getNextCloserOrComma();
        }
        LineString[] array = new LineString[lineStrings.size()];
        return geometryFactory.createMultiLineString((LineString[]) lineStrings.toArray(array));
    }

    /**
     * Creates a <code>MultiPolygon</code> using the next token in the stream.
     * 
     *@return a <code>MultiPolygon</code> specified by the next token in the stream, or if if the
     *         coordinates used to create the <code>Polygon</code> shells and holes do not form
     *         closed linestrings.
     *@throws IOException
     *             if an I/O error occurs
     *@throws ParseException
     *             if an unexpected token was encountered
     */
    private MultiPolygon readMultiPolygonText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createMultiPolygon(new Polygon[] {});
        }
        ArrayList polygons = new ArrayList();
        Polygon polygon = readPolygonText();
        polygons.add(polygon);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            polygon = readPolygonText();
            polygons.add(polygon);
            nextToken = getNextCloserOrComma();
        }
        Polygon[] array = new Polygon[polygons.size()];
        return geometryFactory.createMultiPolygon((Polygon[]) polygons.toArray(array));
    }

    /**
     * Creates a <code>GeometryCollection</code> using the next token in the stream.
     * 
     * @return a <code>GeometryCollection</code> specified by the next token in the stream
     * @throws ParseException
     *             if the coordinates used to create a <code>Polygon</code> shell and holes do not
     *             form closed linestrings, or if an unexpected token was encountered
     * @throws IOException
     *             if an I/O error occurs
     */
    private GeometryCollection readGeometryCollectionText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createGeometryCollection(new Geometry[] {});
        }
        ArrayList geometries = new ArrayList();
        Geometry geometry = readGeometryTaggedText();
        geometries.add(geometry);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            geometry = readGeometryTaggedText();
            geometries.add(geometry);
            nextToken = getNextCloserOrComma();
        }
        Geometry[] array = new Geometry[geometries.size()];
        return geometryFactory.createGeometryCollection((Geometry[]) geometries.toArray(array));
    }

}
