/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.text;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.io.LineFormat;
import org.geotools.factory.GeoTools;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Descriptions;
import org.geotools.resources.i18n.DescriptionKeys;
import org.geotools.image.io.metadata.ImageGeometry;
import org.geotools.image.io.metadata.GeographicMetadata;


/**
 * Image decoder for text files storing pixel values as records.
 * Such text files use one line (record) by pixel. Each line contains
 * at least 3 columns (in arbitrary order):
 *
 * <ul>
 *   <li>Pixel's <var>x</var> coordinate.</li>
 *   <li>Pixel's <var>y</var> coordinate.</li>
 *   <li>An arbitrary number of pixel values.</li>
 * </ul>
 *
 * For example, some Sea Level Anomaly (SLA) files contains rows of longitude
 * (degrees), latitude (degrees), SLA (cm), East/West current (cm/s) and
 * North/South current (cm/s), as below:
 *
 * <blockquote><pre>
 * 45.1250 -29.8750    -7.28     10.3483     -0.3164
 * 45.1250 -29.6250    -4.97     11.8847      3.6192
 * 45.1250 -29.3750    -2.91      3.7900      3.0858
 * 45.1250 -29.1250    -3.48     -5.1833     -5.0759
 * 45.1250 -28.8750    -4.36     -1.8129    -16.3689
 * 45.1250 -28.6250    -3.91      7.5577    -24.6801
 * </pre>(...etc...)
 * </blockquote>
 *
 * From this decoder point of view, the two first columns (longitude and latitude)
 * are pixel's logical coordinate (<var>x</var>,<var>y</var>), while the three last
 * columns are three image's bands. The whole file contains only one image (unless
 * {@link #getNumImages} has been overridden). All (<var>x</var>,<var>y</var>)
 * coordinates belong to pixel's center. This decoder will automatically translate
 * (<var>x</var>,<var>y</var>) coordinates from logical space to pixel space.
 * <p>
 * By default, {@code TextRecordImageReader} assumes that <var>x</var> and
 * <var>y</var> coordinates appear in column #0 and 1 respectively. It also assumes
 * that numeric values are encoded using current defaults {@link java.nio.charset.Charset}
 * and {@link java.util.Locale}, and that there is no pad value. The easiest way to change
 * the default setting is to create a {@link Spi} subclass. There is no need to subclass
 * {@code TextRecordImageReader}, unless you want more control on the decoding process.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class TextRecordImageReader extends TextImageReader {
    /**
     * Petit facteur de tolérance servant à tenir compte des erreurs d'arrondissement.
     */
    private static final float EPS = 1E-5f;

    /**
     * Intervalle (en nombre d'octets) entre les rapports de progrès.
     */
    private static final int PROGRESS_INTERVAL = 4096;

    /**
     * Lorsque la lecture se fait par-dessus une image {@link BufferedReader} existante,
     * indique s'il faut effacer la région dans laquelle sera placée l'image avant de la
     * lire. La valeur {@code false} permettra de conserver les anciens pixels dans
     * les régions ou le fichier ne définit pas de nouvelles valeurs.
     */
    private static final boolean CLEAR = true;

    /**
     * Données des images, ou {@code null} si aucune lecture n'a encore été
     * faite. Chaque élément contient les données de l'image à l'index correspondant
     * (i.e. l'élément {@code data[0]} contient les données de l'image #0,
     * {@code data[1]} contient les données de l'image #1, etc.). Des éléments
     * de ce tableau peuvent être nuls si les données des images correspondantes
     * ne sont pas retenues après chaque lecture (c'est-à-dire si
     * <code>{@link #seekForwardOnly}==true</code>).
     */
    private RecordList[] data;

    /**
     * Index de la prochaine image à lire. Cet index n'est pas nécessairement
     * égal à la longueur du tableau {@link #data}. Il peut être aussi bien
     * plus petit que plus grand.
     */
    private int nextImageIndex;

    /**
     * Nombre moyen de caractères par données (incluant les espaces et les codes
     * de fin de ligne). Cette information n'est qu'à titre indicative, mais son
     * exactitude peut aider à accelerer la lecture et rendre les rapport des
     * progrès plus précis. Elle sera automatiquement mise à jour en fonction
     * des lignes lues.
     */
    private float expectedDatumLength = 10.4f;

    /**
     * Constructs a new image reader.
     *
     * @param provider the provider that is invoking this constructor, or {@code null} if none.
     */
    public TextRecordImageReader(final ImageReaderSpi provider) {
        super(provider);
    }

    /**
     * Returns the grid tolerance (epsilon) value.
     */
    private float getGridTolerance() {
        return (originatingProvider instanceof Spi) ? ((Spi)originatingProvider).gridTolerance : EPS;
    }

    /**
     * Returns the column number for <var>x</var> values. The default implementation returns
     * {@link TextRecordImageReader.Spi#xColumn}. Subclasses should override this method if
     * this information should be obtained in an other way.
     *
     * @param  imageIndex The index of the image to be queried.
     * @throws IOException If an error occurs reading the from the input source.
     */
    protected int getColumnX(final int imageIndex) throws IOException {
        return (originatingProvider instanceof Spi) ? ((Spi)originatingProvider).xColumn : 0;
    }

    /**
     * Invokes {@link #getColumnX} and checks the result.
     */
    private int getCheckedColumnX(final int imageIndex) throws IOException {
        final int xColumn = getColumnX(imageIndex);
        if (xColumn < 0) {
            throw new IllegalStateException(Errors.format(
                    ErrorKeys.NEGATIVE_COLUMN_$2, "x", xColumn));
        }
        return xColumn;
    }

    /**
     * Returns the column number for <var>x</var> values. The default implementation returns
     * {@link TextRecordImageReader.Spi#yColumn}. Subclasses should override this method if
     * this information should be obtained in an other way.
     *
     * @param  imageIndex The index of the image to be queried.
     * @throws IOException If an error occurs reading the from the input source.
     */
    protected int getColumnY(final int imageIndex) throws IOException {
        return (originatingProvider instanceof Spi) ? ((Spi)originatingProvider).yColumn : 1;
    }

    /**
     * Invokes {@link #getColumnY} and checks the result.
     */
    private int getCheckedColumnY(final int imageIndex) throws IOException {
        final int yColumn = getColumnY(imageIndex);
        if (yColumn < 0) {
            throw new IllegalStateException(Errors.format(
                    ErrorKeys.NEGATIVE_COLUMN_$2, "y", yColumn));
        }
        return yColumn;
    }

    /**
     * Retourne le numéro de colonne dans laquelle se trouvent les données de la
     * bande spécifiée. L'implémentation par défaut retourne {@code band}+1
     * ou 2 si la bande est plus grand ou égal à {@link #getColumnX} et/ou
     * {@link #getColumnY}. Cette implémentation devrait convenir pour des données
     * se trouvant aussi bien avant qu'après les colonnes <var>x</var>
     * et <var>y</var>, même si ces dernières ne sont pas consécutives.
     *
     * @param  imageIndex Index de l'image à lire.
     * @param  band Bande de l'image à lire.
     * @return Numéro de colonne des données de l'image.
     * @throws IOException si l'opération nécessitait une lecture du fichier (par exemple
     *         des informations inscrites dans un en-tête) et que cette lecture a échouée.
     */
    private int getColumn(final int imageIndex, int band) throws IOException {
        final int xColumn = getCheckedColumnX(imageIndex);
        final int yColumn = getCheckedColumnY(imageIndex);
        if (band >= Math.min(xColumn, yColumn)) band++;
        if (band >= Math.max(xColumn, yColumn)) band++;
        return band;
    }

    /**
     * Set the input source. It should be one of the following object, in preference order:
     * {@link java.io.File}, {@link java.net.URL}, {@link java.io.BufferedReader}.
     * {@link java.io.Reader}, {@link java.io.InputStream} or
     * {@link javax.imageio.stream.ImageInputStream}.
     */
    @Override
    public void setInput(final Object  input,
                         final boolean seekForwardOnly,
                         final boolean ignoreMetadata)
    {
        clear();
        super.setInput(input, seekForwardOnly, ignoreMetadata);
    }

    /**
     * Returns the number of bands available for the specified image.
     *
     * @param  imageIndex  The image index.
     * @throws IOException if an error occurs reading the information from the input source.
     */
    @Override
    public int getNumBands(final int imageIndex) throws IOException {
        return getRecords(imageIndex).getColumnCount() -
                (getCheckedColumnX(imageIndex) == getCheckedColumnY(imageIndex) ? 1 : 2);
    }

    /**
     * Returns the width in pixels of the given image within the input source.
     *
     * @param  imageIndex the index of the image to be queried.
     * @return Image width.
     * @throws IOException If an error occurs reading the width information from the input source.
     */
    public int getWidth(final int imageIndex) throws IOException {
        return getRecords(imageIndex).getPointCount(getCheckedColumnX(imageIndex), getGridTolerance());
    }

    /**
     * Returns the height in pixels of the given image within the input source.
     *
     * @param  imageIndex the index of the image to be queried.
     * @return Image height.
     * @throws IOException If an error occurs reading the height information from the input source.
     */
    public int getHeight(final int imageIndex) throws IOException {
        return getRecords(imageIndex).getPointCount(getCheckedColumnY(imageIndex), getGridTolerance());
    }

    /**
     * Returns metadata associated with the given image.
     * Calling this method may force loading of full image.
     *
     * @param  imageIndex The image index.
     * @return The metadata, or {@code null} if none.
     * @throws IOException If an error occurs reading the data information from the input source.
     */
    @Override
    public IIOMetadata getImageMetadata(final int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        if (ignoreMetadata) {
            return null;
        }
        final GeographicMetadata metadata = new GeographicMetadata(this);
        final ImageGeometry geometry = metadata.getGeometry();
        /*
         * Computes the smallest bounding box containing the full image in user coordinates.
         * This implementation searchs for minimum and maximum values in x and y columns as
         * returned by getColumnX() and getColumnY(). Reminder: xmax and ymax are INCLUSIVE
         * in the code below, as well as (width-1) and (height-1).
         */
        final float tolerance    = getGridTolerance();
        final RecordList records = getRecords(imageIndex);
        final int xColumn        = getCheckedColumnX(imageIndex);
        final int yColumn        = getCheckedColumnY(imageIndex);
        final int width          = records.getPointCount(xColumn, tolerance);
        final int height         = records.getPointCount(yColumn, tolerance);
        final double xmin        = records.getMinimum(xColumn);
        final double ymin        = records.getMinimum(yColumn);
        final double xmax        = records.getMaximum(xColumn);
        final double ymax        = records.getMaximum(yColumn);
        geometry.setOrdinateRange(0, xmin, xmax);
        geometry.setGridRange(0, 0, width-1);
        geometry.setOrdinateRange(1, ymin, ymax);
        geometry.setGridRange(1, 0, height-1);
        geometry.setPixelOrientation("center");
        /*
         * Now adds the valid range of sample values for each band.
         */
        final int numBands = records.getColumnCount() - (xColumn == yColumn ? 1 : 2);
        for (int band=0; band<numBands; band++) {
            final int column = getColumn(imageIndex, band);
            metadata.getBand(band).setValidRange(records.getMinimum(column), records.getMaximum(column));
        }
        return metadata;
    }

    /**
     * Rounds the specified values. This method is invoked automatically by the {@link #read read}
     * method while reading an image. It provides a place where to fix rounding errors in latitude
     * and longitude coordinates. For example if longitudes have a step 1/6° but are written with
     * only 3 decimal digits, then we get {@linkplain #getColumnX x} values like {@code 10.000},
     * {@code 10.167}, {@code 10.333}, <cite>etc.</cite>, which can leads to an error of 0.001°
     * in longitude. This error may cause {@code TextRecordImageReader} to fails validation tests
     * and throws an {@link javax.imageio.IIOException}: "<cite>Points dont seem to be distributed
     * on a regular grid</cite>". A work around is to multiply the <var>x</var> and <var>y</var>
     * coordinates by 6, round to the nearest integer and divide them by 6.
     * <p>
     * The default implementation do nothing.
     *
     * @param values The values to round in place.
     */
    protected void round(double[] values) {
    }

    /**
     * Retourne les données de l'image à l'index spécifié. Si cette image avait déjà été lue, ses
     * données seront retournées immédiatement.  Sinon, cette image sera lue ainsi que toutes les
     * images qui précèdent {@code imageIndex} et qui n'avaient pas encore été lues. Que ces
     * images précédentes soient mémorisées ou oubliées dépend de {@link #seekForwardOnly}.
     *
     * @param  imageIndex Index de l'image à lire.
     * @return Les données de l'image. Cette méthode ne retourne jamais {@code null}.
     * @throws IOException si une erreur est survenue lors de la lecture du flot,
     *         ou si des nombres n'étaient pas correctement formatés dans le flot.
     * @throws IndexOutOfBoundsException si l'index spécifié est en dehors des
     *         limites permises ou si aucune image n'a été conservée à cet index.
     */
    private RecordList getRecords(final int imageIndex) throws IOException {
        clearAbortRequest();
        checkImageIndex(imageIndex);
        if (imageIndex >= nextImageIndex) {
            processImageStarted(imageIndex);
            final BufferedReader reader = getReader();
            final long          origine = getStreamPosition(reader);
            final long           length = getStreamLength(nextImageIndex, imageIndex+1);
            long   nextProgressPosition = (origine>=0 && length>0) ? 0 : Long.MAX_VALUE;
            for (; nextImageIndex<=imageIndex; nextImageIndex++) {
                /*
                 * Réduit la consommation de mémoire des images précédentes. On ne réduit
                 * pas celle de l'image courante,  puisque la plupart du temps le tableau
                 * sera bientôt détruit de toute façon.
                 */
                if (seekForwardOnly) {
                    minIndex=nextImageIndex;
                }
                if (nextImageIndex!=0 && data!=null) {
                    final RecordList records = data[nextImageIndex-1];
                    if (records != null) {
                        if (seekForwardOnly) {
                            data[nextImageIndex-1]=null;
                        } else {
                            records.trimToSize();
                        }
                    }
                }
                /*
                 * Procède à la lecture de chacune des lignes de données. Que ces lignes
                 * soient mémorisées ou pas dépend de l'image que l'on est en train de
                 * décoder ainsi que de la valeur de {@link #seekForwardOnly}.
                 */
                double[]    values = null;
                RecordList records = null;
                final boolean    keep       = (nextImageIndex==imageIndex) || !seekForwardOnly;
                final int        xColumn    = getCheckedColumnX(nextImageIndex);
                final int        yColumn    = getCheckedColumnY(nextImageIndex);
                final double     padValue   = getPadValue      (nextImageIndex);
                final LineFormat lineFormat = getLineFormat    (nextImageIndex);
                try {
                    String line;
                    while ((line=reader.readLine()) != null) {
                        if (isComment(line) || lineFormat.setLine(line) == 0) {
                            continue;
                        }
                        values = lineFormat.getValues(values);
                        for (int i=0; i<values.length; i++) {
                            if (i!=xColumn && i!=yColumn && values[i]==padValue) {
                                values[i] = Double.NaN;
                            }
                        }
                        round(values);
                        if (keep) {
                            if (records == null) {
                                final int expectedLineCount = Math.max(8, Math.min(65536,
                                        Math.round(length / (expectedDatumLength*values.length))));
                                records = new RecordList(values.length, expectedLineCount);
                            }
                            records.add(values);
                        }
                        final long position = getStreamPosition(reader) - origine;
                        if (position >= nextProgressPosition) {
                            processImageProgress(position * (100f/length));
                            nextProgressPosition = position + PROGRESS_INTERVAL;
                            if (abortRequested()) {
                                processReadAborted();
                                return records;
                            }
                        }
                    }
                } catch (ParseException exception) {
                    throw new IIOException(getPositionString(exception.getLocalizedMessage()), exception);
                }
                /*
                 * Après la lecture d'une image, vérifie s'il y avait un nombre suffisant de lignes.
                 * Une exception sera lancée si l'image ne contenait pas au moins deux lignes. On
                 * ajustera ensuite le nombre moyens de caractères par données.
                 */
                if (records != null) {
                    final int lineCount = records.getLineCount();
                    if (lineCount<2) {
                        throw new IIOException(getPositionString(Errors.format(
                                               ErrorKeys.FILE_HAS_TOO_FEW_DATA)));
                    }
                    if (data == null) {
                        data = new RecordList[imageIndex+1];
                    } else if (data.length <= imageIndex) {
                        data = XArray.resize(data, imageIndex+1);
                    }
                    data[nextImageIndex] = records;
                    final float meanDatumLength = (getStreamPosition(reader)-origine) / (float)records.getDataCount();
                    if (meanDatumLength>0) expectedDatumLength = meanDatumLength;
                }
            }
            processImageComplete();
        }
        /*
         * Une fois les lectures terminées, retourne les données de l'image
         * demandée. Une exception sera lancée si ces données n'ont pas été
         * conservées.
         */
        if (data != null && imageIndex < data.length) {
            final RecordList records = data[imageIndex];
            if (records != null) {
                return records;
            }
        }
        throw new IndexOutOfBoundsException(String.valueOf(imageIndex));
    }

    /**
     * Reads the image indexed by {@code imageIndex} and returns it as a complete buffered image.
     *
     * @param  imageIndex the index of the image to be retrieved.
     * @param  param Parameters used to control the reading process, or {@code null}.
     * @return the desired portion of the image.
     * @throws IOException if an error occurs during reading.
     */
    public BufferedImage read(final int imageIndex, final ImageReadParam param) throws IOException {
        final float    tolerance = getGridTolerance();
        final int        xColumn = getCheckedColumnX(imageIndex);
        final int        yColumn = getCheckedColumnY(imageIndex);
        final RecordList records = getRecords(imageIndex);
        final int          width = records.getPointCount(xColumn, tolerance);
        final int         height = records.getPointCount(yColumn, tolerance);
        final int    numSrcBands = records.getColumnCount() - (xColumn==yColumn ? 1 : 2);
        /*
         * Extracts user's parameters
         */
        final int[]         srcBands;
        final int[]         dstBands;
        final int sourceXSubsampling;
        final int sourceYSubsampling;
        final int subsamplingXOffset;
        final int subsamplingYOffset;
        final int destinationXOffset;
        final int destinationYOffset;
        if (param != null) {
            srcBands           = param.getSourceBands();
            dstBands           = param.getDestinationBands();
            final Point offset = param.getDestinationOffset();
            sourceXSubsampling = param.getSourceXSubsampling();
            sourceYSubsampling = param.getSourceYSubsampling();
            subsamplingXOffset = param.getSubsamplingXOffset();
            subsamplingYOffset = param.getSubsamplingYOffset();
            destinationXOffset = offset.x;
            destinationYOffset = offset.y;
        } else {
            srcBands    = null;
            dstBands    = null;
            sourceXSubsampling = 1;
            sourceYSubsampling = 1;
            subsamplingXOffset = 0;
            subsamplingYOffset = 0;
            destinationXOffset = 0;
            destinationYOffset = 0;
        }
        /*
         * Initializes...
         */
        final int numDstBands = (dstBands!=null) ? dstBands.length :
                                (srcBands!=null) ? srcBands.length : numSrcBands;
        final BufferedImage image = getDestination(imageIndex, param, width, height, null); // TODO
        checkReadParamBandSettings(param, numSrcBands, image.getSampleModel().getNumBands());

        final Rectangle    srcRegion = new Rectangle();
        final Rectangle    dstRegion = new Rectangle();
        computeRegions(param, width, height, image, srcRegion, dstRegion);
        final int         sourceXMin = srcRegion.x;
        final int         sourceYMin = srcRegion.y;
        final int         sourceXMax = srcRegion.width  + sourceXMin;
        final int         sourceYMax = srcRegion.height + sourceYMin;

        final WritableRaster  raster = image.getRaster();
        final int        rasterWidth = raster.getWidth();
        final int       rasterHeigth = raster.getHeight();
        final int        columnCount = records.getColumnCount();
        final int          dataCount = records.getDataCount();
        final float[]           data = records.getData();
        final double            xmin = records.getMinimum(xColumn);
        final double            ymin = records.getMinimum(yColumn);
        final double            xmax = records.getMaximum(xColumn);
        final double            ymax = records.getMaximum(yColumn);
        final double          scaleX = (width -1)/(xmax-xmin);
        final double          scaleY = (height-1)/(ymax-ymin);
        /*
         * Clears the image area. All values are set to NaN.
         */
        if (CLEAR) {
            final int minX = dstRegion.x;
            final int minY = dstRegion.y;
            final int maxX = dstRegion.width  + minX;
            final int maxY = dstRegion.height + minY;
            for (int b=(dstBands!=null) ? dstBands.length : numDstBands; --b>=0;) {
                final int band = (dstBands!=null) ? dstBands[b] : b;
                for (int y=minY; y<maxY; y++) {
                    for (int x=minX; x<maxX; x++) {
                        raster.setSample(x, y, band, Float.NaN);
                    }
                }
            }
        }
        /*
         * Computes column numbers corresponding to source bands,
         * and start storing values into the image.
         */
        final int[] columns = new int[(srcBands!=null) ? srcBands.length : numDstBands];
        for (int i=0; i<columns.length; i++) {
            columns[i] = getColumn(imageIndex, srcBands!=null ? srcBands[i] : i);
        }
        for (int i=0; i<dataCount; i+=columnCount) {
            /*
             * On convertit maintenant la coordonnée (x,y) logique en coordonnée pixel. Cette
             * coordonnée pixel se réfère à l'image "source";  elle ne se réfère pas encore à
             * l'image destination. Elle doit obligatoirement être entière. Plus loin, nous
             * tiendrons compte du "subsampling".
             */
            final double fx = (data[i+xColumn]-xmin)*scaleX; // (fx,fy) may be NaN: Use
            final double fy = (ymax-data[i+yColumn])*scaleY; // "!(abs(...)<=tolerance)".
            int           x = (int)Math.round(fx); // This conversion is not the same than
            int           y = (int)Math.round(fy); // getTransform(), but it should be ok.
            if (!(Math.abs(x-fx)<=tolerance)) {fireBadCoordinate(data[i+xColumn]); continue;}
            if (!(Math.abs(y-fy)<=tolerance)) {fireBadCoordinate(data[i+yColumn]); continue;}
            if (x>=sourceXMin && x<sourceXMax && y>=sourceYMin && y<sourceYMax) {
                x -= subsamplingXOffset;
                y -= subsamplingYOffset;
                if ((x % sourceXSubsampling)==0 && (y % sourceYSubsampling)==0) {
                    x = x/sourceXSubsampling + (destinationXOffset-sourceXMin);
                    y = y/sourceYSubsampling + (destinationYOffset-sourceYMin);
                    if (x<rasterWidth && y<rasterHeigth) {
                        for (int j=0; j<columns.length; j++) {
                            raster.setSample(x, y, (dstBands!=null ? dstBands[j] : j), data[i+columns[j]]);
                        }
                    }
                }
            }
        }
        return image;
    }

    /**
     * Prévient qu'une coordonnée est mauvaise. Cette méthode est appelée lors de la lecture
     * s'il a été détecté qu'une coordonnée est en dehors des limites prévues, ou qu'elle ne
     * correspond pas à des coordonnées pixels entières.
     */
    private void fireBadCoordinate(final float coordinate) {
        processWarningOccurred(getPositionString(Errors.format(ErrorKeys.BAD_COORDINATE_$1, coordinate)));
    }

    /**
     * Supprime les données de toutes les images
     * qui avait été conservées en mémoire.
     */
    private void clear() {
        data                = null;
        nextImageIndex      = 0;
        expectedDatumLength = 10.4f;
    }

    /**
     * Restores the {@code TextRecordImageReader} to its initial state.
     */
    @Override
    public void reset() {
        clear();
        super.reset();
    }




    /**
     * Service provider interface (SPI) for {@link TextRecordImageReader}s. This SPI provides
     * necessary implementation for creating default {@link TextRecordImageReader} using default
     * locale and character set. Subclasses can set some fields at construction time in order to
     * tune the reader to a particular environment, e.g.:
     *
     * <blockquote><pre>
     * public final class CLSImageReaderSpi extends TextRecordImageReader.Spi {
     *     public CLSImageReaderSpi() {
     *         {@link #names      names}      = new String[] {"CLS"};
     *         {@link #MIMETypes  MIMETypes}  = new String[] {"text/x-records-CLS"};
     *         {@link #vendorName vendorName} = "Institut de Recherche pour le Développement";
     *         {@link #version    version}    = "1.0";
     *         {@link #locale     locale}     = Locale.US;
     *         {@link #charset    charset}    = Charset.forName("ISO-LATIN-1");
     *         {@link #padValue   padValue}   = 9999;
     *     }
     * }
     * </pre></blockquote>
     *
     * (Note: fields {@code vendorName} and {@code version} are only informatives).
     * There is no need to override any method in this example. However, developers
     * can gain more control by creating subclasses of {@link TextRecordImageReader}
     * and {@code Spi}.
     *
     * @since 2.1
     * @source $URL$
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static class Spi extends TextImageReader.Spi {
        /**
         * The format names for the default {@link TextRecordImageReader} configuration.
         */
        private static final String[] NAMES = {"records"};

        /**
         * The mime types for the default {@link TextRecordImageReader} configuration.
         */
        private static final String[] MIME_TYPES = {"text/x-records"};

        /**
         * 0-based column number for <var>x</var> values. The default value is 0.
         *
         * @see TextRecordImageReader#getColumnX
         * @see TextRecordImageReader#parseLine
         */
        protected int xColumn;

        /**
         * 0-based column number for <var>y</var> values. The default value is 1.
         *
         * @see TextRecordImageReader#getColumnY
         * @see TextRecordImageReader#parseLine
         */
        protected int yColumn;

        /**
         * A tolerance factor during decoding, between 0 and 1. During decoding,
         * the image reader compute cell's width and height   (i.e. the smallest
         * non-null difference between ordinates in a given column: <var>x</var>
         * for cell's width and <var>y</var> for cell's height). Then, it checks
         * if every coordinate points fall on a grid having this cell's size. If
         * a point depart from more than {@code gridTolerance} percent of cell's
         * width or height, an exception is thrown.
         * <p>
         * {@code gridTolerance} should be a small number like {@code 1E-5f}
         * or {@code 1E-3f}. The later is more tolerant than the former.
         */
        protected float gridTolerance = EPS;

        /**
         * Constructs a default {@code TextRecordImageReader.Spi}. This constructor
         * provides the following defaults in addition to the defaults defined in the
         * {@linkplain TextImageReader.Spi#Spi super-class constructor}:
         *
         * <ul>
         *   <li>{@link #names}           = {@code "records"}</li>
         *   <li>{@link #MIMETypes}       = {@code "text/x-records"}</li>
         *   <li>{@link #pluginClassName} = {@code "org.geotools.image.io.text.TextRecordImageReader"}</li>
         *   <li>{@link #vendorName}      = {@code "Geotools"}</li>
         *   <li>{@link #xColumn}         = {@code 0}</li>
         *   <li>{@link #yColumn}         = {@code 1}</li>
         * </ul>
         *
         * For efficienty reasons, the above fields are initialized to shared arrays. Subclasses
         * can assign new arrays, but should not modify the default array content.
         */
        public Spi() {
            names           = NAMES;
            MIMETypes       = MIME_TYPES;
            pluginClassName = "org.geotools.image.io.text.TextRecordImageReader";
            vendorName      = "GeoTools";
            version         = GeoTools.getVersion().toString();
            xColumn         = 0;
            yColumn         = 1;
            gridTolerance   = EPS;
        }

        /**
         * Returns a brief, human-readable description of this service provider
         * and its associated implementation. The resulting string should be
         * localized for the supplied locale, if possible.
         *
         * @param  locale A Locale for which the return value should be localized.
         * @return A String containing a description of this service provider.
         */
        public String getDescription(final Locale locale) {
            return Descriptions.getResources(locale).getString(DescriptionKeys.CODEC_GRID);
        }

        /**
         * Returns an instance of the ImageReader implementation associated
         * with this service provider.
         *
         * @param  extension An optional extension object, which may be null.
         * @return An image reader instance.
         * @throws IOException if the attempt to instantiate the reader fails.
         */
        public ImageReader createReaderInstance(final Object extension) throws IOException {
            return new TextRecordImageReader(this);
        }

        /**
         * Returns {@code true} if the specified row length is valid. The default implementation
         * returns {@code true} if the row seems "short", where "short" is arbitrary fixed to 10
         * columns. This is an arbitrary choice, which is why this method is not public. It may
         * be changed in any future Geotools version.
         */
        @Override
        boolean isValidColumnCount(final int count) {
            return count >= (xColumn == yColumn ? 2 : 3) && count <= 10;
        }
    }
}
