/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.validate;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import org.geotools.ysld.YamlUtil;
import org.geotools.ysld.parse.ZoomContextFinder;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;

/** Validates a YSLD style */
public class YsldValidator {

    List<ZoomContextFinder> zCtxtFinders = Collections.emptyList();

    /**
     * Validate the passed style
     *
     * @param input Reader for the style
     * @return List of {@link MarkedYAMLException} representing any errors, or an empty list if the
     *     style is valid
     */
    public List<MarkedYAMLException> validate(Reader input) throws IOException {
        YsldValidateContext context = new YsldValidateContext();
        context.zCtxtFinders = this.zCtxtFinders;
        context.push(new RootValidator());

        try {
            for (Event evt : YamlUtil.getSafeYaml().parse(input)) {
                YsldValidateHandler h = context.peek();

                if (evt instanceof MappingStartEvent) {
                    h.mapping((MappingStartEvent) evt, context);
                } else if (evt instanceof MappingEndEvent) {
                    h.endMapping((MappingEndEvent) evt, context);
                } else if (evt instanceof SequenceStartEvent) {
                    h.sequence((SequenceStartEvent) evt, context);
                } else if (evt instanceof SequenceEndEvent) {
                    h.endSequence((SequenceEndEvent) evt, context);
                } else if (evt instanceof ScalarEvent) {
                    h.scalar((ScalarEvent) evt, context);
                } else if (evt instanceof AliasEvent) {
                    h.alias((AliasEvent) evt, context);
                }
            }
        } catch (MarkedYAMLException e) {
            context.error(e);
        } catch (EmptyStackException e) {
            // The ECQLParser uses java.util.Stack for parsing. If we get an exception from here,
            // we can narrow the cause down to CQL.
            throw new RuntimeException("Error parsing CQL expression", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return context.errors();
    }

    public void setZCtxtFinders(List<ZoomContextFinder> zCtxtFinders) {
        if (zCtxtFinders == null) throw new NullPointerException("zCtxtFinders can not be null");
        this.zCtxtFinders = zCtxtFinders;
    }
}
