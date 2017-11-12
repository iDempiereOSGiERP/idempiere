@Rem $Id: RUN_TrlImport.bat,v 1.4 2005/09/16 00:49:37 jjanke Imp $

@if (%IDEMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %IDEMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Import Translation - %IDEMPIERE_HOME% (%ADEMPIERE_DB_NAME%)

@SET AD_LANGUAGE=es_CO
@SET DIRECTORY=%IDEMPIERE_HOME%\data\%AD_LANGUAGE%

@echo This Procedure imports language %AD_LANGUAGE% from directory %DIRECTORY%
@pause

FOR %%c in (plugins\org.eclipse.equinox.launcher_1.*.jar) DO set JARFILE=%%c
@"%JAVA_HOME%\bin\java" -Dosgi.compatibility.bootdelegation=true -Dosgi.noShutdown=false -jar %JARFILE% -application org.adempiere.install.translation import %DIRECTORY% %AD_LANGUAGE%

@pause
