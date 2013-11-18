rem installmmdeps.bat
rem Install Micro-Manager Dependencies
call installjar gproto.jar  org.micromanager  gproto
call installjar TSFProto.jar org.micromanager tsfproto
call installjar lwm-0.0.2.jar org.micromanager lwm2
call installjar lwm.jar org.micromanager lwm
call installjar clooj.jar org.micromanager clooj
call installjar MMAcqEngine.jar org.micromanager mmacqengine
call installjar MMCoreJ.jar org.micromanager mmcorej
call installjar MMJ_.jar org.micromanager mmj_