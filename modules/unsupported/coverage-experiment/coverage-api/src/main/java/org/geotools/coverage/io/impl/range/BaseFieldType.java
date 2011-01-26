/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.range.Axis;
import org.geotools.coverage.io.range.FieldType;
import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * Skeleton implementation of a {@link FieldType}.
 * 
 * <p>
 * This class should be used as a base for more complex implementations of {@link FieldType}
 * 
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class BaseFieldType implements FieldType {

	/**
	 * The description for this {@link FieldType}.
	 */
    private InternationalString description;

    /**
     * The {@link Name} for this {@link FieldType}.
     */
    private Name name;

    /**
     * The {@link List} of {@link Axis} for this {@link FieldType}.
     */
    private List <? extends Axis<?,?>>axes; 

    /**
     * The list of {@link Name}s for the {@link Axis} instances of this {@link FieldType}.
     */
    private List<Name> axesNames;    
    
    /**
     * This {@link Map} holds the mapping between the Keys ( {@link Measure} instances) and the {@link SampleDimension}s 
     * for this field.
     * 
     */
    private Map<Measure<?,?>,SampleDimension> keysSampleDimensionsMap;
    
    
    /**
     * The {@link Unit} for this {@link FieldType}.
     */
    private Unit<?> uom;

    /**
     * 
     * @param <V>
     * @param <QA>
     * @param fieldName
     * @param fieldDescription
     * @param UoM
     * @param axes
     * @param dimensions
     */
    public <V,QA extends Quantity> BaseFieldType(
            final Name fieldName,
            final InternationalString fieldDescription,
            final Unit<? extends Quantity>  UoM,
            final List<? extends Axis<V,QA>> axes,
            final Map<? extends Measure<V,QA>,SampleDimension> dimensions) {
        this.name = fieldName;
        this.description = fieldDescription;
        this.axes = new ArrayList<Axis<V,QA>>(axes);
        this.keysSampleDimensionsMap = new HashMap<Measure<?,?>,SampleDimension>(dimensions);
        this.uom = UoM;
        axesNames = new ArrayList<Name>(axes.size());
        for (Axis<?,?> axis : axes) {
            axesNames.add(axis.getName());
        }
    }
    
    public Axis<?,?> getAxis(Name name) {
        for (Axis<?,?> axis : axes) {
            if (axis.getName().toString().equalsIgnoreCase(name.toString()) ||
                    axis.getName().getLocalPart().equalsIgnoreCase(name.getLocalPart()))
                return  axis;
        }
        throw new IllegalArgumentException("Unable to find axis for the specified name.");
    }
    
    public List<Axis<?,?>> getAxes() {
      return Collections.unmodifiableList(axes);
  }

    public List<Name> getAxesNames() {
        return Collections.unmodifiableList(axesNames);
    }

    public InternationalString getDescription() {
        return description;
    }

    public Name getName() {
        return name;
    }

    public Set<SampleDimension> getSampleDimensions() {
        return new HashSet<SampleDimension>(this.keysSampleDimensionsMap.values());
    }

    public Unit<?> getUnitOfMeasure() {
        return uom;
    }

    public SampleDimension getSampleDimension(Measure<?, ?>key) {
    	if(this.keysSampleDimensionsMap.containsKey(key))
    		return keysSampleDimensionsMap.get(key);
    	throw new IllegalArgumentException("Unable to find SampleDimension for the specified key.");
    }
    
    /**
     * Simple Implementation of toString method for debugging purpose.
     */
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        final String lineSeparator = System.getProperty("line.separator", "\n");
        sb.append("Name:").append(name.toString()).append(lineSeparator);
        sb.append("Description:").append(description.toString()).append(lineSeparator);
        sb.append("UoM:").append(uom.toString()).append(lineSeparator);
        sb.append("Axes:").append(lineSeparator);
        for (Axis<?,?> axis : axes) {
            sb.append("   axisName:").append(axis.getName().toString());
            sb.append(" axisDescription:").append(axis.getDescription().toString());
            sb.append(" axisUoM:").append(axis.getUnitOfMeasure().toString());
            List<? extends Measure<?, ?>> axisKeys = axis.getKeys();
            for (Measure<?, ?> measure: axisKeys){
                sb.append(" key:").append(measure.toString());
            }
            sb.append(lineSeparator);
        }
        sb.append("SampleDimensions:").append(lineSeparator);
        for (SampleDimension sampleDimension :  keysSampleDimensionsMap.values()){
            sb.append("SampleDim: ").append(sampleDimension.toString());
        }
        return sb.toString();
    }
}
