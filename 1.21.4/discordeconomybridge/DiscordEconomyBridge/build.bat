@echo off
echo Building DiscordEconomyBridge JAR file...

REM Run Maven to build the project
call mvnw.cmd clean package

echo Build complete!
echo JAR file available at target/DiscordEconomyBridge-5.3.jar 