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
package org.griffon.plugins.gorm.people

import grails.persistence.Entity
import groovy.transform.ToString
import org.grails.datastore.gorm.GormEntity

@ToString
@Entity
class Person implements GormEntity<Person>, Serializable {
    int id
    String name
    String lastname

    static mapping = {
        datasource 'people'
    }

    Map asMap() {
        [
                id      : id,
                name    : name,
                lastname: lastname
        ]
    }
}