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

import org.geotools.filter.ConstantExpression;


/**
 * OverlapBehavior tells a system how to behave when multiple raster images in
 * a layer overlap each other, for example with satellite-image scenes.
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="OverlapBehavior"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         &quot;OverlapBehavior&quot; tells a
 *              system how to behave when multiple         raster images in
 *              a layer overlap each other, for example with
 *              satellite-image scenes.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:choice&gt;
 *              &lt;xsd:element ref="sld:LATEST_ON_TOP"/&gt;
 *              &lt;xsd:element ref="sld:EARLIEST_ON_TOP"/&gt;
 *              &lt;xsd:element ref="sld:AVERAGE"/&gt;
 *              &lt;xsd:element ref="sld:RANDOM"/&gt;
 *          &lt;/xsd:choice&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 * @deprecated Please use org.opengis.style.OverlapBehavior
 */
public class OverlapBehavior extends ConstantExpression {
    public final static String AVERAGE_RESCTRICTION= "AVERAGE";
    public final static String RANDOM_RESCTRICTION= "RANDOM";
    public final static String LATEST_ON_TOP_RESCTRICTION= "LATEST_ON_TOP";
    public final static String EARLIEST_ON_TOP_RESCTRICTION= "EARLIEST_ON_TOP";
    public final static String UNSPECIFIED_RESCTRICTION = "UNSPECIFIED";
    
    public static final OverlapBehavior LATEST_ON_TOP = new OverlapBehavior(OverlapBehavior.LATEST_ON_TOP_RESCTRICTION);
    public static final OverlapBehavior EARLIEST_ON_TOP = new OverlapBehavior(OverlapBehavior.EARLIEST_ON_TOP_RESCTRICTION);
    public static final OverlapBehavior AVERAGE = new OverlapBehavior(OverlapBehavior.AVERAGE_RESCTRICTION);
    public static final OverlapBehavior RANDOM = new OverlapBehavior(OverlapBehavior.RANDOM_RESCTRICTION);

    private OverlapBehavior(String value) {
        super(value);
    }
    
    public void accept(org.geotools.styling.StyleVisitor visitor){
        visitor.visit(this);
    }
}
