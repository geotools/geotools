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
package org.geotools.styling.visitor;

import java.awt.Color;

import javax.measure.quantity.Length;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizerImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.LineSymbolizer;
import org.opengis.style.PolygonSymbolizer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 * @author milton
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/test/java/org/geotools/styling/visitor/UomRescaleStyleVisitorTest.java $
 */
public class UomRescaleStyleVisitorTest extends TestCase
{
    
    public void testConstructorOK()
    {
        try
        {
            new UomRescaleStyleVisitor(1);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
    
    
    public void testConstructorFail()
    {
    	try
    	{
    		new UomRescaleStyleVisitor(-1);
    	}
    	catch(IllegalArgumentException e)
    	{
    		return;
    	}
    	
    	Assert.fail("Should throw IllegalArgumentException.");
    }
    
    protected double computeExpectedRescaleSize(double size, double scaleMetersToPixel, Unit<Length> uom)
    {
        double expectedRescaledSize = size;
        
        // uom == null means pixels (rescalesize == size)
        if (uom != null)
        {
            double scaleUomToMeters = 1;
            if (uom.equals(NonSI.FOOT))
                scaleUomToMeters *= 0.3048006096012;
            if (!uom.equals(NonSI.PIXEL))
                expectedRescaledSize *= scaleUomToMeters * scaleMetersToPixel;
        }
        
        return expectedRescaledSize;
    }
    
    
    protected void visitPointSymbolizerTest(double scaleMetersToPixel, Unit<Length> uom)
    {
        try
        {
            UomRescaleStyleVisitor visitor = null;
            double size = 100;
            double expectedRescaledSize = computeExpectedRescaleSize(size, scaleMetersToPixel, uom);
            
            StyleBuilder styleBuilder = new StyleBuilder();

            PointSymbolizerImpl pointSymb = (PointSymbolizerImpl) styleBuilder.createPointSymbolizer();
            pointSymb.setUnitOfMeasure(uom);

            FilterFactory2 filterFactory  = new FilterFactoryImpl();
            pointSymb.getGraphic().setSize(filterFactory.literal(size));

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            pointSymb.accept(visitor);
            PointSymbolizer rescaledPointSymb = (PointSymbolizer) visitor.getCopy();
            double rescaledSize = rescaledPointSymb.getGraphic().getSize().evaluate(null, Double.class);
            
            Assert.assertEquals(Math.round(expectedRescaledSize), Math.round(rescaledSize));
            Assert.assertNotSame(rescaledPointSymb, pointSymb);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
    
    protected void visitLineSymbolizerTest(double scaleMetersToPixel, Unit<Length> uom)
    {
        try
        {
            UomRescaleStyleVisitor visitor = null;
            double size = 100;
            double expectedRescaledSize = computeExpectedRescaleSize(size, scaleMetersToPixel, uom);
            
            StyleBuilder styleBuilder = new StyleBuilder();

            LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleBuilder.createLineSymbolizer();
            lineSymb.setUnitOfMeasure(uom);
            
            FilterFactory2 filterFactory  = new FilterFactoryImpl();
            lineSymb.getStroke().setWidth(filterFactory.literal(size));

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            lineSymb.accept(visitor);
            LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();
            double rescaledSize = rescaledLineSymb.getStroke().getWidth().evaluate(null, Double.class);
            
            Assert.assertEquals(Math.round(expectedRescaledSize), Math.round(rescaledSize));
            Assert.assertNotSame(rescaledLineSymb, lineSymb);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
    
    protected void visitPolygonSymbolizerTest(double scaleMetersToPixel, Unit<Length> uom)
    {
        try
        {
            UomRescaleStyleVisitor visitor = null;
            double size = 100;
            double expectedRescaledSize = computeExpectedRescaleSize(size, scaleMetersToPixel, uom);
            
            StyleBuilder styleBuilder = new StyleBuilder();

            PolygonSymbolizerImpl polySymb = (PolygonSymbolizerImpl) styleBuilder.createPolygonSymbolizer();
            polySymb.setUnitOfMeasure(uom);
            
            FilterFactory2 filterFactory  = new FilterFactoryImpl();
            polySymb.getStroke().setWidth(filterFactory.literal(size));

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            polySymb.accept(visitor);
            PolygonSymbolizer rescaledPolySymb = (PolygonSymbolizer) visitor.getCopy();
            double rescaledSize = rescaledPolySymb.getStroke().getWidth().evaluate(null, Double.class);
            
            Assert.assertEquals(Math.round(expectedRescaledSize), Math.round(rescaledSize));
            Assert.assertNotSame(rescaledPolySymb, polySymb);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
    
    protected void visitTextSymbolizerTest(double scaleMetersToPixel, Unit<Length> uom)
    {
        try
        {
            UomRescaleStyleVisitor visitor = null;
            int fontSize = 100;
            double displacementX = 13;
            double displacementY = 17;
            double expectedRescaledFontSize = computeExpectedRescaleSize(fontSize, scaleMetersToPixel, uom);
            double expectedRescaledDisplacementXSize = computeExpectedRescaleSize(displacementX, scaleMetersToPixel, uom);
            double expectedRescaledDisplacementYSize = computeExpectedRescaleSize(displacementY, scaleMetersToPixel, uom);
            
            StyleBuilder styleBuilder = new StyleBuilder();

            TextSymbolizerImpl textSymb = (TextSymbolizerImpl) styleBuilder.createTextSymbolizer();
            textSymb.setUnitOfMeasure(uom);
            
            Font font = styleBuilder.createFont(new java.awt.Font("Verdana",java.awt.Font.PLAIN,fontSize));
            textSymb.setFont(font);
            
            PointPlacement placement = styleBuilder.createPointPlacement(0.3, 0.3, displacementX, displacementY, 10);
            textSymb.setLabelPlacement(placement);

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            textSymb.accept(visitor);
            TextSymbolizer rescaledTextSymb = (TextSymbolizer) visitor.getCopy();
            
            double rescaledFontSize = rescaledTextSymb.getFont().getSize().evaluate(null, Double.class);
            PointPlacement rescaledPlacement = (PointPlacement) rescaledTextSymb.getLabelPlacement();
            double rescaledDisplacementXSize = rescaledPlacement.getDisplacement().getDisplacementX().evaluate(null, Double.class);
            double rescaledDisplacementYSize = rescaledPlacement.getDisplacement().getDisplacementY().evaluate(null, Double.class);
            
            Assert.assertEquals(Math.round(expectedRescaledFontSize), Math.round(rescaledFontSize));
            Assert.assertEquals(Math.round(expectedRescaledDisplacementXSize), Math.round(rescaledDisplacementXSize));
            Assert.assertEquals(Math.round(expectedRescaledDisplacementYSize), Math.round(rescaledDisplacementYSize));
            Assert.assertNotSame(rescaledTextSymb, textSymb);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
    

    // POINT SYMBOLIZER TESTS
    
    public void testVisitPointSymbolizer_ScalePixelNull()
    {
        visitPointSymbolizerTest(10, null);
    }
    
   
    public void testVisitPointSymbolizer_ScalePixelExplicit()
    {
        visitPointSymbolizerTest(10, NonSI.PIXEL);
    }
    
    
    public void testVisitPointSymbolizer_ScaleMeter1()
    {
        visitPointSymbolizerTest(1, SI.METER);
    }
    
   
    public void testVisitPointSymbolizer_ScaleMeter10()
    {
        visitPointSymbolizerTest(10, SI.METER);
    }
    
    
    public void testVisitPointSymbolizer_ScaleFoot1()
    {
        visitPointSymbolizerTest(1, NonSI.FOOT);
    }
    
    
    public void testVisitPointSymbolizer_ScaleFoot10()
    {
        visitPointSymbolizerTest(10, NonSI.FOOT);
    }


    // LINE SYMBOLIZER TESTS
    
    public void testVisitLineSymbolizer_ScalePixelNull()
    {
        visitLineSymbolizerTest(10, null);
    }
    
    
    public void testVisitLineSymbolizer_ScalePixelExplicit()
    {
        visitLineSymbolizerTest(10, NonSI.PIXEL);
    }
    
    
    public void testVisitLineSymbolizer_ScaleMeter1()
    {
        visitLineSymbolizerTest(1, SI.METER);
    }
    
    
    public void testVisitLineSymbolizer_ScaleMeter10()
    {
        visitLineSymbolizerTest(10, SI.METER);
    }
    
    
    public void testVisitLineSymbolizer_ScaleFoot1()
    {
        visitLineSymbolizerTest(1, NonSI.FOOT);
    }
    
    
    public void testVisitLineSymbolizer_ScaleFoot10()
    {
        visitLineSymbolizerTest(10, NonSI.FOOT);
    }
    
    
    // POLYGON SYMBOLIZER TESTS
    
    public void testVisitPolygonSymbolizer_ScalePixelNull()
    {
        visitPolygonSymbolizerTest(10, null);
    }
    
    
    public void testVisitPolygonSymbolizer_ScalePixelExplicit()
    {
        visitPolygonSymbolizerTest(10, NonSI.PIXEL);
    }
    
    
    public void testVisitPolygonSymbolizer_ScaleMeter1()
    {
        visitPolygonSymbolizerTest(1, SI.METER);
    }
    
    
    public void testVisitPolygonSymbolizer_ScaleMeter10()
    {
        visitPolygonSymbolizerTest(10, SI.METER);
    }
    
    
    public void testVisitPolygonSymbolizer_ScaleFoot1()
    {
        visitPolygonSymbolizerTest(1, NonSI.FOOT);
    }
    
    
    public void testVisitPolygonSymbolizer_ScaleFoot10()
    {
        visitPolygonSymbolizerTest(10, NonSI.FOOT);
    }

    
    // TEXT SYMBOLIZER TESTS
    
    public void testVisitTextSymbolizer_ScalePixelNull()
    {
        visitTextSymbolizerTest(10, null);
    }
    
    
    public void testVisitTextSymbolizer_ScalePixelExplicit()
    {
        visitTextSymbolizerTest(10, NonSI.PIXEL);
    }
    
    
    public void testVisitTextSymbolizer_ScaleMeter1()
    {
        visitTextSymbolizerTest(1, SI.METER);
    }
    
    
    public void testVisitTextSymbolizer_ScaleMeter10()
    {
        visitTextSymbolizerTest(10, SI.METER);
    }
    
    
    public void testVisitTextSymbolizer_ScaleFoot1()
    {
        visitTextSymbolizerTest(1, NonSI.FOOT);
    }
    
    
    public void testVisitTextSymbolizer_ScaleFoot10()
    {
        visitTextSymbolizerTest(10, NonSI.FOOT);
    }


    // EXTRA TESTS
    
    public void testVisitLineSymbolizer_NullStroke()
    {
        try
        {
            UomRescaleStyleVisitor visitor = null;
            
            StyleBuilder styleBuilder = new StyleBuilder();

            Stroke stroke = null;
            LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleBuilder.createLineSymbolizer(stroke);
            lineSymb.setUnitOfMeasure(SI.METER);
            
            visitor = new UomRescaleStyleVisitor(10);

            lineSymb.accept(visitor);
            LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();
            
            Assert.assertNull(rescaledLineSymb.getStroke());
            Assert.assertNotSame(rescaledLineSymb, lineSymb);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
    
    
    public void testVisitPolygonSymbolizer_NullStroke()
    {
        try
        {
            UomRescaleStyleVisitor visitor = null;
            
            StyleBuilder styleBuilder = new StyleBuilder();

            Fill fill = styleBuilder.createFill(Color.RED);
            PolygonSymbolizerImpl polySymb = (PolygonSymbolizerImpl) styleBuilder.createPolygonSymbolizer(null, fill);
            
            visitor = new UomRescaleStyleVisitor(10);

            polySymb.accept(visitor);
            PolygonSymbolizer rescaledPolySymb = (PolygonSymbolizer) visitor.getCopy();
            
            Assert.assertEquals(polySymb.getFill(), rescaledPolySymb.getFill());
            Assert.assertNull(rescaledPolySymb.getStroke());
            Assert.assertNotSame(rescaledPolySymb, polySymb);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
    
    
    public void testVisitTextSymbolizer_LinePlacement()
    {
        try
        {
            UomRescaleStyleVisitor visitor = null;
            
            Unit<Length> uom = SI.METER;
            int fontSize = 100;
            double perpOffset = 13;
            double gap = 7;
            double initialGap = 5;
            double scaleMetersToPixel = 17;
            double expectedRescaledFontSize = computeExpectedRescaleSize(fontSize, scaleMetersToPixel, uom);
            double expectedRescaledPerpOffset = computeExpectedRescaleSize(perpOffset, scaleMetersToPixel, uom);
            double expectedRescaledGap = computeExpectedRescaleSize(gap, scaleMetersToPixel, uom);
            double expectedRescaledInitialGap = computeExpectedRescaleSize(initialGap, scaleMetersToPixel, uom);
            
            StyleBuilder styleBuilder = new StyleBuilder();

            TextSymbolizerImpl textSymb = (TextSymbolizerImpl) styleBuilder.createTextSymbolizer();
            textSymb.setUnitOfMeasure(uom);
            
            Font font = styleBuilder.createFont(new java.awt.Font("Verdana",java.awt.Font.PLAIN,fontSize));
            textSymb.setFont(font);
            
            LinePlacement placement = styleBuilder.createLinePlacement(perpOffset);
            placement.setGap(styleBuilder.literalExpression(gap));
            placement.setInitialGap(styleBuilder.literalExpression(initialGap));
            
            textSymb.setLabelPlacement(placement);
            
            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            textSymb.accept(visitor);
            TextSymbolizer rescaledTextSymb = (TextSymbolizer) visitor.getCopy();
            
            double rescaledFontSize = rescaledTextSymb.getFont().getSize().evaluate(null, Double.class);
            LinePlacement rescaledPlacement = (LinePlacement) rescaledTextSymb.getLabelPlacement();
            double rescaledPerpOffset = rescaledPlacement.getPerpendicularOffset().evaluate(null, Double.class);
            double rescaledGap = rescaledPlacement.getGap().evaluate(null, Double.class);
            double rescaledInitialGap = rescaledPlacement.getInitialGap().evaluate(null, Double.class);
            
            Assert.assertEquals(Math.round(expectedRescaledFontSize), Math.round(rescaledFontSize));
            Assert.assertEquals(Math.round(expectedRescaledPerpOffset), Math.round(rescaledPerpOffset));
            Assert.assertEquals(Math.round(expectedRescaledGap), Math.round(rescaledGap));
            Assert.assertEquals(Math.round(expectedRescaledInitialGap), Math.round(rescaledInitialGap));
            Assert.assertNotSame(rescaledTextSymb, textSymb);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
    
    
    public void testVisitLineSymbolizer_ExpressionWithFeatureAttribute()
    {
        try
        {
            double widthValue = 13;
            double scaleMetersToPixel = 17;
            Unit<Length> uom = SI.METER;
        	
            StyleBuilder styleBuilder = new StyleBuilder();
            UomRescaleStyleVisitor visitor = null;

            // creates the feature used for the test
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            featureTypeBuilder.setName("TestType");
            featureTypeBuilder.add("geom", LineString.class, DefaultGeographicCRS.WGS84);
            featureTypeBuilder.add("width", Double.class);
            SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
            
            GeometryFactory geomFactory = new GeometryFactory();
            Geometry geom = geomFactory.createLineString(new Coordinate[] { new Coordinate(1,1), new Coordinate(2,2) });
            
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            featureBuilder.set("geom", geom);
            featureBuilder.set("width", widthValue);
            SimpleFeature feature = featureBuilder.buildFeature("1");
            

            // creates the symbolizer for the test
            Expression color = styleBuilder.colorExpression(Color.RED);
            Expression width = styleBuilder.attributeExpression("width");
            Stroke stroke = styleBuilder.createStroke(color, width);
            LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleBuilder.createLineSymbolizer(stroke);
            lineSymb.setUnitOfMeasure(uom);

            
            // rescales symbolizer
            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            lineSymb.accept(visitor);
            LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();

            
            // tests results
            org.opengis.style.Stroke rescaledStroke = rescaledLineSymb.getStroke();
            Expression rescaledWidth = rescaledStroke.getWidth();
            double rescaledWidthValue = rescaledWidth.evaluate(feature, Double.class);
            double expectedRescaledWidthValue = computeExpectedRescaleSize(widthValue, scaleMetersToPixel, uom); 
            
            Assert.assertEquals(stroke.getColor(), rescaledStroke.getColor());
            Assert.assertEquals(expectedRescaledWidthValue, rescaledWidthValue);
            Assert.assertNotSame(SI.METER, rescaledLineSymb.getUnitOfMeasure());
            Assert.assertNotSame(rescaledLineSymb, lineSymb);
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
            Assert.fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }
}
