# Build and run SPOMAP
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

if (-not $?) { Write-Host "Project directory not found"; exit 1 }

# Ensure output directory exists
New-Item -ItemType Directory -Force -Path "bin" | Out-Null

# Compile all sources
javac --release 17 -d bin `
    -cp "lib/*;src" `
    src/Main.java src/MainFrame.java `
    src/Model/*.java src/Util/*.java `
    src/Components/*.java src/Controller/*.java `
    src/View/*.java src/Service/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful. Running..."
    java -cp "bin;lib/*" Main
}
else {
    Write-Host "Compilation failed."
    exit 1
}
