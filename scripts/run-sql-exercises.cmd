For /f "tokens=*" %%a in ('dir sql-exercises-*.war /b') do (set filename=%%a)
java -jar %filename%