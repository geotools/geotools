/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.ifc;

/**
 * FileConstants.java Created: Fri Dec 27 23:16:01 2002
 *
 * @author <a href="mailto:kobit@users.sf.net">Artur Hefczyc</a>
 *
 * @source $URL$
 * @version $Id$
 */
public interface FileConstants {
    //VPF Separators

    /** Variable constant <code>VPF_ELEMENT_SEPARATOR</code> keeps value of */
    public static final char VPF_ELEMENT_SEPARATOR = ',';

    /** Variable constant <code>VPF_FIELD_SEPARATOR</code> keeps value of */
    public static final char VPF_FIELD_SEPARATOR = ':';

    /** Variable constant <code>VPF_RECORD_SEPARATOR</code> keeps value of */
    public static final char VPF_RECORD_SEPARATOR = ';';

    // Key types

    /** <code>KEY_PRIMARY</code> stores code for primary key identification. */
    public static final char KEY_PRIMARY = 'P';

    /** <code>KEY_UNIQUE</code> stores code for unique key identification. */
    public static final char KEY_UNIQUE = 'U';

    /**
     * <code>KEY_NON_UNIQUE</code> stores code for non unique key
     * identification.
     */
    public static final char KEY_NON_UNIQUE = 'N';

    // Column conditions

    /** <code>COLUMN_OPTIONAL</code> stores code for optional column. */
    public static final String COLUMN_OPTIONAL = "O";

    /**
     * <code>COLUMN_OPTIONAL_FP</code> stores code for optional feature pointer
     * column.
     */
    public static final String COLUMN_OPTIONAL_FP = "OF";

    /** <code>COLUMN_MANDATORY</code> stores code for mandatory column. */
    public static final String COLUMN_MANDATORY = "M";

    /**
     * <code>COLUMN_MANDATORY_AT_LEVEL_0</code> stores code for mandatory
     * column at topology level 0.
     */
    public static final String COLUMN_MANDATORY_AT_LEVEL_0 = "M0";

    /**
     * <code>COLUMN_MANDATORY_AT_LEVEL_1</code> stores code for mandatory
     * column at topology level 1.
     */
    public static final String COLUMN_MANDATORY_AT_LEVEL_1 = "M1";

    /**
     * <code>COLUMN_MANDATORY_AT_LEVEL_2</code> stores code for mandatory
     * column at topology level 2.
     */
    public static final String COLUMN_MANDATORY_AT_LEVEL_2 = "M2";

    /**
     * <code>COLUMN_MANDATORY_AT_LEVEL_3</code> stores code for mandatory
     * column at topology level 3.
     */
    public static final String COLUMN_MANDATORY_AT_LEVEL_3 = "M3";

    /**
     * <code>COLUMN_MANDATORY_IF_TILES</code> stores code for mandatory column
     * if tiles exists.
     */
    public static final String COLUMN_MANDATORY_IF_TILES = "MT";

    // Table reserved names

    /** Variable constant <code>COVERAGE_ATTRIBUTE_TABLE</code> keeps
     *  value of */
    public static final String COVERAGE_ATTRIBUTE_TABLE = "cat";

    /** Variable constant <code>TABLE_CAT</code> keeps value of */
    public static final String TABLE_CAT = COVERAGE_ATTRIBUTE_TABLE;

    /** Variable constant <code>CONNECTED_NODE_PRIMITIVE</code> keeps
     *  value of */
    public static final String CONNECTED_NODE_PRIMITIVE = "cnd";

    /** Variable constant <code>TABLE_CND</code> keeps value of */
    public static final String TABLE_CND = CONNECTED_NODE_PRIMITIVE;

    /**
     * Variable constant <code>CONNECTED_NODE_SPATIAL_INDEX</code>
     * keeps value of
     */
    public static final String CONNECTED_NODE_SPATIAL_INDEX = "csi";

    /** Variable constant <code>TABLE_CSI</code> keeps value of */
    public static final String TABLE_CSI = CONNECTED_NODE_SPATIAL_INDEX;

