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
package org.griffon.gorm.runtime

import griffon.core.GriffonApplication
import griffon.inject.DependsOn
import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon
import org.griffon.gorm.api.GormFactory
import javax.inject.Inject
import javax.inject.Named


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