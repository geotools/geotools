package org.geotools.styling;

import java.util.Map;

import org.opengis.filter.expression.Expression;

/**
 * Custom symbolizer support.
 * <p>
 * This facility is used to allow you to work on your "vendor specific" symbolizer.
 *
 * @source $URL$
 */
public interface ExtensionSymbolizer extends org.opengis.style.ExtensionSymbolizer, Symbolizer {

    /**
     * Vendor specific name for your symbolizer.
     * 
     * @return the symbolizer name
     */
    String getExtensionName();

    /**
     * Name of vendor specific extensions
     * @param name
     */
    void setExtensionName( String name );
    
    /**
     * Live map symbolizer expressions.
     *
     * @return map of all expressions.
     */
    Map<String,Expression> getParameters();
    
}
