rem deploymmdeps.bat
rem Deploy Micro-Manager Dependencies
call deployjar gproto.jar  org.micromanager  gproto
call deployjar TSFProto.jar org.micromanager tsfproto
call deployjar lwm-0.0.2.jar org.micromanager lwm2
call deployjar lwm.jar org.micromanager lwm
call deployjar clooj.jar org.micromanager clooj
call deployjar MMAcqEngine.jar org.micromanager mmacqengine
call deployjar MMCoreJ.jar org.micromanager mmcorej
call deployjar MMJ_.jar org.micromanager mmj_