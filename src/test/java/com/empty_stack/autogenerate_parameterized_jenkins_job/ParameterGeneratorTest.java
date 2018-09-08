package com.empty_stack.autogenerate_parameterized_jenkins_job;

import org.approvaltests.Approvals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class ParameterGeneratorTest
{
	private static Map<String, Object> parameters;
	
	private static BuildParametersContext context;
	
	@Before
	public void setup()
	{
		parameters = new HashMap<>();
		context = mock(parameters);
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

	private static void approveCreatedParametersForClass(Class<?> clazz)
	{
		ParameterGenerator.addClassParametersToContext(clazz, context);
		Approvals.verifyAsJson(parameters);
	}
	
	private static BuildParametersContext mock(Map<String, Object> paramValues) {
		BuildParametersContext parametersContext = Mockito.mock(BuildParametersContext.class);
		Mockito
				.doAnswer(context -> paramValues.put((String) context.getArguments()[0], context.getArguments()[1]))
				.when(parametersContext)
				.booleanParam(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
		return parametersContext;
	}
}
