@echo off
set y=%date:~0,4%
set month=%date:~5,2%
set day=%date:~8,2%
set hour=%time:~0,2%
set min=%time:~3,2%
set sec=%time:~6,2%

set time=%y%.%month%.%day%-%hour%.%min%.%sec%
echo "Commit-%time%"
set msg=
set /p "msg=Input extra message: "

if "%msg%"=="" (
	set msg=Commit
) else (
	set msg=%msg%
)

set commit="%msg%-%time%"

title %commit%

git add .
git commit -m "%commit%"

echo =============
git log --pretty=oneline
echo =============
exit