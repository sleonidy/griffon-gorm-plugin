package org.griffon.gorm.runtime

import griffon.core.GriffonApplication
import griffon.inject.DependsOn
import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon
import org.griffon.gorm.api.GormFactory
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lyanovsk on 24-Feb-16.
 */
@DependsOn("datasource")
@Named("gorm")
class GormAddon : AbstractGriffonAddon() {
    @Inject lateinit var factory: GormFactory

    override fun init(application: GriffonApplication) {
        super.init(application)
        factory.create()
    }

    override fun onShutdown(application: GriffonApplication) {
        factory.destroy()
        super.onShutdown(application)
    }
}