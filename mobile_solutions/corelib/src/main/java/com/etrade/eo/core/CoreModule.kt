package com.etrade.eo.core

import com.etrade.eo.core.json.GsonJsonUtil
import com.etrade.eo.core.json.GsonModule
import com.etrade.eo.core.json.JsonUtil
import com.etrade.eo.core.json.JsonUtilGson

import dagger.Binds
import dagger.Module

@Module(includes = arrayOf(GsonModule::class))
abstract class CoreModule {

    @Binds
    @JsonUtilGson
    abstract fun bindsJsonUtil(gsonJsonUtil: GsonJsonUtil): JsonUtil
}
