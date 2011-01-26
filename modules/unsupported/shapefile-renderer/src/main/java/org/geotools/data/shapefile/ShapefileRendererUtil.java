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
package org.geotools.data.shapefile;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

import org.geotools.data.FIDReader;
import org.geotools.data.shapefile.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.indexed.IndexedFidReader;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.data.shapefile.indexed.RecordNumberTracker;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.renderer.shape.shapehandler.simple.MultiLineHandler;
import org.geotools.renderer.shape.shapehandler.simple.MultiPointHandler;
import org.geotools.renderer.shape.shapehandler.simple.PointHandler;
import org.geotools.renderer.shape.shapehandler.simple.PolygonHandler;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Allows access the the ShapefileReaders.
 * 
 * @author jeichar
 * @since 2.1.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/branches/2.2.x/ext/shaperenderer/src/org/geotools/data/shapefile/ShapefileRendererUtil.java $
 */
public class ShapefileRendererUtil {
    /**
     * gets a shapefile reader with the custom shaperenderer shape handler.
     * 
     * @param ds the datastore used to obtain the reader
     * @param bbox the area, in data coordinates, of the viewed data.
     * @param mt The transform used to transform from data->world coordinates->screen coordinates
     * @param hasOpacity the transform from screen coordinates to world coordinates. Used for
     *        decimation.
     * @return
     * @throws IOException
     * @throws TransformException
     */
    public static ShapefileReader getShpReader( ShapefileDataStore ds, Envelope bbox, 
            Rectangle screenSize, MathTransform mt, boolean hasOpacity, boolean returnJTS ) throws IOException, TransformException {
        ShapefileReader reader = ds.openShapeReader(new GeometryFactory());
        ShapeType type = reader.getHeader().getShapeType();

        if ((type == ShapeType.ARC) || (type == ShapeType.ARCM) || (type == ShapeType.ARCZ)) {
            if( returnJTS )
                reader.setHandler(new org.geotools.renderer.shape.shapehandler.jts.MultiLineHandler(type, bbox, mt, hasOpacity, screenSize));
            else
                reader.setHandler(new MultiLineHandler(type, bbox, mt, hasOpacity, screenSize));
        }

        if ((type == ShapeType.POLYGON) || (type == ShapeType.POLYGONM)
                || (type == ShapeType.POLYGONZ)) {
            if( returnJTS )
                reader.setHandler(new org.geotools.renderer.shape.shapehandler.jts.PolygonHandler(type, bbox, mt, hasOpacity));
            else
            reader.setHandler(new PolygonHandler(type, bbox, mt, hasOpacity));
        }

        if ((type == ShapeType.POINT) || (type == ShapeType.POINTM) || (type == ShapeType.POINTZ)) {
            if( returnJTS )
                reader.setHandler(new org.geotools.renderer.shape.shapehandler.jts.PointHandler(type, bbox, screenSize, mt, hasOpacity));
            else
                reader.setHandler(new PointHandler(type, bbox, screenSize, mt, hasOpacity));
        }

        if ((type == ShapeType.MULTIPOINT) || (type == ShapeType.MULTIPOINTM)
                || (type == ShapeType.MULTIPOINTZ)) {
            if( returnJTS )
                reader.setHandler(new org.geotools.renderer.shape.shapehandler.jts.MultiPointHandler(type, bbox, screenSize, mt, hasOpacity));
            else
                reader.setHandler(new MultiPointHandler(type, bbox, screenSize, mt, hasOpacity));
        }

        return reader;
    }

    public static IndexedDbaseFileReader getDBFReader( ShapefileDataStore ds ) throws IOException {
        return new IndexedDbaseFileReader(ds.shpFiles, ds.useMemoryMappedBuffer, ds.getStringCharset());
    }
    private static final FileReader FILE_READER = new FileReader(){

        public String id() {
            return ShapefileRendererUtil.class.getName()+"$"+getClass().getName();
        }
        
    };

    public static ReadableByteChannel getShpReadChannel( ShapefileDataStore ds ) throws IOException {
        return ds.shpFiles.getReadChannel(ShpFileType.SHP, FILE_READER );
    }

    public static URL getshpURL( ShapefileDataStore ds ) {
        return ds.shpFiles.acquireRead(ShpFileType.SHP, FILE_READER);
    }

    public static FIDReader getFidReader( ShapefileDataStore datastore, RecordNumberTracker tracker )
            throws MalformedURLException {
        if (datastore instanceof IndexedShapefileDataStore) {
            IndexedShapefileDataStore ids = (IndexedShapefileDataStore)datastore;
            if( !ids.indexUseable(ShpFileType.FIX) )
                return createBasicFidReader(datastore, tracker);
            try{
                return new IndexedFidReader(ids.shpFiles, tracker);
            }catch (Exception e) {
                return createBasicFidReader(datastore,tracker);
            }
        } else {
            return createBasicFidReader(datastore, tracker);
        }
    }

    private static FIDReader createBasicFidReader(ShapefileDataStore datastore, final RecordNumberTracker tracker) {
        final String typename = datastore.getCurrentTypeName() + ".";
        return new FIDReader(){
            int i = 0;
            boolean closed = false;
            public void close() throws IOException {
                closed = true;
            }

            public boolean hasNext() throws IOException {
                if (closed)
                    return false;
                return true;
            }

            public String next() throws IOException {
                if (closed)
                    throw new IllegalStateException("Reader is closed"); //$NON-NLS-1$
                i++;
                return typename + tracker.getRecordNumber();
            }

        };
    }

    public static ShpFiles getShpFiles(ShapefileDataStore ds) {
        return ds.shpFiles;
    }
}
