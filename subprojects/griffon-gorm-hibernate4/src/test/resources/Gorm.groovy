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
import org.griffon.gorm.internal.AnotherPerson


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