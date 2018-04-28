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

import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;

/** Handles a Yaml parse event. */
public class YsldValidateHandler {

    public void mapping(MappingStartEvent evt, YsldValidateContext context) {}

    public void scalar(ScalarEvent evt, YsldValidateContext context) {}

    public void sequence(SequenceStartEvent evt, YsldValidateContext context) {}

    public void endMapping(MappingEndEvent evt, YsldValidateContext context) {}

    public void endSequence(SequenceEndEvent evt, YsldValidateContext context) {}

    public void alias(AliasEvent evt, YsldValidateContext context) {}
}
