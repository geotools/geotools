/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 */
package org.geotools.data.oracle.sdo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
* Construct geometry used by test cases.
* <p>
* Several examples are from the the Oracle Spatial Geometry Spec.</p>
* 
* @see net.refractions.jspatial.jts
* @author jgarnett, Refractions Reasearch Inc.
 *
 * @source $URL$
* @version CVS Version
*/
public class GeometryFixture {
  GeometryFactory gf;

  /**
   * Geometry Example "2.3.1 Rectangle".
   * <p>
   * A simple rectangle as used with CAD applications</p>
   * <code><pre>
   * (1,7)         (5,7)
   *   +-------------+
   *   |             |
   *   |             |
   *   +-------------+
   * (1,1)          (5,1)
   * </pre><code>
   */
  public Polygon rectangle;
  
  /** Polygon used for testing */
  public Polygon polygon;

  /**
   * Geometry Example "2.3.2 Polygon with Hole".
   * <p>
   * A Polygon with a Hole as follows:</p>
   * <code><pre>
   *   5,13+-------------+   11,13
   *      /               \
   * 2,11+                 \
   *     | 7,10+----+10,10  \
   *     |     |    |       +13,9
   *     |     |    |       |
   *     |     |    |       |
   *     |  7,5+----+10,5   +13,5
   *  2,4+                  /
   *      \                /
   *   4,3+---------------+10,3
   * </pre></code>
   */
  public Polygon polygonWithHole;        
  /**
   * Geometry Example "2.3.5 Point".
   * <p>
   * Simple Point used to test POINT_TYPE array use.</p>
   * <code><pre>
   *   +   12,14
   * </pre></code>
   */
  public Point point;
  
  /** LineString used for testing */
  public LineString lineString;
  
  /** MultiPoint used for testing */
  public MultiPoint multiPoint;
  
  /** MultiLineString used for testing */
  public MultiLineString multiLineString;
              
  /** MultiPolygon used for testing */
  public MultiPolygon multiPolygon;
  
  /** MultiPolygon used for testing */      
  public MultiPolygon multiPolygonWithHole;
  
