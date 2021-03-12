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
package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:_StatusInfo.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="_StatusInfo" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  			&lt;sequence&gt;
 *
 *  				&lt;!-- reference to JobID --&gt;
 *
 *  				&lt;element ref="wps:JobID"/&gt;
 *
 *  				&lt;!-- definition of Status element --&gt;
 *
 *  				&lt;element name="Status"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							This element is used to communicate basic status information about executed processes.
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  					&lt;simpleType&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								Basic status set to communicate the status of a server-side job to the client.
 *
 *  								Extensions of this specification may introduce additional states for fine-grained
 *
 *  								monitoring or domain-specific purposes.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  						&lt;union&gt;
 *
 *  							&lt;simpleType&gt;
 *
 *  								&lt;restriction base="string"&gt;
 *
 *  									&lt;enumeration value="Succeeded"&gt;
 *
 *  										&lt;annotation&gt;
 *
 *  											&lt;documentation&gt;
 *
 *  												The job has finished with no errors.
 *
 *  											&lt;/documentation&gt;
 *
 *  										&lt;/annotation&gt;
 *
 *  									&lt;/enumeration&gt;
 *
 *  									&lt;enumeration value="Failed"&gt;
 *
 *  										&lt;annotation&gt;
 *
 *  											&lt;documentation&gt;
 *
 *  												The job has finished with errors.
 *
 *  											&lt;/documentation&gt;
 *
 *  										&lt;/annotation&gt;
 *
 *  									&lt;/enumeration&gt;
 *
 *  									&lt;enumeration value="Accepted"&gt;
 *
 *  										&lt;annotation&gt;
 *
 *  											&lt;documentation&gt;
 *
 *  												The job is queued for execution.
 *
 *  											&lt;/documentation&gt;
 *
 *  										&lt;/annotation&gt;
 *
 *  									&lt;/enumeration&gt;
 *
 *  									&lt;enumeration value="Running"&gt;
 *
 *  										&lt;annotation&gt;
 *
 *  											&lt;documentation&gt;
 *
 *  												The job is running.
 *
 *  											&lt;/documentation&gt;
 *
 *  										&lt;/annotation&gt;
 *
 *  									&lt;/enumeration&gt;
 *
 *  								&lt;/restriction&gt;
 *
 *  							&lt;/simpleType&gt;
 *
 *  							&lt;simpleType&gt;
 *
 *  								&lt;restriction base="string"/&gt;
 *
 *  							&lt;/simpleType&gt;
 *
 *  						&lt;/union&gt;
 *
 *  					&lt;/simpleType&gt;
 *
 *  				&lt;/element&gt;
 *
 *  				&lt;!-- reference to job expiration date --&gt;
 *
 *  				&lt;element minOccurs="0" ref="wps:ExpirationDate"/&gt;
 *
 *  				&lt;element minOccurs="0" name="EstimatedCompletion" type="dateTime"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							Estimated date and time by which the job will be completed. Use if available.
 *
 *  							The time of estimated completion lies significantly before the expiration date of this job.
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  				&lt;/element&gt;
 *
 *  				&lt;element minOccurs="0" name="NextPoll" type="dateTime"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							Suggested date and time for the next status poll (GetStatus) for this job. Use if appropriate.
 *
 *  							The time of the next poll shall lie significantly before the expiration date of this job.
 *
 *  							If this element is provided but an expiration date for the job is not given, clients are expected to check
 *
 *  							the job status on time to eventually receive an update on the expiration date and avoid missing the results.
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  				&lt;/element&gt;
 *
 *  				&lt;element minOccurs="0" name="PercentCompleted"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							Use as a progress indicator if appropriate. Like most progress bars the value is an estimate without accuracy guarantees.
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  					&lt;simpleType&gt;
 *
 *  						&lt;restriction base="integer"&gt;
 *
 *  							&lt;minInclusive value="0"/&gt;
 *
 *  							&lt;maxInclusive value="100"/&gt;
 *
 *  						&lt;/restriction&gt;
 *
 *  					&lt;/simpleType&gt;
 *
 *  				&lt;/element&gt;
 *
 *  			&lt;/sequence&gt;
 *
 *  		&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class _StatusInfoBinding extends AbstractComplexEMFBinding {

    public _StatusInfoBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WPS._StatusInfo;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return super.getType();
    }
}
