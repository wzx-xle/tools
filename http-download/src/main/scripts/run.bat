@echo off
cd ..
java -cp .;lib/*;conf/ ren.wxyz.tools.http.download.App -c conf/app.properties

pause ...