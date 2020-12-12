package com.bgi.vtx.annotation;

import com.bgi.vtx.annotation.SwaggerProperty.SwaggerCollection;
import com.bgi.vtx.annotation.SwaggerProperty.SwaggerEntity;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
	String path() default "/";

	HttpMethod method() default HttpMethod.GET;

	String description() default "暂无描述";

	SwaggerProperty[] reqParams() default {};

	SwaggerProperty[] respParams() default {};

	Class<?> request() default Void.class;

	Class<?> response() default Void.class;

	public class RequestMappingEntity extends JsonObject {
		private static final InternalLogger logger = InternalLoggerFactory.getInstance(RequestMappingEntity.class);

		public RequestMappingEntity(RequestMapping mapping) {
			this.put("//_description_", mapping.description());
			this.put("//_method_", mapping.method());
			this.put("//_path_", mapping.path());
			this.put("//_request_", this.swagger(mapping.request(), mapping.reqParams()));
			this.put("//_response_", this.swagger(mapping.response(), mapping.respParams()));
		}

		private JsonObject swagger(Class<?> clazz, SwaggerProperty[] params) {
			JsonObject jsonObjectS = new JsonObject();

			if (null != params && params.length > 0) {
				for (SwaggerProperty swaggerProperty : params) {
					this.dealSwaggerProperty(swaggerProperty, jsonObjectS, null);
				}
			}

			Field[] fields = null;

			if (null == clazz || null == (fields = clazz.getDeclaredFields())) {
				return jsonObjectS;
			}
			List<Field> fieldList = new LinkedList<>();
			fieldList.addAll(Arrays.asList(fields));
			Class<?> superClass = clazz.getSuperclass();
			while (null != superClass && !superClass.equals(Object.class)) {
				fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
				superClass = superClass.getSuperclass();
			}
			fields = new Field[fieldList.size()];
			fields = fieldList.toArray(fields);
			JsonObject jsonObject = new JsonObject();
			try {

				for (Field field : fields) {
					SwaggerProperty swaggerProperty = field.getAnnotation(SwaggerProperty.class);
					this.dealSwaggerProperty(swaggerProperty, jsonObject, field);
				}
			} catch (Exception e) {
				logger.error(e);
				throw e;
			}
			jsonObjectS.mergeIn(jsonObject, false);
			return jsonObjectS;
		}

		private void dealSwaggerProperty(SwaggerProperty swaggerProperty, JsonObject jsonObject, Field field) {
			if (null == swaggerProperty) {
				return;
			}
			Class<?> fieldType = null;
			String fName = null;
			if (null != field) {
				fieldType = field.getType();
				fName = field.getName();
			}
			SwaggerCollection[] swaggerCollection = swaggerProperty.collectionType();
			Class c = null;
			JsonObject fieldObject = new JsonObject();
			Object content = null;
			if (null == swaggerCollection || 0 == swaggerCollection.length
					|| null == (c = swaggerCollection[0].value()) || c.equals(Void.class)) {
				//对象类型
				c = swaggerProperty.type();
				c = null == c || Void.class.equals(c) ? fieldType : c;
				content = this.isPrivateType(swaggerProperty.type()) ? new JsonObject() : this.swagger(c, null);
			} else {
				SwaggerEntity[] swaggerEntities = null;
				if (swaggerCollection.length <= 1) {
					swaggerEntities = swaggerCollection[0].element();
					if (null == swaggerEntities || 0 == swaggerEntities.length
							|| null == (c = swaggerEntities[0].valueType()) || c.equals(Void.class)) {
						//List<对象>类型
						c = swaggerCollection[0].value();
						JsonArray array = new JsonArray();
						content = this.isPrivateType(c) ? array : array.add(new JsonObject()
								.put("//_type_", c.getSimpleName()).put("//_content_", this.swagger(c, null)));
					} else {
						//Map<对象，对象>类型 和List<Map>类型
						JsonObject object = new JsonObject(), keyObject = null, valueObject = null;
						for (SwaggerEntity swaggerEntity : swaggerEntities) {
							keyObject = new JsonObject();
							c = swaggerEntity.keyType();
							keyObject.put("//_keyType_", c.getSimpleName());
							keyObject.put("//_key_", new JsonObject().put(
									swaggerEntity.key(), this.isPrivateType(c) ? c : this.swagger(c, null)));
							valueObject = new JsonObject();
							c = swaggerEntity.valueType();
							valueObject.put("//_valueType_", c.getSimpleName());
							valueObject.put("//_value_", new JsonObject().put(
									swaggerEntity.value(), this.isPrivateType(c) ? c : this.swagger(c, null)));
							object.put(keyObject.toString(), valueObject);
						}
						content = 0 == swaggerCollection[0].collectionIndex() ? new JsonArray().add(object) : object;
					}
				} else {
					content = this.manyCollection(swaggerCollection, 0);
				}
			}
			c = swaggerProperty.type();
			c = null == c || Void.class.equals(c) ? fieldType : c;
			fieldObject.put("//_description_", swaggerProperty.value());
			fieldObject.put("//_type_", c.getSimpleName());
			fieldObject.put("//_in_", swaggerProperty.in());
			fieldObject.put("//_content_", content);
			String fieldName = swaggerProperty.fieldName();
			fieldName = null == fieldName || "".equals(fieldName.trim()) ? fName : fieldName;
			jsonObject.put(fieldName, fieldObject);
		}

		private Object manyCollection(SwaggerCollection[] swaggerCollection, int index) {
			SwaggerEntity[] swaggerEntities = swaggerCollection[index].element();
			Class c = null;
			Object content = null;
			//List<不同对象>类型 List<List<...List<不同对象>...>>类型
			JsonArray array = new JsonArray(), currentArray = null, tmpArray = null;

			if (!this.isPrivateType(swaggerCollection[index].value())) {
				if (swaggerCollection[index].value().isAssignableFrom(List.class)) {
					currentArray = array;
					array = new JsonArray();
					currentArray.add(array);
				} else {
					array.add(this.swagger(swaggerCollection[index].value(), null));
				}
			}
			for (int i = swaggerCollection[index].collectionIndex(); i != -1;
				 i = swaggerCollection[i].collectionIndex()) {
				if (!this.isPrivateType(swaggerCollection[i].value())) {
					if (swaggerCollection[i].value().isAssignableFrom(List.class)) {
						tmpArray = new JsonArray().add(currentArray);
						currentArray = tmpArray;
					} else {
						array.add(this.swagger(swaggerCollection[i].value(), null));
					}
					if (null == swaggerEntities || 0 == swaggerEntities.length
							|| null == (c = swaggerEntities[0].valueType()) || c.equals(Void.class)) { } else {
						//Map<对象，对象>类型
						if (-1 != swaggerCollection[index].collectionIndex()) {
							JsonArray arrayMap = new JsonArray();
							JsonObject object = new JsonObject(), keyObject = null, valueObject = null;
							for (SwaggerEntity swaggerEntity : swaggerEntities) {
								keyObject = new JsonObject();
								c = swaggerEntity.keyType();
								keyObject.put("//_keyType_", c.getSimpleName());
								if (-1 == swaggerEntity.keyCollectionIndex()) {
									keyObject.put("//_key_", new JsonObject().put(
											swaggerEntity.key(), this.isPrivateType(c) ? c.getSimpleName() : this.swagger(c, null)));
								} else {
									//Map<List/Map<对象>, 对象> 类型
									keyObject.put("//_key_", this.manyCollection(
											swaggerCollection, swaggerEntity.keyCollectionIndex()));
								}
								valueObject = new JsonObject();
								c = swaggerEntity.valueType();
								valueObject.put("//_valueType_", c.getSimpleName());
								if (-1 == swaggerEntity.valueCollectionIndex()) {
									valueObject.put("//_value_", new JsonObject().put(
											swaggerEntity.value(), this.isPrivateType(c) ? c.getSimpleName() : this.swagger(c, null)));
								} else {
									//Map<对象, List/Map<对象>> 类型
									valueObject.put("//_value_", this.manyCollection(
											swaggerCollection, swaggerEntity.valueCollectionIndex()));
								}
								object.put(keyObject.toString(), valueObject);

							}
							arrayMap.add(object);
							array = null == currentArray ? array : currentArray;
							array.add(arrayMap);
							currentArray = null;
						}
					}
				}
			}
			array = null == currentArray ? array : currentArray;
			content = array;

			return content;
		}

		private boolean isPrivateType(Class<?> clazz) {
			return null != clazz && (clazz.equals(Integer.class) || clazz.isPrimitive() ||
					clazz.equals(String.class) || clazz.equals(StringBuilder.class) ||
					clazz.equals(StringBuffer.class));
		}

	}
}
