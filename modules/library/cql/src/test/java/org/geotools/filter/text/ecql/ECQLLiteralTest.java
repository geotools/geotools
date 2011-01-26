package org.geotools.filter.text.ecql;

import java.util.LinkedList;
import java.util.List;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.CQLLiteralTest;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
/**
 * 
 * Literal Test Cases
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 * @source $URL$
 */
public class ECQLLiteralTest extends CQLLiteralTest {

    public ECQLLiteralTest(){
        super(Language.ECQL);
    }

    /**
     * Test for LineString Expression
     * Sample: LINESTRING( 1 2, 3 4)
     * @throws Exception
     */
    @Test
    public void lineString() throws Exception {
        Expression resultFilter;

        final String expectedGeometry= "LINESTRING (1 2, 3 4)";
        resultFilter = CompilerUtil.parseExpression(language, expectedGeometry);

        Assert.assertTrue("DistanceBufferOperator filter was expected",
                resultFilter instanceof Literal);

        Literal filter = (Literal) resultFilter;
        Object actualGeometry = filter.getValue();
        
        Assert.assertTrue("LineString class was expected",  actualGeometry instanceof LineString);

        assertEqualsGeometries(expectedGeometry, (LineString) actualGeometry);
    }
    
    /**
     * Sample: POINT(1 2)
     * @throws CQLException
     */
    @Test
    public void point() throws Exception{
        
        // Point
        String strGeometry = "POINT(1 2)";
        Expression result =  CompilerUtil.parseExpression(this.language, strGeometry);

        Literal geom = (Literal) result;

        Object value = geom.getValue();
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof Point);
        
        assertEqualsGeometries(strGeometry, (Point) value);
    }

    
    /**
     * Sample: POLYGON((1 2, 15 2, 15 20, 15 21, 1 2))
     * @throws Exception
     */
    @Test
    public void polygon() throws Exception {
        Expression expression;

        final String expectedGeometry= "POLYGON((1 2, 15 2, 15 20, 15 21, 1 2))";
        expression = CompilerUtil.parseExpression(language,expectedGeometry);

        Assert.assertTrue(expression instanceof Literal);

        Literal literal = (Literal) expression;
        Object actualGeometry = literal.getValue();
        
        Assert.assertTrue( actualGeometry instanceof Polygon);

        assertEqualsGeometries(expectedGeometry, (Polygon) actualGeometry);
    }
    
    
    /**
     * Sample: POLYGON ((40 60, 420 60, 420 320, 40 320, 40 60), (200 140, 160 220, 260 200, 200 140))
     * 
     * @throws Exception
     */
    @Test
    public void polygonWithHole() throws Exception {
        Expression expression;

        final String expectedGeometry = "POLYGON ((40 60, 420 60, 420 320, 40 320, 40 60), (200 140, 160 220, 260 200, 200 140))";

        expression = CompilerUtil.parseExpression(language,expectedGeometry);

        Assert.assertTrue(expression instanceof Literal);

        Literal literal = (Literal) expression;
        Object actualGeometry = literal.getValue();
        
        Assert.assertTrue( actualGeometry instanceof Polygon);

        assertEqualsGeometries(expectedGeometry, (Polygon) actualGeometry);
    }

    /**
     * Sample: MULTIPOINT( (1 2), (15 2), (15 20), (15 21), (1 2) ))
     * @throws Exception
     */
    @Test
    public void multiPoint() throws Exception {
        Expression expression;

        final String expectedGeometry= "MULTIPOINT( 1 2, 15 2, 15 20, 15 21, 1 2)";
        final String txtGeometry= "MULTIPOINT( (1 2), (15 2), (15 20), (15 21), (1 2))";
        expression = CompilerUtil.parseExpression(language,txtGeometry);

        Assert.assertTrue(expression instanceof Literal);

        Literal literal = (Literal) expression;
        Object actualGeometry = literal.getValue();
        
        Assert.assertTrue( actualGeometry instanceof MultiPoint);

        assertEqualsGeometries(expectedGeometry, (MultiPoint) actualGeometry);
    }

    /**
     * Sample: MULTILINESTRING((10 10, 20 20),(15 15,30 15)) 
     * @throws Exception
     */
    @Test
    public void multiLineString() throws Exception {
        Expression expression;

        final String expectedGeom= "MULTILINESTRING((10 10, 20 20),(15 15,30 15))";
        expression = CompilerUtil.parseExpression(language,expectedGeom);

        Assert.assertTrue(expression instanceof Literal);

        Literal literal = (Literal) expression;
        Object actualGeometry = literal.getValue();
        
        Assert.assertTrue( actualGeometry instanceof MultiLineString);

        assertEqualsGeometries(expectedGeom, (MultiLineString) actualGeometry);
    }
    
    /**
     * sample: CROSS(ATTR1, GEOMETRYCOLLECTION (POINT (10 10),POINT (30 30),LINESTRING (15 15, 20 20)) )
     * @throws Exception
     */
    @Test
    public void geometryCollection()throws Exception{

        // prepares the expected geometries expected in the Geometry collection
        List<Geometry> expectedGeometriesList = new LinkedList<Geometry>();
        WKTReader reader = new WKTReader();
        Geometry g1 = reader.read("POINT (10 10)");
        expectedGeometriesList.add(g1);

        Geometry g2 = reader.read("POINT (30 30)");
        expectedGeometriesList.add(g2);

        Geometry g3 = reader.read("LINESTRING (15 15, 20 20))");
        expectedGeometriesList.add(g3);
        
        // executes the txt parser
        final String expectedGeom = "GEOMETRYCOLLECTION (POINT (10 10),POINT (30 30),LINESTRING (15 15, 20 20))"; 
        Expression result =  CompilerUtil.parseExpression(this.language,
                expectedGeom);

        Assert.assertTrue(result instanceof Literal);
        Literal geomLiteral = (Literal) result;
        Object actualGeometry =geomLiteral.getValue();
        Assert.assertNotNull(actualGeometry);
        Assert.assertTrue(actualGeometry instanceof GeometryCollection);

    }

    /**
     * Sample: MULTIPOLYGON( ((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )) )
     * @throws Exception
     */
    @Test
    public void multiPolygon() throws Exception {
        Expression expression;

        final String expectedGeom= "MULTIPOLYGON( ((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )) ) ";
        expression = CompilerUtil.parseExpression(language,expectedGeom);

        Assert.assertTrue(expression instanceof Literal);

        Literal literal = (Literal) expression;
        Object actualGeometry = literal.getValue();
        
        Assert.assertTrue( actualGeometry instanceof MultiPolygon);

        assertEqualsGeometries(expectedGeom, (MultiPolygon) actualGeometry);
    }
    
}
