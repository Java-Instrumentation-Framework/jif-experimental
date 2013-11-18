rem Deploys a dependency to Maven Repo on GitHub
rem Use: deployjar %1=file, %2=group, %3=artifact
rem add -Dsources= 
call  mvn deploy:deploy-file -Dfile="C:\Micro-Manager-1.4\plugins\Micro-Manager\%1" ^
		-DrepositoryId="mavenrepo" ^
        -Durl="https://raw.github.com/LC-PolScope/mavenrepo/master/repo" ^
        -DgroupId=%2 ^
        -DartifactId=%3 ^
        -Dversion=1.0.0 ^
        -Dpackaging=jar
