package live.einfachgustaf.smpclaim.di

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.config.ConfigManager
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.utils.Config
import live.einfachgustaf.smpclaim.utils.WorldGuardApi
import live.einfachgustaf.smpclaim.utils.configs.ListenerConfig
import javax.inject.Named

class SMPClaimModule(private val plugin: SMPClaim) : AbstractModule() {

    @Provides
    @Singleton
    fun provideSMPClaim(): SMPClaim = plugin

    @Provides
    @Singleton
    fun provideConfigManager(plugin: SMPClaim): ConfigManager {
        val configManager = ConfigManager(plugin)
        configManager.initialize()
        return configManager
    }

    @Provides
    @Singleton
    fun provideDataHandler(configManager: ConfigManager): IDataHandler {
        val dataHandler = configManager.config.dataHandlerType.dataHandler
        try {
            dataHandler.init()
        } catch (e: Exception) {
            plugin.logger.severe("Error while initializing data handler: ${e.message}. Disabling Plugin!")
            throw e
        }
        return dataHandler
    }

    @Provides
    @Singleton
    fun provideWorldGuardApi(): WorldGuardApi {
        val worldGuardApi = WorldGuardApi()
        worldGuardApi.init()
        return worldGuardApi
    }

    @Provides
    @Singleton
    @Named("listenerConfig")
    fun provideListenerConfig(): Config {
        val listenerConfig = ListenerConfig()
        listenerConfig.init()
        return listenerConfig
    }
}
