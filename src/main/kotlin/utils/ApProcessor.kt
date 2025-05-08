package me.blueb.utils

import me.blueb.models.PluginMetadata

annotation class ApProcessorPlugin()

interface ApProcessor {
    abstract fun getMetadata(): PluginMetadata
    abstract fun process(body: Any)
}