/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.process.raster;

import it.geosolutions.jaiext.jiffle.Jiffle;
import it.geosolutions.jaiext.jiffle.JiffleException;
import it.geosolutions.jaiext.jiffle.parser.node.Band;
import it.geosolutions.jaiext.jiffle.parser.node.Expression;
import it.geosolutions.jaiext.jiffle.parser.node.GetSourceValue;
import it.geosolutions.jaiext.jiffle.parser.node.ScalarLiteral;
import it.geosolutions.jaiext.jiffle.runtime.BandTransform;
import it.geosolutions.jaiext.jiffleop.JiffleDescriptor;
import it.geosolutions.jaiext.jiffleop.JiffleRIF;
import it.geosolutions.jaiext.range.Range.DataType;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.processing.operation.GridCoverage2DRIA;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.jai.Registry;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.util.ProgressListener;

@DescribeProcess(title = "Jiffle map algebra", description = "Map algebra powered by Jiffle")
public class JiffleProcess implements RasterProcess {

    static {
        Registry.registerRIF(
                JAI.getDefaultInstance(),
                new JiffleDescriptor(),
                new JiffleRIF(),
                "it.geosolutions.jaiext");
    }

    static final Logger LOGGER = Logging.getLogger(JiffleProcess.class);

    public static final String IN_COVERAGE = "coverage";
    public static final String IN_SCRIPT = "script";
    public static final String IN_DEST_NAME = "destName";
    public static final String IN_SOURCE_NAME = "sourceName";
    public static final String IN_OUTPUT_TYPE = "outputType";
    public static final String OUTPUT_BAND_COUNT = "bandCount";
    public static final String OUTPUT_BAND_NAMES = "bandNames";
    public static final String OUT_RESULT = "result";
    public static final String TX_BANDS = "bands";

    /**
     * Executes a Jiffle raster algebra. Check the {@link DescribeParameter} annotations for a
     * description of the various arguments
     */
    @DescribeResult(name = OUT_RESULT, description = "The map algebra output")
    public GridCoverage2D execute(
            @DescribeParameter(name = IN_COVERAGE, description = "Source raster(s)")
                    GridCoverage2D[] coverages,
            @DescribeParameter(
                            name = IN_SCRIPT,
                            description =
                                    "The script performing the map algebra, in Jiffle language")
                    String script,
            @DescribeParameter(
                            name = IN_DEST_NAME,
                            description =
                                    "Name of the output, as used in the script (defaults to 'dest' if not specified)",
                            min = 0)
                    String destName,
            @DescribeParameter(
                            name = IN_SOURCE_NAME,
                            description =
                                    "Name of the inputs, as used in the script (default to src, src1, src2, ... if not specified)",
                            min = 0)
                    String[] sourceNames,
            @DescribeParameter(
                            name = IN_OUTPUT_TYPE,
                            description =
                                    "Output data type, BYTE, USHORT, SHORT, INT, FLOAT, DOUBLE. Defaults to DOUBLE if not specified",
                            min = 0)
                    DataType dataType,
            @DescribeParameter(
                            name = OUTPUT_BAND_COUNT,
                            description =
                                    "Number of output bands. If not specified, will try to infer from the script, which will be possible only if the output band indices are literals.",
                            min = 0,
                            minValue = 1)
                    Integer outputBandCount,
            @DescribeParameter(
                            name = OUTPUT_BAND_NAMES,
                            description =
                                    "Comma separate list of output band names. If not specified, will use 'jiffle' for single banded output, 'jiffle1', 'jiffle2', and so on for multi-band outputs",
                            min = 0)
                    String outputBandNames,
            ProgressListener progressListener)
            throws ProcessException, JiffleException {
        if (coverages.length == 0) {
            // we could remove this limit, but a few extra input parameters are needed (output
            // raster size and output envelope, with CRS)
            throw new IllegalArgumentException("Need at least one coverage in input");
        }
        // prepare the input rendered images
        RenderedImage[] sources = new RenderedImage[coverages.length];
        GridCoverage2D reference = coverages[0];
        sources[0] = reference.getRenderedImage();
        for (int i = 1; i < sources.length; i++) {
            GridCoverage2D coverage = coverages[i];
            double[] nodata = CoverageUtilities.getBackgroundValues(coverage);
            ROI roi = CoverageUtilities.getROIProperty(coverage);
            // TODO: this could be improved to allow some tolerances, e.g., same structure
            // with a max deviation
            if (coverage.getGridGeometry().equals(reference.getGridGeometry())) {
                sources[i] = coverage.getRenderedImage();
            } else {
                sources[i] =
                        GridCoverage2DRIA.create(
                                coverage, reference, nodata, GeoTools.getDefaultHints(), roi);
            }
        }

        // in case we have optimized out band selection, need to remap the band access
        BandTransform[] bandTransforms = null;
        if (sources.length == 1) {
            BandTransform bt =
                    getRenderingTransformationBandTransform(script, sourceNames, sources[0]);
            bandTransforms = new BandTransform[] {bt};
        }

        Integer awtDataType = dataType == null ? null : dataType.getDataType();
        if (outputBandCount == null && outputBandNames != null) {
            outputBandCount = outputBandNames.split("\\s*,\\s*").length;
        }
        RenderedOp result =
                JiffleDescriptor.create(
                        sources,
                        sourceNames,
                        destName,
                        script,
                        null,
                        awtDataType,
                        outputBandCount,
                        null,
                        bandTransforms,
                        GeoTools.getDefaultHints());

        GridSampleDimension[] sampleDimensions = getSampleDimensions(result, outputBandNames);
        GridCoverageFactory factory = new GridCoverageFactory(GeoTools.getDefaultHints());
        return factory.create(
                "jiffle", result, reference.getEnvelope(), sampleDimensions, coverages, null);
    }

