package org.griffon.gorm.api

interface GormFactory {
    fun create()
    fun destroy()
    fun getConfigurationAsMap(): MutableMap<String, Map<*, *>>
}