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
package org.geotools.data;

import java.awt.geom.AffineTransform;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.referencing.operation.matrix.AffineTransform2D;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.referencing.operation.MathTransform;

/**
 * This class is responsible for creating a world file from a
 * {@link MathTransform} or {@link AffineTransform}.
 * 
 * <p>
 * The content of a world file describes an affine transformation which was used
 * by the ESRI software to map from the rater world to the spatial world (what we usually call a 
 * grid to world transform).
 * 
 * <p>
 * Specifically a world file should be as follows:
 * 
 * scalex
 * shearx
 * sheary
 * scaley
 * upperleftx
 * upperlefty
 * 
 * 
 * which translates into the following affine transformation.
 * 
 * |				:			|
 * | scalex	shearx	: translatex|
 * |				:			|
 * | sheary scaley	: transaltey|
 * |----------------------------|	
 * |    0     0		:      1	|	
 * |				:			|
 * 
 * <p>
 * Note that a world file usually takes as reference the centre of the pixel.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 * @source $URL$
 */
public class WorldFileWriter {
	/**
	 * Default buffer size we wil luse to write out.
	 */
	public static final int DEFAULT_BUFFER_SIZE = 4096;
	
	private static AffineTransform checkTransform(AffineTransform transform) {
		if(transform==null)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"transform"));
		return transform;
	}
	
	private static MathTransform checkMathTransform(MathTransform transform) {
		if(transform==null)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"transform"));
		return transform;
	}
	
	/**
	 * Logger for this class.
	 */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger("org.geotools.data.data");

	/**
	 * Constructor.
	 * 
	 * @param outLocation where to create the world file.
	 * @param transform the transformation that we want to write out.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileWriter(final File outLocation,
			final AffineTransform transform) throws IOException {
		this(outLocation, ProjectiveTransform.create(checkTransform(transform)),DEFAULT_BUFFER_SIZE);
	}
	


	/**
	 * Constructor.
	 * 
	 * @param outLocation where to create the world file.
	 * @param transform the transformation that we want to write out.
	 * @param buffSize size of the buffer to use.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileWriter(final File outLocation,
			final AffineTransform transform, final int buffSize) throws IOException {
		this(outLocation, ProjectiveTransform.create(checkTransform(transform)),buffSize);
	}

	/**
	 * Constructor. 
	 * 
	 * @param outLocation where to create the world file.
	 * @param transform the transformation that we want to write out.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileWriter(final OutputStream outLocation,
			final AffineTransform transform) throws IOException {
		this(outLocation, ProjectiveTransform.create(checkTransform(transform)));
	}
	
	/**
	 * Constructor. 
	 * 
	 * @param outLocation where to create the world file.
	 * @param transform the transformation that we want to write out.
	 * @param buffSize size of the buffer to use.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileWriter(final OutputStream outLocation,
			final AffineTransform transform, final int buffSize) throws IOException {
		this(outLocation, ProjectiveTransform.create(checkTransform(transform)),buffSize);
	}

	/**
	 * Constructor.
	 * 
	 * <p>
	 * Note that the {@link MathTransform} must be a 2D  affine transform.
	 * 
	 * @param outLocation where to create the world file.
	 * @param transform the transformation that we want to write out.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileWriter(final OutputStream outLocation,
			final MathTransform transform) throws IOException {
		this(outLocation, transform, DEFAULT_BUFFER_SIZE);
	}
	
	/**
	 * Constructor.
	 * 
	 * <p>
	 * Note that the {@link MathTransform} must be a 2D  affine transform.
	 * 
	 * @param outLocation where to create the world file.
	 * @param transform the transformation that we want to write out.
	 * @param buffSize size of the buffer to use.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileWriter(final OutputStream outLocation,
			final MathTransform transform, final int buffSize) throws IOException {
		if(outLocation==null)
			throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"outLocation"));
		if(transform.getSourceDimensions()!=2||transform.getTargetDimensions()!=2)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$3,"transform",transform.getSourceDimensions(),2));
		if(buffSize<=0)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"buffSize",buffSize));
		write( new BufferedWriter(
				new OutputStreamWriter(outLocation), buffSize), transform);
	}


	/**
	 * Write the provided transformation to the provided output.
	 * 
	 * @param writer to use for writing the transformation.
	 * @param transform is the transformation that we want to write out.
	 * @throws IOException in case something bad happens.
	 */
	private void write(BufferedWriter writer, MathTransform transform){
		try {
			if ((transform instanceof IdentityTransform)) {
				writer.write("1");
				writer.newLine();
				writer.write("0");
				writer.newLine();
				writer.write("0");
				writer.newLine();
				writer.write("1");
				writer.newLine();
				writer.write("0");
				writer.newLine();
				writer.write("0");
				close(writer);
				return;
			}
			if ((transform instanceof AffineTransform2D)) {
				final AffineTransform2D affine = (AffineTransform2D) transform;
				writer.write(Double.toString(affine.getScaleX()));
				writer.newLine();
				writer.write(Double.toString(affine.getShearX()));
				writer.newLine();
				writer.write(Double.toString(affine.getShearY()));
				writer.newLine();
				writer.write(Double.toString(affine.getScaleY()));
				writer.newLine();
				writer.write(Double.toString(affine.getTranslateX()));
				writer.newLine();
				writer.write(Double.toString(affine.getTranslateY()));
				close(writer);
				return;
			}
			assert false:transform;
		} catch (IOException e) {
			if(LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE,e.getLocalizedMessage(),e);
		} finally {
			close(writer);
		}
		

	}

	private void close(BufferedWriter writer) {
		try{
			writer.close();
		}catch (Throwable t) {
			if(LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE,t.getLocalizedMessage(),t);
		}
		
	}

	/**
	 * 
	 * Constructor.
	 * 
	 * <p>
	 * Note that the {@link MathTransform} must be a 2D  affine transform.
	 * 
	 * @param outLocation where to create the world file.
	 * @param transform the transformation that we want to write out.
	 * @param buffSize size of the buffer to use.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileWriter(final File outLocation, final MathTransform transform, int buffSize)
			throws IOException {
		if (outLocation == null)
			throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"outLocation"));
		checkMathTransform(transform);
		if(transform.getSourceDimensions()!=2||transform.getTargetDimensions()!=2)
			throw new IllegalArgumentException(Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$3,"transform",transform.getSourceDimensions(),2));
		if( !outLocation.canRead()
			|| !outLocation.isFile() ) 
			throw new IllegalArgumentException(Errors.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1,outLocation));
		// create a writer
		write( new BufferedWriter(new FileWriter(
				outLocation), buffSize), transform);
	}
	/**
	 * 
	 * Constructor.
	 * 
	 * <p>
	 * Note that the {@link MathTransform} must be a 2D  affine transform.
	 * 
	 * @param outLocation where to create the world file.
	 * @param transform the transformation that we want to write out.
	 * @throws IOException in case something bad happens.
	 */
	public WorldFileWriter(final File outLocation, final MathTransform transform)
			throws IOException {
		this(outLocation, transform, DEFAULT_BUFFER_SIZE);
	}
}
