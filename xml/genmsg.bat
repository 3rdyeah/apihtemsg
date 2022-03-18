@echo off

set rootfile="./xmsg/root.xml"
set targetdir="../src/test/java"
java -cp apihtemsg.jar apihtemsg.gen.MsgGen -root %rootfile% -target %targetdir%

PAUSE