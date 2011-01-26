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

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * High-level geographic data thematic classification to assist in the grouping and
 * search of available geographic data sets. Can be used to group keywords as well.
 * Listed examples are not exhaustive.
 * <p>
 * NOTE: It is understood there are overlaps between general categories and the user
 *       is encouraged to select the one most appropriate.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_TopicCategoryCode", specification=ISO_19115)
public final class TopicCategory extends CodeList<TopicCategory> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -4987523565852255081L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<TopicCategory> VALUES = new ArrayList<TopicCategory>(19);

    /**
     * Rearing of animals and/or cultivation of plants.
     *
     * Examples: agriculture, irrigation, aquaculture, plantations, herding, pests and
     *           diseases affecting crops and livestock.
     */
    @UML(identifier="farming", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory FARMING = new TopicCategory("FARMING");

    /**
     * Flora and/or fauna in natural environment.
     *
     * Examples: wildlife, vegetation, biological sciences, ecology, wilderness, sealife,
     *           wetlands, habitat
     */
    @UML(identifier="biota", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory BIOTA = new TopicCategory("BIOTA");

    /**
     * Legal land descriptions.
     *
     * Examples: political and administrative boundaries.
     */
    @UML(identifier="boundaries", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory BOUNDARIES = new TopicCategory("BOUNDARIES");

    /**
     * Processes and phenomena of the atmosphere.
     *
     * Examples: cloud cover, weather, climate, atmospheric conditions, climate change,
     *           precipitation.
     */
    @UML(identifier="climatologyMeteorologyAtmosphere", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory CLIMATOLOGY_METEOROLOGY_ATMOSPHERE = new TopicCategory("CLIMATOLOGY_METEOROLOGY_ATMOSPHERE");

    /**
     * Economic activities, conditions and employment.
     *
     * Examples: production, labour, revenue, commerce, industry, tourism and
     *           ecotourism, forestry, fisheries, commercial or subsistence hunting,
     *           exploration and exploitation of resources such as minerals, oil and gas.
     */
    @UML(identifier="economy", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory ECONOMY = new TopicCategory("ECONOMY");

    /**
     * Height above or below sea level.
     *
     * Examples: altitude, bathymetry, digital elevation models, slope, derived products.
     */
    @UML(identifier="elevation", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory ELEVATION = new TopicCategory("ELEVATION");

    /**
     * Environmental resources, protection and conservation.
     *
     * Examples: environmental pollution, waste storage and treatment, environmental
     *           impact assessment, monitoring environmental risk, nature reserves, landscape.
     */
    @UML(identifier="environment", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory ENVIRONMENT = new TopicCategory("ENVIRONMENT");

    /**
     * Information pertaining to earth sciences.
     *
     * Examples: geophysical features and processes, geology, minerals, sciences
     *           dealing with the composition, structure and origin of the earth's rocks, risks of
     *           earthquakes, volcanic activity, landslides, gravity information, soils, permafrost,
     *           hydrogeology, erosion.
     */
    @UML(identifier="geoscientificInformation", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory GEOSCIENTIFIC_INFORMATION = new TopicCategory("GEOSCIENTIFIC_INFORMATION");

    /**
     * Health, health services, human ecology, and safety.
     *
     * Examples: disease and illness, factors affecting health, hygiene, substance abuse,
     *           mental and physical health, health services.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="health", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory HEALTH = new TopicCategory("HEALTH");

    /**
     * Base maps.
     *
     * Examples: land cover, topographic maps, imagery, unclassified images,
     *           annotations.
     */
    @UML(identifier="imageryBaseMapsEarthCover", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory IMAGERY_BASE_MAPS_EARTH_COVER = new TopicCategory("IMAGERY_BASE_MAPS_EARTH_COVER");

    /**
     * Military bases, structures, activities.
     *
     * Examples: barracks, training grounds, military transportation, information collection.
     */
    @UML(identifier="intelligenceMilitary", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory INTELLIGENCE_MILITARY = new TopicCategory("INTELLIGENCE_MILITARY");

    /**
     * Inland water features, drainage systems and their characteristics.
     *
     * Examples: rivers and glaciers, salt lakes, water utilization plans, dams, currents,
     *           floods, water quality, hydrographic charts.
     */
    @UML(identifier="inlandWaters", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory INLAND_WATERS = new TopicCategory("INLAND_WATERS");

    /**
     * Positional information and services.
     *
     * Examples: addresses, geodetic networks, control points, postal zones and
     *           services, place names.
     */
    @UML(identifier="location", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory LOCATION = new TopicCategory("LOCATION");

    /**
     * Features and characteristics of salt water bodies (excluding inland waters).
     *
     * Examples: tides, tidal waves, coastal information, reefs.
     */
    @UML(identifier="oceans", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory OCEANS = new TopicCategory("OCEANS");

    /**
     * Information used for appropriate actions for future use of the land.
     *
     * Examples: land use maps, zoning maps, cadastral surveys, land ownership.
     */
    @UML(identifier="planningCadastre", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory PLANNING_CADASTRE = new TopicCategory("PLANNING_CADASTRE");

    /**
     * Characteristics of society and cultures.
     *
     * Examples: settlements, anthropology, archaeology, education, traditional beliefs,
     *           manners and customs, demographic data, recreational areas and activities, social
     *           impact assessments, crime and justice, census information
     */
    @UML(identifier="society", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory SOCIETY = new TopicCategory("SOCIETY");

    /**
     * Man-made construction.
     *
     * Examples: buildings, museums, churches, factories, housing, monuments, shops, towers.
     */
    @UML(identifier="structure", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory STRUCTURE = new TopicCategory("STRUCTURE");

    /**
     * Means and aids for conveying persons and/or goods.
     *
     * Examples: roads, airports/airstrips, shipping routes, tunnels, nautical charts,
     *           vehicle or vessel location, aeronautical charts, railways.
     */
    @UML(identifier="transportation", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory TRANSPORTATION = new TopicCategory("TRANSPORTATION");

    /**
     * Energy, water and waste systems and communications infrastructure and services.
     *
     * Examples: hydroelectricity, geothermal, solar and nuclear sources of energy, water
     *           purification and distribution, sewage collection and disposal, electricity and gas
     *           distribution, data communication, telecommunication, radio, communication
     *           networks.
     */
    @UML(identifier="utilitiesCommunication", obligation=CONDITIONAL, specification=ISO_19115)
    public static final TopicCategory UTILITIES_COMMUNICATION = new TopicCategory("UTILITIES_COMMUNICATION");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private TopicCategory(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code TopicCategory}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static TopicCategory[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new TopicCategory[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public TopicCategory[] family() {
        return values();
    }

    /**
     * Returns the topic category that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static TopicCategory valueOf(String code) {
        return valueOf(TopicCategory.class, code);
    }
}
