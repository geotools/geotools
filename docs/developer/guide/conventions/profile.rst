Code Profiling
===============

Code profilers are tools which analyse performance and memory of applications. We have several good free tools that we have used.

YourKit
-------

YourKit is a CPU and memory profiler for Java applications.
Can be run stand alone or integrated with various IDEs.
For a full list of features, please refer the YourKit pages

* http://www.yourkit.com/

License management
^^^^^^^^^^^^^^^^^^^

YourKit is a commercial product, which provides open source project
committers with free licenses.

Geotools PMC has been granted 10 licenses, each one can be used by at most
one developer in a given period of time.
Andrea Aime is managing the licenses now, so if you feel like using
YourKit for profiling the gt2 source code base please:

check that at least one license it available or
ask someone to step down and give the license to you
ask Andrea Aime about the license file
update this page to allow us to track the licenses used.
License list

* license 1: Andrea Aime
* license 2: Simone Giannecchini
* license 3: Justin Deoliveira
* license 4: Gabriel Roldan
* license 5: available
* license 6: available
* license 7: available
* license 8: Jody Garnett
* license 9: Christophe Rousson
* license 10: Daniele Romagnoli

Eclipse Profiler PlugIn
^^^^^^^^^^^^^^^^^^^^^^^

The following eclipse profiler is recommended:

* http://eclipsecolorer.sourceforge.net/index_profiler.html

HPJMeter
^^^^^^^^

HPJMeter analyses profiling data from hprof (provided with JDK).

To invoke hprof, run java with the following options::
   
   -Xrunhprof:cpu=samples,thread=n,depth=40,cutoff=0,format=a

At the end of the run you'll find a java.hprof.txt file that can be opened with HPJMeter.

If you are running from Netbeans:

1. Select Tools -> Options -> Debugging and Executing -> Execution Types -> right click -> New -> External Executor

2. Rename the new executor HProf External Executor (or whatever you like).
3. till in the Options dialog box, open HProf External Executor -> Properties -> External Process and in arguments add at the beginning::
      
      -Xrunhprof:cpu=samples,thread=n,depth=40,cutoff=0,format=a {assertEnabled}

4. hprof will be launched every time you select this kind of executor.
5. Go to: HProf External Executor -> Expert -> Working Directory
6. Set a working directory: Eg /home/<username>/tmp/hprof
7. Hprof dumps the resulting text file in this directory. This setting modifies the working directory of the java JVM. Make sure the working directory exists, otherwise the executor won't work.
8. Close the options dialog and select the file you want to profile
9. Select (right click on file to profile) -> Properties -> Execution -> Executor
10. Set to the HProf External Profiler.
11. Run your application, test or stress test

At the end of the run you'll find a java.hprof.txt file that can be opened with HPJMeter.