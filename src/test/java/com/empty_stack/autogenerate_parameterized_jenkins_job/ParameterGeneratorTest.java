package com.empty_stack.autogenerate_parameterized_jenkins_job;

import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;
import org.approvaltests.Approvals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.empty_stack.autogenerate_parameterized_jenkins_job.loading.LoaderResources;

import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterGeneratorTest
{
	private static List<Pair<String, Object>> parameters;
	
	private static BuildParametersContext context;
	
	@Before
	public void setup()
	{
		parameters = new ArrayList<>();
		context = mock();
	}
	
	@Data
	public static class TestClass
	{
		private boolean bla = true;
	}

	@Data
	public static class TestClass2
	{
		private boolean bla;
	}
	
	@Data
	public static class TestClass3
	{
		private boolean firstParam;
		
		private boolean secondParam = false;
		
		private boolean thirdParam = true;
	}

	@Data
	@Accessors(chain = true)
	public static class TestClass4
	{
		private String firstParam = "dsads";

		private String secondParam;
	}
	
	@Data
	@Accessors(chain = true)
	public static class TestClass5
	{
		private Integer firstParam;

		private Integer secondParam = 112;
		
		private int thirdParam;

		private Integer fourthParam = -112;
	}

	@Data
	public static class TestClass6
	{
		private TestClass5 firstParam;
		private TestClass5 secondParam = new TestClass5()
				.setFirstParam(1)
				.setSecondParam(2)
				.setThirdParam(3)
				.setFourthParam(42);
	}
	
	@Data
	@ConfigurationProperties("a-b-c")
	public static class TestClass7
	{
		private Integer firstParam = 111;
		
		private TestClass4 secondParam;
		
		private TestClass4 thirdParam = new TestClass4()
				.setFirstParam(null)
				.setSecondParam("second");
		
		private Integer fourthParam;
	}
	
	@Test
	public void approveParameterGenerationForTestClass()
	{
		approveCreatedParametersForClass(TestClass.class);
	}

	@Test
	public void approveParameterGenerationForTestClass2()
	{
		approveCreatedParametersForClass(TestClass2.class);
	}
	
	@Test
	public void approveParameterGenerationForTestClass3()
	{
		approveCreatedParametersForClass(TestClass3.class);
	}

	@Test
	public void approveParameterGenerationForTestClass4()
	{
		approveCreatedParametersForClass(TestClass4.class);
	}
	
	@Test
	public void approveParameterGenerationForTestClass5()
	{
		approveCreatedParametersForClass(TestClass5.class);
	}

	@Test
	public void approveParameterGenerationForTestClass6()
	{
		approveCreatedParametersForClass(TestClass6.class);
	}

	@Test
	public void approveParameterGenerationForTestClass7()
	{
		approveCreatedParametersForClass(TestClass7.class);
	}
	
	@Test
	public void bigIntegrationTestWithRealJar()
	{
		List<File> classResources = Arrays.asList(LoaderResources.EXAMPLE_JAR);
		List<String> classes = Arrays.asList("a.MyExample");
		
		ParameterGenerator parameterGenerator = new ParameterGenerator(classResources, classes);
		parameterGenerator.setDelegate(context);
		parameterGenerator.call();
		
		Approvals.verifyAsJson(parameters);
	}

	@Test
	public void bigIntegrationTestWithLargeRealJar()
	{
		List<File> classResources = Arrays.asList(LoaderResources.EXAMPLE_JAR);
		List<String> classes = Arrays.asList("b.MyExample");
		
		ParameterGenerator parameterGenerator = new ParameterGenerator(classResources, classes);
		parameterGenerator.setDelegate(context);
		parameterGenerator.call();
		
		Approvals.verifyAsJson(parameters);
	}
	
	@Test
	public void bigIntegrationTestWithJarWithEmbeddedJar()
	{
		List<File> classResources = Arrays.asList(LoaderResources.EXAMPLE_JAR_WITH_EMBEDDED_JAR);
		List<String> classes = Arrays.asList("b.MyExample");
		
		ParameterGenerator parameterGenerator = new ParameterGenerator(classResources, classes);
		parameterGenerator.setDelegate(context);
		parameterGenerator.call();
		
		Approvals.verifyAsJson(parameters);
	}
	
	private static void approveCreatedParametersForClass(Class<?> clazz)
	{
		ParameterGenerator.addClassParametersToContext(clazz, context);
		Approvals.verifyAsJson(parameters);
	}
	
	private static BuildParametersContext mock() {
		BuildParametersContext parametersContext = Mockito.mock(BuildParametersContext.class);
		
		Mockito
			.doAnswer(ParameterGeneratorTest::addToParameters)
			.when(parametersContext)
			.booleanParam(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
		
		Mockito
			.doAnswer(ParameterGeneratorTest::addToParameters)
			.when(parametersContext)
			.textParam(ArgumentMatchers.anyString(), ArgumentMatchers.any());
		
		return parametersContext;
	}
	
	private static Object addToParameters(InvocationOnMock invocation)
	{
		Pair<String, Object> pair = Pair.of((String) invocation.getArguments()[0], invocation.getArguments()[1]); 
		parameters.add(pair);
		
		return pair.getValue();
	}
}
