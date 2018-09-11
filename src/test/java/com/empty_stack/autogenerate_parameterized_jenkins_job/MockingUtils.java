package com.empty_stack.autogenerate_parameterized_jenkins_job;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.experimental.UtilityClass;

/**
 * 
 * @author Maik Fröbe
 *
 */
@UtilityClass
public class MockingUtils
{
	public static BuildParametersContext mockBuildParametersContext(List<Pair<String, Object>> parameters)
	{
		BuildParametersContext parametersContext = Mockito.mock(BuildParametersContext.class);
		
		Mockito
			.doAnswer(invogation -> addToParameters(invogation, parameters))
			.when(parametersContext)
			.booleanParam(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
		
		Mockito
			.doAnswer(invogation -> addToParameters(invogation, parameters))
			.when(parametersContext)
			.textParam(ArgumentMatchers.anyString(), ArgumentMatchers.any());
		
		return parametersContext;
	}
	
	private static Object addToParameters(InvocationOnMock invocation, List<Pair<String, Object>> parameters)
	{
		Pair<String, Object> pair = Pair.of((String) invocation.getArguments()[0], invocation.getArguments()[1]); 
		parameters.add(pair);
		
		return pair.getValue();
	}
}
