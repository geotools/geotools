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
package org.geotools.geometry.text;


import org.geotools.geometry.GeometryBuilder;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.complex.ComplexFactory;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.Surface;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * This class is used to parse well known text (WKT) which describes an
 * ISO 19107 Geometry. The grammar described comes from the ISO 19125-1
 * spec which describes feature geometry. It doesn't seem to exactly mesh up
 * with the geometry as described in 19107 so not all of the grammar is supported.
 * <p/>
 *
 * The types in the WKT format, and their mappings:
 * <ul>
 * <li>
 * POINT            org.opengis.geometry.primitive.Point
 * </li>
 * <li>
 * LINESTRING       org.opengis.geometry.primitive.Curve
 * </li>
 * <li>
 * POLYGON          org.opengis.geometry.primitive.Surface
 * </li>
 * <li>
 * MULTIPOINT       org.opengis.geometry.coordinate.aggregate.MultiPoint
 *                  Note that there is no factory method for MultiPoint.
 *                  <br>
 *                  For now, to keep implementation-independance I'm returning it as a List
 * </li>
 * <li>
 * MULTILINESTRING  no matching type in the GeoAPI interfaces
 *                  Could also be returned as list.
 *                  <br>
 *                  Not handled for now
 * </li>
 * <li>
 * MULTIPOLYGON     no matching type in the GeoAPI interfaces
 *                  Could also be returned as list.
 *                  <br>
 *                  Not handled for now
 * </li>
 * </ul>>
 * Please note that this parser is not thread safe; you can however reuse the parser.
 * 
 * @author Jody Garnett
 * @author Joel Skelton
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 */
public class WKTParser {

    private static final String EMPTY = "EMPTY";
    private static final String COMMA = ",";
    private static final String L_PAREN = "(";
    private static final String R_PAREN = ")";
    
    private GeometryFactory geometryFactory;
    private PrimitiveFactory primitiveFactory;
    private PositionFactory positionFactory;
    private AggregateFactory aggregateFactory;


    public WKTParser(GeometryBuilder builder) {
        this( builder.getGeometryFactory(), builder.getPrimitiveFactory(), builder.getPositionFactory(), builder.getAggregateFactory() );
    }
    /**
     * Constructor takes pre-created geometry and primitive factories that will be used to
     * parse the Well Known Text (WKT). The geometries created from the WKT will be created
     * in the <code>CoordinateReferenceSystem</code>
     *
     * @param geometryFactory A <code>GeometryFactory</code> created with a <code>CoordinateReferenceSystem</code> and <code>PrecisionModel</code>
     * @param primitiveFactory A <code>PrimitiveFactory</code> created with the same crs and precision as above
     * @param positionFactory A <code>PositionFactory</code> created with the same crs and precision as above
     * @param aggregateFactory A <Code>AggregateFactory</code> created with the same crs and precision as above
     */
    public WKTParser(GeometryFactory geometryFactory, PrimitiveFactory primitiveFactory, PositionFactory positionFactory, AggregateFactory aggregateFactory) {
        this.geometryFactory = geometryFactory;
        this.primitiveFactory = primitiveFactory;
        this.positionFactory = positionFactory;
        this.aggregateFactory = aggregateFactory;
    }
    /**
     * Provide a GeometryFactory for the parser.
     * <p>
     * Should be called prior to use.
     * @param factory
     */
    public void setFactory( GeometryFactory factory){
        this.geometryFactory = factory;
    }

    /**
     * Provide a PrimitiveFactory for the parser.
     * <p>
     * Should be called prior to use.
     * @param factory
     */
    public void setFactory( PrimitiveFactory factory){
        this.primitiveFactory = factory;
    }
    /**
     * Provide a PositionFactory for the parser.
     * <p>
     * Should be called prior to use.
     * @param factory
     */
    public void setFactory( PositionFactory factory){
        this.positionFactory = factory;
    }
    
    /**
     * Takes a string containing well known text geometry description and
     * wraps it in a Reader which is then passed on to parseWKT for handling.
     *
     * @param text A string containing the well known text to be parsed.
     * @return Geometry indicated by text (as created with current factories)
     */
    public Geometry parse(String text) throws ParseException {
        return read(new StringReader(text));
    }

