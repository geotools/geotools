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

import java.util.Map;

import javax.measure.quantity.Length;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;


/**
 * This is a style visitor that will produce a copy of the provided
 * style. The copy will be rescaled by a provided factor if UOM is PIXEL.
 */
public class DpiRescaleStyleVisitor extends RescaleStyleVisitor {

    private boolean rescaling = true;
    

    public DpiRescaleStyleVisitor(double scale) {
        super(scale);
    }

    public DpiRescaleStyleVisitor(Expression scale) {
        super(scale);
    }

    public DpiRescaleStyleVisitor(FilterFactory2 filterFactory, double scale) {
        super(filterFactory, scale);
    }

    public DpiRescaleStyleVisitor(FilterFactory2 filterFactory, Expression scale) {
        super(filterFactory, scale);
    }

    
    @Override
    protected Expression rescale(Expression expr) {
        if (rescaling) {
            return super.rescale(expr);
        } else {
            return expr;
        }
    }
    
    @Override
    float[] rescale(float[] values) {
        if (rescaling) {
            return super.rescale(values);
        } else {
            return values;
        }        
    }
    
    @Override    
    protected void rescaleOption(Map<String, String> options, String key, double defaultValue) {
        if (rescaling) {
            super.rescaleOption(options, key, defaultValue);
        } else {
            if (options.get(key) == null && defaultValue != 0) {
                options.put(key, String.valueOf(defaultValue));
            }
        }
    }
    
    @Override        
    protected void rescaleOption(Map<String, String> options, String key, int defaultValue) {
        if (rescaling) {
            super.rescaleOption(options, key, defaultValue);
        } else {
            if (options.get(key) == null && defaultValue != 0) {
                options.put(key, String.valueOf(defaultValue));
            }
        }        
    }
    
    private void setRescaling(Symbolizer symbolizer) {
        // scaling to do only if UOM is PIXEL (or null, which stands for PIXEL as well)        
        Unit<Length> uom = symbolizer.getUnitOfMeasure();
        setRescaling(uom == null || uom.equals(NonSI.PIXEL));
    }
    
    private void setRescaling(boolean rescaling) {
        this.rescaling = rescaling;
    }
 
    @Override    
    public void visit(Symbolizer sym) {
        setRescaling(sym);
        try {
            super.visit(sym);
        } finally {
            setRescaling(true);
        }        
    }
    
    @Override    
    public void visit(PointSymbolizer sym) {
        setRescaling(sym);
        try {
            super.visit(sym);
        } finally {
            setRescaling(true);
        }        
    }
    
    @Override    
    public void visit(LineSymbolizer sym) {
        setRescaling(sym);
        try {
            super.visit(sym);
        } finally {
            setRescaling(true);
        }        
    }
    
    @Override    
    public void visit(PolygonSymbolizer sym) {
        setRescaling(sym);
        try {
            super.visit(sym);
        } finally {
            setRescaling(true);
        }        
    }
    
    @Override    
    public void visit(TextSymbolizer sym) {
        setRescaling(sym);
        try {
            super.visit(sym);
        } finally {
            setRescaling(true);
        }        
    }
    
    @Override    
    public void visit(RasterSymbolizer sym) {
        setRescaling(sym);
        try {
            super.visit(sym);
        } finally {
            setRescaling(true);
        }        
    }
}