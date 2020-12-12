package com.bgi.vtx.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
	String value();
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Autowired { }
}
