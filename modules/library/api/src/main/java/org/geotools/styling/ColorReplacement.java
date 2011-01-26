package org.geotools.styling;

import org.opengis.filter.expression.Function;

/**
 * Apply color replacement to an external graphic.
 * <p>
 * Can be used to indicate the background color to make transparent; or to swap colors
 * around as needed.
 *
 * @source $URL$
 */
public interface ColorReplacement extends org.opengis.style.ColorReplacement {

    /**
     * Function providing recoding of values.
     */
    Function getRecoding();
    
    /**
     * @param function Recoding function to use
     */
    void setRecoding( Function function );

}
