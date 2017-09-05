var ioc = {
		conf : {
			type : "org.nutz.ioc.impl.PropertiesProxy",
			fields : {
				paths : ["config/custom/"]
			}
		},
	    dataSource : {
			factory : "$conf#make",
			args : ["com.alibaba.druid.pool.DruidDataSource", "db."],
	        type : "com.alibaba.druid.pool.DruidDataSource",
	        events : {
	        	create : "init",
	            depose : 'close'
	        }
	    },
		dao : {
			type : "org.nutz.dao.impl.NutDao",
			args : [{refer:"dataSource"}],
			fields : {
				executor : {refer:"cacheExecutor"}
			}
		},
		cacheExecutor : {
			type : "org.nutz.plugins.cache.dao.CachedNutDaoExecutor",
			fields : {
				cacheProvider : {refer:"cacheProvider"},
				cachedTableNames : ["sys_user", "sys_role", "sys_menu"]
			}
		},
		/*
		// 基于内存的简单LRU实现
		cacheProvider : {
			type : "org.nutz.plugins.cache.dao.impl.provider.MemoryDaoCacheProvider",
			fields : {
				cacheSize : 10000 // 缓存的对象数
			},
			events : {
				create : "init"
			}
		}
		*/
		// 基于Ehcache的DaoCacheProvider
		cacheProvider : {
			type : "org.nutz.plugins.cache.dao.impl.provider.EhcacheDaoCacheProvider",
			fields : {
				cacheManager : {refer:"cacheManager"} // 引用ehcache.json中定义的CacheManager
			},
			events : {
				create : "init"
			}
		}
};