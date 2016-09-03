@echo off
cd ..
java -cp .;lib/*;conf/ ren.wxyz.tool.compare.copy.App -c conf/app.properties

pause ...