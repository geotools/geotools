/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.unidata.cv;

import java.io.IOException;
import java.util.AbstractList;
import java.util.List;

import org.geotools.imageio.unidata.utilities.UnidataCRSUtilities;
import org.geotools.util.Converter;
import org.geotools.util.NumericConverterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.nc2.Attribute;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis1D;

/**
 * @author User
 *
 * TODO caching
 */
class NumericCoordinateVariable<T  extends Number> extends CoordinateVariable<T>{      

    private double scaleFactor= Double.NaN;
    
    private double offsetFactor=Double.NaN;

    private Converter converter;
    
    private CoordinateReferenceSystem crs;
    
    private final static NumericConverterFactory CONVERTER_FACTORY= new NumericConverterFactory();
 
    /**
     * @param binding
     * @param coordinateAxis
     */
    public NumericCoordinateVariable(Class<T> binding,CoordinateAxis1D coordinateAxis) {
        super(binding, coordinateAxis);      
        // If the axis is not numeric, we can't process any further. 
        if (!coordinateAxis.isNumeric()) {
            throw new IllegalArgumentException("Unable to process non numeric coordinate variable: "+coordinateAxis.toString());
        }
        
        // scale and offset
        Attribute scaleFactor = coordinateAxis.findAttribute("scale_factor");
        if(scaleFactor!=null){
            this.scaleFactor=scaleFactor.getNumericValue().doubleValue();
        }
        Attribute offsetFactor = coordinateAxis.findAttribute("offset");  
        if(offsetFactor!=null){
            this.offsetFactor=offsetFactor.getNumericValue().doubleValue();
        }
        
        // converter from double to binding
        this.converter=CONVERTER_FACTORY.createConverter(Double.class,this.binding, null);
    }

    @Override
    public T getMinimum() throws IOException {
        try {
            return converter.convert(coordinateAxis.getMinValue(),this.binding);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public T getMaximum() throws IOException {
        try {
            return converter.convert(coordinateAxis.getMaxValue(),this.binding);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public T read(int index) throws IndexOutOfBoundsException {
        if(index>=this.coordinateAxis.getSize()){
            throw new IndexOutOfBoundsException("index >= "+coordinateAxis.getSize());
        }
        double val = handleValues(index);
        try {
            return converter.convert(val,this.binding);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param index
     * @return
     */
    private double handleValues(int index) {
        double val=coordinateAxis.getCoordValue(index);
        if(!Double.isNaN(scaleFactor)){
            val*=scaleFactor;
        }
        if(!Double.isNaN(offsetFactor)){
            val+=offsetFactor;
        }
        return val;
    }

    @Override
    public List<T> read() throws IndexOutOfBoundsException {
        
        return new AbstractList<T>() {

            @Override
            public T get(int index) {
                double val = handleValues(index);
                try {
                     return converter.convert(val,NumericCoordinateVariable.this.binding);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } 
            }

            @Override
            public int size() {
                return coordinateAxis.getShape()[0];
            }
        };     
        
    }

    @Override
    public boolean isNumeric() {
        return true;
    }
    
    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if(this.crs==null){
         synchronized (this) {
             final AxisType axisType = coordinateAxis.getAxisType();
             switch(axisType){
             case GeoZ:case Height:case Pressure:
                 String axisName =getName();
                 if (UnidataCRSUtilities.VERTICAL_AXIS_NAMES.contains(axisName)) {
                     this.crs= UnidataCRSUtilities.buildVerticalCrs(coordinateAxis);
                 }
                 break;
             }             
             
         }   
        }
        return crs;
    }
}
