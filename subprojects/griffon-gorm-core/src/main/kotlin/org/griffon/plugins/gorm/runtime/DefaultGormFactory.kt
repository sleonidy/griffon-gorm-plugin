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
package org.griffon.plugins.gorm.runtime


import grails.persistence.Entity
import griffon.core.Configuration
import griffon.core.GriffonApplication
import griffon.plugins.datasource.DataSourceFactory
import griffon.plugins.datasource.DataSourceStorage
import griffon.util.ConfigUtils
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import org.grails.datastore.gorm.bootstrap.AbstractDatastoreInitializer
import org.griffon.plugins.gorm.api.GormBootstrap
import org.griffon.plugins.gorm.api.GormFactory
import org.griffon.plugins.gorm.api.GormStorage
import java.util.*
import javax.annotation.Nonnull
import javax.inject.Inject
import javax.inject.Named
import javax.sql.DataSource


abstract class DefaultGormFactory @Inject constructor(@Nonnull @Named("gorm") val configuration: Configuration, @Nonnull val application: GriffonApplication) :
        GormFactory {
    @Inject lateinit var dataSourceFactory: DataSourceFactory

    @Inject lateinit var dataSourceStorage: DataSourceStorage
    @Inject lateinit var storage: GormStorage


    lateinit var dataSourceNameToDataSource: Map<String, DataSource>


    protected fun event(eventName: String, args: List<Any>) {
        application.eventRouter.publishEvent(eventName, args)
    }

    override fun create() {


        application.injector.getInstances(GormBootstrap::class.java)
                .forEach {
                    it.init()
                }
        dataSourceNameToDataSource = dataSourceFactory
                .dataSourceNames
                .map { it to getDataSource(it) }
                .map { if (it.first == "default") return@map "dataSource" to it.second else it }
                .associate { it.first to it.second }
        readConfig(configuration)

        storage.set("dataSourceNamesToDataSources", dataSourceNameToDataSource)

    }

    fun getMapOfDataSourceNameToConfiguration(): Map<String, Map<String, *>> {
        return dataSourceFactory
                .dataSourceNames
                .groupBy {
                    return@groupBy if (it == "default")
                        "dataSource"
                    else
                        "dataSources"
                }
                .mapValues {
                    it.value.associate { it to dataSourceFactory.getConfigurationFor(it) }
                }
    }

    override fun getConfigurationAsMap(): MutableMap<String, Map<*, *>> {
        val configurationMap = HashMap<String, Map<*, *>>()
        configurationMap.putAll(getMapOfDataSourceNameToConfiguration())
        return configurationMap
    }

    @Suppress("UNCHECKED_CAST")
    fun readConfig(configuration: Configuration) {
        val config = configuration.get(GORM_CONFIG) as Map<String, Any>
        val classes = mutableListOf<Class<out Any>>()
        if (config.contains(PACKAGES)) {
            val packages = ConfigUtils.getConfigValueAsString(config, PACKAGES, "")!!.split(",")
            storage.set(PACKAGES, packages)

            FastClasspathScanner(*packages.toTypedArray())
                    .matchClassesWithAnnotation(Entity::class.java, {classes.add(it)})
                    .scan()
        }
        if (config.contains(CLASSES)) {
            classes += ConfigUtils.getConfigValue<List<Class<*>>>(config, CLASSES)!!
            storage.set(CLASSES, classes)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun applyConfigToInitializer(initializer: AbstractDatastoreInitializer) {
        if (storage.contains("packages")) {
            initializer.packages = storage.get("packages") as MutableCollection<String>?
        }
        if (storage.contains("classes"))
            initializer.persistentClasses = storage.get("classes") as MutableCollection<Class<Any>>?

    }

    override fun destroy() {
        application.injector.getInstances(GormBootstrap::class.java)
                .forEach {
                    it.destroy()
                }
    }


    private fun getDataSource(dataSourceName: String): DataSource {
        var dataSource = dataSourceStorage.get(dataSourceName)
        if (dataSource == null) {
            dataSource = dataSourceFactory.create(dataSourceName)
            dataSourceStorage.set(dataSourceName, dataSource)
        }
        return dataSource
    }


    companion object {
        val GORM_CONFIG = "gorm"
        val PACKAGES = "packages"
        val CLASSES = "classes"
    }
}