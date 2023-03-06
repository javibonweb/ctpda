@echo off

set RUTA_THEMES=..\themes

echo Incorporamos los css de jquery-ui, si existen
pause

if exist %RUTA_THEMES%\jquery-ui.css (
	del /q %RUTA_THEMES%\_jquery-ui.scss
	rename %RUTA_THEMES%\jquery-ui.css _jquery-ui.scss
)

if exist %RUTA_THEMES%\jquery-ui.structure.css (
	del /q %RUTA_THEMES%\_jquery-ui.structure.scss
	rename %RUTA_THEMES%\jquery-ui.structure.css _jquery-ui.structure.scss
)

if exist %RUTA_THEMES%\jquery-ui.theme.css (
	del /q %RUTA_THEMES%\_jquery-ui.theme.scss
	rename %RUTA_THEMES%\jquery-ui.theme.css _jquery-ui.theme.scss
)

echo Generamos las variables scss
pause

rem java 8
rem javac -cp . UI2Sass.java
rem if "%errorlevel%"=="0" java -cp . UI2Sass

rem Java 11 (Hay que eliminar los ficheros .class)
java UI2Sass.java

pause
