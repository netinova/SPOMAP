# Build and run SPOMAP : cross‑platform

# Detect OS and set classpath separator
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
    CP_SEP=";"
else
    CP_SEP=":"
fi

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Ensure output directory exists
mkdir -p bin

# Compile
javac --release 17 -d bin \
    -cp "lib/*${CP_SEP}src" \
    src/Main.java src/MainFrame.java \
    src/Model/*.java src/Util/*.java \
    src/Components/*.java src/Controller/*.java \
    src/View/*.java src/Service/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful. Running..."
    java -cp "bin${CP_SEP}lib/*" Main
else
    echo "Compilation failed."
    exit 1
fi
