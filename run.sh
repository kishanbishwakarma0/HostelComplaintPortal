#!/bin/bash
echo "=== Hostel Complaint Portal — Build Script ==="

mkdir -p out data

echo "Compiling..."
javac -d out -sourcepath src $(find src -name "*.java")

if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo ""
    echo "Running..."
    cd out
    java Main
else
    echo "Build failed. Make sure Java JDK is installed."
fi
