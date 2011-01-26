package org.geotools.data.dxf.entities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.algorithm.Angle;
import org.geotools.data.GeometryType;
import org.geotools.data.dxf.header.DXFBlockReference;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFLineType;
import org.geotools.data.dxf.parser.DXFColor;
import org.geotools.data.dxf.parser.DXFConstants;
import org.geotools.data.dxf.parser.DXFUnivers;


public abstract class DXFEntity implements DXFConstants {

    private static final Log log = LogFactory.getLog(DXFEntity.class); 
    protected GeometryType geometryType;
    /* feature write */
    protected String _name = null;
    protected String key = null;
    protected String urlLink = null;
    protected boolean parseError = false;
    protected String errorDescription = null;
    protected Geometry geometry = null;
    /* dxf read */
    protected DXFUnivers univers;
    protected int startingLineNumber = -1;
    protected DXFLineType _lineType;
    protected int _color;
    protected DXFLayer _refLayer;
    protected double _thickness;
    protected boolean visible = true;
    private double _entRotationAngle = 0.0;
    protected Coordinate _entBase = new Coordinate(0.0, 0.0);

    public DXFEntity(DXFEntity newEntity) {
        this(newEntity.getColor(), newEntity.getRefLayer(), 1, newEntity.getLineType(), newEntity.getThickness());
    }

    public DXFEntity(int c, DXFLayer l, int visibility, DXFLineType lineType, double thickness) {

        _refLayer = l;

        if (lineType != null && lineType._name.equalsIgnoreCase("BYLAYER") && _refLayer != null) {
            //TODO waar zit linetype in layer?
        }
        _lineType = lineType;

        if (!(this instanceof DXFBlockReference) && !(this instanceof DXFLayer)) {
            if ((c < 0) || (c == 255 && _refLayer != null)) {
                if (_refLayer == null) {
                    c = DXFColor.getDefaultColorIndex();
                } else {
                    c = _refLayer._color;
                }
            }
        }
        _color = c;
        _thickness = thickness;

        if (visibility == 0) {
            visible = true;
        } else {
            visible = false;
        }
    }

    public void setBase(Coordinate coord) {
        this._entBase = coord;
    }

    public void setAngle(double angle) {
        this._entRotationAngle = angle;
    }

    abstract public DXFEntity translate(double x, double y);

    protected Coordinate rotateAndPlace(Coordinate coord) {
        Coordinate[] array = new Coordinate[1];
        array[0] = coord;

        return rotateAndPlace(array)[0];
    }

    protected Coordinate[] rotateAndPlace(Coordinate[] coordarray) {
        for (int i = 0; i < coordarray.length; i++) {
            coordarray[i] = rotateCoordDegrees(coordarray[i], _entRotationAngle);
            coordarray[i].x += _entBase.x;
            coordarray[i].y += _entBase.y;
        }
        return coordarray;
    }

    private Coordinate rotateCoordDegrees(Coordinate coord, double angle) {
        angle = Angle.toRadians(angle);
        angle = Angle.angle(coord) + angle;

        Coordinate newCoord = new Coordinate(coord);
        double radius = Math.sqrt(Math.pow(coord.x, 2) + Math.pow(coord.y, 2));

        newCoord.x = radius * Math.cos(angle);
        newCoord.y = radius * Math.sin(angle);

        return newCoord;
    }

    @Override
    abstract public DXFEntity clone();

    public Geometry getGeometry() {
        if (geometry == null) {
            updateGeometry();
        }
        return geometry;
    }

    public void updateGeometry() {
        geometry = getUnivers().getErrorGeometry();
        addError("error geometry created for: " + this.getClass().toString());
    }

    public String getName() {
        return _name;
    }

    public String getKey() {
        return key;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public boolean isParseError() {
        return parseError;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public int getStartingLineNumber() {
        return startingLineNumber;
    }

    public void setStartingLineNumber(int startingLineNumber) {
        this.startingLineNumber = startingLineNumber;
    }

    public GeometryType getType() {
        return geometryType;
    }

    public void setType(GeometryType geometryType) {
        this.geometryType = geometryType;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public DXFLineType getLineType() {
        return _lineType;
    }

    public String getLineTypeName() {
        if (_lineType == null) {
            return DXFLineType.DEFAULT_NAME;
        }
        return _lineType._name;
    }

    public void setLineType(DXFLineType lineType) {
        this._lineType = lineType;
    }

    public int getColor() {
        return _color;
    }

    public String getColorRGB() {
        return DXFColor.getColorRGB(_color);
    }

    public void setColor(int color) {
        this._color = color;
    }

    public DXFLayer getRefLayer() {
        return _refLayer;
    }

    public String getRefLayerName() {
        if (_refLayer == null) {
            return DXFLayer.DEFAULT_NAME;
        }
        return _refLayer.getName();
    }

    public void setRefLayer(DXFLayer refLayer) {
        this._refLayer = refLayer;
    }

    public double getThickness() {
        return _thickness;
    }

    public void setThickness(double thickness) {
        this._thickness = thickness;
    }

    public DXFUnivers getUnivers() {
        return univers;
    }

    public void setUnivers(DXFUnivers univers) {
        this.univers = univers;
    }

    /**
     * Called when an error occurs but that error is constrained to a single
     * feature/subgeometry. Try to continue parsing features, but do set parseError
     * property to true and add and error message.
     * @param msg
     */
    public void addError(String msg) {
        if (!parseError) {
            parseError = true;
            errorDescription = "entry starting line " + getStartingLineNumber() + ": " + msg;
        } else {
            errorDescription += "; " + msg;
        }
    }

    public void setParseError(boolean parseError) {
        this.parseError = parseError;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
