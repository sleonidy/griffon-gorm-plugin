import org.griffon.gorm.internal.AnotherPerson
/**
 * Created by lyanovsk on 24-Feb-16.
 */

gorm {
    hibernate {
        log_sql = true
        format_sql = true
        use_sql_comments = true
        show_sql = true
        cache {
            queries = true
            use_second_level_cache = true
            use_query_cache = false
            region {
                factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
            }
        }
    }
    hibernate_internal {
        show_sql = false
    }
    packages = "org.griffon.gorm.people"
    classes = [AnotherPerson]
}