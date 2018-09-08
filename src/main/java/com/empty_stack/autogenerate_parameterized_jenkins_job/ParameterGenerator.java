package com.empty_stack.autogenerate_parameterized_jenkins_job;

import groovy.lang.Closure;
import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * 
 * @author Maik Fröbe
 *
 */
public class ParameterGenerator
{
	@SuppressWarnings("serial")
	public static Closure<Void> generate()
	{
		return new Closure<Void>(null)
		{
			@Override
			public Void call()
			{
				BuildParametersContext context = getBuildParametersContextOrFail();
				
				context.booleanParam("First Boolean Param", false, "description 1");
				context.booleanParam("second Boolean Param", true, "description 2");
				
				return null;
			}
			
			private BuildParametersContext getBuildParametersContextOrFail()
			{
				if(getDelegate() instanceof BuildParametersContext)
				{
					return (BuildParametersContext) getDelegate();
				}
				
				throw new RuntimeException("Illegal usage: Please use this as closure for parameters...");
			}
		};
	}

	@SneakyThrows
	static void addClassParametersToContext(Class<?> clazz, BuildParametersContext buildParametersContext) {
		for(Field field: clazz.getDeclaredFields()) {
			if(field.getType().getName().equalsIgnoreCase("boolean")){
				field.setAccessible(true);
				boolean b = (boolean) field.get(clazz.newInstance());
				buildParametersContext.booleanParam(field.getName(), b);
			}
			else if(field.getType().isAssignableFrom(String.class)) {
				field.setAccessible(true);
				String value = (String) field.get(clazz.newInstance());
				buildParametersContext.textParam(field.getName(), value);
			} else if(field.getType().isAssignableFrom(Integer.class) || field.getType().getName().equalsIgnoreCase("int")) {
				field.setAccessible(true);
				Integer value = (Integer) field.get(clazz.newInstance());
				buildParametersContext.textParam(field.getName(), value == null ? null : String.valueOf(value));
			}
		}
		
	}
}
