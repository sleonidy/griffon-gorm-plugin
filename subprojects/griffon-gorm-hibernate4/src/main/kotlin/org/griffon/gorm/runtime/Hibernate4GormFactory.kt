package org.griffon.gorm.runtime

import grails.orm.bootstrap.HibernateDatastoreSpringInitializer
import griffon.core.Configuration
import griffon.core.GriffonApplication
import java.util.*
import javax.annotation.Nonnull
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Leon on 26-Feb-16.
 */
class Hibernate4GormFactory @Inject constructor(@Nonnull @Named("gorm") configuration: Configuration, @Nonnull application: GriffonApplication)
: HibernateGormFactory(configuration, application) {
    override fun create() {
        event("GormCreateStart", listOf(GORM_CONFIG))
        super.create()
        val configurationMap = getConfigurationAsMap()
        val initializer = HibernateDatastoreSpringInitializer(configurationMap)
        applyConfigToInitializer(initializer)
        initializer.configureForDataSources(dataSourceNameToDataSource)
        event("GormCreateEnd", listOf(GORM_CONFIG))
    }
}