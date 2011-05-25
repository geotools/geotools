/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    
 *    THIS IS AN AUTOMATICALLY GENERATED FILE. DO NOT EDIT!
 *    Generated with: org.geotools.resources.IndexedResourceCompiler
 */
package org.geotools.resources.i18n;


/**
 * Resource keys. This class is used when compiling sources, but
 * no dependencies to {@code ResourceKeys} should appear in any
 * resulting class files.  Since Java compiler inlines final integer
 * values, using long identifiers will not bloat constant pools of
 * classes compiled against the interface, provided that no class
 * implements this interface.
 *
 * @see org.geotools.resources.IndexedResourceBundle
 * @see org.geotools.resources.IndexedResourceCompiler
 *
 * @source $URL$
 */
public final class LoggingKeys {
    private LoggingKeys() {
    }

    /**
     * Grid geometry has been adjusted for coverage "{0}".
     */
    public static final int ADJUSTED_GRID_GEOMETRY_$1 = 0;

    /**
     * Ambiguity between inverse flattening and semi minor axis length. Using inverse flattening.
     */
    public static final int AMBIGUOUS_ELLIPSOID = 1;

    /**
     * {3,choice,0#Apply|Reuse} operation "{1}" on coverage "{0}" with interpolation "{2}".
     */
    public static final int APPLIED_OPERATION_$4 = 2;

    /**
     * Resampled coverage "{0}" from coordinate system "{1}" (for an image of size {2}×{3}) to
     * coordinate system "{4}" (image size {5}×{6}). JAI operation is "{7}" with "{9}"
     * interpolation on {8,choice,0#packed|1#geophysics} pixels values. Background value is ({10}).
     */
    public static final int APPLIED_RESAMPLE_$11 = 3;

    /**
     * Converted "{0}" from "{1}" to "{2}" units. We assume that this is the expected units for
     * computation purpose.
     */
    public static final int APPLIED_UNIT_CONVERSION_$3 = 4;

    /**
     * Failed to bind a "{0}" entry.
     */
    public static final int CANT_BIND_DATASOURCE_$1 = 5;

    /**
     * Failed to create a coordinate operation from "{0}" authority factory.
     */
    public static final int CANT_CREATE_COORDINATE_OPERATION_$1 = 6;

    /**
     * Failed to dispose the backing store after timeout.
     */
    public static final int CANT_DISPOSE_BACKING_STORE = 7;

    /**
     * Can't load a service for category "{0}". Cause is "{1}".
     */
    public static final int CANT_LOAD_SERVICE_$2 = 8;

    /**
     * Can't read "{0}".
     */
    public static final int CANT_READ_FILE_$1 = 9;

    /**
     * Can't register JAI operation "{0}". Some grid coverage operations may not work.
     */
    public static final int CANT_REGISTER_JAI_OPERATION_$1 = 10;

    /**
     * Changed the renderer coordinate system. Cause is:
     */
    public static final int CHANGED_COORDINATE_REFERENCE_SYSTEM = 11;

    /**
     * Closed the EPSG database connection.
     */
    public static final int CLOSED_EPSG_DATABASE = 12;

    /**
     * Connected to EPSG database "{0}" on "{1}".
     */
    public static final int CONNECTED_EPSG_DATABASE_$2 = 13;

    /**
     * Created coordinate operation "{0}" for source CRS "{1}" and target CRS "{2}".
     */
    public static final int CREATED_COORDINATE_OPERATION_$3 = 14;

    /**
     * Created a "{0}" entry in the naming system.
     */
    public static final int CREATED_DATASOURCE_ENTRY_$1 = 15;

    /**
     * Created serializable image for coverage "{0}" using the "{1}" codec.
     */
    public static final int CREATED_SERIALIZABLE_IMAGE_$2 = 16;

    /**
     * Creating cached EPSG database version {0}. This operation may take a few minutes...
     */
    public static final int CREATING_CACHED_EPSG_DATABASE_$1 = 17;

    /**
     * Deferred painting for tile ({0},{1}).
     */
    public static final int DEFERRED_TILE_PAINTING_$2 = 18;

    /**
     * Excessive memory usage.
     */
    public static final int EXCESSIVE_MEMORY_USAGE = 19;

    /**
     * Tile cache capacity exceed maximum heap size ({0} Mb).
     */
    public static final int EXCESSIVE_TILE_CACHE_$1 = 20;

    /**
     * Factory implementations for category {0}:
     */
    public static final int FACTORY_IMPLEMENTATIONS_$1 = 21;

