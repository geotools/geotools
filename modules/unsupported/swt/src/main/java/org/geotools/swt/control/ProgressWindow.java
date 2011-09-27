/*
 * JGrass - Free Open Source Java GIS http://www.jgrass.org 
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.geotools.swt.control;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * Wrapper for geotools' {@link ProgressListener}.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class ProgressWindow extends ProgressMonitorDialog implements ProgressListener {

    private IProgressMonitor monitor;
    private String description;
    private final int taskSize;
    private float percent;
    private float previousPercent = -1;

    public ProgressWindow( Shell parent, int taskSize ) {
        super(parent);
        this.taskSize = taskSize;
        monitor = getProgressMonitor();
    }

    public boolean isCanceled() {
        return monitor.isCanceled();
    }

    public void setCanceled( boolean value ) {
        monitor.setCanceled(value);
    }

    public void setTask( InternationalString task ) {
        setDescription(task.toString());
    }

    public InternationalString getTask() {
        return new SimpleInternationalString(getDescription());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public void started() {
        monitor.beginTask(description, taskSize);
    }

    public void progress( float percent ) {
        this.percent = percent;
        if (previousPercent == -1) {
            monitor.worked((int) percent);
        } else {
            monitor.worked((int) (percent - previousPercent));
        }
        previousPercent = percent;
    }

    public float getProgress() {
        return percent;
    }

    public void complete() {
        monitor.done();
    }

    public void dispose() {
    }

    public void warningOccurred( String source, String location, String warning ) {

    }

    public void exceptionOccurred( Throwable exception ) {

    }

}
