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
package org.geotools.renderer.lite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Graphic;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * 
 * @author jfc173
 * @source $URL$
 */
public class CustomGlyphRenderer implements GlyphRenderer {

    
    private GlyphPropertiesList list = new GlyphPropertiesList();
    private boolean maxFound = false;
    private int maxBarHeight = 0;
    
    /** Creates a new instance of CustomGlyphRenderer */
    public CustomGlyphRenderer() {
    	 FilterFactory2 factory = (FilterFactory2) org.geotools.factory.CommonFactoryFinder
         .getFilterFactory(null);
        
        list.addProperty("radius", Expression.class, factory.literal(50));
        list.addProperty("circle color", Expression.class, factory.literal("#000066"));
        list.addProperty("bar height", Expression.class, factory.literal(150));
        list.addProperty("bar color", Expression.class, factory.literal("#000000"));
        list.addProperty("bar uncertainty", Expression.class, factory.literal(50));
        list.addProperty("bar uncertainty width", Expression.class, factory.literal(5));
        list.addProperty("bar uncertainty color", Expression.class, factory.literal("#999999"));
        list.addProperty("pointer length", Expression.class, factory.literal(100));
        list.addProperty("pointer color", Expression.class, factory.literal("#FF0000"));
        list.addProperty("pointer direction", Expression.class, factory.literal(21));
        list.addProperty("wedge width", Expression.class, factory.literal(25));
        list.addProperty("wedge color", Expression.class, factory.literal("#9999FF"));
    }
    
    public boolean canRender(String format) {
        return format.equalsIgnoreCase("image/hack");
    }
    
    public List getFormats() {
        Vector ret = new Vector();
        ret.add("image/hack");
        return ret;
    }
    
    public String getGlyphName(){
        return "exploded clock";  //I think Alan called it this once, so here it sticks.
    }
    
    public GlyphPropertiesList getGlyphProperties(){
        return list;
    }
    
    //Is this really necessary, since the values in the properties list can be set?
    public void setGlyphProperties(GlyphPropertiesList gpl){
        list = gpl;
    }
    
