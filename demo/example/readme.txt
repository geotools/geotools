To Run the following application you can either...

Use maven from the command line:
1. mvn exec:java -Dexec.mainClass="org.geotools.demo.example.WFSExample
2. mvn exec:java -Dexec.mainClass="org.geotools.demo.example.WMSExample
3. mvn exec:java -Dexec.mainClass="org.geotools.demo.example.SLDExample" -Dexec.keepAlive=true

Open it up in your IDE (instructions for eclipse follow):
1. Use maven to produce the eclipse project and classpath files:
   mvn eclipse:eclipse
2. Import the project into eclipse using the import wizard
3. Select one of the sample applications and run as a java application


