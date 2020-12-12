package com.bgi.vtx.annotation;

import java.lang.annotation.*;

;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SwaggerProperty {
	String value() default "";
	String fieldName() default "";
	String in() default BODY;
	Class type() default Void.class;
	SwaggerCollection[] collectionType() default { @SwaggerCollection(Void.class)};

	public static final String HEADER = "header";
	public static final String BODY = "body";
	public static final String PARAMS = "params";

    public @interface SwaggerEntity {
		String key() default "";
		Class keyType() default Void.class;
		int keyCollectionIndex() default -1;
		String value() default "";
		Class valueType() default Void.class;
		int valueCollectionIndex() default -1;
	}

	public @interface SwaggerCollection {
		Class value() default Void.class;
		int collectionIndex() default -1;
		SwaggerEntity[] element() default @SwaggerEntity;
	}
}