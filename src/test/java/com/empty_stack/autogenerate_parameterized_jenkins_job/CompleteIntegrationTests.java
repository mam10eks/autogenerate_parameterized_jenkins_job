package com.empty_stack.autogenerate_parameterized_jenkins_job;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.approvaltests.Approvals;
import org.junit.Before;
import org.junit.Test;

import com.empty_stack.autogenerate_parameterized_jenkins_job.loading.LoaderResources;

import javaposse.jobdsl.dsl.helpers.BuildParametersContext;

/**
 * 
 * @author Maik Fröbe
 *
 */
public class CompleteIntegrationTests
{
	private static List<Pair<String, Object>> parameters;
	
	private static BuildParametersContext context;
	
	@Before
	public void setup()
	{
		parameters = new ArrayList<>();
		context = MockingUtils.mockBuildParametersContext(parameters);
	}
	
	@Test
	public void bigIntegrationTestWithRealJar()
	{
		executeApprovalTestForClassResourcesAndClasses(
				Arrays.asList(LoaderResources.EXAMPLE_JAR),
				Arrays.asList("a.MyExample"));
	}

	@Test
	public void bigIntegrationTestWithLargeRealJar()
	{
		executeApprovalTestForClassResourcesAndClasses(
				Arrays.asList(LoaderResources.EXAMPLE_JAR),
				Arrays.asList("b.MyExample"));
	}
	
	@Test
	public void bigIntegrationTestWithJarWithEmbeddedJar()
	{
		executeApprovalTestForClassResourcesAndClasses(
				Arrays.asList(LoaderResources.EXAMPLE_JAR_WITH_EMBEDDED_JAR),
				Arrays.asList("b.MyExample"));
	}
	
	private static void executeApprovalTestForClassResourcesAndClasses(List<File> classResources, List<String> classes)
	{
		ParameterGenerator parameterGenerator = new ParameterGenerator(classResources, classes);
		parameterGenerator.setDelegate(context);
		parameterGenerator.call();
		
		Approvals.verifyAsJson(parameters);
	}
}