  /** GeometryCollection used for testing */
  public GeometryCollection geometryCollection;      
  /**
   * Construct Fixture for use with default GeometryFactory.
   */
  public GeometryFixture(){
      this( new GeometryFactory(new PrecisionModel(), -1) );
  }
  /**
   * Construct Fixture for use with provided <code>GeometryFactory</code>.
   */
  public GeometryFixture( GeometryFactory geometryFactory ) {
      gf = geometryFactory;
      rectangle = createRectangle();
      polygon = createPolygon();
      polygonWithHole = createPolygonWithHole();
      point = createPoint();
      lineString = createLineString();
      multiPoint = createMultiPoint();
      multiLineString = createMultiLineString();
      multiPolygon = createMultiPolygon();
      multiPolygonWithHole = createMultiPolygonWithHole();
      geometryCollection = createGeometryCollection();         
  }
  /**
   * Construct a rectangle according to Geometry Examples "2.3.1 Rectangle".
   * <p>
   * A simple rectangle as used with CAD applications</p>
   * <code><pre>
   * (1,7)         (5,7)
   *   +-------------+
   *   |             |
   *   |             |
   *   +-------------+
   * (1,1)          (5,1)
   * </pre><code>
   * <p>The polygon is not consturcted with an SRID (ie srid == -1)</p>
   * A Rectangle with expected encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2003</code><br/>
   *     2 dimensional polygon
   *     </li>
   * <li><b>SDO_SRID:</b><code>NULL</code></li>
   * <li><b>SDO_POINT:</b>NULL></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,1003,3)</code><br/>
   *     03 indicates this is a rectangle
   *     </li>
   * <li><b>SDO_ORDINATES:</b><code>(1,1,5,7)</code><br/>
   *     bottom left and upper right
   *     </li> 
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2003,
   *   NULL,
   *   NULL,
   *   MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),
   *   MDSYS.SDO_ORDINATE_ARRAY(1,1,5,7)
   * )
   * </pre></code>
   * 
   * @see GeometryFixture.rectangle
   */    
  protected Polygon createRectangle(){
      Polygon rect = gf.createPolygon( ring( new double[]{1,1,5,1,5,7,1,7,1,1}), null );
      return rect;
  }
  /**
   * Construct a polygon of a triangle.
   * <p>
   * Used to illustrate polyugon encoding.</p>
   * <code><pre>
   *        +11,8
   *       / \
   *      /   \
   *     /     \
   * 9,5+-------+13,5
   * </pre></code>
   * <p>
   * A Rectangle with expected encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2003</code><br/>
   *     2 dimensional polygon
   *     </li>
   * <li><b>SDO_SRID:</b><code>NULL</code></li>
   * <li><b>SDO_POINT:</b>NULL></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,1003,1)</code><br/>
   *     1000 for external, 03 for polygon,
   *     1 indicates this polygon uses strait edges
   *     </li>
   * <li><b>SDO_ORDINATES:</b><code>(1,1,5,7)</code><br/>
   *     bottom left and upper right
   *     </li> 
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2003,
   *   NULL,
   *   NULL,
   *   MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),
   *   MDSYS.SDO_ORDINATE_ARRAY(9,5, 13,5, 11,8, 9,5)
   * )
   * </pre></code>
   */ 
  protected Polygon createPolygon(){
      Polygon poly = gf.createPolygon( ring( new double[]{9,5, 13,5, 11,8, 9,5}), null );
      poly.setSRID( -1 ); // don't have an SRID number
      return poly;
  }
  /**
   * Construct a polygon with hole according to Geometry Examples 2.3.2.
   * <p>
   * Polygon examples used to illustrate compound encoding.</p>
   * <code><pre>
   *   5,13+-------------+   11,13
   *      /               \
   * 2,11+                 \
   *     | 7,10+----+10,10  \
   *     |     |    |       +13,9
   *     |     |    |       |
   *     |     |    |       |
   *     |  7,5+----+10,5   +13,5
   *  2,4+                  /
   *      \                /
   *   4,3+---------------+10,3
   * </pre></code>
   * <p>
   * A Polygon with expected encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2003</code><br/>
   *     2 dimensional polygon, 3 for polygon
   *     </li>
   * <li><b>SDO_SRID:</b><code>NULL</code></li>
   * <li><b>SDO_POINT:</b>NULL></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,1003,1,19,2003,1)</code><br/>
   *     Two triplets
   *     <ul>
   *     <li>(1,1003,1): exterior polygon ring starting at 1
   *         </li>
   *         
   *     <li>(19,2003,1): interior polygon ring starting at 19
   *         </li>
   *     </ul>
   *     </li>
   * <li><b>SDO_ORDINATES:</b>
   *     <code><pre>
   *        (2,4, 4,3, 10,3, 13,5, 13,9, 11,13, 5,13, 2,11, 2,4,
   *         7,5, 7,10, 10,10, 10,5, 7,5)
   *     </code><pre/>
   *     </li> 
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2003,
   *   NULL,
   *   NULL,
   *   MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1, 19,2003,1),
   *   MDSYS.SDO_ORDINATE_ARRAY(2,4, 4,3, 10,3, 13,5, 13,9, 11,13, 5,13, 2,11, 2,4,
   *       7,5, 7,10, 10,10, 10,5, 7,5)
   * )
   * </pre></code> 
   */
  protected Polygon createPolygonWithHole(){
      Polygon poly = gf.createPolygon(
          ring( new double[]{2,4, 4,3, 10,3, 13,5, 13,9, 11,13, 5,13, 2,11, 2,4}),
          new LinearRing[]{
              ring( new double[]{7,5, 7,10, 10,10, 10,5, 7,5}),
          }
      );
      poly.setSRID( -1 ); // don't have an SRID number
      return poly;
  }
  /**
   * Geometry Example "2.3.5 Point".
   * <p>
   * Simple Point used to test POINT_TYPE array use.</p>
   * <code><pre>
   *   +   12,14
   * </pre></code>
   * <p>
   * Expected Encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2001</code><br/>
   *     2 dimensional, 0 measures, 01 for point
   *     </li>
   * <li><b>SDO_SRID:</b><code>NULL</code></li>
   * <li><b>SDO_POINT:</b><code>(12,14,NULL)</code></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,1,1)</code></li>
   * <li><b>SDO_ORDINATES:</b><code>(12,14)</code></li>
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2001,
   *   NULL,
   *   MDSYS.SDO_POINT_TYPE(12, 14, NULL),
   *   NULL,
   *   NULL
   * )
   * </pre></code> 
   */
  protected Point createPoint(){
      Point point = gf.createPoint(
          coords( new double[]{ 12, 14} )        
      );
      return point;
  }
  /**
   * LineString geometry for testing fixture.
   * <code><pre>
   *        +4,7
   *        |
   *        |
   *        |
   * 1,2+   +4,2
   *    \   /
   *  2,1+-+3,1
   * </pre></code>
   * <p>
   * Expected Encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2002</code><br/>
   *     2 dimensional, 0 measures, 02 for Line
   *     </li>
   * <li><b>SDO_SRID:</b><code>NULL</code></li>
   * <li><b>SDO_POINT:</b><code>NULL</code></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,2,5)</code></li>
   * <li><b>SDO_ORDINATES:</b><code>(1,2, 2,1, 3,1, 4,2 4,7)</code></li>
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2002,
   *   NULL,
   *   NULL,
   *   (1,2,5),
   *   (1,2, 2,1, 3,1, 4,2 4,7)
   * )
   * </pre></code> 
   */    
  protected LineString createLineString(){
      LineString lineString = gf.createLineString(
          coords( new double[]{ 1,2, 2,1, 3,1, 4,2, 4,7 })
      );
      return lineString;        
  }
  /**
   * MultiPoint geometry for testing fixture.
   * <code><pre>
   * 
   *      5,5+
   * 
   *    3,3+
   * 
   *  2,2+
   * 1,1+
   * </pre></code>
   * <p>
   * Expected Encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2004</code><br/>
   *     2 dimensional, 0 measures, 05 for MultiPoint
   *     </li>
   * <li><b>SDO_SRID:</b><code>NULL</code></li>
   * <li><b>SDO_POINT:</b><code>NULL</code></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,1,4)</code></li>
   * <li><b>SDO_ORDINATES:</b><code>(1,1, 2,2, 3,3, 5,5)</code></li>
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2005,
   *   NULL,
   *   NULL,
   *   (1,1,4),
   *   (1,1, 2,2, 3,3, 5,5)
   * )
   * </pre></code> 
   */  
  protected MultiPoint createMultiPoint(){
      MultiPoint multiPoint = gf.createMultiPoint(
          coords( new double[]{1,1, 2,2, 3,3, 5,5})
      );
      return multiPoint;
  }
  