    /** Variable constant <code>DATABASE_HEADER_TABLE</code> keeps value of */
    public static final String DATABASE_HEADER_TABLE = "dht";

    /** Variable constant <code>TABLE_DHT</code> keeps value of */
    public static final String TABLE_DHT = DATABASE_HEADER_TABLE;

    /** Variable constant <code>DATA_QUALITY_TABLE</code> keeps value of */
    public static final String DATA_QUALITY_TABLE = "dqt";

    /** Variable constant <code>TABLE_DQT</code> keeps value of */
    public static final String TABLE_DQT = DATA_QUALITY_TABLE;

    /** Variable constant <code>EDGE_BOUNDING_RECTANGLE</code> keeps
     * value of */
    public static final String EDGE_BOUNDING_RECTANGLE = "ebr";

    /** Variable constant <code>TABLE_EBR</code> keeps value of */
    public static final String TABLE_EBR = EDGE_BOUNDING_RECTANGLE;

    /** Variable constant <code>EDGE_PRIMITIVE</code> keeps value of */
    public static final String EDGE_PRIMITIVE = "edg";

    /** Variable constant <code>TABLE_EDG</code> keeps value of */
    public static final String TABLE_EDG = EDGE_PRIMITIVE;

    /** Variable constant <code>ENTITY_NODE_PRIMITIVE</code> keeps
     * value of
     */
    public static final String ENTITY_NODE_PRIMITIVE = "end";

    /** Variable constant <code>TABLE_END</code> keeps value of */
    public static final String TABLE_END = ENTITY_NODE_PRIMITIVE;

    /** Variable constant <code>EDGE_SPATIAL_INDEX</code> keeps value of */
    public static final String EDGE_SPATIAL_INDEX = "esi";

    /** Variable constant <code>TABLE_ESI</code> keeps value of */
    public static final String TABLE_ESI = EDGE_SPATIAL_INDEX;

    /** Variable constant <code>FACE_PRIMITIVE</code> keeps value of */
    public static final String FACE_PRIMITIVE = "fac";

    /** Variable constant <code>TABLE_FAC</code> keeps value of */
    public static final String TABLE_FAC = FACE_PRIMITIVE;

    /** Variable constant <code>FACE_BOUNDING_RECTANGLE</code> keeps
     * value of
     */
    public static final String FACE_BOUNDING_RECTANGLE = "fbr";

    /** Variable constant <code>TABLE_FBR</code> keeps value of */
    public static final String TABLE_FBR = FACE_BOUNDING_RECTANGLE;

    /**
     * Variable constant <code>FEATURE_CLASS_ATTRIBUTE_TABLE</code> keeps
     * value of
     */
    public static final String FEATURE_CLASS_ATTRIBUTE_TABLE = "fca";

    /** Variable constant <code>TABLE_FCA</code> keeps value of */
    public static final String TABLE_FCA = FEATURE_CLASS_ATTRIBUTE_TABLE;

    /**
     * Variable constant <code>FEATURE_CLASS_SCHEMA_TABLE</code> keeps
     * value of
     */
    public static final String FEATURE_CLASS_SCHEMA_TABLE = "fcs";

    /** Variable constant <code>TABLE_FCS</code> keeps value of */
    public static final String TABLE_FCS = FEATURE_CLASS_SCHEMA_TABLE;

    /** Variable constant <code>FACE_SPATIAL_INDEX</code> keeps value of */
    public static final String FACE_SPATIAL_INDEX = "fsi";

    /** Variable constant <code>TABLE_FSI</code> keeps value of */
    public static final String TABLE_FSI = FACE_SPATIAL_INDEX;

    /**
     * Variable constant <code>GEOGRAPHIC_REFERENCE_TABLE</code> keeps
     * value of
     */
    public static final String GEOGRAPHIC_REFERENCE_TABLE = "grt";

    /** Variable constant <code>TABLE_GRT</code> keeps value of */
    public static final String TABLE_GRT = GEOGRAPHIC_REFERENCE_TABLE;

