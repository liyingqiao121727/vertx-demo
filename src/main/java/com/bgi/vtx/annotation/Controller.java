package com.bgi.vtx.annotation;

import io.vertx.core.json.JsonObject;

import java.lang.annotation.*;
import java.util.UUID;

/**
 * 功能与Spring的Controller注解类似，在vertx框架上的新实现
 *
 * @author 李英乔
 * @since 2019-06-17
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

	/**
	 * 标记@Controller注解的所在类
	 */
	Class<?> value();

	/**
	 * 将标记@Controller注解所在的类命名，可在swagger上面显示
	 */
	String name() default "未命名";

	/**
	 * 将标记@Controller注解所在的类进行描述，可在swagger上面显示
	 */
	String description() default "暂无描述";

	/**
	 * 获取@Controller注解，将其显示在自定义的swagger上
	 *
	 * @author 李英乔
	 * @since 2019-06-17
	 */
	public class ControllerEntity extends JsonObject {
		public ControllerEntity(Controller controller) {
			this.put("//_class_", controller.value().getName());
			this.put("//_name_", controller.name());
			this.put("//_description_", controller.description());
		}
	}
}
