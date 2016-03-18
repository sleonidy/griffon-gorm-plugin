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

import grails.orm.bootstrap.HibernateDatastoreSpringInitializer
import griffon.core.Configuration
import griffon.core.GriffonApplication
import javax.annotation.Nonnull
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Leon on 26-Feb-16.
 */
class Hibernate5GormFactory @Inject constructor(@Nonnull @Named("gorm") configuration: Configuration, @Nonnull application: GriffonApplication)
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