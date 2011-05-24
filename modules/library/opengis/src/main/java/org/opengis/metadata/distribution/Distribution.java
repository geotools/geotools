/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.distribution;

import java.util.Collection;
import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;


/**
 * Information about the distributor of and options for obtaining the resource.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/distribution/Distribution.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@Profile (level=CORE)
@UML(identifier="MD_Distribution", specification=ISO_19115)
public interface Distribution {
    /**
     * Provides a description of the format of the data to be distributed.
     *
     * @return Description of the format of the data to be distributed.
     */
    @Profile (level=CORE)
    @UML(identifier="distributionFormat", obligation=CONDITIONAL, specification=ISO_19115)
    Collection<? extends Format> getDistributionFormats();

    /**
     * Provides information about the distributor.
     *
     * @return Information about the distributor.
     */
    @UML(identifier="distributor", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends Distributor> getDistributors();

    /**
     * Provides information about technical means and media by which a resource is obtained
     * from the distributor.
     *
     * @return Technical means and media by which a resource is obtained from the distributor.
     */
    @Profile (level=CORE)
    @UML(identifier="transferOptions", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends DigitalTransferOptions> getTransferOptions();
}