    /**
     * Reads a Well-Known Text representation of a geometry
     * from a {@link Reader}.
     *
     * @param reader a Reader which will return a [Geometry Tagged Text]
     *               string (see the OpenGIS Simple Features Specification)
     * @return a <code>Geometry</code> read from <code>reader</code>
     * @throws ParseException if a parsing problem occurs
     */
    public Geometry read(Reader reader) throws ParseException {
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        setUpTokenizer(tokenizer);

        try {
            return readGeometryTaggedText(tokenizer);
        } catch (IOException e) {
            throw new ParseException(e.toString(), tokenizer.lineno());
        }
    }

    /**
     * Sets up a {@link StreamTokenizer} for use in parsing the geometry text.
     *
     * @param tokenizer A <code>StreamTokenizer</code>
     */
    private void setUpTokenizer(StreamTokenizer tokenizer) {
        final int char128 = 128;
        final int skip32 = 32;
        final int char255 = 255;
        // set tokenizer to NOT parse numbers
        tokenizer.resetSyntax();
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('A', 'Z');
        tokenizer.wordChars(char128 + skip32, char255);
        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('-', '-');
        tokenizer.wordChars('+', '+');
        tokenizer.wordChars('.', '.');
        tokenizer.whitespaceChars(0, ' ');
        tokenizer.commentChar('#');
    }


    /**
     * Creates a <code>Geometry</code> using the next token in the stream.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next tokens must form a &lt;Geometry Tagged Text&gt;.
     * @return a <code>Object</code> of the correct type for the next item
     *         in the stream
     * @throws ParseException if the coordinates used to create a <code>Polygon</code>
     *                        shell and holes do not form closed linestrings, or if an unexpected
     *                        token was encountered
     * @throws IOException    if an I/O error occurs
     */
    private Geometry readGeometryTaggedText(StreamTokenizer tokenizer) throws IOException, ParseException {
        String type = getNextWord(tokenizer);
        if (type.equals("POINT")) {
            return readPointText(tokenizer);
        }  else if (type.equalsIgnoreCase("LINESTRING")) {
            return readLineStringText(tokenizer);
        } else if (type.equalsIgnoreCase("LINEARRING")) {
        	return readLinearRingText(tokenizer);
        }  else if (type.equalsIgnoreCase("POLYGON")) {
            return readPolygonText(tokenizer);
        } else if (type.equalsIgnoreCase("MULTIPOINT")) {
            return readMultiPointText(tokenizer);
        } else if (type.equalsIgnoreCase("MULTIPOLYGON")) {
            return readMultiPolygonText(tokenizer);
        } else if (type.equalsIgnoreCase("GEOMETRYCOLLECTION")) {
        	return readGeometryCollectionText(tokenizer);
        } else if (type.equalsIgnoreCase("MULTILINESTRING")) {
        	return readMultiLineStringText(tokenizer);
        }
        throw new ParseException("Unknown geometry type: " + type, tokenizer.lineno());
    }


