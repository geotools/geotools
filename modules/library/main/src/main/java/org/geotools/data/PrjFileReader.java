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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.geotools.factory.Hints;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.resources.NIOUtilities;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Simone Giannecchini
 * @since 2.3
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/module/main/src/org/geotools/data/PrjFileReader.java $
 */
public class PrjFileReader {

	ByteBuffer buffer;

	ReadableByteChannel channel;

	CharBuffer charBuffer;

	CharsetDecoder decoder;

	CoordinateReferenceSystem crs;

	/**
	 * Load the index file from the given channel.
	 * 
	 * @param channel
	 *            The channel to read from.
	 * @throws IOException
	 *             If an error occurs.
	 */
	public PrjFileReader(ReadableByteChannel channel) throws IOException,
			FactoryException {
		this(channel, null);
	}

	/**
	 * Load the index file from the given channel.
	 * 
	 * @param channel
	 *            The channel to read from.
	 * @param hints
	 * 
	 * @throws IOException
	 *             If an error occurs.
	 */
	public PrjFileReader(ReadableByteChannel channel, final Hints hints)
			throws IOException, FactoryException {
		Charset chars = Charset.forName("ISO-8859-1");
		decoder = chars.newDecoder();
		this.channel = channel;

		init();

		// ok, everything is ready...
		decoder.decode(buffer, charBuffer, true);
		buffer.limit(buffer.capacity());
		charBuffer.flip();
		crs = ReferencingFactoryFinder.getCRSFactory(hints).createFromWKT(charBuffer.toString());
	}

	/**
	 * Return the Coordinate Reference System retrieved by this reader.
	 * 
	 * @deprecated use {@link #getCoordinateReferenceSystem()}.
	 */
	public CoordinateReferenceSystem getCoodinateSystem() {
		return crs;
	}
	
	/**
	 * Return the Coordinate Reference System retrieved by this reader.
	 * 
	 * @return the Coordinate Reference System 
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return crs;
	}

	private int fill(ByteBuffer buffer, ReadableByteChannel channel)
			throws IOException {
		int r = buffer.remaining();
		// channel reads return -1 when EOF or other error
		// because they a non-blocking reads, 0 is a valid return value!!
		while (buffer.remaining() > 0 && r != -1) {
			r = channel.read(buffer);
		}
		if (r == -1) {
			buffer.limit(buffer.position());
		}
		return r;
	}

	private void init() throws IOException {
		// create the ByteBuffer
		// if we have a FileChannel, lets map it
		if (channel instanceof FileChannel) {
			FileChannel fc = (FileChannel) channel;
			buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			buffer.position((int) fc.position());
		} else {
			// Some other type of channel
			// start with a 8K buffer, should be more than adequate
			int size = 8 * 1024;
			// if for some reason its not, resize it
			// size = header.getRecordLength() > size ? header.getRecordLength()
			// : size;
			buffer = ByteBuffer.allocateDirect(size);
			// fill it and reset
			fill(buffer, channel);
			buffer.flip();
		}

		// The entire file is in little endian
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		charBuffer = CharBuffer.allocate(8 * 1024);
		Charset chars = Charset.forName("ISO-8859-1");
		decoder = chars.newDecoder();

	}

	public void close() throws IOException {
	    if(buffer != null) {
	        NIOUtilities.clean(buffer); // will close if a MappedByteBuffer
	        buffer = null;
	    }
		if (channel.isOpen()) {
			channel.close();
		}
	}

}
