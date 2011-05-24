/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.identification;

import java.net.URI;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Graphic that provides an illustration of the dataset (should include a legend for the graphic).
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/identification/BrowseGraphic.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_BrowseGraphic", specification=ISO_19115)
public interface BrowseGraphic {
    /**
     * Name of the file that contains a graphic that provides an illustration of the dataset.
     *
     * @return File that contains a graphic that provides an illustration of the dataset.
     */
    @UML(identifier="fileName", obligation=MANDATORY, specification=ISO_19115)
    URI getFileName();

    /**
     * Text description of the illustration.
     *
     * @return Text description of the illustration, or {@code null}.
     */
    @UML(identifier="fileDescription", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getFileDescription();

    /**
     * Format in which the illustration is encoded.
     * Examples: CGM, EPS, GIF, JPEG, PBM, PS, TIFF, XWD.
     * Raster formats are encouraged to use one of the names returned by
     * {@link javax.imageio.ImageIO#getReaderFormatNames()}.
     *
     * @return Format in which the illustration is encoded, or {@code null}.
     *
     * @see javax.imageio.ImageIO#getReaderFormatNames()
     */
    @UML(identifier="fileType", obligation=OPTIONAL, specification=ISO_19115)
    String getFileType();
}
