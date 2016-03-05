package org.griffon.gorm.runtime

import org.codehaus.griffon.runtime.core.ResourceBundleConfiguration
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lyanovsk on 24-Feb-16.
 */
class DefaultGormHibernate5Configuration @Inject constructor(@Named("gorm") resourceBundle: ResourceBundle) :
        ResourceBundleConfiguration(resourceBundle)