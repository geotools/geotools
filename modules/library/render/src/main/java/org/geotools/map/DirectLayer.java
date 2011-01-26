package org.geotools.map;

import java.awt.Graphics2D;

/**
 * A Layer directly responsible for its own rendering.
 * <p>
 * Direct layers are responsible for their own rendering and are useful for:
 * <ul>
 * <li>Map Decorations such as legends or scalebars that depend only on the map and viewport and do
 * not actually make use of external data.</li>
 * <li>Data services that are visual in nature, such as a Web Map Service (which you can think of as
 * an external renderer)</li>
 * <li>You may also consider data formats, such as CAD files, where the style information is
 * "baked into" the data format as suitable for a DirectLayer. In these cases you are only going for
 * a visual display and are not making the raw features available to the geotools library for use.
 * </ul>
 * While any and all data sources could be wrapped up as a DirectLayer we encourage you to consider
 * a separate data mode, style model and renderer. 
 * @author Jody
 */
public abstract class DirectLayer extends Layer {

    protected DirectLayer() {
    }

    /**
     * Draw layer contents onto screen
     * 
     * @param map
     *            Map being drawn; check map bounds and crs
     * @param graphics
     *            Graphics to draw into
     * @param viewport
     *            Area to draw the map into; including screen area
     */
    public abstract void draw(Graphics2D graphics, MapContent map, MapViewport viewport);
    
}
