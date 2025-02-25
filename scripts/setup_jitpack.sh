#!/bin/bash
# Make the script executable
chmod +x gradlew
# Apply the jitpack.gradle file
echo "apply from: 'jitpack.gradle.kts'" >> settings.gradle.kts 