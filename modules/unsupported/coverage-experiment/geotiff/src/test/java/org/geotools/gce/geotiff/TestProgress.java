package org.geotools.gce.geotiff;

import java.util.LinkedList;
import java.util.List;

import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

public class TestProgress implements ProgressListener {
	public float progress = Float.NaN;
	public InternationalString task;
	public boolean isCanceled;
	public List<String> warnings = new LinkedList<String>();
	public Throwable exception;
	public String description;
	
	public void complete() {
		progress = 1.0f;
	}

	public void dispose() {
		progress = Float.NaN;
	}

	public void exceptionOccurred(Throwable t) {
		exception = t;
	}

	public String getDescription() {
		return description;
	}

	public float getProgress() {
		return progress;
	}

	public InternationalString getTask() {
		return task;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void progress(float percent) {
		progress = percent;
	}

	public void setCanceled(boolean cancel) {
		isCanceled = cancel;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTask(InternationalString task) {
		this.task = task;
	}

	public void started() {
		progress = 0.0f;
	}

	public void warningOccurred(String file, String location, String warning) {
		StringBuffer warn = new StringBuffer();
		
		if( file == null ) {
			warn.append( file );
			warn.append( " " );
		}
		if( location == null ) {
			warn.append("#");
			warn.append( location );
			warn.append(" ");
		}
		warn.append( warning );
		warnings.add( warn.toString() );
	}

}
