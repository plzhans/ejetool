package com.ejetool.account.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HasRole {

	/**
	 * Returns the list of security configuration attributes (e.g.&nbsp;ROLE_USER,
	 * ROLE_ADMIN).
	 * @return String[] The secure method attributes
	 */
	String[] value();

}
