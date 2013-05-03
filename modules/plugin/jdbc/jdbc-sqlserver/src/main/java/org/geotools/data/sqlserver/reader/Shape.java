package org.geotools.data.sqlserver.reader;

/**
 * @author Anders Bakkevold, Bouvet
 *
 * @source $URL$
 */
public class Shape {

    private int parentOffset;
    private int figureOffset;
    private Type type;

    public Shape(int parentOffset, int figureOffset, int type) {
        this.parentOffset = parentOffset;
        this.figureOffset = figureOffset;
        this.type = Type.findType(type);
    }

    public int getParentOffset() {
        return parentOffset;
    }

    public int getFigureOffset() {
        return figureOffset;
    }

    public Type getType() {
        return type;
    }
}
