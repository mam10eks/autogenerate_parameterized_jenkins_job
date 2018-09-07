package com.empty_stack.autogenerate_parameterized_jenkins_job;

import groovy.lang.Closure;
import javaposse.jobdsl.dsl.Job;
import javaposse.jobdsl.dsl.jobs.FreeStyleJob;

/**
 * 
 * @author Maik Fröbe
 *
 */
public class ParameterGenerator
{
	@SuppressWarnings("serial")
	public static Closure<Void> parameters(Object o)
	{
		System.out.println(nullSaveClassOfObject(o));
		return new Closure<Void>(null)
		{
			@Override
			public Void call()
			{
				System.out.println(nullSaveClassOfObject(getDelegate()));
				
				return null;
			}
		};
	}
	
	public static String nullSaveClassOfObject(Object o)
	{
		return o == null ? "null was passed" : o.getClass().toString();
	}
}
