/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.lineage;

import static org.geotools.api.annotation.Obligation.CONDITIONAL;
import static org.geotools.api.annotation.Specification.ISO_19115;

import java.util.Collection;
import org.geotools.api.annotation.UML;
import org.geotools.api.util.InternationalString;

/**
 * Information about the events or source data used in constructing the data specified by the scope
 * or lack of knowledge about lineage.
 *
 * <p>Only one of {@linkplain #getStatement statement}, {@linkplain #getProcessSteps process steps}
 * and {@linkplain #getSources sources} should be provided.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "LI_Lineage", specification = ISO_19115)
public interface Lineage {
    /**
     * General explanation of the data producer's knowledge about the lineage of a dataset. Should
     * be provided only if {@linkplain org.geotools.api.metadata.quality.Scope#getLevel scope level}
     * is {@linkplain org.geotools.api.metadata.maintenance.ScopeCode#DATASET dataset} or
     * {@linkplain org.geotools.api.metadata.maintenance.ScopeCode#SERIES series}.
     *
     * @return Explanation of the data producer's knowledge about the lineage, or {@code null}.
     */
    @UML(identifier = "statement", obligation = CONDITIONAL, specification = ISO_19115)
    InternationalString getStatement();

    /**
     * Information about an event in the creation process for the data specified by the scope.
     *
     * @return Information about an event in the creation process.
     */
    @UML(identifier = "processStep", obligation = CONDITIONAL, specification = ISO_19115)
    Collection<? extends ProcessStep> getProcessSteps();

    /**
     * Information about the source data used in creating the data specified by the scope.
     *
     * @return Information about the source data.
     */
    @UML(identifier = "source", obligation = CONDITIONAL, specification = ISO_19115)
    Collection<? extends Source> getSources();
}
