/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing;

import java.util.concurrent.Callable;

/**
 *
 * @author michael
 */
public interface RenderingTask extends Callable<Boolean> {

    /**
     * Called by the executor to run this rendering task.
     *
     * @return result of the task: completed or failed
     * @throws Exception
     */
    Boolean call() throws Exception;

    void cancel();

    boolean isCancelled();

    boolean isRunning();
    
}
