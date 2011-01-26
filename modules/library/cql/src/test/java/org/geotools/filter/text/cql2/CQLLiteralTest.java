/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cql2;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BinarySpatialOperator;

/**
 * Literal parser test
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 * @source $URL$
 */
public class CQLLiteralTest {
    protected final Language language;

    public CQLLiteralTest(){
        
        this(Language.CQL);
    }

    public CQLLiteralTest(final Language language){
        
        assert language != null: "language cannot be null value";
        
        this.language = language;
    }
    


    /**
     * Tests Geometry Literals
     * <p>
     *
     * <pre>
     *  &lt;geometry literal &gt; :=
     *          &lt;Point Tagged Text &gt;
     *      |   &lt;LineString Tagged Text &gt;
     *      |   &lt;Polygon Tagged Text &gt;
     *      |   &lt;MultiPoint Tagged Text &gt;
     *      |   &lt;MultiLineString Tagged Text &gt;
     *      |   &lt;MultiPolygon Tagged Text &gt;
     *      |   &lt;GeometryCollection Tagged Text &gt;
     *      |   &lt;Envelope Tagged Text &gt;
     * </pre>
     *
     * </p>
     */
    @Test
    public void geometryLiterals() throws Exception {
        BinarySpatialOperator result;
        Literal geom;

        // Point
        result = (BinarySpatialOperator) CompilerUtil.parseFilter(this.language, "WITHIN(ATTR1, POINT(1 2))");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof com.vividsolutions.jts.geom.Point);

        // LineString
        result = (BinarySpatialOperator) CompilerUtil.parseFilter(this.language,"WITHIN(ATTR1, LINESTRING(1 2, 10 15))");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof com.vividsolutions.jts.geom.LineString);

        // Polygon
        result = (BinarySpatialOperator) CompilerUtil.parseFilter(this.language,
                "WITHIN(ATTR1, POLYGON((1 2, 15 2, 15 20, 15 21, 1 2)))");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof com.vividsolutions.jts.geom.Polygon);

        // MultiPoint
        result = (BinarySpatialOperator) CompilerUtil.parseFilter(this.language,
                "WITHIN(ATTR1, MULTIPOINT( (1 2), (15 2), (15 20), (15 21), (1 2) ))");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof com.vividsolutions.jts.geom.MultiPoint);

        // MultiLineString
        result = (BinarySpatialOperator) CompilerUtil.parseFilter(this.language,
                "WITHIN(ATTR1, MULTILINESTRING((10 10, 20 20),(15 15,30 15)) )");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof com.vividsolutions.jts.geom.MultiLineString);

        // MultiPolygon
        result = (BinarySpatialOperator) CompilerUtil.parseFilter(this.language,
                "WITHIN(ATTR1, MULTIPOLYGON( ((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )) ) )");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof com.vividsolutions.jts.geom.MultiPolygon);

        // ENVELOPE
        result = (BinarySpatialOperator) CompilerUtil.parseFilter(this.language,
                "WITHIN(ATTR1, ENVELOPE( 10, 20, 30, 40) )");

        geom = (Literal) result.getExpression2();

        Assert.assertNotNull(geom.getValue());
        Assert.assertTrue(geom.getValue() instanceof com.vividsolutions.jts.geom.Polygon);
    }

    /**
     * Test error at geometry literal
     * @throws CQLException 
     */
    @Test(expected=CQLException.class)
    public void geometryLiteralsError() throws CQLException {

        final String filterError = "WITHIN(ATTR1, POLYGON(1 2, 10 15), (10 15, 1 2)))";
        CompilerUtil.parseFilter(this.language,filterError);
    }
   
    
    /**
     * <pre>
     * &lt;character string literal&gt; ::= &lt;quote&gt; [ {&lt;character representation&lt;} ]  &lt;quote&gt;
     * &lt;character representation&gt; ::= 
     *                  &lt;nonquote character&gt; 
     *          |       &lt;quote symbol&gt;
     * &lt;quote symbol&gt; ::=  &lt;quote&gt; &lt;quote&gt;
     * </pre>
     */
    @Test
    public void characterStringLiteral() throws Exception {

        PropertyIsEqualTo eqFilter;

        // space check
        testCharacterString("ALL PRACTICES" );

        // empty string ''
        testCharacterString("" );

        // character string without quote
        testCharacterString("ab" );

        // <quote symbol>
        final String expected = "cde'' fg";

        Filter filter = CompilerUtil.parseFilter(language, "MAJOR_WATERSHED_SYSTEM = '" + expected
                + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filter;
        Expression actual = eqFilter.getExpression2();
        Assert.assertEquals(expected.replaceAll("''", "'"), actual.toString());

        // special characters
        testCharacterString("üä" );
    }

    @Test 
    public void russianCharacterStringLiteral() throws Exception{
    	
    	testCharacterString("ДОБРИЧ" );
    	testCharacterString("название");
    	testCharacterString("фамилия");
    	testCharacterString("среды");
    }
    
    /**
     * Japan charset
     * @throws Exception
     */
    @Test 
    public void japanCharacterStringLiteral() throws Exception{
    	
        testCharacterString("名" );
        testCharacterString("姓");
        testCharacterString("環境");
    }


    private void testCharacterString(final String str) throws Exception{

        Filter filter = (PropertyIsEqualTo) CompilerUtil.parseFilter(language, "NAME = '" + str
                + "'");

        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);
        
        PropertyIsEqualTo eqFilter = (PropertyIsEqualTo) filter;
        Expression actual = eqFilter.getExpression2();
        Assert.assertEquals(str, actual.toString());
    }    
    
    @Test(expected = CQLException.class)
    public void badCharacterString() throws CQLException {

        String cqlExpression = "A=\"2\""; //should be simple quote
        CQL.toFilter(cqlExpression);
    }

    @Test
    public void doubleLiteral() throws Exception{

        final String expected = "4.20082008E4";

        Expression expr = CompilerUtil.parseExpression(language, expected);

        Literal doubleLiteral = (Literal) expr;
        Double actual = (Double) doubleLiteral.getValue();
     
        Assert.assertEquals(Double.parseDouble(expected), actual.doubleValue(), 8);
    }

    @Test
    public void longLiteral() throws Exception{

        {
            final String expected = "123456789012345";

            Expression expr = CompilerUtil.parseExpression(language, expected);

            Literal intLiteral = (Literal) expr;
            Long actual = (Long) intLiteral.getValue();

            Assert.assertEquals(Long.parseLong(expected), actual.longValue());

        }
        
        {
            final String maxLongValue = "9223372036854775807";

            Expression expr = CompilerUtil.parseExpression(language, maxLongValue);

            Literal intLiteral = (Literal) expr;
            Long actual = (Long) intLiteral.getValue();

            Assert.assertEquals(Long.parseLong(maxLongValue), actual.longValue());
        }
    
    }

}
