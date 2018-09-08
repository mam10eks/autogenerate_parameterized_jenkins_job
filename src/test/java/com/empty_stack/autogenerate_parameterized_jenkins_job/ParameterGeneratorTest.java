package com.empty_stack.autogenerate_parameterized_jenkins_job;

import org.approvaltests.Approvals;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class ParameterGeneratorTest
{
	@Data
	public static class TestClass
	{
		private boolean bla = true;
	}
	
	@Test
	public void approveParameterGenerationForTestClass()
	{
		Map<String, Object> map = new HashMap<>();
		BuildParametersContext parametersContext = mock(map);
		ParameterGenerator.addClassParametersToContext(TestClass.class, parametersContext);

		Approvals.verifyAsJson(map);
	}

	private BuildParametersContext mock(Map<String, Object> paramValues) {
		BuildParametersContext parametersContext = Mockito.mock(BuildParametersContext.class);
		Mockito
				.doAnswer(context -> paramValues.put((String) context.getArguments()[0], context.getArguments()[1]))
				.when(parametersContext)
				.booleanParam(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
		return parametersContext;
	}
}
