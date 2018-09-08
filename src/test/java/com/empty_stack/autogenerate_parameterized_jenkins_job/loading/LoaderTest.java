package com.empty_stack.autogenerate_parameterized_jenkins_job.loading;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Maik Fröbe
 *
 */
public class LoaderTest
{
	@Test
	public void checkThatClassWithSingleBooleanAndNoJavadocCouldBeLoaded()
	{
		Loader loader = Loader.builder()
				.className("MyExample")
				.classResourceFile(LoaderResources.CLASS_WITH_SINGLE_EXISTING_BOOLEAN_AND_NO_JAVADOC)
				.build();
		
		List<Class<?>> clazzes = loader.loadClasses();
		Assert.assertEquals(1, clazzes.size());

		Assert.assertEquals("MyExample", clazzes.get(0).getName());
	}
	
	@Test
	public void checkThatClassWithTwoBooleansAndSingleStringAndNoJavadocCouldBeLoaded()
	{
		Loader loader = Loader.builder()
				.className("MyExample")
				.classResourceFile(LoaderResources.CLASS_WITH_TWO_BOOLEANS_AND_SINGLE_STRING_AND_NO_JAVADOC)
				.build();
		
		List<Class<?>> clazzes = loader.loadClasses();
		Assert.assertEquals(1, clazzes.size());

		Assert.assertEquals("MyExample", clazzes.get(0).getName());
	}
	
	@Test
	public void checkThatExampleJarBeLoaded()
	{
		Loader loader = Loader.builder()
				.className("a.MyExample")
				.classResourceFile(LoaderResources.EXAMPLE_JAR)
				.build();
		
		List<Class<?>> clazzes = loader.loadClasses();
		Assert.assertEquals(1, clazzes.size());

		Assert.assertEquals("a.MyExample", clazzes.get(0).getName());
	}
}
