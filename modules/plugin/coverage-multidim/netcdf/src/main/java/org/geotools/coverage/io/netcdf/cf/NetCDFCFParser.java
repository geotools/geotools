/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.cf;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * A NetCDF CF (ClimateForecast) parser.
 *
 * <p>It can be used to load from a standard name table (XML) all the available CF compliant standard names and the
 * related information, such as canonical units, descriptions and alias.
 *
 * <p>See <a href="http://cfconventions.org/standard-names.html">CF Convention: Standard names</a>
 */
public class NetCDFCFParser {

    /** Map containing all the available entries */
    private Map<String, Entry> entriesMap;

    /** Map containing all the defined alias */
    private Map<String, Alias> aliasMap;

    /** {@link Set} containing all the keys */
    private Set<String> keys;

    public boolean hasEntryId(String id) {
        return entriesMap.containsKey(id);
    }

    public boolean hasAliasId(String id) {
        return aliasMap.containsKey(id);
    }

    public Entry getEntry(String id) {
        if (hasEntryId(id)) {
            return entriesMap.get(id);
        }
        return null;
    }

    public Set<String> getEntryIds() {
        return keys;
    }

    public Entry getEntryFromAlias(String id) {
        if (hasAliasId(id)) {
            Alias alias = aliasMap.get(id);
            Entry entryId = (Entry) alias.getEntryId();
            return entryId; // the Entry is already the proper one.
            // return getEntry(entryId.getId());
        }
        return null;
    }

    private static JAXBContext context;

    private static final Logger LOGGER = Logging.getLogger(NetCDFCFParser.class);

    static {
        try {
            context = JAXBContext.newInstance(StandardNameTable.class);
        } catch (JAXBException e) {
            LOGGER.severe(e.getLocalizedMessage());
        }
    }

    private NetCDFCFParser(Map<String, Entry> entriesMap, Map<String, Alias> aliasMap) {
        this.entriesMap = entriesMap;
        this.aliasMap = aliasMap;
        // Create the KeySet
        keys = new TreeSet<>();
        // Populate the keySet
        keys.addAll(entriesMap.keySet());
        keys.addAll(aliasMap.keySet());
    }

    /** Create a {@link NetCDFCFParser} instance for the specified file. */
    public static NetCDFCFParser unmarshallXml(File file) throws JAXBException {

        // We assume we have a single (or reduced number) of files to be unmarshalled
        // since the CF standard name table is a single one.
        // Therefore we instantiated an unmarshaller each time we need to do an unmarshalling
        // Note that we can't use the same unmarshaller for concurrent unmarshalling.
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StandardNameTable table = (StandardNameTable) unmarshaller.unmarshal(file);
        List<Entry> entries = table.getEntry();
        List<Alias> aliases = table.getAlias();
        Map<String, Entry> entriesMap = new HashMap<>(entries.size());
        Map<String, Alias> aliasMap = new HashMap<>(aliases.size());
        for (Entry entry : entries) {
            entriesMap.put(entry.getId(), entry);
        }
        for (Alias alias : aliases) {
            aliasMap.put(alias.getId(), alias);
        }
        return new NetCDFCFParser(entriesMap, aliasMap);
    }
}