    private GridSampleDimension[] getSampleDimensions(RenderedOp result, String outputBandNames) {
        SampleModel sm = result.getSampleModel();
        Stream<String> names = getSampleDimensionNames(sm.getNumBands(), outputBandNames);
        SampleDimensionType sourceType = TypeMap.getSampleDimensionType(sm, 0);
        NumberRange<? extends Number> range = TypeMap.getRange(sourceType);
        double[] nodata = null; // {Double.NaN};
        double min = range.getMinimum();
        double max = range.getMaximum();
        return names.map(
                        n ->
                                new GridSampleDimension(
                                        n, sourceType, null, nodata, min, max, 1, 0, null))
                .toArray(n -> new GridSampleDimension[n]);
    }

    private Stream<String> getSampleDimensionNames(int numBands, String outputBandNames) {
        if (outputBandNames == null) {
            return getDefaultBandNames(numBands);
        } else {
            String[] split = outputBandNames.split("\\s*,\\s*");
            if (split.length < numBands) {
                String[] defaultBands = getDefaultBandNames(numBands).toArray(b -> new String[b]);
                System.arraycopy(split, 0, defaultBands, 0, split.length);
                return Arrays.stream(defaultBands);
            }
            return Arrays.stream(split);
        }
    }

    private Stream<String> getDefaultBandNames(int numBands) {
        if (numBands == 1) {
            return Stream.of("jiffle");
        } else {
            return IntStream.range(1, numBands + 1).mapToObj(n -> "jiffle" + n);
        }
    }

    private BandTransform getRenderingTransformationBandTransform(
            String script, String[] sourceNames, RenderedImage source) throws JiffleException {
        // were we able to determine which bands are needed?
        int[] scriptBands = getTransformationBands(script, sourceNames);
        if (scriptBands == null) {
            return null;
        }

        // is there a mismatch between the available bands and the ones used in the script?
        // if so assume bands mapping has taken place in the RT
        int maxReadBand = Arrays.stream(scriptBands).max().getAsInt();
        // build the mapping as a lookup table
        final int[] map = new int[maxReadBand + 1];
        boolean mapRequired = false;
        for (int i = 0; i < scriptBands.length; i++) {
            int scriptBand = scriptBands[i];
            map[scriptBand] = i;
            mapRequired |= scriptBand != i;
        }

        if (!mapRequired) {
            return null;
        }

        // perform a simple mapping using the above lookup table
        return (x, y, scriptBand) -> map[scriptBand];
    }

