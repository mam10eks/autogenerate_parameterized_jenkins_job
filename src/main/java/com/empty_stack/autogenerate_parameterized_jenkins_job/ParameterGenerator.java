package com.empty_stack.autogenerate_parameterized_jenkins_job;

import groovy.lang.Closure;
import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import com.empty_stack.autogenerate_parameterized_jenkins_job.loading.Loader;

/**
 * 
 * @author Maik Fröbe, Auryn Engel
 *
 */
@SuppressWarnings("serial")
public class ParameterGenerator extends Closure<Void>
{
	List<File> javaResources;
	
	List<String> clazzes;
	
	public ParameterGenerator(List<File> javaResources, List<String> clazzes)
	{
		super(null);
		
		this.javaResources = javaResources;
		this.clazzes = clazzes;
	}
	
	@Override
	public Void call()
	{
		BuildParametersContext context = getBuildParametersContextOrFail();
		Loader loader = Loader.builder()
				.classNames(clazzes)
				.classResourceFiles(javaResources)
				.build();
		
		List<Class<?>> parsedClazzes = loader.loadClasses();
		
		parsedClazzes.stream().forEach(clazz -> addClassParametersToContext(clazz, context));
		
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

	@SneakyThrows
	static void addClassParametersToContext(Class<?> clazz, BuildParametersContext context)
	{
		Annotation[] annotations = clazz.getAnnotations();
		String prefix = null;
		for(Annotation annotation: annotations){
			if(annotation.annotationType().getName().equals("org.springframework.boot.context.properties.ConfigurationProperties")) {
				Method m = annotation.annotationType().getMethod("value");
				prefix = (String) m.invoke(annotation);
			}
		}
		for(Triple<Field, String, Object> fieldWithNameAndDefaultValue : determineNameAndValueOfField(prefix, clazz, clazz.newInstance()))
		{
			String name = fieldWithNameAndDefaultValue.getMiddle();
			Object value = fieldWithNameAndDefaultValue.getRight();
			
			if(fieldWithNameAndDefaultValue.getLeft().getType().getName().equalsIgnoreCase("boolean"))
			{
				context.booleanParam(name ,(boolean) value);
			}
			else
			{
				context.textParam(name, value == null ? null : String.valueOf(value));
			}
		}
	}

	@SneakyThrows
	private static List<Triple<Field, String, Object>> determineNameAndValueOfField(String prefix, Class<?> clazz, Object instance)
	{
		List<Triple<Field, String, Object>> ret = new ArrayList<>();
		
		for(Field field: clazz.getDeclaredFields())
		{
			if(Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()))
			{
				continue;
			}
			
			String fieldName = (prefix == null || prefix.isEmpty() ? "" : prefix+".") + field.getName();
			field.setAccessible(true);
			Object value = field.get(instance);
			
			if(field.getType().getName().equalsIgnoreCase("boolean"))
			{
				ret.add(Triple.of(field, fieldName, (boolean) value));
			}
			else if(field.getType().isAssignableFrom(String.class))
			{
				ret.add(Triple.of(field, fieldName, (String) value));
			}
			else if(field.getType().isAssignableFrom(Integer.class) || field.getType().getName().equalsIgnoreCase("int"))
			{
				ret.add(Triple.of(field, fieldName, (Integer) value));
			}
			else if(field.getType().isAssignableFrom(Long.class) || field.getType().getName().equalsIgnoreCase("long"))
			{
				ret.add(Triple.of(field, fieldName, (Long) value));
			}
			else if(field.getType().isAssignableFrom(Double.class) || field.getType().getName().equalsIgnoreCase("double"))
			{
				ret.add(Triple.of(field, fieldName, (Double) value));
			}
			else if(field.getType().isAssignableFrom(Float.class) || field.getType().getName().equalsIgnoreCase("float"))
			{
				ret.add(Triple.of(field, fieldName, (Float) value));
			}
			else
			{
				value = value == null ? field.getType().newInstance() : value;

				ret.addAll(determineNameAndValueOfField(fieldName, field.getType(), value));
			}
		}
		
		return ret;
	}
}
