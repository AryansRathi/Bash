/*
 * Copyright 2000-2018 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.build;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;


/**
 * @author <a href="mailto:Alexander.Veit@unitedplanet.de">Alexander Veit</a>
 */
public final class OnlineHelpDoclet implements Doclet
{
	@SuppressWarnings("unused")
	private Locale m_locale;

	@SuppressWarnings("unused")
	private Reporter m_reporter;


	public OnlineHelpDoclet()
	{
		System.out.println("Create OnlineHelpDoclet instance.");
	}


	@Override
	public String getName()
	{
		return "OnlineHelpDoclet";
	}


	@Override
	public Set<? extends Option> getSupportedOptions()
	{
		return Collections.emptySet();
	}


	@Override
	public SourceVersion getSupportedSourceVersion()
	{
		return SourceVersion.RELEASE_9;
	}


	@Override
	public void init(Locale p_locale, Reporter p_reporter)
	{
		System.out.println("Initialize OnlineHelpDoclet.");

		m_locale   = p_locale;
		m_reporter = p_reporter;
	}


	@Override
	public boolean run(DocletEnvironment p_environment)
	{
		final Path        l_dirTodo;
		final Path        l_dirDone;
		final PrintWriter l_pw;

		System.out.println("Run OnlineHelpDoclet.");

		l_dirTodo = _getTodoDirectory();
		l_dirDone = _getDoneDirectory();

		try
		{
			Files.createDirectories(l_dirTodo);
			Files.createDirectories(l_dirDone);
		}
		catch (IOException l_e)
		{
			throw new UncheckedIOException(l_e);
		}

		l_pw = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

		for (final TypeElement l_element : ElementFilter.typesIn(p_environment.getIncludedElements()))
		{
			new TypeHelpTextExtractor(p_environment, l_element, l_dirTodo, l_dirDone, l_pw).run();
		}

		System.out.println("Finished running OnlineHelpDoclet.");

		return true;
	}


	private static Path _getTodoDirectory()
	{
		final String l_strDir;
		final Path   l_dir;

		l_strDir = System.getProperty("build.help.todo.dir");

		if (l_strDir == null)
			throw new IllegalStateException("System property build.help.todo.dir not specified.");

		l_dir = Paths.get(l_strDir);

		if (!Files.isDirectory(l_dir))
		{
			try
			{
				Files.createDirectories(l_dir);
			}
			catch (IOException l_e)
			{
				throw new UncheckedIOException(l_e);
			}
		}

		return l_dir;
	}


	private static Path _getDoneDirectory()
	{
		final String l_strDir;
		final Path   l_dir;

		l_strDir = System.getProperty("build.help.done.dir");

		if (l_strDir == null)
			throw new IllegalStateException("System property build.help.done.dir not specified.");

		l_dir = Paths.get(l_strDir);

		if (!Files.isDirectory(l_dir))
		{
			try
			{
				Files.createDirectories(l_dir);
			}
			catch (IOException l_e)
			{
				throw new UncheckedIOException(l_e);
			}
		}

		return l_dir;
	}
}
