BATCHFILES For Mavenized Micro-Manager (MMM)
in C:\GitHub\jif\MMgrMaven\bat

Assuming the local files are installed as follows:
   Micro-Manager runtime installation is in: C:/Micro-Manager-1.4
   A copy of the source code is in: C:/Projects/micromanager...

------------------------------------------------------------------------
Sequence to update to new release of Micro-Manager:
  SVN updating the code.
  Install the latest (nightly) build.
  Run updatemm
  Build the project - 
  mvn clean install
  Run deploymmdeps

------------------------------------------------------------------------
TODO:
[ ] Add batch files wit git commands ...
------------------------------------------------------------------------

updatemm.bat ===============================================================
rem Updates the local MMgrMaven project to the latest installed version 
rem   and the latest source
call copysrc
call copyroot

copysrc.bat ================================================================
rem Copies source and resource files to local MMgrMaven project
copyMMSrcFiles "C:\Projects" "C:\github\jif\mmgrmaven" 

copyMMSrcFiles.bat =========================================================
rem Copies the source files to a local MMgrMaven project directory
xcopy "%1\micromanager\mmstudio\src\*.*"  "%2\src\main\java\" /S
rem Copy resources
del "%2\src\main\java\org\micromanager\icons" /Q
xcopy "%1\micromanager\mmstudio\src\org\micromanager\icons\*.*" ^
      "%2\src\main\resources\org\micromanager\icons" /I
      
copyroot.bat ===============================================================
rem Copies the files in the root, mostly dlls, to MMgrMaven project
copyMMRootFiles "C:\Micro-Manager-1.4" "C:\GitHub\jif\MMgrMaven"

copyrootMMfiles.bat ========================================================
copy %1\atmcd64d.dll                   %2
copy %1\FxLib.dll                      %2
copy %1\inpoutx64.dll                  %2
copy %1\LaserCombinerSDK64.dll         %2
copy %1\libusb0.dll                    %2
copy %1\lucamapi.dll                   %2
copy %1\mcammr64.dll                   %2
copy %1\MMCoreJ_wrap.dll               %2
copy %1\mmgr_dal_AndorSDK3.dll         %2
copy %1\mmgr_dal_DemoCamera.dll        %2
copy %1\mmgr_dal_FreeSerialPort.dll    %2
copy %1\mmgr_dal_Hamamatsu.dll         %2
copy %1\mmgr_dal_HamamatsuHam.dll      %2
copy %1\mmgr_dal_Lumenera.dll          %2
copy %1\mmgr_dal_Nikon.dll             %2
copy %1\mmgr_dal_NikonTE2000.dll       %2
copy %1\mmgr_dal_NikonTI.dll           %2
copy %1\mmgr_dal_QCam.dll              %2
copy %1\mmgr_dal_SerialManager.dll     %2
copy %1\mmgr_dal_SimpleAutofocus.dll   %2
copy %1\mmgr_dal_USBManager.dll        %2
copy %1\mmgr_dal_Utilities.dll         %2
copy %1\mmgr_dal_VariLC.dll            %2
copy %1\mmgr_dal_Vincent.dll           %2
copy %1\mrfw64.dll                     %2
copy %1\opencv_core231.dll             %2
copy %1\opencv_highgui231.dll          %2
copy %1\SysInfo.dll                    %2
copy %1\XCLIBW64.dll                   %2
copy %1\IJ_Prefs.txt                   %2
copy %1\ImageJ.cfg                     %2
copy %1\ImageJ.exe                     %2
copy %1\MMConfig_demo.cfg              %2

------------------------------------------------------------------------
Deploying to the local Maven Repo...
------------------------------------------------------------------------

deployjar.bat ===========================================================
rem Deploys a dependency to Maven Repo on GitHub
rem Usage: deployjar %1=file, %2=group, %3=artifact
rem add -Dsources= 
call  mvn deploy:deploy-file -Dfile="C:\Micro-Manager-1.4\plugins\Micro-Manager\%1" ^
		-DrepositoryId="github.repo" ^
        -Durl="file:///C:/GitHub/mavenrepo/repo" ^
        -DgroupId=%2 ^
        -DartifactId=%3 ^
        -Dversion=1.0.0 ^
        -Dpackaging=jar

deploymmdeps.bat ==========================================================
rem Deploy Micro-Manager Dependencies
call deployjar gproto.jar  org.micromanager  gproto
call deployjar TSFProto.jar org.micromanager tsfproto
call deployjar lwm-0.0.2.jar org.micromanager lwm2
call deployjar lwm.jar org.micromanager lwm
call deployjar clooj.jar org.micromanager clooj
call deployjar MMAcqEngine.jar org.micromanager mmacqengine
call deployjar MMCoreJ.jar org.micromanager mmcorej
call deployjar MMJ_.jar org.micromanager mmj_

	
call  mvn install:install-file -Dfile="C:\Micro-Manager-1.4\plugins\Micro-Manager\%1" ^
        -DgroupId=%2 ^
        -DartifactId=%3 ^
        -Dversion=1.0.0 ^
        -Dpackaging=jar