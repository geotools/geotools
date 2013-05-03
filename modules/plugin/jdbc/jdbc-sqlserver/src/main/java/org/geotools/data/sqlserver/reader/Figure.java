package org.geotools.data.sqlserver.reader;

/**
 * @author Anders Bakkevold, Bouvet
 *
 * @source $URL$
 */
public class Figure {

    private int attribute;
    private int pointOffset;

    public Figure(int attribute, int pointOffset) {
        this.attribute = attribute;
        this.pointOffset = pointOffset;
    }

    public int getAttribute() {
        return attribute;
    }

    public int getPointOffset() {
        return pointOffset;
    }
}
