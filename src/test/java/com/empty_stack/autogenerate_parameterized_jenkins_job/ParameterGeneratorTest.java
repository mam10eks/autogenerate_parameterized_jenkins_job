package com.empty_stack.autogenerate_parameterized_jenkins_job;

import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;
import org.approvaltests.Approvals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javaposse.jobdsl.dsl.helpers.BuildParametersContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Maik Fröbe
 *
 */
public class ParameterGeneratorTest
{
	private static List<Pair<String, Object>> parameters;
	
	private static BuildParametersContext context;
	
	@Before
	public void setup()
	{
		parameters = new ArrayList<>();
		context = MockingUtils.mockBuildParametersContext(parameters);
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
	
	@Data
	public static class TestClass8
	{
		private Long firstLong = 21l;

		private Long secondLong;
		
		private long thirdLong;
		
		private long fourthLong = 11;
		
		private Double firstDouble = 1.1;
		
		private Double secondDouble;
		
		private double thirdDouble;
		
		private double fourthDouble = 4;
		
		private Float firstFloat = 2.2f;
		
		private Float secondFloat;
		
		private float thirdFloat;
		
		private float fifthFloat = 2.3f;
	}
	
//	@Data
//	public static class TestClass9
//	{
//		private File firstFile;
//		
//		private File secondFile = new File("a");
//		
//		private File thirdFile = new File("b");
//	}
	
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
	public void approveParemeterGenerationForTestClass8()
	{
		approveCreatedParametersForClass(TestClass8.class);
	}
	
//	@Test
//	public void approveParemeterGenerationForTestClass9()
//	{
//		approveCreatedParametersForClass(TestClass9.class);
//	}
	
	private static void approveCreatedParametersForClass(Class<?> clazz)
	{
		ParameterGenerator.addClassParametersToContext(clazz, context);
		Approvals.verifyAsJson(parameters);
	}
}
