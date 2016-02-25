/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.dbf.index;

import java.io.PrintWriter;

import org.geotools.util.NullProgressListener;
import org.opengis.util.InternationalString;

/**
 * Implements a ProgressListener to write to a PrintWriter.
 * 
 * @author Alvaro Huarte
 */
class PrintWriterProgressListener extends NullProgressListener {
    
    PrintWriter out;
    int lastPercent = -1;
    
    public PrintWriterProgressListener(PrintWriter out) {
        this.out = out;
    }
    
    @Override
    public void setDescription(String description) {
        out.println(description);
    }
    @Override
    public void setTask(InternationalString task) {
        out.println(task);
    }
    
    @Override
    public void started() {
        lastPercent = 0;
        out.print("  0%");
    }
    
    @Override
    public void progress(float percent) {
        int percentDone = (int)percent;
        if (percentDone != lastPercent && percentDone % 5 == 0) {
            lastPercent = percentDone;
            out.print("\b\b\b\b" + String.format("%3s", percentDone) + "%");
            out.flush();
        }
    }
    
    @Override
    public void complete() {
        progress(100);
        out.println();
    }
}
