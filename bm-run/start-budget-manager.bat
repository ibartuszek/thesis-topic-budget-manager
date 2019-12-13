ECHO OFF
ECHO Budget manager is starting...
REM start /b cmd /c call "Files\start-app.bat" transaction-service Files\bm-transaction-service-app.jar
REM start /b cmd /c call "Files\start-app.bat" calculation-service Files\bm-calculation-service-app-0.0.1-SNAPSHOT-exec.jar
REM start /b cmd /c call "Files\start-app.bat" authentication-service Files\bm-authentication-service-app-0.0.2-SNAPSHOT-exec.jar
REM start /b cmd /c call "Files\start-frontend-app.bat"

start /min "transaction service" "Files\start-app.bat" transaction-service Files\bm-transaction-service-app-0.0.2-SNAPSHOT-exec.jar
start /min "calculation-service" "Files\start-app.bat" calculation-service Files\bm-calculation-service-app-0.0.1-SNAPSHOT-exec.jar
start /min "authentication-service" "Files\start-app.bat" authentication-service Files\bm-authentication-service-app-0.0.2-SNAPSHOT-exec.jar
REM start /b cmd /c call "Files\start-frontend-app.bat"

PAUSE