@echo off

set rootfile="./xmsg/root.xml"
set targetdir="../src/test/java"
java -cp apihtecore.jar apihte.util.gen.MsgGen -root %rootfile% -target %targetdir%

PAUSE