  /**
   * MultiLineString geometry for testing fixture.
   * <code><pre>
   *  2,7+==+==+5,7
   *        |4,7
   *        |
   *        |
   * 1,2+   +4,2
   *    \   /
   *  2,1+-+3,1
   * </pre></code>
   * <p>
   * Expected Encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2005</code><br/>
   *     2 dimensional, 0 measures, 05 for MultiLine
   *     </li>
   * <li><b>SDO_SRID:</b><code>NULL</code></li>
   * <li><b>SDO_POINT:</b><code>NULL</code></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,2,1,11,2,1)</code><br/>
   *     Two triplets
   *     <ul>
   *     <li>(1,2,1): linestring(2) of straight lines(1) starting at 1
   *         </li>
   *         
   *     <li>(11,2,1): linestring(2) of straight lines(1) starting at 1
   *         </li>
   *     </ul>
   *     </li>
   * <li><b>SDO_ORDINATES:</b><code>(1,2, 2,1, 3,1, 4,2 4,7,
   *                                 2,7, 4,7, 5,7)</code></li>
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2005,
   *   NULL,
   *   NULL,
   *   (1,2,1,11,2,1),
   *   (1,2, 2,1, 3,1, 4,2 4,7, 2,7, 4,7, 5,7)
   * )
   * </pre></code> 
   */    
  protected MultiLineString createMultiLineString(){
      LineString line1 = gf.createLineString(
          coords( new double[]{ 1,2, 2,1, 3,1, 4,2, 4,7 })
      );
      LineString line2 = gf.createLineString(
          coords( new double[]{ 2,7, 4,7, 5,7 })
      );
      MultiLineString multiLineString =
          gf.createMultiLineString( new LineString[]{line1,line2});
          
      return multiLineString;        
  }   
  /**
   * Construct a multipolyugon with a square and a triangle.
   * <p>
   * Used to illustrate multi polyugon encoding.</p>
   * <code><pre>
   * 
   * 2,9+------+7,9   
   *    |      |      +11,8
   *    |      |     / \
   *    |      |    /   \
   *    |      |9,5-----+13,5
   *    |      |
   * 2,3+------+7,3
   * </pre></code>
   * <p>
   * A MultiPolygon with expected encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2006</code><br/>
   *     2 dimensional polygon, 6 for multi polygon
   *     </li>
   * <li><b>SDO_SRID:</b><code>NULL</code></li>
   * <li><b>SDO_POINT:</b>NULL></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,1003,1,11,1003,1)</code><br/>
   *     Three triplets
   *     <ul>
   *     <li>(1,1003,1): exterior(1000) polygon(3) starting at 1 with
   *         straight edges(1)
   *         </li>
   *         
   *     <li>(11,1003,1): exterior(1000) polygon(3) starting at 11 with
   *         straight edges(1)
   *         </li>
   *     </ul>
   *     </li>
   * <li><b>SDO_ORDINATES:</b>
   *     <code><pre>
   *        (2,3, 7,3, 7,9, 2,9, 2,3,
   *         9,5, 13,5, 11,5, 9,5)
   *     </code><pre/>
   *     </li> 
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2006,
   *   NULL,
   *   NULL,
   *   MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1, 11,1003,1),
   *   MDSYS.SDO_ORDINATE_ARRAY(2,3, 7,3, 7,9, 2,9, 2,3,
   *         9,5, 13,5, 11,5, 9,5)
   * )
   * </pre></code> 
   */
  protected MultiPolygon createMultiPolygon(){
      Polygon poly1 = gf.createPolygon(
          ring( new double[]{2,3, 7,3, 7,9, 2,9, 2,3}),
          null
      );
      Polygon poly2 = gf.createPolygon(
          ring( new double[]{9,5, 13,5, 11,8, 9,5}),
          null
      );
      MultiPolygon multiPolygon = gf.createMultiPolygon(
          new Polygon[]{ poly1, poly2 }            
      );
      return multiPolygon;
  }
  /**
   * Construct a multipolyugon with a square with a hole and a triangle.
   * <p>
   * Used to illustrate multi polyugon encoding.</p>
   * <code><pre>
   * 
   * 2,9+-------+7,9   
   *    |3,8 6,8|        +11,8
   *    | +---+ |      / \
   *    | |  /  |     /   \
   *    | | /   |    /     \
   *    | +     |9,5+-------+13,5
   *    |3,4    |
   * 2,3+-------+7,3
   * </pre></code>
   * <p>
   * A MultiPolygon with expected encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2007</code><br/>
   *     2 dimensional, 6 for multipolygon
   *     </li>
   * <li><b>SDO_SRID:</b><code>0</code></li>
   * <li><b>SDO_POINT:</b>NULL></li>
   * <li><b>SDO_ELEM_INFO:</b><code>(1,1003,1,11,2003,1,19,1003,1)</code><br/>
   *     Two triplets
   *     <ul>
   *     <li>(1,1003,1): exterior(1000) polygon(3) starting at 1 with
   *         straight edges(1)
   *         <ul>
   *         <li>(1,2003,1): interior(2000) polygon(3) starting at 11 with
   *         straight edges(1)
   *             </li>
   *         </ul>
   *         </li>
   *         
   *     <li>(11,1003,1): exterior(1000) polygon(3) starting at 19 with
   *         straight edges(1)
   *         </li>
   *     </ul>
   *     </li>
   * <li><b>SDO_ORDINATES:</b>
   *     <code><pre>
   *        (2,3, 7,3, 7,9, 2,9, 2,3,
   *         3,4, 3,8, 6,8, 3,4,
   *         9,5, 13,5, 11,8, 9,5)
   *     </code><pre/>
   *     </li> 
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2006,
   *   NULL,
   *   NULL,
   *   MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1,11,2003,1,19,1003,1),
   *   MDSYS.SDO_ORDINATE_ARRAY(2,3, 7,3, 7,9, 2,9, 2,3,
   *         3,4, 3,8, 6,8, 3,4, 
   *         9,5, 13,5, 11,8, 9,5)
   * )
   * </pre></code> 
   */    
  protected MultiPolygon createMultiPolygonWithHole(){
      Polygon poly1 = gf.createPolygon(
          ring( new double[]{2,3, 7,3, 7,9, 2,9, 2,3}),
          new LinearRing[]{ring( new double[]{3,4, 6,8, 3,8, 3,4}),}
      );
      Polygon poly2 = gf.createPolygon(
          ring( new double[]{9,5, 11,8, 13,5, 9,5}),
          null
      );
      MultiPolygon multiPolygon = gf.createMultiPolygon(
          new Polygon[]{ poly1, poly2 }            
      );
      return multiPolygon;
  }
  /**
   * General Geometry Collection - with point, line, polygon, and a polygonWithHole.
   * <code><pre>
   *                          
   *                5,5+-------+9,5
   *                   |  +6,4/
   *                   | /|  /
   *                   |/ | / 
   *  2,3 +---+3,3  5,3+--+/6,3
   *      |2,2|        |  /
   * 1,2+ +---+3,2     | / 
   *     \             |/
   * 1,1+ +2,1      5,1+
   * </pre></code>
   * A GeometryCollection with expected encoding:</p>
   * <ul>
   * <li><b>SDO_GTYPE:</b><code>2004</code><br/>
   *     2000 dimensional polygon, 000 for no LRS, 4 for geometry collection
   *     </li>
   * <li><b>SDO_SRID:</b><code>0</code></li>
   * <li><b>SDO_POINT:</b>NULL></li>
   * <li><b>SDO_ELEM_INFO:</b>
   *     <code>(1,1,1, 3,2,1, 7,1003,1, 15,1003,1, 23,2003,1)</code><br/>
   *     Two triplets
   *     <ul>
   *     <li>(1,1,1): starting at 1, a point(1) (single(1))
   *         </li>
   *     <li>(3,2,1): starting at 3, a line(2) with straight segments(1)
   *         </li>
   *     <li>(7,1003,1): starting at 5, an exterior(1000), polygon(3)
   *         </li>
   * 
   *     <li>(15,1003,1, 23,2003,1) polygon with:
   *         <ul>
   *         <li>starting at 15 and exterior(1003) and straight edges 1
   *             </li>
   *         <li>starting at 23 and interior(2003) and straight edges 1
   *             </li>
   *         </ul>
   *         </li>
   *     </ul>
   * <li><b>SDO_ORDINATES:</b>
   *     <code><pre>
   *        (1,1,
   *         1,2, 2,1,
   *         2,2, 3,2, 3,3, 2,3, 2,2
   *         5,1, 5,5, 9,5, 5,1,
   *         5,3, 6,4, 6,3, 5,3)
   *     </code><pre/>
   *     </li> 
   * </ul>
   * <p>
   * SQL:</p>
   * <code><pre>
   * MDSYS.SDO_GEOMETRY(
   *   2004,
   *   NULL,
   *   NULL,
   *   MDSYS.SDO_ELEM_INFO_ARRAY(1,1,1, 3,2,1, 7,1003,1, 17,1003,1, 25,2003,1),
   *   MDSYS.SDO_ORDINATE_ARRAY(
   *         1,1,
   *         1,2, 2,1,
   *         2,2, 3,2, 3,3, 2,3, 2,2,
   *         5,1, 5,5, 9,5, 5,1,
   *         5,3, 6,4, 6,3, 5,3
   *   )
   * )
   * </pre></code> 
   */
  protected GeometryCollection createGeometryCollection(){
      return gf.createGeometryCollection(
          new Geometry[]{
              gf.createPoint( coords( new double[]{1,1}) ),
              gf.createLineString( coords( new double[]{1,2, 2,1})),
              gf.createPolygon( ring(new double[]{2,2, 3,2, 3,3, 2,3, 2,2}), null ),
              gf.createPolygon(
                  ring(new double[]{5,1, 9,5, 5,5, 5,1 }),
                  new LinearRing[]{
                      ring(new double[]{5,3, 6,4, 6,3, 5,3,})
                  }
              )
          }
      );                    
  }
  //
  // Utility Methods
  //
  protected LinearRing ring( double coords[]){
      CoordinateSequence seq = coords( coords );
      return gf.createLinearRing( seq ); 
  }
  protected CoordinateSequence coords( double coords[]){
      Coordinate array[] = new Coordinate[ coords.length / 2 ];
      for( int i=0; i<array.length;i++){
          array[i]=new Coordinate( coords[i*2], coords[i*2+1] );
      }
      return gf.getCoordinateSequenceFactory().create( array );        
  }    
}
