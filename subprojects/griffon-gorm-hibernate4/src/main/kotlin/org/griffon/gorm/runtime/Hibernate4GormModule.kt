package org.griffon.gorm.runtime

import griffon.core.Configuration
import griffon.core.injection.Module
import griffon.inject.DependsOn
import griffon.util.AnnotationUtils
import org.griffon.gorm.api.GormFactory
import javax.inject.Named

/**
 * Created by Leon on 26-Feb-16.
 */
@DependsOn("datasource")
@Named("gorm")
class Hibernate4GormModule : HibernateGormModule(), Module {
    override fun doConfigure() {
        super.doConfigure()
        bind(GormFactory::class.java)
                .to(Hibernate4GormFactory::class.java)
                .asSingleton()
        bind(Configuration::class.java)
                .withClassifier(AnnotationUtils.named("gorm"))
                .to(DefaultGormHibernate5Configuration::class.java)
                .asSingleton()
    }
}