    /** Variable constant <code>LIBRARY_ATTTIBUTE_TABLE</code> keeps
     * value of
     */
    public static final String LIBRARY_ATTTIBUTE_TABLE = "lat";

    /** Variable constant <code>TABLE_LAT</code> keeps value of */
    public static final String TABLE_LAT = LIBRARY_ATTTIBUTE_TABLE;

    /** Variable constant <code>LIBRARY_HEADER_TABLE</code> keeps value of */
    public static final String LIBRARY_HEADER_TABLE = "lht";

    /** Variable constant <code>TABLE_LHT</code> keeps value of */
    public static final String TABLE_LHT = LIBRARY_HEADER_TABLE;

    /**
     * Variable constant <code>ENTITY_NODE_SPATIAL_INDEX</code> keeps
     * value of
     */
    public static final String ENTITY_NODE_SPATIAL_INDEX = "nsi";

    /** Variable constant <code>TABLE_NSI</code> keeps value of */
    public static final String TABLE_NSI = ENTITY_NODE_SPATIAL_INDEX;

    /** Variable constant <code>RING_TABLE</code> keeps value of */
    public static final String RING_TABLE = "rng";

    /** Variable constant <code>TABLE_RNG</code> keeps value of */
    public static final String TABLE_RNG = RING_TABLE;

    /** Variable constant <code>TEXT_PRIMITIVE</code> keeps value of */
    public static final String TEXT_PRIMITIVE = "txt";

    /** Variable constant <code>TABLE_TXT</code> keeps value of */
    public static final String TABLE_TXT = TEXT_PRIMITIVE;

    /** Variable constant <code>TEXT_SPATIAL_INDEX</code> keeps value of */
    public static final String TEXT_SPATIAL_INDEX = "tsi";

    /** Variable constant <code>TABLE_TSI</code> keeps value of */
    public static final String TABLE_TSI = TEXT_SPATIAL_INDEX;

    /**
     * Variable constant <code>CHARACTER_VALUE_DESCRIPTION_TABLE</code> keeps
     * value of
     */
    public static final String CHARACTER_VALUE_DESCRIPTION_TABLE = "char.vdt";

    /** Variable constant <code>TABLE_CHAR</code> keeps value of */
    public static final String TABLE_CHAR = CHARACTER_VALUE_DESCRIPTION_TABLE;

    /**
     * Variable constant <code>INTEGER_VALUE_DESCRIPTION_TABLE</code> keeps
     * value of
     */
    public static final String INTEGER_VALUE_DESCRIPTION_TABLE = "int.vdt";

    /** Variable constant <code>TABLE_INT</code> keeps value of */
    public static final String TABLE_INT = INTEGER_VALUE_DESCRIPTION_TABLE;

    // Table reserved extensions

    /**
     * Variable constant <code>AREA_BOUMDING_RECTANGLE_TABLE</code> keeps
     * value of
     */
    public static final String AREA_BOUMDING_RECTANGLE_TABLE = ".abr";

    /** Variable constant <code>EXT_ABR</code> keeps value of */
    public static final String EXT_ABR = AREA_BOUMDING_RECTANGLE_TABLE;

    /** Variable constant <code>AREA_FEATURE_TABLE</code> keeps value of */
    public static final String AREA_FEATURE_TABLE = ".aft";

    /** Variable constant <code>EXT_AFT</code> keeps value of */
    public static final String EXT_AFT = AREA_FEATURE_TABLE;

    /** Variable constant <code>AREA_JOIN_TABLE</code> keeps value of */
    public static final String AREA_JOIN_TABLE = ".ajt";

    /** Variable constant <code>EXT_AJT</code> keeps value of */
    public static final String EXT_AJT = AREA_JOIN_TABLE;

    /** Variable constant <code>AREA_THEMATIC_INDEX</code> keeps value of */
    public static final String AREA_THEMATIC_INDEX = ".ati";

    /** Variable constant <code>EXT_ATI</code> keeps value of */
    public static final String EXT_ATI = AREA_THEMATIC_INDEX;

