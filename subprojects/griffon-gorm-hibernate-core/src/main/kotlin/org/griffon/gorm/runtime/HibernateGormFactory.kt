/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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