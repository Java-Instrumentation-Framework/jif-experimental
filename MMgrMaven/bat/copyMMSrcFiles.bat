rem Copy source files
xcopy "%1\micromanager\mmstudio\src\*.*"  "%2\src\main\java\" /S /Y
rem Copy resources
del "%2\src\main\java\org\micromanager\icons" /Q
xcopy "%1\micromanager\mmstudio\src\org\micromanager\icons\*.*" "%2\src\main\resources\org\micromanager\icons" /I /Y
