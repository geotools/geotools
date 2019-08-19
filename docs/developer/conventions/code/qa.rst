Automatic Quality Assurance checks
----------------------------------

The GeoTools builds on Travis and `https://build.geoserver.org/ <https://build.geoserver.org/>`_ apply a handful of tools
to statically check code quality and fail the build in case of rule violation:

* `PMD <https://pmd.github.io/>`_, 
* `Error Prone <https://errorprone.info/>`_, 
* `Spotbugs <https://spotbugs.github.io/>`_
* `CheckStyle <http://checkstyle.sourceforge.net/>`_ on build servers .
* ``javac`` own linting abilities, in particular, checking calls to deprecated APIs

Each tool is setup to run on high priority checks in order to limit the number of false positives,
but some might still happen, the rest of this document shows how errors are reported and what
can be done to selectively turn off the checks.

In case you want to just run the build with the full checks locally, use the following command::

    mvn clean install -Dqa -Dall

Add extra parameters as you see fit, like ``-T1C -nsu`` to speed up the build, or ``-Dfmt.skip=true -DskipTests``
to avoid running tests and code formatting.

PMD checks
^^^^^^^^^^

The `PMD <https://pmd.github.io/>`_ checks are based on the basic PMD validation, but limited to priority 2 checks:

https://github.com/geotools/geotools/blob/qa/pmd-ruleset.xml

In order to activate the PMD checks, use the "-Ppmd" profile.

PMD will fail the build in case of violation, reporting the specific errors before the build
error message, and a reference to a XML file with the same information after it::

    7322 [INFO] --- maven-pmd-plugin:3.11.0:check (default) @ gt-main ---
    17336 [INFO] PMD Failure: org.geotools.data.DataStoreAdaptor:98 Rule:SystemPrintln Priority:2 System.out.println is used.
    17336 [INFO] PMD Failure: org.geotools.data.DataStoreAdaptor:98 Rule:SystemPrintln Priority:2 System.out.println is used.
    17337 [INFO] ------------------------------------------------------------------------
    17337 [INFO] BUILD FAILURE
    17337 [INFO] ------------------------------------------------------------------------
    17338 [INFO] Total time:  16.727 s
    17338 [INFO] Finished at: 2018-12-29T11:34:33+01:00
    17338 [INFO] ------------------------------------------------------------------------
    17340 [ERROR] Failed to execute goal org.apache.maven.plugins:maven-pmd-plugin:3.11.0:check (default) on project gt-main: You have 1 PMD violation. For more details see:       /home/yourUser/devel/git-gt/modules/library/main/target/pmd.xml -> [Help 1]
    17340 [ERROR] 

In case of parallel build, the specific error messages will be in the body of the build, while the
XML file reference wil be at end end, just search for "PMD Failure" in the build logs to find the specific code issues.

PMD false positive suppression
""""""""""""""""""""""""""""""

