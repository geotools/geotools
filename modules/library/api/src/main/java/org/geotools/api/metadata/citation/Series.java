/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.citation;

import org.geotools.api.util.InternationalString;

/**
 * Information about the series, or aggregate dataset, to which a dataset belongs.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface Series {
    /**
     * Name of the series, or aggregate dataset, of which the dataset is a part. Returns {@code
     * null} if none.
     *
     * @return The name of the series or aggregate dataset.
     */
    InternationalString getName();

    /**
     * Information identifying the issue of the series.
     *
     * @return Information identifying the issue of the series.
     */
    String getIssueIdentification();

    /**
     * Details on which pages of the publication the article was published.
     *
     * @return Details on which pages of the publication the article was published.
     */
    String getPage();
}
