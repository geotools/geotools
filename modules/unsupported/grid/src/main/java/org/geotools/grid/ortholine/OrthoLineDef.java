/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid.ortholine;

/**
 *
 * @author michael
 */
public class OrthoLineDef {

    private final int level;
    private final LineOrientation orientation;
    private final double spacing;

    public OrthoLineDef(LineOrientation orientation, double spacing) {
        this(orientation, 0, spacing);
    }
    
    public OrthoLineDef(LineOrientation orientation, int level, double spacing) {
        this.level = level;
        this.orientation = orientation;
        this.spacing = spacing;
    }
    
    public OrthoLineDef(OrthoLineDef lineDef) {
        if (lineDef == null) {
            throw new IllegalArgumentException("lineDef arg must not be null");
        }
        this.level = lineDef.level;
        this.orientation = lineDef.orientation;
        this.spacing = lineDef.spacing;
    }

    public int getLevel() {
        return level;
    }

    public LineOrientation getOrientation() {
        return orientation;
    }

    public double getSpacing() {
        return spacing;
    }

}
