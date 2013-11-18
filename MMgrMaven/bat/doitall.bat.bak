rem installs a dependency to Maven Repo on GitHub
rem Use: installjar %1=file, %2=group, %3=artifact
rem add -Dsources= 
call  mvn install:install-file -Dfile="C:\Micro-Manager-1.4\plugins\Micro-Manager\%1" ^
        -DgroupId=%2 ^
        -DartifactId=%3 ^
        -Dversion=1.0.0 ^
        -Dpackaging=jar
