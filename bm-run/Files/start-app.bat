ECHO OFF
CONSOLESTATE /Hide
IF "%3" == "prod" (
	ECHO start %1
	call java -jar %2 --spring.profiles.active=%3
) ELSE (
	ECHO start %1 with test environment.
	call java -jar %2
)