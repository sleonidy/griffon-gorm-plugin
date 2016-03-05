package org.griffon.gorm.runtime

import grails.orm.bootstrap.HibernateDatastoreSpringInitializer
import griffon.core.Configuration
import griffon.core.injection.Module
import griffon.inject.DependsOn
import griffon.util.AnnotationUtils
import org.grails.datastore.gorm.bootstrap.AbstractDatastoreInitializer
import org.griffon.gorm.api.GormFactory
import javax.inject.Named

/**
 * Created by Leon on 26-Feb-16.
 */
@DependsOn("datasource")
@Named("gorm")
class Hibernate5GormModule : HibernateGormModule(), Module {
    override fun doConfigure() {
        super.doConfigure()
        bind(AbstractDatastoreInitializer::class.java)
                .to(HibernateDatastoreSpringInitializer::class.java)
                .asSingleton()
        bind(GormFactory::class.java)
                .to(Hibernate5GormFactory::class.java)
                .asSingleton()
        bind(Configuration::class.java)
                .withClassifier(AnnotationUtils.named("gorm"))
                .to(DefaultGormHibernate5Configuration::class.java)
                .asSingleton()
    }
}