/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

import java.util.Map;
import org.geotools.api.filter.expression.Expression;

/**
 * An symbolizer interface for all unnormalized symbolizers, This interface should be used for
 * vendor specific symbolizers.
 *
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.3
 */
public interface ExtensionSymbolizer extends Symbolizer {

    /**
     * Vendor specific name for your symbolizer.
     *
     * @return the symbolizer name
     */
    String getExtensionName();

    /** Name of vendor specific extensions */
    void setExtensionName(String name);

    /**
     * Live map symbolizer expressions.
     *
     * @return map of all expressions.
     */
    Map<String, Expression> getParameters();

    /**
     * Calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Override
    Object accept(TraversingStyleVisitor visitor, Object extraData);
}
