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
 */
package org.geotools.data;

import java.io.IOException;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * Provides ...
 *
 * @author Sean Geoghegan, Defence Science and Technology Organisation.
 */
public class JoiningAttributeWriter implements AttributeWriter {
    private AttributeWriter[] writers;
    private int[] index;
    private AttributeDescriptor[] metaData;

    /** */
    public JoiningAttributeWriter(AttributeWriter... writers) {
        this.writers = writers;
        metaData = joinMetaData(writers);
    }

    private AttributeDescriptor[] joinMetaData(AttributeWriter... writers) {
        int total = 0;
        index = new int[writers.length];
        for (int i = 0, ii = writers.length; i < ii; i++) {
            index[i] = total;
            total += writers[i].getAttributeCount();
        }
        AttributeDescriptor[] md = new AttributeDescriptor[total];
        int idx = 0;
        for (AttributeWriter writer : writers) {
            for (int j = 0, jj = writer.getAttributeCount(); j < jj; j++) {
                md[idx] = writer.getAttributeType(j);
                idx++;
            }
        }
        return md;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.AttributeWriter#close()
     */
    @Override
    public void close() throws IOException {
        IOException dse = null;
        for (AttributeWriter writer : writers) {
            try {
                writer.close();
            } catch (IOException e) {
                dse = e;
            }
        }
        if (dse != null) throw dse;
    }

    @Override
    public boolean hasNext() throws IOException {
        for (AttributeWriter writer : writers) {
            if (writer.hasNext()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void next() throws IOException {
        for (AttributeWriter writer : writers) {
            // if (writers[i].hasNext()) Dont want to check this, need to be able to insert
            writer.next();
        }
    }

    /* (non-Javadoc)
     * @see org.geotools.data.AttributeWriter#write(int, java.lang.Object)
     */
    @Override
    public void write(int position, Object attribute) throws IOException {
        AttributeWriter writer = null;
        for (int i = index.length - 1; i >= 0; i--) {
            if (position >= index[i]) {
                position -= index[i];
                writer = writers[i];
                break;
            }
        }
        if (writer == null) throw new ArrayIndexOutOfBoundsException(position);

        writer.write(position, attribute);
    }

    @Override
    public int getAttributeCount() {
        return metaData.length;
    }

    @Override
    public AttributeDescriptor getAttributeType(int i) {
        return metaData[i];
    }
}