    /**
     * Returns a list of DirectPosition objects which it read from
     * the StreamTokenizer
     *
     * @param tokenizer
     * @return a <code>List\<DirectPosition\></code>
     * @throws IOException
     * @throws ParseException
     */
    private List getCoordinates(StreamTokenizer tokenizer)
            throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener(tokenizer);
        List coordinates = new ArrayList();
        if (!nextToken.equals(EMPTY)) {
            coordinates.add(getPreciseCoordinate(tokenizer));
            nextToken = getNextCloserOrComma(tokenizer);
            while (nextToken.equals(COMMA)) {
                coordinates.add(getPreciseCoordinate(tokenizer));
                nextToken = getNextCloserOrComma(tokenizer);
            }
        }
        return coordinates;
    }

    /**
     * Parse a single coordinate from a <code>StreamTokenizer</code>
     *
     * @param tokenizer
     * @return a single DirectPosition
     * @throws IOException
     * @throws ParseException
     */
    private DirectPosition getPreciseCoordinate(StreamTokenizer tokenizer)
            throws IOException, ParseException {
        DirectPosition pos = geometryFactory.createDirectPosition();
        pos.setOrdinate(0, getNextNumber(tokenizer));
        pos.setOrdinate(1, getNextNumber(tokenizer));
        if (isNumberNext(tokenizer)) {
            pos.setOrdinate(1, getNextNumber(tokenizer));
        }
        return pos;
    }


    private boolean isNumberNext(StreamTokenizer tokenizer) throws IOException {
        int type = tokenizer.nextToken();
        tokenizer.pushBack();
        return type == StreamTokenizer.TT_WORD;
    }

    /**
     * Parses the next number in the stream.
     * Numbers with exponents are handled.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next token must be a number.
     * @return the next number in the stream
     * @throws ParseException if the next token is not a valid number
     * @throws IOException    if an I/O error occurs
     */
    private double getNextNumber(StreamTokenizer tokenizer) throws IOException,
            ParseException {
        int type = tokenizer.nextToken();
        switch (type) {
            case StreamTokenizer.TT_WORD: {
                try {
                    return Double.parseDouble(tokenizer.sval);
                } catch (NumberFormatException ex) {
                    throw new ParseException("Invalid number: " + tokenizer.sval, tokenizer.lineno());
                }
            }
            default:
        }
        parseError("number", tokenizer);
        return 0.0;
    }

    /**
     * Returns the next EMPTY or L_PAREN in the stream as uppercase text.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next token must be EMPTY or L_PAREN.
     * @return the next EMPTY or L_PAREN in the stream as uppercase
     *         text.
     * @throws ParseException if the next token is not EMPTY or L_PAREN
     * @throws IOException    if an I/O error occurs
     */
    private String getNextEmptyOrOpener(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextWord = getNextWord(tokenizer);
        if (nextWord.equals(EMPTY) || nextWord.equals(L_PAREN)) {
            return nextWord;
        }
        parseError(EMPTY + " or " + L_PAREN, tokenizer);
        return null;
    }

    /**
     * Returns the next R_PAREN or COMMA in the stream.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next token must be R_PAREN or COMMA.
     * @return the next R_PAREN or COMMA in the stream
     * @throws ParseException if the next token is not R_PAREN or COMMA
     * @throws IOException    if an I/O error occurs
     */
    private String getNextCloserOrComma(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextWord = getNextWord(tokenizer);
        if (nextWord.equals(COMMA) || nextWord.equals(R_PAREN)) {
            return nextWord;
        }
        parseError(COMMA + " or " + R_PAREN, tokenizer);
        return null;
    }

    /**
     * Returns the next R_PAREN in the stream.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next token must be R_PAREN.
     * @return the next R_PAREN in the stream
     * @throws ParseException if the next token is not R_PAREN
     * @throws IOException    if an I/O error occurs
     */
    private String getNextCloser(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextWord = getNextWord(tokenizer);
        if (nextWord.equals(R_PAREN)) {
            return nextWord;
        }
        parseError(R_PAREN, tokenizer);
        return null;
    }

    /**
     * Returns the next word in the stream.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next token must be a word.
     * @return the next word in the stream as uppercase text
     * @throws ParseException if the next token is not a word
     * @throws IOException    if an I/O error occurs
     */
    private String getNextWord(StreamTokenizer tokenizer) throws IOException, ParseException {
        int type = tokenizer.nextToken();
        String value;
        switch (type) {
            case StreamTokenizer.TT_WORD:
                String word = tokenizer.sval;
                if (word.equalsIgnoreCase(EMPTY)) {
                    value = EMPTY;
                }
                value = word;
                break;
            case'(':
                value = L_PAREN;
                break;
            case')':
                value = R_PAREN;
                break;
            case',':
                value = COMMA;
                break;
            default:
                parseError("word", tokenizer);
                value = null;
                break;
        }
        return value;
    }

    /**
     * Throws a formatted ParseException for the current token.
     *
     * @param expected a description of what was expected
     * @throws ParseException
     */
    private void parseError(String expected, StreamTokenizer tokenizer)
            throws ParseException {
        String tokenStr = tokenString(tokenizer);
        throw new ParseException("Expected " + expected + " but found " + tokenStr, 0);
    }

    /**
     * Gets a description of the current token
     *
     * @return a description of the current token
     */
    private String tokenString(StreamTokenizer tokenizer) {
        switch (tokenizer.ttype) {
            case StreamTokenizer.TT_NUMBER:
                return "<NUMBER>";
            case StreamTokenizer.TT_EOL:
                return "End-of-Line";
            case StreamTokenizer.TT_EOF:
                return "End-of-Stream";
            case StreamTokenizer.TT_WORD:
                return "'" + tokenizer.sval + "'";
            default:
        }
        return "'" + (char) tokenizer.ttype + "'";
    }

    /**
     * Creates a <code>Point</code> using the next token in the stream.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next tokens must form a &lt;Point Text&gt;.
     * @return a <code>Point</code> specified by the next token in
     *         the stream
     * @throws IOException    if an I/O error occurs
     * @throws ParseException if an unexpected token was encountered
     */
    private Point readPointText(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener(tokenizer);
        if (nextToken.equals(EMPTY)) {
            return primitiveFactory.createPoint(new double[2]);
        }
        Point point = primitiveFactory.createPoint(getPreciseCoordinate(tokenizer));
        getNextCloser(tokenizer);
        return point;
    }

    /**
     * Creates a <code>LineString</code> using the next token in the stream.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next tokens must form a &lt;LineString Text&gt;.
     * @return a <code>LineString</code> specified by the next
     *         token in the stream
     * @throws IOException    if an I/O error occurs
     * @throws ParseException if an unexpected token was encountered
     */
    private Curve readLineStringText(StreamTokenizer tokenizer) throws IOException, ParseException {
        List coordList = getCoordinates(tokenizer);
        LineString lineString = geometryFactory.createLineString(coordList);
        List curveSegmentList = Collections.singletonList(lineString);
        Curve curve = primitiveFactory.createCurve(curveSegmentList);
        return curve;
    }

    /**
     * Creates a <code>Curve</code> using the next token in the stream.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next tokens must form a &lt;LineString Text&gt;.
     * @return a <code>Curve</code> specified by the next
     *         token in the stream
     * @throws IOException    if an I/O error occurs
     * @throws ParseException if the coordinates used to create the <code>Curve</code>
     *                        do not form a closed linestring, or if an unexpected token was
     *                        encountered
     */
    private Curve readLinearRingText(StreamTokenizer tokenizer)
            throws IOException, ParseException {
        List coordList = getCoordinates(tokenizer);
        LineString lineString = geometryFactory.createLineString(coordList);
        List curveSegmentList = Collections.singletonList(lineString);
        Curve curve = primitiveFactory.createCurve(curveSegmentList);
        return curve;
        //List curveList = Collections.singletonList(curve);
        //return primitiveFactory.createRing(curveList);
    }

    /**
     * Creates an array of <code>Point</code>s having the given <code>Coordinate</code>s.
     *
     * @param coordinates the <code>Coordinate</code>s with which to create the
     *                    <code>Point</code>s
     * @return <code>Point</code>s created using this <code>WKTReader</code>
     *         s <code>GeometryFactory</code>
     */
    private List toPoints(List coordinates) {
        List points = new ArrayList();
        for (int i = 0; i < coordinates.size(); i++) {
            points.add(positionFactory.createPosition((Point)coordinates.get(i)));
        }
        return points;
    }

    /**
     * Creates a <code>SurfaceBoundary</code> using the next token in the stream.
     *
     * @param tokenizer tokenizer over a stream of text in Well-known Text
     *                  format. The next tokens must form a &lt;Polygon Text&gt;.
     * @return a <code>Surface</code> specified by the vertices in the stream
     * @throws ParseException if the coordinates used to create the <code>Polygon</code>
     *                        shell and holes do not form closed linestrings, or if an unexpected
     *                        token was encountered.
     * @throws IOException    if an I/O error occurs
     */
    private Surface readPolygonText(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener(tokenizer);
        if (nextToken.equals(EMPTY)) {
            return null;
        }
        Curve curve = readLinearRingText(tokenizer);
        List curveList = Collections.singletonList(curve);
        Ring shell = primitiveFactory.createRing(curveList);
        //Ring shell = readLinearRingText(tokenizer);
        List holes = new ArrayList();
        nextToken = getNextCloserOrComma(tokenizer);
        while (nextToken.equals(COMMA)) {
        	Curve holecurve = readLinearRingText(tokenizer);
            List holeList = Collections.singletonList(holecurve);
            Ring hole = primitiveFactory.createRing(holeList);
            //Ring hole = readLinearRingText(tokenizer);
            holes.add(hole);
            nextToken = getNextCloserOrComma(tokenizer);
        }
        SurfaceBoundary sb = primitiveFactory.createSurfaceBoundary(shell, holes);
        return primitiveFactory.createSurface(sb);
    }

    /**
     * Creates a {@code MultiPrimitive} using the next token in the stream.
     *
     * @param tokenizer tokenizer on top of a stream of text in Well-known Text
     *                  format. The next tokens must form a &lt;Polygon Text&gt;.
     * @return a <code>MultiPrimitive</code> specified by the next token
     *         in the stream
     * @throws ParseException if the coordinates used to create the <code>Polygon</code>
     *                        shell and holes do not form closed linestrings, or if an unexpected
     *                        token was encountered.
     * @throws IOException    if an I/O error occurs
     */
    private MultiPrimitive readMultiPolygonText(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener(tokenizer);        
        if (nextToken.equals(EMPTY)) {
        	return null;
        }
        MultiPrimitive multi = geometryFactory.createMultiPrimitive();
        Surface surface  = readPolygonText(tokenizer);
        //multi.getElements().add(surface);
        Set elements = multi.getElements();
        elements.add(surface);
        nextToken = getNextCloserOrComma(tokenizer);
        while (nextToken.equals(COMMA)) {
            surface = readPolygonText(tokenizer);
            //multi.getElements().add(surface);
            elements.add(surface);
            nextToken = getNextCloserOrComma(tokenizer);
        }
        return multi;
    }
    
    /**
     * Creates a {@code MultiPrimitive} using the next token in the stream.
     *
     * @param tokenizer tokenizer on top of a stream of text in Well-known Text
     *                  format. The next tokens must form a &lt;Point Text&gt;.
     * @return a <code>MultiPrimitive</code> specified by the next token
     *         in the stream
     * @throws ParseException if the coordinates used to create the <code>Polygon</code>
     *                        shell and holes do not form closed linestrings, or if an unexpected
     *                        token was encountered.
     * @throws IOException    if an I/O error occurs
     */
    private MultiPrimitive readMultiPointText(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener(tokenizer);        
        if (nextToken.equals(EMPTY)) {
        	return null;
        }
        MultiPrimitive multi = geometryFactory.createMultiPrimitive();        
        Point point = primitiveFactory.createPoint(getPreciseCoordinate(tokenizer));
        //multi.getElements().add(point);
        Set elements = multi.getElements();
        elements.add(point);
        nextToken = getNextCloserOrComma(tokenizer);
        while (nextToken.equals(COMMA)) {
        	point = primitiveFactory.createPoint(getPreciseCoordinate(tokenizer));
            //multi.getElements().add(point);
        	elements.add(point);
            nextToken = getNextCloserOrComma(tokenizer);
        }
        return multi;
    }
        
    /**
     * Creates a {@code MultiPrimitive} out of a GEOMETRYCOLLECCTION specifier.
     *
     * @param tokenizer tokenizer on top of a stream of text in Well-known Text
     *                  format.
     * @return a <code>MultiPrimitive</code> specified by the next tokens
     *         in the stream
     * @throws ParseException 
     * @throws IOException    if an I/O error occurs
     */
    private MultiPrimitive readGeometryCollectionText(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener(tokenizer);        
        if (nextToken.equals(EMPTY)) {
        	return null;
        }
        MultiPrimitive multi = geometryFactory.createMultiPrimitive();
        Geometry geom = readGeometryTaggedText(tokenizer);
        //multi.getElements().add(geom);
        Set elements = multi.getElements();
        elements.add(geom);
        nextToken = getNextCloserOrComma(tokenizer);
        while (nextToken.equals(COMMA)) {
            geom  = readGeometryTaggedText(tokenizer);
            //multi.getElements().add(geom);
            elements.add(geom);
            nextToken = getNextCloserOrComma(tokenizer);
        }
        return multi;
    }

    /**
     * Creates a {@code MultiPrimitive} out of a MULTILINESTRING specifier
     *
     * @param tokenizer tokenizer on top of a stream of text in Well-known Text
     *                  format. 
     * @return a <code>MultiPrimitive</code> specified by the next tokens
     *         in the stream
     * @throws ParseException 
     * @throws IOException    if an I/O error occurs
     */
    private MultiPrimitive readMultiLineStringText(StreamTokenizer tokenizer) throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener(tokenizer);        
        if (nextToken.equals(EMPTY)) {
        	return null;
        }
        MultiPrimitive multi = geometryFactory.createMultiPrimitive();
    	Curve curve = readLineStringText(tokenizer);
        //multi.getElements().add(curve);
        Set elements = multi.getElements();
        elements.add(curve);
        nextToken = getNextCloserOrComma(tokenizer);
        while (nextToken.equals(COMMA)) {
            curve = readLineStringText(tokenizer);
            //multi.getElements().add(curve);
            elements.add(curve);
            nextToken = getNextCloserOrComma(tokenizer);
        }
        return multi;
    }
    
}
