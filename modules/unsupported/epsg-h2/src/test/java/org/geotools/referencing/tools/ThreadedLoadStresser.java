package org.geotools.referencing.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.geotools.referencing.CRS;

/**
 * Starts a certain number of parallel testers, each one randomly asking
 * for 1000 codes
 *
 *
 * @source $URL$
 */
public class ThreadedLoadStresser {
    private static final int THREADS = 100;
    static Pattern p = Pattern.compile("\\d+");
    
    public static void main(String[] args) throws Exception {
        final long start = System.currentTimeMillis();
        Set<String> codes = CRS.getSupportedCodes("EPSG");
        final List<String> codeList = new ArrayList(codes);
        
        Runnable r = new Runnable() {
        
            public void run() {
                for(int i = 0; i < 1000; i++) {
                    int codeIdx = (int) (Math.random() * codeList.size());
                    String code = codeList.get(codeIdx);
                    if(p.matcher(code).matches()) {
                        try {
                            CRS.decode("EPSG:" + code);
                        } catch(Exception e) {
                            // System.out.println("EPSG:" + code + " failed: " + e.getMessage());
                        }
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("Thread : " + Thread.currentThread().getName() + " ended after " + (end - start) / 1000.0 + "s");
        
            }
        };
        
        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(r);
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
    }
}
