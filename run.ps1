$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$libDir = Join-Path $projectRoot 'lib'
$buildDir = Join-Path $projectRoot 'build\classes'
$h2Jar = Join-Path $libDir 'h2-2.3.232.jar'

New-Item -ItemType Directory -Force -Path $libDir, $buildDir | Out-Null
if (-not (Test-Path $h2Jar)) {
    Invoke-WebRequest 'https://repo1.maven.org/maven2/com/h2database/h2/2.3.232/h2-2.3.232.jar' -OutFile $h2Jar
}

$sources = Get-ChildItem (Join-Path $projectRoot 'src\main\java') -Recurse -Filter '*.java' | ForEach-Object FullName
javac --add-modules jdk.httpserver -cp $h2Jar -d $buildDir $sources
Copy-Item (Join-Path $projectRoot 'src\main\resources\*') $buildDir -Recurse -Force
java --add-modules jdk.httpserver -cp "$buildDir;$h2Jar" com.codearena.App
