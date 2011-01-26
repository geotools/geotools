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
package org.geotools.legend;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

/**
 * Quick utility class used to "resize" an icon.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class ReIcon {
    final Icon icon;
    public ReIcon( Icon icon ){
        this.icon = icon;
    }
    public Icon getIcon() {
        return icon;
    }
    /**
     * Produce an icon of the required size.
     * <p>
     * The returned icon is not always an ImageIcon; and may in fact pay attention
     * to your component foreground and background color.
     * </p>
     * @param size Size of the icon (usually 16,32 and 48 are supported)
     * @return A square icon of the requested size
     */
    public Icon getIcon( final int size) {
        if (icon.getIconHeight() < size && icon.getIconWidth() < size ){
            return icon;
        }
        else {
            return new Icon(){
                public int getIconHeight() {
                    return size;
                }

                public int getIconWidth() {
                    return size;
                }

                public void paintIcon( Component c, Graphics g, int x, int y ) {
                    BufferedImage img = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB );
                    // we can cache the img when aaime gets out a profiler and tells us it is worthwhile
                    icon.paintIcon( c, img.getGraphics(), 0, 0 );
                    
                    if( c != null ){
                        g.drawImage( img, x, y, size, size, c.getBackground(), c );
                    }
                    else {
                        g.drawImage( img, x, y, size, size, c );
                    }                    
                }                
            };
        }        
    }    
}
