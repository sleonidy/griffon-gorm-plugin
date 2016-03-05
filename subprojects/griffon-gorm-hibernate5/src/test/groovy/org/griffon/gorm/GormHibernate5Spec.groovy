package org.griffon.gorm

import griffon.core.GriffonApplication
import griffon.core.test.GriffonUnitRule
import griffon.inject.BindTo
import org.griffon.gorm.api.GormBootstrap
import org.griffon.gorm.api.GormFactory
import org.griffon.gorm.api.GormStorage
import org.griffon.gorm.internal.AnotherPerson
import org.griffon.gorm.people.Person
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject
/**
 * Created by lyanovsk on 24-Feb-16.
 */
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
            AnotherPerson.findById(1)
        }
        then:
        anotherPersonIn == anotherPersonOut
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
