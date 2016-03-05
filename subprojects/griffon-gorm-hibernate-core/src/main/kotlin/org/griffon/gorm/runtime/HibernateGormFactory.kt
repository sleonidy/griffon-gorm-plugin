package org.griffon.gorm.runtime

import griffon.core.Configuration
import griffon.core.GriffonApplication
import java.util.*
import javax.annotation.Nonnull
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Leon on 26-Feb-16.
 */
abstract class HibernateGormFactory @Inject constructor(@Nonnull @Named("gorm") configuration: Configuration, @Nonnull application: GriffonApplication)
: DefaultGormFactory(configuration, application) {
    override fun getConfigurationAsMap(): MutableMap<String, Map<*, *>> {
        val configurationMap = super.getConfigurationAsMap()
        configurationMap.putAll(getHibernateConfigurationAsMapOfDataSourceNameToConfigurationMap())
        return configurationMap

    }

    @Suppress("UNCHECKED_CAST")
    fun getHibernateConfigurationAsMapOfDataSourceNameToConfigurationMap(): Map<String, Map<*, *>> {
        return dataSourceFactory.dataSourceNames.associate {
            val dataSourceName = if (it == "default") "" else it
            var config: MutableMap<Any, Any> = HashMap()
            if (configuration.get("$GORM_CONFIG.$HIBERNATE") != null) {
                config.putAll(configuration.get("$GORM_CONFIG.$HIBERNATE") as Map<Any, Any>)
            }
            if (configuration.get("$GORM_CONFIG.${HIBERNATE}_$dataSourceName") != null) {
                config.putAll(configuration.get("$GORM_CONFIG.${HIBERNATE}_$dataSourceName") as Map<Any, Any>)
            }
            if (dataSourceName.isBlank())
                "$HIBERNATE" to config
            else
                "${HIBERNATE}_$dataSourceName" to config

        }
    }


    companion object {
        val HIBERNATE = "hibernate"
    }
}