    /**
     * djb -- addd "height" which is ignored as per API change
     * @param graphic
     * @param eg
     * @param feature
     * @param height
     */
    public BufferedImage render(Graphic graphic, ExternalGraphic eg, Object feature, int height) {
        Map props = eg.getCustomProperties();
        Set propNames = props.keySet();
        Iterator it = propNames.iterator();
        
        while (it.hasNext()) {
            String nextName = (String) it.next();
            
            if (list.hasProperty(nextName)) {
                list.setPropertyValue(nextName, props.get(nextName));
            } else {
                //DO I WANT TO THROW AN EXCEPTION OR ADD THE NEW PROPERTY TO THE LIST OR DO NOTHING?
                System.out.println("Tried to set the property " + nextName
                + " to a glyph that does not have this property.");
            }
        }
        
        //Change this to get values from list.
        int radius = 50;
        Expression e = (Expression) list.getPropertyValue("radius");
        if (e != null){
            radius = ((Number) e.evaluate(feature)).intValue();
        }
        
        Color circleColor = Color.BLUE.darker();
        e = (Expression) list.getPropertyValue("circle color");
        if (e != null){
            circleColor = Color.decode((String) e.evaluate(feature));
        }
        
        int barHeight = 150;
        e = (Expression) list.getPropertyValue("bar height");
        if (e != null){
            barHeight = ((Number) e.evaluate(feature)).intValue();
        }
        
        Color barColor = Color.BLACK;
        e = (Expression) list.getPropertyValue("bar color");
        if (e != null){
            barColor = Color.decode((String) e.evaluate(feature));
        }
        
        int barUncertainty = 50;
        e = (Expression) list.getPropertyValue("bar uncertainty");
        if (e != null){
            barUncertainty = ((Number) e.evaluate(feature)).intValue();
        }
        
        int barUncWidth = 5;
        e = (Expression) list.getPropertyValue("bar uncertainty width");
        if (e != null){
            barUncWidth = ((Number) e.evaluate(feature)).intValue();
        }
        
        Color barUncColor = Color.GRAY;
        e = (Expression) list.getPropertyValue("bar uncertainty color");
        if (e != null){
            barUncColor = Color.decode((String) e.evaluate(feature));
        }
        
        int pointerDirection = 21;
        e = (Expression) list.getPropertyValue("pointer direction");
        if (e != null){
            pointerDirection = ((Number) e.evaluate(feature)).intValue();
        }
        
        Color pointerColor = Color.RED;
        e = (Expression) list.getPropertyValue("pointer color");
        if (e != null){
            pointerColor = Color.decode((String) e.evaluate(feature));
        }
        
        int pointerLength = 100;
        e = (Expression) list.getPropertyValue("pointer length");
        if (e != null){
            pointerLength = ((Number) e.evaluate(feature)).intValue();
        }
        
        int wedgeWidth = 25;
        e = (Expression) list.getPropertyValue("wedge width");
        if (e != null){
            wedgeWidth = ((Number) e.evaluate(feature)).intValue();
        }
        
        Color wedgeColor = Color.BLUE;
        e = (Expression) list.getPropertyValue("wedge color");
        if (e != null){
            wedgeColor = Color.decode((String) e.evaluate(feature));
        }
        
        int circleCenterX, circleCenterY, imageHeight, imageWidth;
        
        
        BufferedImage image;
        Graphics2D imageGraphic;
        
        //calculate maximum value of barHeight + barUncertainty & use that instead of "barHeight + barUncertainty"
        Expression tempExp = (Expression) list.getPropertyValue("bar height");
        int temp1 = 0;
        if (tempExp != null){
            temp1 = ((Number) tempExp.evaluate(feature)).intValue();
        }
        tempExp = (Expression) list.getPropertyValue("bar uncertainty");
        int temp2 = 0;
        if (tempExp != null){
            temp2 = ((Number) tempExp.evaluate(feature)).intValue();
        }
        if (temp1 + temp2 > maxBarHeight){
            maxBarHeight = temp1 + temp2;
        }
        
//chorner: feature.getParent is no more... is this needed?        
//        if (!maxFound){
//            maxFound = true;
//            SimpleFeatureCollection fc = feature.getParent();
//            SimpleFeatureIterator features = fc.features();
//            while (features.hasNext()){
//                Feature next = features.next();
//                Expression tempExp = (Expression) list.getPropertyValue("bar height");
//                int temp1 = 0;
//                if (tempExp != null){
//                    temp1 = ((Number) tempExp.evaluate(next)).intValue();
//                }
//                tempExp = (Expression) list.getPropertyValue("bar uncertainty");
//                int temp2 = 0;
//                if (tempExp != null){
//                    temp2 = ((Number) tempExp.evaluate(next)).intValue();
//                }
//                if (temp1 + temp2 > maxBarHeight){
//                    maxBarHeight = temp1 + temp2;
//                }
//            }
//        }
        
        circleCenterX = Math.max(pointerLength, radius);
        circleCenterY = Math.max(maxBarHeight, Math.max(pointerLength, radius));
        
        imageHeight = Math.max(radius * 2, Math.max(radius + pointerLength, Math.max(radius + maxBarHeight, pointerLength + maxBarHeight)));
        imageWidth = Math.max(radius * 2, pointerLength * 2);
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        pointerLength = Math.max(pointerLength, radius);
        imageGraphic = image.createGraphics();
        imageGraphic.setColor(circleColor);
        imageGraphic.fillOval(circleCenterX - radius, circleCenterY - radius, radius * 2, radius * 2);
        imageGraphic.setColor(wedgeColor);
        imageGraphic.fillArc(circleCenterX - radius,
        circleCenterY - radius,
        radius * 2,
        radius * 2,
        calculateWedgeAngle(pointerDirection, wedgeWidth),
        wedgeWidth * 2);
        imageGraphic.setColor(barUncColor);
        imageGraphic.fillRect(circleCenterX - barUncWidth,
        circleCenterY - barHeight - barUncertainty,
        barUncWidth * 2,
        barUncertainty * 2);
        //pointer
        int[] endPoint = calculateEndOfPointer(circleCenterX, circleCenterY, pointerLength, pointerDirection);
        imageGraphic.setStroke(new java.awt.BasicStroke(3));
        imageGraphic.setColor(pointerColor);
        imageGraphic.draw(new java.awt.geom.Line2D.Double(circleCenterX, circleCenterY, endPoint[0], endPoint[1]));
        //bar
        imageGraphic.setStroke(new java.awt.BasicStroke(3));
        imageGraphic.setColor(barColor);
        imageGraphic.draw(new java.awt.geom.Line2D.Double(circleCenterX, circleCenterY, circleCenterX, circleCenterY - barHeight));
        
        imageGraphic.dispose();
        return image;
    }
    
    private int calculateWedgeAngle(int pointerDirection, int wedgeWidth){
        return 450 - (pointerDirection + wedgeWidth);
    }
    
    private int[] calculateEndOfPointer(int circleCenterX, int circleCenterY, int pointerLength, int pointerDirection){
        int x = circleCenterX + (int) Math.round(pointerLength * Math.cos(Math.toRadians(pointerDirection - 90)));
        int y = circleCenterY + (int) Math.round(pointerLength * Math.sin(Math.toRadians(pointerDirection - 90)));
        return new int[]{x, y};
    }
    
}
