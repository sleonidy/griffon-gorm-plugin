package org.griffon.gorm.runtime

import griffon.core.Configuration
import griffon.core.injection.Module
import griffon.inject.DependsOn
import griffon.util.AnnotationUtils
import javax.inject.Named

/**
 * Created by Leon on 26-Feb-16.
 */
@DependsOn("datasource")
@Named("gorm")
abstract class HibernateGormModule : GormModule(), Module {
    override fun doConfigure() {
        super.doConfigure()

        bind(Configuration::class.java)
                .withClassifier(AnnotationUtils.named("gorm"))
                .to(DefaultGormHibernateConfiguration::class.java)
                .asSingleton()
    }
}