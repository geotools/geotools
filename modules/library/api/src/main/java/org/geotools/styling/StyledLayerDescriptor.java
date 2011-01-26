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
package org.geotools.styling;

import java.util.List;



/**
 * Holds styling information (from a StyleLayerDescriptor document).
 *
 * <p>
 * This interface is bound to version 1.0 of the SLD specification.
 * </p>
 *
 * <p>
 * For many of us in geotools this is the reason we came along for the ride - a
 * pretty picture. For documentation on the use of this class please consult
 * the SLD 1.0 specification.
 * </p>
 *
 * <p>
 * We may experiment with our own (or SLD 1.1) ideas but will mark such
 * experiments for you. This is only an issue of you are considering writing
 * out these objects for interoptability with other systems.
 * </p>
 *
 * <p>
 * General stratagy for supporting multiple SLD versions (and experiments):
 *
 * <ul>
 * <li>
 * These interfaces will reflect the current published specification
 * </li>
 * <li>
 * Our implementations will be <b>BIGGER</b> and more capabile then any one
 * specification
 * </li>
 * <li>
 * We cannot defined explicit interfaces tracking each version until we move to
 * Java 5 (perferably GeoAPI would hold these anyways)
 * </li>
 * <li>
 * We can provided javadocs indicating extentions, and isolate these using the
 * normal java convention: TextSymbolizer and TextSymbolizer2 (In short you
 * will have to go out of your way to work with a hack or experiment, you
 * won't depend on one by accident)
 * </li>
 * <li>
 * We can use Factories (aka SLD1Factory and SLD1_1Factory and SEFactory) to
 * support the creation of conformant datastructures. Code (such as user
 * interfaces) can be parameratized with these factories when they need to
 * confirm to an exact version supported by an individual service. We hope
 * that specifications are always adative, and will be forced to throw
 * unsupported exceptions when functionality is removed from a specification.
 * </li>
 * </ul>
 * </p>
 *
 * @author Ian Turton, CCG
 * @author James Macgill, CCG
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 * @version SLD 1.0
 *
 * @since GeoTools 2.0
 */
public interface StyledLayerDescriptor {
    public StyledLayer[] getStyledLayers();

    public void setStyledLayers(StyledLayer[] layers);

    public void addStyledLayer(StyledLayer layer);
    
    /**
     * Direct access to layers list.
     * @return Direct access to layers list.
     */
	public List<StyledLayer> layers();

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName();

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(String name);

    /**
     * Getter for property title.
     *
     * @return Value of property title.
     */
    public String getTitle();

    /**
     * Setter for property title.
     *
     * @param title New value of property title.
     */
    public void setTitle(String title);

    /**
     * Getter for property abstractStr.
     *
     * @return Value of property abstractStr.
     */
    public java.lang.String getAbstract();

    /**
     * Setter for property abstractStr.
     *
     * @param abstractStr New value of property abstractStr.
     */
    public void setAbstract(java.lang.String abstractStr);

    /**
     * Used to navigate a Style/SLD.
     *
     * @param visitor
     */
    void accept(StyleVisitor visitor);
}
