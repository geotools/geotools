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
package org.geotools.coverage.io.range.impl;

import java.util.Collections;
import java.util.Set;
import org.geotools.coverage.io.range.FieldType;
import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

public class DefaultFieldType implements FieldType {
    // private List<Axis<?, ?>> axes;
    private Name name;

    private InternationalString description;

    // private Unit<Quantity> unit;
    private Set<SampleDimension> sampleDimensions;

    /** */
    public DefaultFieldType(
            Name name,
            InternationalString description,
            // Unit<?> unit,
            // List<Axis<?,?>> axes,
            Set<SampleDimension> samples) {
        this.name = name;
        this.description = description;
        // this.axes = axes;
        this.sampleDimensions = samples;
    }

    // public List<Axis<?,?>> getAxes() {
    // return Collections.unmodifiableList(axes);
    // }

    // public List<Name> getAxesNames() {
    // List<Name> list = new ArrayList<Name>();
    // for( Axis<?,?> axis : axes ){
    // list.add( axis.getName() );
    // }
    // return list;
    // }

    // public Axis<?,?> getAxis(Name name) {
    // for( Axis<?,?> axis : axes ){
    // if( name.equals( axis.getName() )){
    // return axis;
    // }
    // }
    // return null;
    // }

    public InternationalString getDescription() {
        return description;
    }

    public Name getName() {
        return name;
    }

    // public SampleDimension getSampleDimension(Measure<?,?> key) {
    // return null; // TODO: need to figure out how to record this association
    // }

    public Set<SampleDimension> getSampleDimensions() {
        if (sampleDimensions != null) {
            return Collections.unmodifiableSet(sampleDimensions);
        }
        return Collections.emptySet();
    }

    // /** Unit type for this field */
    // public Unit<Quantity> getUnitOfMeasure() {
    // return unit; // TODO Is this duplicated with sample dimensions ?
    // }

}
