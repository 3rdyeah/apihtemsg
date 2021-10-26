@echo off

set rootfile="./xmsg/root.xml"
set targetdir="../src/test/java"
java -cp apihteproto.jar apihteproto.gen.MsgGen -root %rootfile% -target %targetdir%

PAUSE