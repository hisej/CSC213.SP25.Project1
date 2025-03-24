#!/bin/bash

# Define variables
SRC_DIR="src/main/java"
OUT_DIR="out"
JAR_FILE="uniquehands.jar"
MAIN_CLASS="edu.canisius.csc213.project1.UniqueHands"

# Clean previous build
echo "🧹 Cleaning previous build..."
rm -rf $OUT_DIR
mkdir -p $OUT_DIR
echo "Cleaned previous build."

# Compile Java files
echo "🚀 Compiling Java files..."
javac -d $OUT_DIR $(find $SRC_DIR -name "*.java")
echo "Compilation complete."

# Package into a JAR
echo "📦 Creating JAR file..."
cd $OUT_DIR
jar cvfe ../$JAR_FILE $MAIN_CLASS $(find . -name "*.class")
cd ..
echo "JAR file created: $JAR_FILE"


# Done!
echo "✅ Build complete! Run it with: java -jar $JAR_FILE"