package com.empty_stack.autogenerate_parameterized_jenkins_job.loading;

import java.io.File;
import java.net.URI;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

/**
 * 
 * @author Maik Fröbe
 *
 */
public class IllegalLoaderUsageTest
{
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void checkThatNiceExceptionIsThrownForNonDefinedFiles()
	{
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("A Loader needs to have some resources where classes should be loaded. Got: []");
		
		Loader.builder().build().loadClasses();
	}
	
	@Test
	public void checkThatNiceExceptionIsThrownForNonExistingFileAtTheStart()
	{
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Couldnt retrieve the file '/bla' since it does not exist.");
		
		Loader.builder()
			.classResourceFile(file("/bla", false))
			.classResourceFile(file("/ba", true))
			.build()
			.loadClasses();
	}
	
	@Test
	public void checkThatNiceExceptionIsThrownForNonExistingFileAtTheEnd()
	{
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Couldnt retrieve the file '/doesNotExist' since it does not exist.");
		
		Loader.builder()
			.classResourceFile(file("/bla", true))
			.classResourceFile(file("/doesNotExist", false))
			.build()
			.loadClasses();
	}
	
	@Test
	public void checkThatNiceExceptionIsThrownForNonDefinedClassNames()
	{
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("A Loader needs to have some class-names which should be loaded. Got: []");
		
		Loader.builder()
			.classResourceFile(file("/bla", true))
			.build()
			.loadClasses();
	}
	
	@Test
	public void checkThatNiceExceptionIsThrownForNonExistingClass()
	{
		exception.expect(RuntimeException.class);
		exception.expectMessage("Could not load class 'DoesNotExist' from resources locatet in");
		exception.expectMessage("src/test/resources/example_classes/no_classes");
		
		Loader.builder()
			.classResourceFile(LoaderResources.NO_EXISTING_CLASSES)
			.className("DoesNotExist")
			.build()
			.loadClasses();
	}
	
	private static File file(String absolutePath, boolean exists)
	{
		File ret = Mockito.mock(File.class);
		Mockito.when(ret.getAbsolutePath()).thenReturn(absolutePath);
		Mockito.when(ret.exists()).thenReturn(exists);
		Mockito.when(ret.toURI()).thenReturn(URI.create("file://"+ absolutePath));
		
		return ret;
	}
}
