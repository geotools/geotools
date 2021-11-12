/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geotools.data.elasticsearch.date;

import java.time.temporal.TemporalAccessor;

/**
 * Interface to convert from and to {@link TemporalAccessor}s.
 *
 * @author Peter-Josef Meisch
 * @since 4.2
 */
public interface DateFormatter {
    /**
     * Formats a {@link TemporalAccessor} into a String.
     *
     * @param accessor must not be {@literal null}
     * @return the formatted String
     */
    String format(TemporalAccessor accessor);

    /**
     * Parses a String into a {@link TemporalAccessor}.
     *
     * @param input the String to parse, must not be {@literal null}
     * @param type the class of T
     * @param <T> the {@link TemporalAccessor} implementation
     * @return the parsed instance
     */
    <T extends TemporalAccessor> T parse(String input, Class<T> type);
}
