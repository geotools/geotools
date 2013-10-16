/*
 *    GeoTools - The Open Source Java GIS Toolkit
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

package org.geotools.filter.text.ecql;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.CQLGeoOperationTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;

/**
 * ECQL Geo Operation Test
 * <p>
 * Execute all cql test for spatial relation using the ECQL compiler
 * </p>
 * <p>
 * 
 * <pre>
 *   &lt;routine invocation &gt; ::=
 *           &lt;geoop name &gt; &lt;georoutine argument list &gt;
 *       |   &lt;relgeoop name &gt; &lt;relgeoop argument list &gt;
 *       |   &lt;routine name &gt; &lt;argument list &gt;
 *       |   &lt;relate geoop &gt; 
 *       
 *   &lt;geoop name &gt; ::=
 *           EQUALS | DISJOINT | INTERSECTS | TOUCHES | CROSSES | 
 *           WITHIN | CONTAINS |OVERLAPS  
 *           
 *   &lt;relate geoop &gt; ::=  &lt;RELATE>&gt; &quot;(&quot; Attribute()&quot;,&quot; &lt;geometry literal&gt; &quot;,&quot; &lt;DE9IM_PATTERN&gt; &quot;)&quot;
 *   &lt;DE9IM_PATTERN&gt; ::= &lt;DIMENSION_CHAR&gt;&lt;DIMENSION_CHAR&gt;&lt;DIMENSION_CHAR&gt; ...
 *   &lt;DIMENSION_CHAR&gt; ::= *| T | F | 0 | 1 | 2
 *           
 *   That rule is extended with bbox for convenience.
 *   &lt;bbox argument list &gt;::=
 *       &quot;(&quot;  &lt;attribute &gt; &quot;,&quot; &lt;min X &gt; &quot;,&quot; &lt;min Y &gt; &quot;,&quot; &lt;max X &gt; &quot;,&quot; &lt;max Y &gt;[&quot;,&quot;  &lt;srs &gt;] &quot;)&quot;
 *       &lt;min X &gt; ::=  &lt;signed numerical literal &gt;
 *       &lt;min Y &gt; ::=  &lt;signed numerical literal &gt;
 *       &lt;max X &gt; ::=  &lt;signed numerical literal &gt;
 *       &lt;max Y &gt; ::=  &lt;signed numerical literal &gt;
 *       &lt;srs &gt; ::=
 *     
 * </pre>
 *</p>
 * <p>
 * Note: the symbols of geoop names were changed to adjust these name to geoapi filter.
 * 
 * 
 * </p>
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public final class ECQLGeoOperationTest extends CQLGeoOperationTest{

    public ECQLGeoOperationTest(){
        super(Language.ECQL);
    }

    @Test
    public void disjoint() throws CQLException{
        

        Filter resultFilter = CompilerUtil.parseFilter(language, "DISJOINT(the_geom, POINT(1 2))");

        Assert.assertTrue("Disjoint was expected", resultFilter instanceof Disjoint);
    }

    @Test
    public void Intersects() throws CQLException {

        Filter resultFilter = CompilerUtil.parseFilter(language,"INTERSECTS(the_geom, POINT(1 2))");

        Assert.assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    @Test
    public void IntersectsWithExpressions() throws CQLException {

        Filter resultFilter = CompilerUtil.parseFilter(language,"INTERSECTS(POLYGON((1 2, 2 2, 2 3, 1 2)), POINT(1 2))");

        Assert.assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    @Test
    public void touches() throws CQLException{
        Filter resultFilter = CompilerUtil.parseFilter(language,"TOUCHES(the_geom, POINT(1 2))");

        Assert.assertTrue("Touches was expected", resultFilter instanceof Touches);
    }
    
    @Test
    public void crosses() throws CQLException {
        Filter resultFilter = CompilerUtil.parseFilter(language,"CROSSES(the_geom, POINT(1 2))");

        Assert.assertTrue("Crosses was expected", resultFilter instanceof Crosses);
        
    }
    @Test
    public void contains() throws CQLException      {
        Filter resultFilter = CompilerUtil.parseFilter(language,"CONTAINS(the_geom, POINT(1 2))");

        Assert.assertTrue("Contains was expected", resultFilter instanceof Contains);
    }
    
    @Test
    public void overlaps() throws Exception {
        Filter resultFilter;


        resultFilter = CompilerUtil.parseFilter(language,"OVERLAPS(the_geom, POINT(1 2))");

        Assert.assertTrue("Overlaps was expected", resultFilter instanceof Overlaps);
    }
    
    @Test
    public void equals() throws CQLException{
        // EQUALS
        Filter resultFilter = CompilerUtil.parseFilter(language,"EQUALS(the_geom, POINT(1 2))");

        Assert.assertTrue("not an instance of Equals", resultFilter instanceof Equals);
    }
    

    @Test
    public void functionAsFirstArgument() throws CQLException {
        
        Filter resultFilter = CompilerUtil.parseFilter(language,"INTERSECTS(centroid(the_geom), POINT(1 2))");

        Assert.assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    @Test
    public void functionAsSecondArgument() throws CQLException {
        
        Filter resultFilter = CompilerUtil.parseFilter(language,"INTERSECTS(the_geom, buffer(POINT(1 2),10))");
        
        Assert.assertTrue("Intersects was expected", resultFilter instanceof Intersects);

        
        resultFilter = CompilerUtil.parseFilter(language,"INTERSECTS(the_geom, buffer(the_geom,10))"); 
        
        Assert.assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    @Test
    public void functionAsFirstAndSecondArgument() throws CQLException {
        
        Filter resultFilter = CompilerUtil.parseFilter(language,"INTERSECTS(centroid(the_geom), buffer(POINT(1 2) ,10))");

        Assert.assertTrue("Intersects was expected", resultFilter instanceof Intersects);

    }
    
    /**
     * This test require an extension of bbox filter
     */
    @Ignore
    public void bboxWithFunctionOnFirstArgument(){
        
        //  TODO function on first arg BBOX( buffer( the_geom , 10), 10,20,30,40 )
        //  TODO function on second argument BBOX( buffer( the_geom , 10), buffer( POINT( 15,15), 10) )
        //  TODO Geometry on first and second arguments BBOX( POLYGON( .... ), POLYGON( .... ), 10) )
        //  TODO Proposal in geotools wiki for this
    }

  
    
}
