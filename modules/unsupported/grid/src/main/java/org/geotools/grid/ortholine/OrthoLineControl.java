/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid.ortholine;

/**
 *
 * @author michael
 */
public class OrthoLineControl {
    
    private final LineOrientation orientation;
    private final int level;
    private final double spacing;

    public OrthoLineControl(LineOrientation orientation, double spacing) {
        this(orientation, 0, spacing);
    }
    
    public OrthoLineControl(LineOrientation orientation, int level, double spacing) {
        this.orientation = orientation;
        this.level = level;
        this.spacing = spacing;
    }

    public int getLevel() {
        return level;
    }

    public double getSpacing() {
        return spacing;
    }

    public LineOrientation getOrientation() {
        return orientation;
    }

}
