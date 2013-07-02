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
package org.geotools.coverage.io.hdf4;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.range.Axis;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.impl.BaseFieldType;
import org.geotools.coverage.io.range.impl.SimpleScalarAxis;
import org.geotools.feature.NameImpl;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * <p>
 * This sample implementation should be extended in the future in order to 
 * account for different products as well as for different source.
 * 
 * @author Simone Giannecchini, GeoSolutions 
 * 
 * TODO: Improve the mapping pairs.
 *         (leveraging on class) 
 *
 *
 *
 * @source $URL$
 */
public class HDF4ProductFieldType implements FieldType {
	
	/**
	 * Simple class to hold basic descriptions and {@link Unit} for a certain product.
	 * 
	 * <p>
	 * In the future it would be nice to extend this class in order to use to make
	 * mappings between standard name tables like the netCDF-CF one.
	 * 
	 * @author Simone Giannecchini, GeoSolutions
	 *
	 */
	public static class Product{
		private String id;
		
		private Unit<?> UoM;
		
		private String description;

		public Product(String id, Unit<? extends Quantity> uoM,
				String description) {
			super();
			this.id = id;
			UoM = uoM;
			this.description = description;
		}

		public String getId() {
			return id;
		}

		public Unit<?> getUoM() {
			return UoM;
		}

		public String getDescription() {
			return description;
		}
		
	}
	
	private final static HashMap<String, Product> productMap;


    static {
        productMap = new HashMap<String, HDF4ProductFieldType.Product>();
        //TODO add more products
        productMap.put("mcsst",new HDF4ProductFieldType.Product("mcsst",SI.CELSIUS,"sea surface temperature"));
        productMap.put("sst",new HDF4ProductFieldType.Product("sst",SI.CELSIUS,"sea surface temperature"));
        productMap.put("sst4",new HDF4ProductFieldType.Product("sst4",SI.CELSIUS,"temperature"));
        productMap.put("lowcloud",new HDF4ProductFieldType.Product("lowcloud",Unit.ONE,"lowcloud"));
        productMap.put("K_490",new HDF4ProductFieldType.Product("K_490",Unit.valueOf("m^-1"),"k_490"));
    }

    public static boolean isSupportedProduct(final String product) {
        return productMap.containsKey(product);
    }

    public static Product getProduct(final String product) {
        if (isSupportedProduct(product))
            return productMap.get(product);
        return null;
    }

    private static SimpleScalarAxis createSimpleScalarAxis(final Name productName, final InternationalString productDescription) {
        return new SimpleScalarAxis(new NameImpl(new StringBuilder("axis:").append(productName.toString()).toString()),
                new SimpleInternationalString(new StringBuilder("axis for product:").append(
                        productDescription.toString()).toString()));
    }

    private BaseFieldType wrappedFieldType;

    public HDF4ProductFieldType(final String productName, final SampleDimension sd) {
        this(new NameImpl(productName), new SimpleInternationalString(getProduct(productName).getDescription()), getProduct(productName).getUoM(), sd);
    }

    public HDF4ProductFieldType(final Name name, final InternationalString description, final Unit<?> UoM, final SampleDimension sd) {
        this(name, description, UoM, createSimpleScalarAxis(name, description), sd);
    }

    public HDF4ProductFieldType(final Name name, final InternationalString description, final SampleDimension sd) {
        this(name, description, sd.getUnits(), createSimpleScalarAxis(name, description), sd);
    }

    /**
     * 
     */
    public HDF4ProductFieldType(final Name name, final InternationalString description, final Unit<?> unit,
            final SimpleScalarAxis axis, final SampleDimension sd) {

        Unit<? extends Quantity> uom = unit;
        if (unit == null) {
            Product product = getProduct(name.getLocalPart());
            if (product!=null)
                uom = product.getUoM();
        }

        this.wrappedFieldType = new BaseFieldType(name, description, uom,
                Collections.singletonList(axis), Collections.singletonMap(axis.getKey(0), sd));
    }

    public HDF4ProductFieldType(final Name name, final InternationalString description, final SimpleScalarAxis axis,
            final SampleDimension sd) {

        this(name, description, sd.getUnits(), axis, sd);
    }

    /**
     * @see org.geotools.coverage.io.range.FieldType#getAxes()
     */
    public List<Axis<?, ?>> getAxes() {
        return this.wrappedFieldType.getAxes();
    }

    /**
     * @see org.geotools.coverage.io.range.FieldType#getAxesNames()
     */
    public List<Name> getAxesNames() {
        return this.wrappedFieldType.getAxesNames();
    }

    /**
     * @see org.geotools.coverage.io.range.FieldType#getAxis(org.opengis.feature.type.Name)
     */
    public Axis<?, ?> getAxis(Name name) {
        return this.wrappedFieldType.getAxis(name);
    }

    /**
     * @see org.geotools.coverage.io.range.FieldType#getDescription()
     */
    public InternationalString getDescription() {
        return this.wrappedFieldType.getDescription();
    }

    /**
     * @see org.geotools.coverage.io.range.FieldType#getName()
     */
    public Name getName() {
        return this.wrappedFieldType.getName();
    }

    /**
     * @see org.geotools.coverage.io.range.FieldType#getSampleDimensions()
     */
    public Set<SampleDimension> getSampleDimensions() {
        return this.wrappedFieldType.getSampleDimensions();
    }

    /**
     * @see org.geotools.coverage.io.range.FieldType#getUnitOfMeasure()
     */
    public Unit<?> getUnitOfMeasure() {
        return this.wrappedFieldType.getUnitOfMeasure();
    }

    public SampleDimension getSampleDimension(Measure<?, ?> key) {
        return this.wrappedFieldType.getSampleDimension(key);
    }

    public String toString() {
        return this.wrappedFieldType.toString();
    }

}
