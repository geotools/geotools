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

import org.geotools.api.style.OverlapBehavior;
import org.geotools.api.style.StyleVisitor;
import org.geotools.filter.ConstantExpression;

/**
 * OverlapBehavior tells a system how to behave when multiple raster images in a layer overlap each
 * other, for example with satellite-image scenes.
 *
 * <p>
 *
 * <pre>
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
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class OverlapBehaviorImpl extends ConstantExpression implements OverlapBehavior {

    public static final OverlapBehaviorImpl LATEST_ON_TOP =
            new OverlapBehaviorImpl(OverlapBehavior.LATEST_ON_TOP_RESCTRICTION);
    public static final OverlapBehaviorImpl EARLIEST_ON_TOP =
            new OverlapBehaviorImpl(OverlapBehavior.EARLIEST_ON_TOP_RESCTRICTION);
    public static final OverlapBehaviorImpl AVERAGE =
            new OverlapBehaviorImpl(OverlapBehavior.AVERAGE_RESCTRICTION);
    public static final OverlapBehaviorImpl RANDOM =
            new OverlapBehaviorImpl(OverlapBehavior.RANDOM_RESCTRICTION);

    private OverlapBehaviorImpl(String value) {
        super(value);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }
}
