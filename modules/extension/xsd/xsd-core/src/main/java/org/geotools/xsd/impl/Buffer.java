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
package org.geotools.xsd.impl;

/**
 * A simple thread safe buffer.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class Buffer {
    static final int DEFAULT_SIZE = 1024;
    Object[] buffer;
    int in;
    int out;
    int size;
    boolean closed;

    public Buffer() {
        this(DEFAULT_SIZE);
    }

    public Buffer(int size) {
        buffer = new Object[size];
        in = 0;
        out = 0;
        size = 0;
        closed = false;
    }

    public synchronized void put(Object object) {
        while (size == buffer.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        buffer[in++] = object;
        in = in == buffer.length ? 0 : in;
        size++;

        notifyAll();
    }

    public synchronized Object get() {
        while (size == 0) {
            if (closed) {
                return null;
            }

            try {
                wait(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Object object = buffer[out];
        buffer[out++] = null;
        out = out == buffer.length ? 0 : out;
        size--;

        notifyAll();

        return object;
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}
