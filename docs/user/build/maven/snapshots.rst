Maven Snapshots
---------------

The current **live** jar published by GeoTools is known as a SNAPSHOT. Projects that want to work
directly with GeoTools will depend “SNAPSHOT” rather than specifying a version number.

Grab SNAPSHOT Dependency
^^^^^^^^^^^^^^^^^^^^^^^^

We depend on a "snapshot" of several projects (usually project GeoTools community members are
involved such as ImageIO-EXT). In these cases an email will be sent to the developer list asking
people to "update with -U".

To respond to one of these emails include "-u" in your next build.

1. Update (for example from upstream)::
     
      git pull --rebase upstream master
     
2. build using the -U option::
      
      mvn clean install -U -DskipTests

The above example skipped running the tests (which is common when you are trying for a quick update), please note by definition that "-U" is not compatible with the "-o" offline mode.

Publish GeoTools SNAPSHOT
^^^^^^^^^^^^^^^^^^^^^^^^^

If you are working on GeoServer or uDig or another project that depends on the latest greatest
GeoTools release you will need to know how to deploy a SNAPSHOT (so members of your developer
community do not get compile errors).

The build server ares is watching the repository, and will build and deploy a snapshot. If you really cannot wait:

1. Update to make sure you are not missing out on anyones work::
     
      git pull --rebase upstream master
     
2. Build with tests to make sure your commit is not fatal::
     
     mvn clean install
     
3. Commit - remember to include any Jira numbers in your log message::
      
     mvn commit -m "Change to fix shapefile charset handling, see GEOT-1437"
     
4. Push the commits back to upstream::
     
     mvn push upstream master
      
4. Ensure your ~/.m2/settings.xml has your webdav credentials.
   
   * osgeo - this is the same as your osgeo credentials
   * boundlessgeo - ask on the developer email list
   
   The settings.xml should list both::
   
      <?xml version="1.0" encoding="ISO-8859-1"?> 
      <settings>
          <offline>false</offline>
          <servers>
           <server>
             <id>osgeo</id>
             <username>USERID</username>
             <password>PASSWORD</password>
           </server> 
           <server>
             <id>boundlessgeo</id>
             <username>USERID</username>
             <password>PASSWOD</password>
           </server>
          </servers>
      </settings>

5. Deploy for members of your community::
      
      mvn deploy -Dmaven.test.skip=true

6. Let your community know via email! (The email can ask them to build iwth -U).