    /**
     * Variable constant <code>COMPLEX_BOUNDING_RECTANGLE_TABLE</code> keeps
     * value of
     */
    public static final String COMPLEX_BOUNDING_RECTANGLE_TABLE = ".cbr";

    /** Variable constant <code>EXT_CBR</code> keeps value of */
    public static final String EXT_CBR = COMPLEX_BOUNDING_RECTANGLE_TABLE;

    /** Variable constant <code>COMPLEX_FEATURE_TABLE</code> keeps value of */
    public static final String COMPLEX_FEATURE_TABLE = ".cft";

    /** Variable constant <code>EXT_CFT</code> keeps value of */
    public static final String EXT_CFT = COMPLEX_FEATURE_TABLE;

    /** Variable constant <code>COMPLEX_JOIN_TABLE</code> keeps value of */
    public static final String COMPLEX_JOIN_TABLE = ".cjt";

    /** Variable constant <code>EXT_CJT</code> keeps value of */
    public static final String EXT_CJT = COMPLEX_JOIN_TABLE;

    /** Variable constant <code>COMPLEX_THEMATIC_INDEX</code> keeps value of */
    public static final String COMPLEX_THEMATIC_INDEX = ".cti";

    /** Variable constant <code>EXT_CTI</code> keeps value of */
    public static final String EXT_CTI = COMPLEX_THEMATIC_INDEX;

    /** Variable constant <code>NARRATIVE_TABLE</code> keeps value of */
    public static final String NARRATIVE_TABLE = ".doc";

    /** Variable constant <code>EXT_DOC</code> keeps value of */
    public static final String EXT_DOC = NARRATIVE_TABLE;

    /** Variable constant <code>DIAGNOSITC_POINT_TABLE</code> keeps value of */
    public static final String DIAGNOSITC_POINT_TABLE = ".dpt";

    /** Variable constant <code>EXT_DPT</code> keeps value of */
    public static final String EXT_DPT = DIAGNOSITC_POINT_TABLE;

    /** Variable constant <code>FEATURE_INDEX_TABLE</code> keeps value of */
    public static final String FEATURE_INDEX_TABLE = ".fit";

    /** Variable constant <code>EXT_FIT</code> keeps value of */
    public static final String EXT_FIT = FEATURE_INDEX_TABLE;

    /** Variable constant <code>FEATURE_THEMATIC_INDEX</code> keeps value of */
    public static final String FEATURE_THEMATIC_INDEX = ".fti";

    /** Variable constant <code>EXT_FTI</code> keeps value of */
    public static final String EXT_FTI = FEATURE_THEMATIC_INDEX;

    /** Variable constant <code>JOIN_THEMATIC_INDEX</code> keeps value of */
    public static final String JOIN_THEMATIC_INDEX = ".jti";

    /** Variable constant <code>EXT_JTI</code> keeps value of */
    public static final String EXT_JTI = JOIN_THEMATIC_INDEX;

    /**
     * Variable constant <code>LINE_BOUNDING_RECTANGLE_TABLE</code> keeps
     * value of
     */
    public static final String LINE_BOUNDING_RECTANGLE_TABLE = ".lbr";

    /** Variable constant <code>EXT_LBR</code> keeps value of */
    public static final String EXT_LBR = LINE_BOUNDING_RECTANGLE_TABLE;

    /** Variable constant <code>LINE_FEATURE_TABLE</code> keeps value of */
    public static final String LINE_FEATURE_TABLE = ".lft";

    /** Variable constant <code>EXT_LFT</code> keeps value of */
    public static final String EXT_LFT = LINE_FEATURE_TABLE;

    /** Variable constant <code>LINE_JOIN_TABLE</code> keeps value of */
    public static final String LINE_JOIN_TABLE = ".ljt";

    /** Variable constant <code>EXT_LJT</code> keeps value of */
    public static final String EXT_LJT = LINE_JOIN_TABLE;

    /** Variable constant <code>LINE_THEMATIC_INDEX</code> keeps value of */
    public static final String LINE_THEMATIC_INDEX = ".lti";

    /** Variable constant <code>EXT_LTI</code> keeps value of */
    public static final String EXT_LTI = LINE_THEMATIC_INDEX;

