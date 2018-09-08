package com.empty_stack.autogenerate_parameterized_jenkins_job;

import org.approvaltests.Approvals;
import org.junit.Test;
import org.mockito.Mockito;

import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.Data;

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
		BuildParametersContext parametersContext = Mockito.mock(BuildParametersContext.class);
		ParameterGenerator.addClassParametersToContext(TestClass.class, parametersContext);
		
		Approvals.verify(parametersContext);
	}
}
