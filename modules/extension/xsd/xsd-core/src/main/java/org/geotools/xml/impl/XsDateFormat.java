/*
 * Copyright 2004  The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geotools.xml.impl;


/** <p>An instance of {@link java.text.Format}, which may be used to parse
 * and format <code>xs:dateTime</code> values.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 *
 *
 * @source $URL$
 */
public class XsDateFormat extends XsDateTimeFormat {
    /** Creates a new instance.
     */
    public XsDateFormat() {
        super(true, false);
    }
}
