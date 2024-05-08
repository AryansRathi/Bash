/*
 * Copyright 2000-2018 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.build;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.SortedMap;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import de.uplanet.build.io.NoopOutputStream;
import de.uplanet.build.util.TextUtil;
import jdk.javadoc.doclet.DocletEnvironment;


/**
 * @author <a href="mailto:Alexander.Veit@unitedplanet.de">Alexander Veit</a>
 */
public final class TypeHelpTextExtractor
{
	private static final char[] LF = new char[] {'\n'};

	private static final PrintWriter NOOPPW = new PrintWriter(new NoopOutputStream());

	private final DocletEnvironment m_environment;

	private final TypeElement m_element;

	private final Path m_dirTodo;

	private final Path m_dirDone;

	private final PrintWriter m_log;


	public TypeHelpTextExtractor(DocletEnvironment p_environment,
	                             TypeElement       p_element,
	                             Path              p_dirTodo,
	                             Path              p_dirDone)
	{
		this(p_environment, p_element, p_dirTodo, p_dirDone, NOOPPW);
	}


	public TypeHelpTextExtractor(DocletEnvironment p_environment,
	                             TypeElement       p_element,
	                             Path              p_dirTodo,
	                             Path              p_dirDone,
	                             PrintWriter       p_log)
	{
		m_environment = p_environment;
		m_element     = p_element;
		m_dirTodo     = p_dirTodo;
		m_dirDone     = p_dirDone;
		m_log         = p_log;
	}


	public void run()
	{
		final String                          l_strDocComment;
		final SortedMap<String, List<String>> l_tags;
		final String                          l_strTypeName;
		final String                          l_strFileName;
		final String                          l_strText;
		final Path                            l_fileTodo;
		final Path                            l_fileDone;

		l_strDocComment = m_environment.getElementUtils().getDocComment(m_element);

		if (l_strDocComment == null)
			return; // nothing to do

		l_tags = TextUtil.splitTags(l_strDocComment);

		if (!l_tags.containsKey(TAG.HELP_TEXT))
			return; // nothing to do

		l_strTypeName = _getQualifiedTypeName(m_element);
		l_strFileName = l_strTypeName + ".md";
		l_strText     = _getOutputText(l_strTypeName, l_tags).toString();

		m_log.println("# " + l_strTypeName);

		l_fileTodo = m_dirTodo.resolve(l_strFileName);
		l_fileDone = m_dirDone.resolve(l_strFileName);

		try
		{
			if (Files.isRegularFile(l_fileDone))
			{
				if (!_equals(Files.readString(l_fileDone), l_strText))
				{
					Files.writeString(l_fileTodo, l_strText);
					Files.delete(l_fileDone);
				}
			}
			else if (Files.isRegularFile(l_fileTodo))
			{
				if (!_equals(Files.readString(l_fileTodo), l_strText))
					Files.writeString(l_fileTodo, l_strText);
			}
			else
			{
				Files.writeString(l_fileTodo, l_strText);
			}
		}
		catch (IOException l_e)
		{
			throw new UncheckedIOException(l_e);
		}
	}


	private StringBuilder _getOutputText(String                          p_strTypeName,
	                                     SortedMap<String, List<String>> p_tags)
	{
		final StringBuilder l_sbuf;

		assert p_tags.containsKey(TAG.HELP_TEXT); // the caller ensures this

		l_sbuf = new StringBuilder(4096);

		l_sbuf.append(HEADER.CLASS).append(LF);
		l_sbuf.append(p_strTypeName).append(LF);
		l_sbuf.append(LF);

		if (p_tags.containsKey(TAG.AUTHOR))
		{
			l_sbuf.append(HEADER.AUTHOR).append(LF);

			for (final String l_str : p_tags.get(TAG.AUTHOR))
				l_sbuf.append("* ").append(_getAuthor(l_str)).append(LF);

			l_sbuf.append(LF);
		}

		l_sbuf.append(HEADER.TEXT).append(LF);

		for (final String l_str : p_tags.get(TAG.HELP_TEXT))
			l_sbuf.append(_helpTextHtmlToMarkdown(l_str)).append(LF);

		return l_sbuf;
	}


	@SuppressWarnings("unused")
	private String _getPackageName(Element p_element)
	{
		return m_environment.getElementUtils().getPackageOf(p_element).toString();
	}


	private String _getQualifiedTypeName(TypeElement p_element)
	{
		return m_environment.getElementUtils().getBinaryName(p_element).toString();
	}


	private String _getAuthor(String p_strAuthorTag)
	{
		final int l_iPosStart;
		final int l_iPosEnd;

		l_iPosStart = p_strAuthorTag.indexOf('>');
		l_iPosEnd   = p_strAuthorTag.indexOf('<', l_iPosStart);

		if (l_iPosStart == -1 || l_iPosEnd == -1)
			return p_strAuthorTag;

		return TextUtil.replaceHtmlEntities(p_strAuthorTag.substring(l_iPosStart + 1, l_iPosEnd));
	}


	private static boolean _equals(String p_str1, String p_str2)
	{
		final String l_str1;
		final String l_str2;

		l_str1 = _normalizeHelpText(_removeVersionHeader(p_str1));
		l_str2 = _normalizeHelpText(_removeVersionHeader(p_str2));

		return l_str1.equals(l_str2);
	}


	private static String _removeVersionHeader(String p_str)
	{
		final int l_iPos;

		l_iPos = p_str.indexOf(HEADER.VERSION);

		if (l_iPos == -1)
			return p_str;

		return p_str.substring(0, l_iPos) + p_str.substring(p_str.indexOf("\n\n", l_iPos));
	}


	/**
	 * Normalize help text for comparison.
	 * @param p_strText The text to be normalized.
	 * @return The normalized text.
	 */
	private static String _normalizeHelpText(String p_strText)
	{
		return p_strText.replaceAll("[\r\t- ]", "");
	}


	private String _helpTextHtmlToMarkdown(String p_str)
	{
		return
			TextUtil.replaceHtmlEntities(p_str)
			.replace("&lt; ", "< ")
			.replace("&gt;", ">");
	}
}
