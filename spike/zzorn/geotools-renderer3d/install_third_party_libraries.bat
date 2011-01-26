
rem  Install JME libraries to local repository
call mvn install:install-file -Dfile=lib/jme-0.11/jme.jar -DgroupId=jme -DartifactId=jme -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-awt.jar -DgroupId=jme -DartifactId=jme-awt -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-collada.jar -DgroupId=jme -DartifactId=jme-collada -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-editors.jar -DgroupId=jme -DartifactId=jme-editors -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-effects.jar -DgroupId=jme -DartifactId=jme-effects -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-font.jar -DgroupId=jme -DartifactId=jme-font -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-gamestates.jar -DgroupId=jme -DartifactId=jme-gamestates -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-model.jar -DgroupId=jme -DartifactId=jme-model -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-scene.jar -DgroupId=jme -DartifactId=jme-scene -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-sound.jar -DgroupId=jme -DartifactId=jme-sound -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/jme-0.11/jme-terrain.jar -DgroupId=jme -DartifactId=jme-terrain -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true

rem  Install LWJGL libraries to local repository
call mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/lwjgl.jar -DgroupId=lwjgl -DartifactId=lwjgl -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/lwjgl_test.jar -DgroupId=lwjgl -DartifactId=lwjgl_test -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/lwjgl_util.jar -DgroupId=lwjgl -DartifactId=lwjgl_util -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/lwjgl_util_applet.jar -DgroupId=lwjgl -DartifactId=lwjgl_util_applet -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/jinput.jar -DgroupId=lwjgl -DartifactId=jinput -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true

