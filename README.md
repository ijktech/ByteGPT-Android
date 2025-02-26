# ByteGPT-Android

ByteGPT-Android is a lightweight Android library for running small GPT models locally using ONNX Runtime Mobile.

## 🚀 Getting Started

### 1️⃣ Add ByteGPT to Your Project

Add the following **to your `build.gradle.kts`**:

```kotlin
repositories {
    maven { 
        url = uri("https://raw.githubusercontent.com/ijktech/ByteGPT-Android/maven-repo") 
    }
}

dependencies {
    implementation("com.github.ijktech:ByteGPT-Android:1.0.9")
}
```


### 2️⃣ Usage Example

```kotlin
package com.ijktech.bytegpt

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText = findViewById<EditText>(R.id.inputText)
        val generateButton = findViewById<Button>(R.id.generateButton)
        val outputText = findViewById<TextView>(R.id.outputText)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)

        val tokenizer = ByteGPTTokenizer()
        val generator = ByteGPTGenerator(this)

        generateButton.setOnClickListener {
            val input = inputText.text.toString().trim()

            if (input.isEmpty()) {
                outputText.text = "⚠️ Please enter a prompt before generating."
                return@setOnClickListener
            }

            // Run generation in a background thread to prevent UI freezing
            Thread {
                try {
                    val generatedOutput = generator.generate(input, temperature = 0.3f)

                    // Update UI safely on the main thread
                    runOnUiThread {
                        outputText.text = "✅ Generated Text:\n$generatedOutput"

                        // Scroll to the bottom to see new output
                        scrollView.post {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        outputText.text = "❌ Error during generation:\n${e.message}"
                    }
                    e.printStackTrace()  // Print error in Logcat for debugging
                }
            }.start()
        }
    }
}
```

### 3️⃣ UI Layout Example (activity_main.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp"
    android:orientation="vertical">

    <EditText
        android:id="@+id/inputText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter a prompt"
        android:textSize="18sp"/>

    <Button
        android:id="@+id/generateButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Generate"
        android:layout_marginTop="10dp"/>

    <ScrollView
        android:id="@+id/scrollView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_marginTop="20dp">

        <TextView
            android:id="@+id/outputText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Generated text will appear here"
            android:textSize="18sp"
            android:padding="8dp"/>
    </ScrollView>

</LinearLayout>
```

### 4️⃣ Run the App 🎉

Install the app on an **Android device** or **emulator**.
Enter a text prompt.
Tap "Generate" to see the AI-generated text.

### 🛠 Requirements

Android API Level 24+
ONNX Runtime Mobile
Gradle 8+

### 📄 License

This project is available for non-commercial use under the create-commons 4.0 License. For commercial licensing please contact me.

### 📧 Contact

For questions or support, please file issues in the repo.
