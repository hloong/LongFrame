package com.hloong.ui.common.component

import android.app.Application
import com.google.gson.Gson
import com.hloong.lib.longlog.LongConsolePrinter
import com.hloong.lib.longlog.LongFilePrinter
import com.hloong.lib.longlog.LongLogManager
import com.hloong.lib.longlog.base.LongLogConfig

open class BaseApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        initLog()
    }

    fun initLog(){
        LongLogManager.init(object : LongLogConfig(){
            override fun injectJsonParser(): JsonParser? {
                return object : JsonParser {
                    override fun toJson(obj: Any): String {
                        return Gson().toJson(obj)
                    }
                }
            }
            override fun getGlobalTag(): String {
                return "LongApp"
            }
            override fun enable(): Boolean {
                return true
            }
            override fun includeThread(): Boolean {
                return true
            }
        },  LongConsolePrinter(), LongFilePrinter.getInstance(cacheDir.absolutePath,0)!!)
    }

}