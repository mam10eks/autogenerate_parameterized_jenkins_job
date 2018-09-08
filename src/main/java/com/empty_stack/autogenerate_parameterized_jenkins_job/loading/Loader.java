package com.empty_stack.autogenerate_parameterized_jenkins_job.loading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Singular;

/**
 * 
 * @author Maik Fröbe
 *
 */
@Builder
public class Loader
{
	@Singular
	private Set<File> classResourceFiles;
	
	@Singular
	private Set<String> classNames;
	
	public List<Class<?>> loadClasses()
	{
		URLClassLoader classLoader = createClassLoaderForClassResourceFilesOrFail();
		failIfClassNamesAreNotValid();
		
		return classNames.stream()
				.map(className -> loadClassOrFail(classLoader, className))
				.collect(Collectors.toList());
	}
	
	private URLClassLoader createClassLoaderForClassResourceFilesOrFail()
	{
		if(classResourceFiles == null || classResourceFiles.isEmpty())
		{
			throw new IllegalArgumentException("A Loader needs to have some resources "
				+ "where classes should be loaded. Got: " + classResourceFiles);
		}
		
		URL[] urls = mapToUrlArrayOrFail(classResourceFiles);
		return new URLClassLoader(urls);
	}
	
	private Class<?> loadClassOrFail(URLClassLoader classLoader, String className)
	{
		try
		{
			return classLoader.loadClass(className);
		}
		catch(ClassNotFoundException exception)
		{
			throw new RuntimeException("Could not load class '"+ className
					+"' from resources located in " + classResourceFiles);
		}
	}
	
	private static URL[] mapToUrlArrayOrFail(Set<File> classResourceFiles)
	{
		return classResourceFiles.stream()
				.map(Loader::mapFileToUrlOrFail)
				.toArray(size -> new URL[size]);
	}
	
	private static URL mapFileToUrlOrFail(File file)
	{
		if(file == null)
		{
			throw new IllegalArgumentException("A class-resource-file could not be null");
		}
		else if(!file.exists())
		{
			throw new IllegalArgumentException("Couldn't retrieve the file '"+
					file.getAbsolutePath()
					+"' since it does not exist.");
		}
		
		try
		{
			return file.toURI().toURL();
		}
		catch(MalformedURLException exception)
		{
			throw new IllegalArgumentException("Couldn't transform the file '"+
					file.getAbsolutePath()
					+"' to a url.", exception);
		}
	}
	
	private void failIfClassNamesAreNotValid()
	{
		if(classNames == null || classNames.isEmpty())
		{
			throw new IllegalArgumentException("A Loader needs to have some class-names "
					+ "which should be loaded. Got: "+ classNames);
		}
	}
}
