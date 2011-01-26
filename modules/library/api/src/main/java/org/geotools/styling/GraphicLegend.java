package org.geotools.styling;

import java.util.List;

import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

/**
 * Used to represent a Rule (or other construct) in a user interface or legend.
 * 
 * @author Jody
 *
 * @source $URL$
 */
public interface GraphicLegend extends org.opengis.style.GraphicLegend {

    public AnchorPoint getAnchorPoint();
    
    public void setAnchorPoint( org.opengis.style.AnchorPoint anchor );

    public Displacement getDisplacement();
    
    public void setDisplacement( org.opengis.style.Displacement displacement );

    public Expression getOpacity();

    public void setOpacity( Expression opacity);
    
    public Expression getRotation();

    /**
     * The size of the mark if specified.
     * <p>
     * If unspecified:
     * <ul>
     * <li>The natural size will be used for formats such as PNG that have an image width and height
     * <li> 16 x 16 will be used for formats like SVG that do not have a size
     * </ul>
     */
    public Expression getSize();

    /**
     * Indicates the size at which the graphic should be displayed.
     * <p>
     * If this value is null the natural size of the graphic will be used;
     * or for graphics without a natural size like SVG files 16x16 will be
     * used.
     * 
     * @param size
     */
    public void setSize( Expression size );
    /**
     * The items in this list are either a Mark or a ExternalGraphic.
     * <p>
     * This list may be directly edited; the items are considered in order
     * from most preferred (say an SVG file) to least  preferred (a simple
     * shape) with the intension that the system will make use of the
     * first entry which it is capabile of displaying.
     */
    public List<GraphicalSymbol> graphicalSymbols();

}