Occasionally PMD will report a false positive failure, for those it's possible to annotate the method
or the class in question with a SuppressWarnings using ``PMD.<RuleName``, e.g. if the above error
was actually a legit use of ``System.out.println`` it could have been annotated with::

    @SuppressWarnings("PMD.SystemPrintln")
    public void methodDoingPrintln(...) {
    
PMD CloseResource checks
""""""""""""""""""""""""

PMD can check for Closeable that are not getting property closed by the code, and report about it.
PMD by default only checks for SQL related closeables, like "Connection,ResultSet,Statement", but it
can be instructed to check for more by configuration (do check the PMD configuration in 
``build/qa/pmd-ruleset.xml``.

The check is a bit fragile, in that there are multiple ways to close an object between direct calls,
utilities and delegate methods. The configuration lists the type of methods, and the eventual
prefix, that will be used to perform the close, for example::

    <rule ref="category/java/errorprone.xml/CloseResource" >
        <properties>
            <property name="closeTargets" value="releaseConnection,store.releaseConnection,closeQuietly,closeConnection,closeSafe,store.closeSafe,dataStore.closeSafe,getDataStore().closeSafe,close,closeResultSet,closeStmt"/>
        </properties>
    </rule>

For closing delegates that use an instance object instead of a class static method, the variable
name is included in the prefix, so some uniformity in variable names is required.


Error Prone
^^^^^^^^^^^

The `Error Prone <https://errorprone.info/>`_ checker runs a compiler plugin.

In order to activate the Error Prone checks, use the "-Perrorprone" for JDK 11 builds, or "-Perrorprone8" for JDK 8 builds.

Any failure to comply with the "Error Prone" rules will show up as a compile error in the build output, e.g.::

        9476 [ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.8.0:compile (default-compile) on project gt-coverage: Compilation failure
        9476 [ERROR] /home/user/devel/git-gt/modules/library/coverage/src/main/java/org/geotools/image/ImageWorker.java:[380,39] error: [IdentityBinaryExpression] A binary expression where both operands are the same is usually incorrect; the value of this expression is equivalent to `255`.
        9477 [ERROR]     (see https://errorprone.info/bugpattern/IdentityBinaryExpression)
        9477 [ERROR] 
        9477 [ERROR] -> [Help 1]
        org.apache.maven.lifecycle.LifecycleExecutionException: Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.8.0:compile (default-compile) on project gt-coverage: Compilation failure
        /home/user/devel/git-gt/modules/library/coverage/src/main/java/org/geotools/image/ImageWorker.java:[380,39] error: [IdentityBinaryExpression] A binary expression where both operands are the same is usually incorrect; the value of this expression is equivalent to `255`.
        (see https://errorprone.info/bugpattern/IdentityBinaryExpression)

In case Error Prone is reporting an invalid error, the method or class in question can be annotated
with SuppressWarnings with the name of the rule, e.g., to get rid of the above the following annotation could be used::

   @SuppressWarnings("IdentityBinaryExpression")

Spotbugs
^^^^^^^^

The `Spotbugs <https://spotbugs.github.io/>`_ checker runs as a post-compile bytecode analyzer.

Any failure to comply with the rules will show up as a compile error, e.g.::

        33630 [ERROR] page could be null and is guaranteed to be dereferenced in org.geotools.swing.wizard.JWizard.setCurrentPanel(String) [org.geotools.swing.wizard.JWizard, org.geotools.swing.wizard.JWizard, org.geotools.swing.wizard.JWizard, org.geotools.swing.wizard.JWizard] Dereferenced at JWizard.java:[line 278]Dereferenced at JWizard.java:[line 269]Null value at JWizard.java:[line 254]Known null at JWizard.java:[line 255] NP_GUARANTEED_DEREF

It is also possible to run the spotbugs:gui goal to have a Swing based issue explorer, e.g.::

    mvn spotbugs:gui -Pspotbugs -f modules/unsupported/swing/

In case an invalid report is given, an annotation on the class/method/variable can be added to ignore it:

   @SuppressFBWarnings("NP_GUARANTEED_DEREF")

or if it's a general one that should be ignored, the ``${geotoolsBaseDir}/spotbugs-exclude.xml`` file can be modified.

Checkstyle
^^^^^^^^^^

Google Format is already in use to keep the code formatted, so Checkstyle is used mainly to verify javadocs errors
and presence of copyright headers, which none of the other tools can cover.

Any failure to comply with the rules will show up as a compiler error in the build output, e.g.::

        14610 [INFO] --- maven-checkstyle-plugin:3.0.0:check (default) @ gt-jdbc ---
        15563 [INFO] There is 1 error reported by Checkstyle 6.18 with /home/aaime/devel/git-gt/build/qa/checkstyle.xml ruleset.
        15572 [ERROR] src/main/java/org/geotools/jdbc/JDBCDataStore.java:[325,8] (javadoc) JavadocMethod: Unused @param tag for 'foobar'.

javac
^^^^^

The Java compiler has a set of options to "lint" the source code. The build server in particular
enable checks for deprecated APIs, making javac return a compile error any time a deprecated method
or object is used. 

In most cases, one should check the javadoc of the API in question, learn about replacements, and
stop using the deprecated API. This is not always possible, for example, when creating an object
wrapper it might happen that a deprecated API needs to be implemented and delegated.
In those cases, it's possible to solve the compile error by suppressing the deprecation via annotations, e.g.
``@SuppressWarnings("deprecation")``.

Care should be taken when deprecating an existing API, as all call points to it will immediately trigger
the compiler error. It's often possible to simply "refactor away" the call points by inlining or
other automated operations. If that is not feasible, manually resolving deprecated call will provide
a good perspective on what the library users will have to face, and help improve suggestions for
replacement in the deprecated API javadocs.