package com.bgi.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import com.bgi.vtx.DbOperation;
import com.bgi.vtx.DbOperation;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class DButil {

	private static InternalLogger logger = InternalLoggerFactory.getInstance(DButil.class);

	public static void config(Field[] fields, JsonObject config, Object instance, 
			Vertx vertx, Map<String, Object> map) throws Exception {
		if (null == fields) { return ;}
		JsonObject dbConfig = null;
		Object value = null;
		for (Field field : fields) {
			if (DbOperation.class.isAssignableFrom(field.getType())) {
				if (null != (value = map.get(field.getName()))) {
					field.set(instance, value);
				} else {
					if (null == (dbConfig = config.getJsonObject(field.getName()))) { 
						logger.error("liyingqiao: cannot find config of " + field.getName());
						throw new Exception("liyingqiao: cannot find config of " + field.getName()); 
					}
					if (null == (value = dbConfig.getString("class"))) {
						logger.error("liyingqiao: cannot config property class of DB class in " 
								+ field.getName());
						throw new Exception("liyingqiao: cannot config property class of DB class in " 
								+ field.getName());
					}
					Class<?> dbClass = Class.forName((String )value);
					if (!DbOperation.class.isAssignableFrom(dbClass)) {
						logger.error("liyingqiao: the property class of " + field.getName() 
						+ " is not implements " + DbOperation.class);
						throw new Exception("liyingqiao: the property class of " + field.getName() 
						+ " is not implements " + DbOperation.class);
					}
					value = dbConfig.getString("poolName");
					value = null == value ? UUID.randomUUID().toString() : value;
					dbConfig = dbConfig.getJsonObject("config");
					value = dbClass.getDeclaredConstructor(Vertx.class, JsonObject.class, String.class)
							.newInstance(vertx, null == dbConfig ? new JsonObject() : dbConfig, (String )value);
					try {
						field.setAccessible(true);
						field.set(instance, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
					map.put(field.getName(), value);
					logger.info("liyingqiao: " + field.getName() + "  :  " + value.getClass().getName());
				}
			}
		}
	}
}
