package org.geotools.data.dxf.parser;

/**
 *
 * @author Chris
 */
public interface DXFConstants {

    public static final String SECTION = "SECTION";
    public static final String HEADER = "HEADER";
    public static final String TABLES = "TABLES";
    public static final String TABLE = "TABLE";
    public static final String LAYER = "LAYER";
    public static final String BLOCK_RECORD = "BLOCK_RECORD"; // TODO GJ COMMENT: Added
    public static final String LTYPE = "LTYPE";
    public static final String ENDTAB = "ENDTAB";
    public static final String BLOCKS = "BLOCKS";
    public static final String BLOCK = "BLOCK";
    public static final String ENDBLK = "ENDBLK";
    public static final String ENTITIES = "ENTITIES";
    public static final String LINE = "LINE";
    public static final String SPLINE = "SPLINE";
    public static final String ARC = "ARC";
    public static final String CIRCLE = "CIRCLE";
    public static final String POLYLINE = "POLYLINE";
    public static final String VERTEX = "VERTEX";
    public static final String SEQEND = "SEQEND";
    public static final String LWPOLYLINE = "LWPOLYLINE";
    public static final String POINT = "POINT";
    public static final String SOLID = "SOLID";
    public static final String TEXT = "TEXT";
    public static final String MTEXT = "MTEXT";
    public static final String INSERT = "INSERT";
    public static final String DIMENSION = "DIMENSION";
    public static final String TRACE = "TRACE";
    public static final String ELLIPSE = "ELLIPSE";
    public static final String CLASSES = "CLASSES";
    public static final String ENDSEC = "ENDSEC";
    /* not supported */
    public static final String OBJECTS = "OBJECTS";
    public static final String THUMBNAILIMAGE = "THUMBNAILIMAGE";
    /* Supported header variables */
    public static final String $LIMMIN = "$LIMMIN";
    public static final String $LIMMAX = "$LIMMAX";
    public static final String $EXTMIN = "$EXTMIN";
    public static final String $EXTMAX = "$EXTMAX";
    public static final String $ACADVER = "$ACADVER";
    public static final String $FILLMODE = "$FILLMODE";
    /* layer constants */
    public static final short LAYER_FROZEN = 1;  /* layer is frozen */

    public static final short LAYER_AUTO_FROZEN = 2;  /* layer automatically frozen in all VIEWPORTS */

    public static final short LAYER_LOCKED = 4;  /* layer is locked */

    public static final short LAYER_XREF = 8;  /* layer is from XREF */

    public static final short LAYER_XREF_FOUND = 16;  /* layer is from known XREF */

    public static final short LAYER_USED = 32;  /* layer was used */

    public static final short LAYER_INVISIBLE = 16384;  /* (own:) layer is invisible */
    
    /*
    Polyline flag (bit-coded); default is 0:
    1 = This is a closed polyline (or a polygon mesh closed in the M direction).
    2 = Curve-fit vertices have been added.
    4 = Spline-fit vertices have been added.
    8 = This is a 3D polyline.
    16 = This is a 3D polygon mesh.
    32 = The polygon mesh is closed in the N direction.
    64 = The polyline is a polyface mesh.
    128 = The linetype pattern is generated continuously around the vertices of this polyline.
     */


}