    /**
     * Variable constant <code>POINT_BOUNDING_RECTANGLE_TABLE</code> keeps
     * value of
     */
    public static final String POINT_BOUNDING_RECTANGLE_TABLE = ".pbr";

    /** Variable constant <code>EXT_PBR</code> keeps value of */
    public static final String EXT_PBR = POINT_BOUNDING_RECTANGLE_TABLE;

    /** Variable constant <code>POINT_FEATURE_TABLE</code> keeps value of */
    public static final String POINT_FEATURE_TABLE = ".pft";

    /** Variable constant <code>EXT_PFT</code> keeps value of */
    public static final String EXT_PFT = POINT_FEATURE_TABLE;

    /** Variable constant <code>POINT_JOIN_TABLE</code> keeps value of */
    public static final String POINT_JOIN_TABLE = ".pjt";

    /** Variable constant <code>EXT_PJT</code> keeps value of */
    public static final String EXT_PJT = POINT_JOIN_TABLE;

    /** Variable constant <code>POINT_THEMATIC_INDEX</code> keeps value of */
    public static final String POINT_THEMATIC_INDEX = ".pti";

    /** Variable constant <code>EXT_PTI</code> keeps value of */
    public static final String EXT_PTI = POINT_THEMATIC_INDEX;

    /** Variable constant <code>RELATED_ATTRIBUTE_TABLE</code> keeps
     * value of
     */
    public static final String RELATED_ATTRIBUTE_TABLE = ".rat";

    /** Variable constant <code>EXT_RAT</code> keeps value of */
    public static final String EXT_RAT = RELATED_ATTRIBUTE_TABLE;

    /** Variable constant <code>REGISTRATION_POINT_TABLE</code> keeps
     * value of
     */
    public static final String REGISTRATION_POINT_TABLE = ".rpt";

    /** Variable constant <code>EXT_RPT</code> keeps value of */
    public static final String EXT_RPT = REGISTRATION_POINT_TABLE;

    /** Variable constant <code>TEXT_FEATURE_TABLE</code> keeps value of */
    public static final String TEXT_FEATURE_TABLE = ".tft";

    /** Variable constant <code>EXT_TFT</code> keeps value of */
    public static final String EXT_TFT = TEXT_FEATURE_TABLE;

    /** Variable constant <code>TEXT_THEMATIC_TABLE</code> keeps value of */
    public static final String TEXT_THEMATIC_TABLE = ".tti";

    /** Variable constant <code>EXT_TTI</code> keeps value of */
    public static final String EXT_TTI = TEXT_THEMATIC_TABLE;

    // Reserved directory names

    /**
     * Variable constant <code>LIBRARY_REFERENCE_COVERAGE</code> keeps value of
     */
    public static final String LIBRARY_REFERENCE_COVERAGE = "libref";

    /** Variable constant <code>DIR_LIBREF</code> keeps value of */
    public static final String DIR_LIBREF = LIBRARY_REFERENCE_COVERAGE;

    /** Variable constant <code>DATA_QUALITY_COVERAGE</code> keeps value of */
    public static final String DATA_QUALITY_COVERAGE = "dq";

    /** Variable constant <code>DIR_DQ</code> keeps value of */
    public static final String DIR_DQ = DATA_QUALITY_COVERAGE;

    /** Variable constant <code>TILE_REFERENCE_COVERAGE</code> keeps
     * value of
     */
    public static final String TILE_REFERENCE_COVERAGE = "tileref";

    /** Variable constant <code>DIR_TILEREF</code> keeps value of */
    public static final String DIR_TILEREF = TILE_REFERENCE_COVERAGE;

    /** Variable constant <code>NAMES_REFERENCE_COVERAGE</code> keeps
     * value of
     */
    public static final String NAMES_REFERENCE_COVERAGE = "gazette";

    /** Variable constant <code>DIR_GAZETTE</code> keeps value of */
    public static final String DIR_GAZETTE = NAMES_REFERENCE_COVERAGE;
}

// FileConstants
