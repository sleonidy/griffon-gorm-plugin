package org.griffon.gorm.runtime

import griffon.core.addon.GriffonAddon
import griffon.inject.DependsOn
import griffon.util.AnnotationUtils.named
import org.codehaus.griffon.runtime.core.injection.AbstractModule
import org.codehaus.griffon.runtime.util.ResourceBundleProvider
import org.griffon.gorm.api.GormFactory
import org.griffon.gorm.api.GormHandler
import org.griffon.gorm.api.GormStorage
import java.util.*

@DependsOn("datasource")
abstract class GormModule : AbstractModule() {

    override fun doConfigure() {
        bind(ResourceBundle::class.java)
                .withClassifier(named("gorm"))
                .toProvider(ResourceBundleProvider("Gorm"))

        bind<GormStorage>(GormStorage::class.java)
                .to(DefaultGormStorage::class.java)
                .asSingleton()

        bind<GormFactory>(GormFactory::class.java)
                .to(DefaultGormFactory::class.java)
                .asSingleton()

        bind<GormHandler>(GormHandler::class.java)
                .to(DefaultGormHandler::class.java)
                .asSingleton()

        bind(GriffonAddon::class.java)
                .to(GormAddon::class.java).asSingleton()

    }
}


