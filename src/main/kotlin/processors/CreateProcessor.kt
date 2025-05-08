package me.blueb.processors

import me.blueb.utils.ApProcessorPlugin
import me.blueb.utils.ApProcessor
import me.blueb.models.PluginMetadata

@ApProcessorPlugin()
class CreateProcessor : ApProcessor {
    override fun getMetadata() = PluginMetadata(
        id = "BaseCreate",
        version = "1",
        type = "Create"
    )
    
    override fun process(body: Any) {
        return;
    }
}