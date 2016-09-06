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
package org.griffon.plugins.gorm

import griffon.core.GriffonApplication
import griffon.core.test.GriffonUnitRule
import griffon.inject.BindTo
import org.griffon.plugins.gorm.api.GormBootstrap
import org.griffon.plugins.gorm.api.GormFactory
import org.griffon.plugins.gorm.api.GormStorage
import org.griffon.plugins.gorm.internal.AnotherPerson
import org.griffon.plugins.gorm.people.Person
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

@Unroll
class GormHibernate5Spec extends Specification {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace")
    }


    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule()

    @Inject
    private GormFactory factory
    @Inject
    private GormStorage storage
    @Inject
    private GriffonApplication application
    @BindTo(GormBootstrap)
    private TestGormBootstrap bootstrap = new TestGormBootstrap()


    void 'Bootstrap init is called'() {
        expect:
        bootstrap.initWitness
        !bootstrap.destroyWitness
    }

    void 'Bootstrap destroy is called'() {
        when:
        application.shutdown()

        then:
        bootstrap.initWitness
        bootstrap.destroyWitness
    }

    void 'execute statements on people table with Person class with transaction'() {
        when:
        List peopleIn = Person.withTransaction {
            [[id: 1, name: 'Danno', lastname: 'Ferrin'],
             [id: 2, name: 'Andres', lastname: 'Almiray'],
             [id: 3, name: 'James', lastname: 'Williams'],
             [id: 4, name: 'Guillaume', lastname: 'Laforge'],
             [id: 5, name: 'Jim', lastname: 'Shingler'],
             [id: 6, name: 'Alexander', lastname: 'Klein'],
             [id: 7, name: 'Rene', lastname: 'Groeschke']].each { data ->
                new Person(data).save()
            }

        }
        List peopleOut = Person.withTransaction {
            Person.findAll {

            }*.asMap()
        }

        then:
        peopleIn.size() == peopleOut.size()
    }

    void 'execute statements on another person which mapped to "internal" datasource'() {
        when:
        def anotherPersonIn = AnotherPerson.withTransaction {
            new AnotherPerson([id: 1, name: 'Danno', lastname: 'Ferrin']).save()
        }
        def anotherPersonOut = AnotherPerson.withTransaction {
            AnotherPerson.findAll()
        }
        then:
        anotherPersonIn == anotherPersonOut[0]
    }

    void 'execute statements on not mapped person throws exception'() {
        when:
        NotMappedPerson.withTransaction {
            new NotMappedPerson([id: 1, name: 'Danno', lastname: 'Ferrin']).save()
        }
        then:
        thrown(IllegalStateException)
    }

    void 'gorm configuration stored'() {

        expect:
        storage.contains("packages")
        storage.contains("classes")
    }

    void 'hibernate configuration applied'() {
        when:
        def map = factory.getConfigurationAsMap()

        then:
        map.containsKey("hibernate_people")
        map.containsKey("hibernate_internal")
        map.containsKey("hibernate")
    }

    void 'specific datasource hibernate configuration applied'() {
        when:
        def map = factory.getConfigurationAsMap()

        then:
        map.get("hibernate_internal").get("show_sql") == false
    }
}
