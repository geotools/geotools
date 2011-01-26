/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer3d.example;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.feature.*;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.styling.BasicLineStyle;

/**
 * A simple test data generator.
 *
 * @author Hans Häggström
 */
public final class ExampleDataGenerator
{


    /**
     * @return an map context with generated test data.
     */
    public MapContext createExampleMap()
    {
        // TODO: Read up on Coordinate Reference System, and pass one to the constructor below:
        final MapContext exampleMap = new DefaultMapContext();

        // Some random height coverage data
        // TODO

        // Some random roads
        // TODO

        // Some random lakes and rivers
        // TODO

        // Some random cities containing building polygons
        // TODO

        // Some random coverage color (population density?)
        // TODO

        try
        {
            //AttributeType geom = AttributeTypeFactory.newAttributeType("the_geom", Point.class);
            AttributeType geom = AttributeTypeFactory.newAttributeType( "the_geom", LineString.class );
            AttributeType roadWidth = AttributeTypeFactory.newAttributeType( "width", Float.class );
            FeatureType ftRoad = FeatureTypes.newFeatureType( new AttributeType[]{ geom, roadWidth }, "road" );

            WKTReader wktReader = new WKTReader();
            //Point geometry = (Point) wktReader.read("POINT (" + lat + " " + lon + ")");
            LineString geometry = (LineString) wktReader.read( "LINESTRING (0 0, 10 10, 10 20, 20 30, 10 40, 15 50)" );
            Float width = new Float( 10 );
            Feature theRoad = ftRoad.create( new Object[]{ geometry, width }, "myRoad" );

            System.out.println( theRoad );

            final FeatureCollection featureCollection = new DefaultFeatureCollection( "roads", ftRoad );

            featureCollection.add( theRoad );

            exampleMap.addLayer( new DefaultMapLayer( featureCollection, new BasicLineStyle() ) );

            System.out.println( "exampleMap = " + exampleMap );
        }
        catch ( SchemaException e )
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch ( ParseException e )
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch ( IllegalAttributeException e )
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return exampleMap;
    }

}
