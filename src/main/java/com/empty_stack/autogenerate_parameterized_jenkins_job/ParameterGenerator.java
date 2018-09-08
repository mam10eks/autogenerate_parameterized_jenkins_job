package com.empty_stack.autogenerate_parameterized_jenkins_job;

import groovy.lang.Closure;
import javaposse.jobdsl.dsl.helpers.BuildParametersContext;

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

	static void addClassParametersToContext(Class<?> clazz, BuildParametersContext buildParametersContext) {
		if("TestClass".equals(clazz.getSimpleName()))
		{
			buildParametersContext.booleanParam("bla", true);
		}
		else
		{
			buildParametersContext.booleanParam("bla", false);			
		}
		
	}
}
