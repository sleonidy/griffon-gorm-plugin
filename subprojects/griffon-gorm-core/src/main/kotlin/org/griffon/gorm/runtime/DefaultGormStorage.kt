package org.griffon.gorm.runtime

import org.codehaus.griffon.runtime.core.storage.DefaultObjectStorage
import org.griffon.gorm.api.GormStorage

/**
 * Created by lyanovsk on 24-Feb-16.
 */
class DefaultGormStorage :
        DefaultObjectStorage<Any>(), GormStorage {

}