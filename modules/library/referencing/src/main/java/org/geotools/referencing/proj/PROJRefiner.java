/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.proj;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PROJ supports more than 7400 EPSG CRS definitions. Some of them have specific fields that cannot be easily inferred
 * by the GeoTools referencing entities. This class refines the buffered PROJ String built through a PROJFormatter based
 * on Referencing IdentifiedObjects inspection, by remapping keys, applying sorting, converting units, appending fixed
 * constant strings to the output.
 *
 * <p>Check PROJRefinements.txt resource file for more details.
 */
class PROJRefiner {

    // A class to hold regex and its replacement
    private static class Refinement {
        @SuppressWarnings("UnusedVariable")
        String regex;

        String replacement;
        Pattern pattern;

        Refinement(String regex, String replacement) {
            this.regex = regex;
            this.replacement = replacement;
            this.pattern = Pattern.compile(regex);
        }
    }

    private static final String REFINEMENTS_FILE = "PROJRefinements.txt";

    private Properties properties;

    /** These are Strings that need to be globally added to each PROJ String */
    private String globalAdditions = "";

    /** Map of needed refinement for EPSG code */
    private Map<String, List<Refinement>> epsgRefinements;

    /** List of unit refinements, i.e converting unit=m*VALUE to +units=m +to_meters=VALUE */
    private List<Refinement> unitRefinements;

    /** Sorted List of PROJ keys */
    private List<String> projKeysOrder;

    public PROJRefiner() {
        properties = new Properties();
        projKeysOrder = new ArrayList<>();
        unitRefinements = new ArrayList<>();
        epsgRefinements = new HashMap<>();

        URL aliasURL = PROJRefiner.class.getResource(REFINEMENTS_FILE);
        try (InputStream input = aliasURL.openStream()) {
            properties.load(input);
            if (properties.containsKey("global.additions")) {
                globalAdditions = properties.getProperty("global.additions");
            }
            String order = properties.getProperty("proj.order");
            if (order != null) {
                projKeysOrder.addAll(Arrays.asList(order.split(",")));
            }
            loadUnitRefinements();
            loadRefinements();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadUnitRefinements() {
        /*
         * Unit refinements are applied to any input proj String during refinement. A typical example is converting
         * ft_survey_us to us-ft Another example is converting units=m*Value TO +units=m +to_meters=Value
         */
        Map<String, String> regexMap = new HashMap<>();
        Map<String, String> replacementMap = new HashMap<>();

        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("unit.regex.")) {
                regexMap.put(key.substring(11), properties.getProperty(key));
            } else if (key.startsWith("unit.replacement.")) {
                replacementMap.put(key.substring(17), properties.getProperty(key));
            }
        }
        for (String id : regexMap.keySet()) {
            String regex = regexMap.get(id);
            String replacement = replacementMap.get(id);
            unitRefinements.add(new Refinement(regex, replacement));
        }
    }

    private void loadRefinements() {
        // Step 1: Identify all regex, replacement, and codes entries
        Map<Integer, String> regexMap = new HashMap<>();
        Map<Integer, String> replacementMap = new HashMap<>();
        Map<Integer, String> codesMap = new HashMap<>();

        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("regex.")) {
                regexMap.put(Integer.valueOf(key.substring(6)), properties.getProperty(key));
            } else if (key.startsWith("replacement.")) {
                replacementMap.put(Integer.valueOf(key.substring(12)), properties.getProperty(key));
            } else if (key.startsWith("codes.")) {
                codesMap.put(Integer.valueOf(key.substring(6)), properties.getProperty(key));
            }
        }

        // Step 2: Process each regex code and associate with EPSG codes
        List<Integer> sortedKeys = new ArrayList<>(regexMap.keySet());
        Collections.sort(sortedKeys);
        for (int id : sortedKeys) {
            String regex = regexMap.get(id);
            String replacement = replacementMap.get(id);
            String codes = codesMap.get(id);

            if (regex != null && replacement != null && codes != null) {
                List<String> epsgCodes = parseCodes(codes);
                for (String code : epsgCodes) {
                    epsgRefinements
                            .computeIfAbsent(code, k -> new ArrayList<>())
                            .add(new Refinement(regex, replacement));
                }
            }
        }
    }

    private List<String> parseCodes(String codes) {
        List<String> result = new ArrayList<>();
        String[] codeParts = codes.split(",");
        for (String part : codeParts) {
            if (part.contains("-")) {
                String[] range = part.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int i = start; i <= end; i++) {
                    result.add(String.valueOf(i));
                }
            } else {
                result.add(part);
            }
        }
        return result;
    }

    // Apply global updates and regex replacements for specific EPSG codes
    public String refine(String projString, String epsgCode) {
        // Apply global updates
        StringBuilder updatedProjString = new StringBuilder(sortProjString(projString));
        updatedProjString.append(" " + globalAdditions);

        // First refine the units
        for (Refinement refinement : unitRefinements) {
            Matcher matcher = refinement.pattern.matcher(updatedProjString);
            updatedProjString = new StringBuilder(matcher.replaceAll(refinement.replacement));
        }
        // Finally apply the EPSG based refinements
        List<Refinement> refinements = epsgRefinements.get(epsgCode);
        if (refinements != null) {
            for (Refinement refinement : refinements) {
                Matcher matcher = refinement.pattern.matcher(updatedProjString);
                updatedProjString = new StringBuilder(matcher.replaceAll(refinement.replacement));
            }
        }
        // Final sorting, just in case
        return sortProjString(updatedProjString.toString().replaceAll("\\s+", " "));
    }

    private String sortProjString(String projString) {
        Map<String, String> projComponents = new LinkedHashMap<>();

        // Split the proj string into components
        String[] components = projString.trim().split("\\s+");
        for (String component : components) {
            if (component.contains("=")) {
                String[] pair = component.split("=");
                projComponents.put(pair[0], pair[1]);
            } else {
                projComponents.put(component, null);
            }
        }

        // Reorder components according to the defined order
        StringBuilder sortedProjString = new StringBuilder();
        for (String key : projKeysOrder) {
            if (projComponents.containsKey("+" + key)) {
                sortedProjString.append("+").append(key);
                String value = projComponents.get("+" + key);
                if (value != null) {
                    sortedProjString.append("=").append(value);
                }
                sortedProjString.append(" ");
            }
        }

        // Add any remaining components not in the order
        for (String key : projComponents.keySet()) {
            if (!projKeysOrder.contains(key.substring(1))) {
                sortedProjString
                        .append(key)
                        .append("=")
                        .append(projComponents.get(key))
                        .append(" ");
            }
        }

        return sortedProjString.toString().trim();
    }
}
