/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.filter.expression.Expression;
import org.opengis.style.Description;


/**
 * Contains label shield hack
 *
 * @source $URL$
 */
public interface TextSymbolizer2 extends TextSymbolizer {
    /**
     * The nonstandard-SLD graphic element supports putting little graphical-bits onto labels.
     * Useful for things like interstate road shields or labeled logos
     * @return - the Graphic object to be rendered under the label text
     */
    public Graphic getGraphic();

    /**
     * The nonstandard-SLD graphic element supports putting little graphical-bits onto labels.
     * Useful for things like interstate road shields or labeled logos
     * @param graphic - the Graphic object which will be rendered under the label text
     */
    public void setGraphic(Graphic graphic);
    
    /**
     * Abstract is used by text oriented renderers such as KML and RSS to specify
     * an abstract (RSS) or a snippet (KML)
     * @return
     */
    public Expression getSnippet();
    
    public void setSnippet(Expression expression);
    
    /**
     * Description is used by text oriented renders such as KML and RSS to specify
     * a feature's description
     * @return
     */
    public Expression getFeatureDescription();
    
    public void setFeatureDescription(Expression description);
    
    /**
     * Other text can be used to allow open ended extensions on text oriented output formats
     * @return
     */
    public OtherText getOtherText();
    
    public void setOtherText(OtherText otherText);
}
