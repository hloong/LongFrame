package com.hloong.frame

import android.app.Application
import com.google.gson.Gson
import com.hloong.longlog.LongConsolePrinter
import com.hloong.longlog.LongLogConfig
import com.hloong.longlog.LongLogManager

class LongApp:Application(){
    override fun onCreate() {
        super.onCreate()
        LongLogManager.init(object : LongLogConfig(){
            override fun injectJsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }
            override fun getGlobalTag(): String {
                return "LongApp"
            }
            override fun enable(): Boolean {
                return true
            }
        },LongConsolePrinter())
    }
}