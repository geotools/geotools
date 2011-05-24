/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.citation;

import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information about the series, or aggregate dataset, to which a dataset belongs.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/citation/Series.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="CI_Series", specification=ISO_19115)
public interface Series {
    /**
     * Name of the series, or aggregate dataset, of which the dataset is a part.
     * Returns {@code null} if none.
     *
     * @return The name of the series or aggregate dataset.
     */
    @UML(identifier="name", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getName();

    /**
     * Information identifying the issue of the series.
     *
     * @return Information identifying the issue of the series.
     */
    @UML(identifier="issueIdentification", obligation=OPTIONAL, specification=ISO_19115)
    String getIssueIdentification();

    /**
     * Details on which pages of the publication the article was published.
     *
     * @return Details on which pages of the publication the article was published.
     */
    @UML(identifier="page", obligation=OPTIONAL, specification=ISO_19115)
    String getPage();
}
