@echo off

setlocal enabledelayedexpansion
pushd %~d0%~p0
cd ..
set CURRENT_DIR=%cd%
echo %cd%
set CLASSPATH=%cd%\conf
for %%i in (%cd%\lib\*.jar) do (SET "CLASSPATH=!CLASSPATH!;%%i")

echo %CLASSPATH%
java -classpath %CLASSPATH% com.newnoriental.base.MinitorBase

popd
pause