    /**
     * Failure in the primary factory: {0} Now trying the fallback factory...
     */
    public static final int FALLBACK_FACTORY_$1 = 22;

    /**
     * Found {0} reference systems in {1} elements. The most frequent appears {2} time and the less
     * frequent appears {3} times.
     */
    public static final int FOUND_MISMATCHED_CRS_$4 = 23;

    /**
     * Ignored "{0}" hint.
     */
    public static final int HINT_IGNORED_$1 = 24;

    /**
     * Initializing transformation from {0} to {1}.
     */
    public static final int INITIALIZING_TRANSFORMATION_$2 = 25;

    /**
     * Loaded "{0}" JDBC driver version {1}.{2}.
     */
    public static final int LOADED_JDBC_DRIVER_$3 = 26;

    /**
     * Loading datum aliases from "{0}".
     */
    public static final int LOADING_DATUM_ALIASES_$1 = 27;

    /**
     * Text were discarted for some locales.
     */
    public static final int LOCALES_DISCARTED = 28;

    /**
     * No coordinate operation from "{0}" to "{1}" because of mismatched factories.
     */
    public static final int MISMATCHED_COORDINATE_OPERATION_FACTORIES_$2 = 29;

    /**
     * The type of the requested object doesn't match the "{0}" URN type.
     */
    public static final int MISMATCHED_URN_TYPE_$1 = 30;

    /**
     * Native acceleration {1,choice,0#disabled|enabled} for "{0}" operation.
     */
    public static final int NATIVE_ACCELERATION_STATE_$2 = 31;

    /**
     * Offscreen rendering failed for layer "{0}". Fall back on default rendering.
     */
    public static final int OFFSCREEN_RENDERING_FAILED_$1 = 32;

    /**
     * Renderer "{0}" painted in {1} seconds.
     */
    public static final int PAINTING_LAYER_$2 = 33;

    /**
     * Polygons drawn with {0,number,percent} of available points, reusing {1,number,percent} from
     * the cache (resolution: {2} {3}).
     */
    public static final int POLYGON_CACHE_USE_$4 = 34;

    /**
     * Failed to allocate {0} Mb of memory. Trying a smaller memory allocation.
     */
    public static final int RECOVERABLE_OUT_OF_MEMORY_$1 = 35;

    /**
     * Log records are redirected to Apache commons logging.
     */
    public static final int REDIRECTED_TO_COMMONS_LOGGING = 36;

    /**
     * Registered Geotools extensions to JAI operations.
     */
    public static final int REGISTERED_JAI_OPERATIONS = 37;

    /**
     * Select an image of "{0}" decimated to level {1} of {2}.
     */
    public static final int RESSAMPLING_RENDERED_IMAGE_$3 = 38;

    /**
     * Creates a {1,choice,0#packed|1#geophysics|2#photographic} view of grid coverage "{0}" using
     * operation "{2}".
     */
    public static final int SAMPLE_TRANSCODE_$3 = 39;

    /**
     * Layer "{0}" send a repaint event for the whole widget area.
     */
    public static final int SEND_REPAINT_EVENT_$1 = 40;

    /**
     * Layer "{0}" send a repaint event for pixels x=[{1}..{2}] and y=[{3}..{4}] in widget area.
     */
    public static final int SEND_REPAINT_EVENT_$5 = 41;

    /**
     * Unavailable authority factory: {0}
     */
    public static final int UNAVAILABLE_AUTHORITY_FACTORY_$1 = 42;

    /**
     * Attempt to recover from unexpected exception.
     */
    public static final int UNEXPECTED_EXCEPTION = 43;

    /**
     * Unexpected unit "{0}". Map scale may be inacurate.
     */
    public static final int UNEXPECTED_UNIT_$1 = 44;

    /**
     * Ignoring unknow parameter: "{0}" = {1} {2}.
     */
    public static final int UNKNOW_PARAMETER_$3 = 45;

    /**
     * Can't handle style of class {0}. Consequently, geometry "{1}" will ignore its style
     * information.
     */
    public static final int UNKNOW_STYLE_$2 = 46;

    /**
     * Unrecognized scale type: "{0}". Default to linear.
     */
    public static final int UNRECOGNIZED_SCALE_TYPE_$1 = 47;

    /**
     * Update the cache for layer "{0}".
     */
    public static final int UPDATE_RENDERER_CACHE_$1 = 48;

    /**
     * Using "{0}" as {1} factory.
     */
    public static final int USING_FILE_AS_FACTORY_$2 = 49;
}