    /**
     * This is called by the renderer to optimize the read, if possible, we'll customize the band
     * reading so that we read only what we know will be used by the script. At the time of writing,
     * this works only if band positions in the script are literals.
     */
    public GeneralParameterValue[] customizeReadParams(
            @DescribeParameter(
                            name = IN_SCRIPT,
                            description =
                                    "The script performing the map algebra, in Jiffle language")
                    String script,
            @DescribeParameter(
                            name = IN_DEST_NAME,
                            description =
                                    "Name of the output, as used in the script (defaults to 'dest' if not specified)",
                            min = 0)
                    String destName,
            @DescribeParameter(
                            name = IN_SOURCE_NAME,
                            description =
                                    "Name of the inputs, as used in the script (default to src, src1, src2, ... if not specified)",
                            min = 0)
                    String[] sourceNames,
            @DescribeParameter(
                            name = TX_BANDS,
                            description = "Bands read by the transformation",
                            min = 0)
                    int[] usedBands,
            GridCoverageReader reader,
            GeneralParameterValue[] params) {
        try {
            // do we have a band selection parameter in the input?
            ParameterValueGroup readerParams = reader.getFormat().getReadParameters();
            ParameterValue<?> bands =
                    readerParams.parameter(AbstractGridFormat.BANDS.getName(null));
            if (bands == null) {
                LOGGER.log(Level.FINE, "The reader does not support band selection, reading all");
                return params;
            }

            // cannot do anything if we cannot access the sample model
            if (!(reader instanceof GridCoverage2DReader)) {
                LOGGER.log(Level.FINE, "The reader is not a 2D one, reading all bands");
                return params;
            }
            GridCoverage2DReader r2d = (GridCoverage2DReader) reader;
            ImageLayout layout = r2d.getImageLayout();
            if (layout == null || layout.getSampleModel(null) == null) {
                LOGGER.log(Level.FINE, "Cannot determine the reader bands, reading them all");
                return params;
            }
            SampleModel sampleModel = layout.getSampleModel(null);

            // are we using less bands than available in the reader?
            if (usedBands == null) {
                usedBands = getTransformationBands(script, sourceNames);
            }
            if (usedBands == null || usedBands.length >= sampleModel.getNumBands()) {
                return params;
            }

            return mergeBandParam(params, usedBands);
        } catch (Exception e) {
            LOGGER.log(
                    Level.INFO,
                    "Failed to determine if we can read less bands based on the Jiffle script, continuing reading all source bands",
                    e);
            return params;
        }
    }

    private GeneralParameterValue[] mergeBandParam(GeneralParameterValue[] params, int[] bands) {
        // is it already there for some reason?
        if (params != null) {
            for (GeneralParameterValue param : params) {
                if (param.getDescriptor().getName().equals(AbstractGridFormat.BANDS.getName())) {
                    ((ParameterValue) param).setValue(bands);
                    return params;
                }
            }
        }

        // not found, need to add
        List<GeneralParameterValue> list =
                new ArrayList<>(params == null ? Collections.emptyList() : Arrays.asList(params));
        ParameterValue<int[]> value = AbstractGridFormat.BANDS.createValue();
        value.setValue(bands);
        list.add(value);
        return list.toArray(new GeneralParameterValue[list.size()]);
    }

    /**
     * Returns the source bands used, or null the bands indexes cannot be computed (e.g., they
     * depend on script variables)
     */
    private int[] getTransformationBands(String script, String[] sourceNames)
            throws JiffleException {
        // a rendering transformation only uses a single input
        String sourceName = "src";
        if (sourceNames != null && sourceNames.length > 0) {
            sourceName = sourceNames[0];
        }

        // get the reading positions
        Set<GetSourceValue> positions = Jiffle.getReadPositions(script, Arrays.asList(sourceName));
        if (positions.isEmpty()) {
            return null;
        }
        // extract the bands, bail out if a band is specified through an expression
        Set<Integer> bands = new HashSet<>();
        for (GetSourceValue position : positions) {
            Band band = position.getPos().getBand();
            Expression index = band.getIndex();
            if (index == null) {
                bands.add(0);
            } else if (index instanceof ScalarLiteral) {
                bands.add(Integer.valueOf(((ScalarLiteral) band.getIndex()).getValue()));
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                            Level.FINE,
                            "Cannot determine read bands, the source read spec use an expression "
                                    + "for the band, not a literal: "
                                    + position);
                }
                return null;
            }
        }

        return bands.stream().mapToInt(b -> b).sorted().toArray();
    }
}
