package com.empty_stack.autogenerate_parameterized_jenkins_job.loading;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

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
		ClassLoader classLoader = createClassLoaderForClassResourceFilesOrFail();
		failIfClassNamesAreNotValid();
		
		return classNames.stream()
				.map(className -> loadClassOrFail(classLoader, className))
				.collect(Collectors.toList());
	}
	
	private ClassLoader createClassLoaderForClassResourceFilesOrFail()
	{
		if(classResourceFiles == null || classResourceFiles.isEmpty())
		{
			throw new IllegalArgumentException("A Loader needs to have some resources "
				+ "where classes should be loaded. Got: " + classResourceFiles);
		}
		
		URL[] urls = mapToUrlArrayOrFail(classResourceFiles);
		
		return new URLClassLoader(urls);
	}
	
	private Class<?> loadClassOrFail(ClassLoader classLoader, String className)
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
				.map(Loader::mapResourceFiles)
				.flatMap(List::stream)
				.map(Loader::mapFileToUrlsOrFail)
				.toArray(size -> new URL[size]);
	}
	
	private static URL mapFileToUrlsOrFail(File file)
	{
		failIfFileIsNoValidSource(file);
		
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
	
	private static void failIfFileIsNoValidSource(File file)
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
	}
	
	private static boolean isJarFile(File file)
	{
		return file.isFile() && file.getAbsolutePath().endsWith(".jar");
	}
	
	private static boolean isJarFile(JarEntry jarEntry)
	{
		return !jarEntry.isDirectory() && jarEntry.getName().endsWith(".jar");
	}
	
	private static List<File> mapResourceFiles(File file)
	{
		failIfFileIsNoValidSource(file);
		
		if(isJarFile(file))
		{
			try(JarFile jarFile = new JarFile(file))
			{
				List<File> ret = new ArrayList<>();
				ret.add(file);
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				
				while(jarEntries.hasMoreElements())
				{
					JarEntry jarEntry = jarEntries.nextElement();
					
					if(isJarFile(jarEntry))
					{
						ret.addAll(mapResourceFiles(writeToTmpFile(jarFile, jarEntry)));
					}
				}
				
				return ret;
			}
			catch(IOException exception)
			{
				throw new RuntimeException("Couldnt analyse jar-file '"+ file.getAbsolutePath() +"'.", exception);
			}
		}
		
		return Arrays.asList(file);
	}
	
	private static File writeToTmpFile(JarFile jarFile, JarEntry jarEntry) throws IOException
	{
		File tmpFile = File.createTempFile("tmp_loader", ".jar");
		tmpFile.deleteOnExit();
		
		FileUtils.copyInputStreamToFile(jarFile.getInputStream(jarEntry), tmpFile);
		
		return tmpFile;
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
