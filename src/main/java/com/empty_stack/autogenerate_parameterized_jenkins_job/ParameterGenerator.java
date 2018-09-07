package com.empty_stack.autogenerate_parameterized_jenkins_job;

/**
 * 
 * @author Maik Fröbe
 *
 */
public class ParameterGenerator
{
	public static void parameters(Object o)
	{
		System.out.println(nullSaveClassOfObject(o));
	}
	
	public static String nullSaveClassOfObject(Object o)
	{
		return o == null ? "null was passed" : o.getClass().toString();
	}
}
