hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}

environments {
	development {
        elasticSearch {
            client.mode = 'node' // local
        }
		
		grails {			
			mongo {
			  host = "" // -> 127.0.0.1
			  port = "27107"
			  username = "udoctor"
			  password= "6f82b0a3426f82b0a342111111"
			  databaseName = "udoctor"
			  options {
				  autoConnectRetry = true
				  connectTimeout = 300
			  }
			}
		  }
	}

	test {
		elasticSearch {
          client.mode = 'local'
          index.store.type = 'memory' // store local node in memory and not on disk
		}
		grails {
			mongo {
			  host = "" // -> 127.0.0.1
			  port = "27107"
			  username = "udoctor"
			  password= "6f82b0a3426f82b0a342111111"
			  databaseName = "udoctor"
			  options {
				  autoConnectRetry = true
				  connectTimeout = 300
			  }
			}
		  }
	}

	production {
		elasticSearch {
            client.mode = 'node'
		}
		grails {
			mongo {
			  host = "" // -> 127.0.0.1
			  port = "27107"
			  username = "udoctor"
			  password= "6f82b0a3426f82b0a342111111"
			  databaseName = "udoctor"
			  options {
				  autoConnectRetry = true
				  connectTimeout = 300
			  }
			}
		  }
	}
}