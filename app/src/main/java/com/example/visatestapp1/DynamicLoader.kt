package com.example.visatestapp1

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Environment
import android.util.Log
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Method
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class DynamicLoader(private val context: Context) {
    private var classLoader: DexClassLoader? = null
    private var resources: Resources? = null

    @Throws(IOException::class)
    fun loadAarFromAssets(aarFileName: String) {
        // Step 1: Extract the AAR file from assets
        val assetManager = context.assets
        val inputStream = assetManager.open(aarFileName)

        val codeCacheDir = context.getDir("extracted_aar", Context.MODE_PRIVATE)
        if (!codeCacheDir.exists()) {
            codeCacheDir.mkdirs()
        }

        // Comment from 31-51 after first run

//        ZipInputStream(inputStream).use { zis ->
//            var entry: ZipEntry?
//            while (zis.nextEntry.also { entry = it } != null) {
//                val entryFile = File(codeCacheDir, entry!!.name)
//                Log.i("File", entryFile.absolutePath)
//                if (entry!!.isDirectory) {
//                    entryFile.mkdirs()
//                } else {
//                    entryFile.parentFile?.takeIf { !it.exists() }?.mkdirs()
//                    FileOutputStream(entryFile).use { fos ->
//                        val buffer = ByteArray(1024)
//                        var length: Int
//                        while (zis.read(buffer).also { length = it } > 0) {
//                            fos.write(buffer, 0, length)
//                            Log.i("File","Writing")
//                        }
//                    }
//                }
//            }
//        }

        // Step 2: Load the classes

        val dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE)
//        val dexOutputDir = context.getDir("sdk1-release", Context.MODE_PRIVATE)
        val fullPath = "${codeCacheDir.absolutePath}"
        val data = Environment.getDataDirectory()

        val dexFile = File(fullPath, "classes.jar")
//        dexFile.setReadOnly()
        if (!dexFile.exists()) {
            Log.e("Loader", "File not found")
            throw IOException("classes.jar not found in the extracted AAR.")
        }
        classLoader = DexClassLoader(dexFile.absolutePath, dexOutputDir.absolutePath, null, context.classLoader)
        Log.i("Loader", "Loading Classes path ${dexFile.absolutePath}")
        // Step 3: Load the resources
//        try {
//            val newAssetManager = AssetManager::class.java.newInstance()
//            val addAssetPath: Method = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
//            addAssetPath.invoke(newAssetManager, codeCacheDir.absolutePath)
//            val baseResources = context.resources
//            resources = Resources(newAssetManager, baseResources.displayMetrics, baseResources.configuration)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    @Throws(ClassNotFoundException::class)
    fun loadClass(className: String): Class<*> {
        checkNotNull(classLoader) { "AAR file not loaded. Call loadAarFromAssets() first." }
        return classLoader!!.loadClass(className)
    }

    fun getResources(): Resources {
        return resources ?: throw IllegalStateException("AAR file not loaded or no resources available. Call loadAarFromAssets() first.")
    }
}
