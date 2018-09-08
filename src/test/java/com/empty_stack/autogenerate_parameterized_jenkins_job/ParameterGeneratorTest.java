package com.empty_stack.autogenerate_parameterized_jenkins_job;

import org.apache.commons.lang3.tuple.Pair;
import org.approvaltests.Approvals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.Data;

import java.util.ArrayList;
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
	public static class TestClass4
	{
		private String firstParam = "dsads";

		private String secondParam;
	}
	
	@Data
	public static class TestClass5
	{
		private Integer firstParam;

		private Integer secondParam = 112;
		
		private int thirdParam;

		private Integer fourthParam = -112;
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
			.textParam(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
		
		return parametersContext;
	}
	
	private static Object addToParameters(InvocationOnMock invocation)
	{
		Pair<String, Object> pair = Pair.of((String) invocation.getArguments()[0], invocation.getArguments()[1]); 
		parameters.add(pair);
		
		return pair.getValue();
	}
}
