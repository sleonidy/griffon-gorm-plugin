package org.griffon.gorm.internal

import grails.persistence.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Created by leonidyanovsky on 9/10/15.
 */
@ToString
@Entity
@EqualsAndHashCode(includes = ["id"])
class AnotherPerson implements Serializable {
    int id
    String name
    String lastname

    Map asMap() {
        [
                id      : id,
                name    : name,
                lastname: lastname
        ]
    }
    static mapping = {
        datasource 'internal'
    }
}