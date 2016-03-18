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

import griffon.core.Configuration
import griffon.core.injection.Module
import griffon.inject.DependsOn
import griffon.util.AnnotationUtils
import org.griffon.plugins.gorm.api.GormFactory
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