package com.empty_stack.autogenerate_parameterized_jenkins_job.loading;

import java.io.File;
import java.nio.file.Paths;

import lombok.experimental.UtilityClass;

/**
 * 
 * @author Maik Fröbe
 *
 */
@UtilityClass
public class LoaderResources
{
	public static final File NO_EXISTING_CLASSES = preparedTestResource("no_classes");
	
	public static final File CLASS_WITH_SINGLE_EXISTING_BOOLEAN_AND_NO_JAVADOC = preparedTestResource("class_with_single_boolean_and_no_javadoc");
	
	public static final File CLASS_WITH_TWO_BOOLEANS_AND_SINGLE_STRING_AND_NO_JAVADOC = preparedTestResource("class_with_two_booleans_single_string_and_no_javadoc");
	
	private static File preparedTestResource(String testResource)
	{
		return Paths.get("src")
			.resolve("test")
			.resolve("resources")
			.resolve("example_classes")
			.resolve(testResource)
			.toFile();
	}
}
