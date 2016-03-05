package org.griffon.gorm.api

/**
 * Created by Leon on 25-Feb-16.
 */
interface GormBootstrap {
    fun init()
    fun destroy()
}