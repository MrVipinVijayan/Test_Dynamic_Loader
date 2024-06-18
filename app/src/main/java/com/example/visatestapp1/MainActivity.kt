package com.example.visatestapp1

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.jar.JarFile


class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var btnSdk1: Button
    private lateinit var btnSdk2: Button

    private val REQUEST_STORAGE_PERMISSION: Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        btnSdk1 = findViewById(R.id.btnSdk1)
        btnSdk2 = findViewById(R.id.btnSdk2)

        btnSdk1.setOnClickListener(this)
        btnSdk2.setOnClickListener(this)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
            // Permission already granted, proceed with file access
            // ...
        }

//        val condition = true
//        try {
//            val aarFileName = if (condition) "sdk1-release.aar" else "sdk2-release.aar"
//            val aarPath = DynamicClassLoader.extractAarToDex(this, aarFileName)
//
//            // List all classes in the AAR file
//            val classNames = DynamicClassLoader.listClassesInJar(aarPath)
//            classNames.forEach { className ->
//                Log.d("ClassName", className)
//            }
//
//            // Load and use a specific class
//            val className = "com.example.sdk.TestClass"
//            val clazz = DynamicClassLoader.loadClass(this, aarPath, className)
//            val instance = clazz.getDeclaredConstructor().newInstance()
//            val method = clazz.getMethod("printInfo")
//            method.invoke(instance)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        val condition = true
//        try {
//            val sdkPath = if (condition) "libs/sdk1-release.aar" else "libs/sdk2-release.aar"
//            val className = "com.example.sdk.TestClass"
//            val clazz = DynamicClassLoader.loadClass(sdkPath, className)
//            val instance = clazz.newInstance()
//            val method = clazz.getMethod("printInfo")
//            method.invoke(instance)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        val condition = true
//        try {
//            val aarFileName = if (condition) "sdk1-release.aar" else "sdk2-release.aar"
//            val aarPath = DynamicClassLoader.extractAarToDex(this, aarFileName)
//
//            val className = "com.example.sdk.TestClass"
//            val clazz = DynamicClassLoader.loadClass(this, aarPath, className)
//            val instance = clazz.getDeclaredConstructor().newInstance()
//            val method = clazz.getMethod("someMethod")
//            method.invoke(instance)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        loadAAR()
    }

    fun loadAAR() {
        val dynamicLoader = DynamicLoader(applicationContext)
        try {
            dynamicLoader.loadAarFromAssets("sdk1-release.aar")

            // Load a class from the AAR file
            val clazz = dynamicLoader.loadClass("com.example.sdk.TestClass")
            val instance = clazz.newInstance()

//            // Access resources
//            val resources = dynamicLoader.getResources()
//            val stringId = resources.getIdentifier("some_string", "string", "com.example")
//            val someString = resources.getString(stringId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadAAR()
            } else {
                // Permission denied, inform user and handle the situation
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == btnSdk1) {
            Log.i("Main", "SDK1 Clicked")
        }
        if (v == btnSdk2) {
            Log.i("Main", "SDK2 Clicked")
        }
    }

//    private fun copyAarToInternalStorage(aarPath: String) {
//        // Copy the AAR file from assets to the internal storage
//        val assetManager = assets
//        val inputStream = assetManager.open("libs/sdk1-release.aar")
//        val outputFile = File(aarPath)
//        val outputStream = outputFile.outputStream()
//
//        inputStream.use { input ->
//            outputStream.use { output ->
//                input.copyTo(output)
//            }
//        }
//    }

    private fun copyAarToInternalStorage(aarFileName: String): String {
        val inputStream = assets.open(aarFileName)
        val outputFile = File(filesDir, aarFileName)
        val outputStream = FileOutputStream(outputFile)

        try {
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return outputFile.absolutePath
    }

}

//object DynamicClassLoader {
//    @Throws(Exception::class)
//    fun loadClass(jarPath: String?, className: String?): Class<*> {
//        val jarFile = File(jarPath)
//        val urls = arrayOf(jarFile.toURI().toURL())
//        val classLoader = URLClassLoader(urls, DynamicClassLoader::class.java.classLoader)
//        return Class.forName(className, true, classLoader)
//    }
//}


//object DynamicClassLoader {
//    @Throws(Exception::class)
//    fun loadClass(context: Context, aarPath: String, className: String): Class<*> {
//        val codeCacheDir = context.codeCacheDir
//        val dexOutputDir = File(codeCacheDir, "dex")
//        if (!dexOutputDir.exists()) {
//            dexOutputDir.mkdirs()
//        }
//
//        val optimizedDexOutputPath = dexOutputDir.absolutePath
//
//        val classLoader = DexClassLoader(
//            aarPath,
//            optimizedDexOutputPath,
//            null,
//            context.classLoader
//        )
//        return classLoader.loadClass(className)
//    }
//
//    fun extractAarToDex(context: Context, aarFileName: String): String {
//        val inputStream = context.assets.open(aarFileName)
//        val dexDir = File(context.filesDir, "dex")
//        if (!dexDir.exists()) {
//            dexDir.mkdirs()
//        }
//
//        val aarFile = File(dexDir, aarFileName)
//        val outputStream = FileOutputStream(aarFile)
//
//        try {
//            inputStream.use { input ->
//                outputStream.use { output ->
//                    input.copyTo(output)
//                }
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        return aarFile.absolutePath
//    }
//}


object DynamicClassLoader {
    @Throws(Exception::class)
    fun loadClass(context: Context, aarPath: String, className: String): Class<*> {
        val codeCacheDir = context.codeCacheDir
        val dexOutputDir = File(codeCacheDir, "dex")
        if (!dexOutputDir.exists()) {
            dexOutputDir.mkdirs()
        }

        val optimizedDexOutputPath = dexOutputDir.absolutePath

        val classLoader = DexClassLoader(
            aarPath,
            optimizedDexOutputPath,
            null,
            context.classLoader
        )
        return classLoader.loadClass(className)
    }

    fun extractAarToDex(context: Context, aarFileName: String): String {
        val inputStream = context.assets.open(aarFileName)
        val dexDir = File(context.filesDir, "dex")
        if (!dexDir.exists()) {
            dexDir.mkdirs()
        }

        val aarFile = File(dexDir, aarFileName)
        val outputStream = FileOutputStream(aarFile)

        try {
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return aarFile.absolutePath
    }

    @Throws(IOException::class)
    fun listClassesInJar(jarPath: String): List<String> {
        val jarFile = JarFile(jarPath)
        val classNames = mutableListOf<String>()
        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.name.endsWith(".class")) {
                val className = entry.name.replace("/", ".").removeSuffix(".class")
                classNames.add(className)
            }
        }
        return classNames
    }
}