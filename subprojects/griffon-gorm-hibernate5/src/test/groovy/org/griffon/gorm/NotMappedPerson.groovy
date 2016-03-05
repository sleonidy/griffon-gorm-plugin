package org.griffon.gorm

import grails.persistence.Entity
import groovy.transform.ToString

/**
 * Created by Leon on 05-Mar-16.
 */
@ToString
@Entity
class NotMappedPerson implements Serializable {
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
}