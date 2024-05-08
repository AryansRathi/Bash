/*
 * Copyright 2000-2018 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.build.util;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author <a href="mailto:alexander.veit@unitedplanet.de">Alexander Veit</a>
 */
public final class TextUtil
{
	private TextUtil()
	{
	}


	public static SortedMap<String, List<String>> splitTags(CharSequence p_chseq)
	{
		final TreeMap<String, List<String>> l_map;
		final StringBuilder                 l_sbuf;
		final int                           l_iLen;

		l_map  = new TreeMap<>();
		l_sbuf = trimLines(p_chseq);
		l_iLen = l_sbuf.length();

		for (int l_iPos = 0; l_iPos < l_iLen; l_iPos++)
		{
			if (l_sbuf.charAt(l_iPos) == '@' && (l_iPos == 0 || l_sbuf.charAt(l_iPos - 1) == '\n'))
			{
				// new tag
				final int    l_iStartTagName;
				final String l_strTagName;
				final int    l_iStartContent;
				final String l_strContent;

				l_iStartTagName = l_iPos;

				// parse the tag name
				while (l_iPos < l_iLen && !Character.isWhitespace(l_sbuf.charAt(l_iPos)))
					l_iPos++;

				l_strTagName = l_sbuf.substring(l_iStartTagName, l_iPos);

				// eat up whitespace
				while (l_iPos < l_iLen && Character.isWhitespace(l_sbuf.charAt(l_iPos)))
					l_iPos++;

				l_iStartContent = l_iPos;

				// parse tag content
				while (l_iPos < l_iLen && (l_sbuf.charAt(l_iPos - 1) != '\n' || l_sbuf.charAt(l_iPos) != '@'))
					l_iPos++;

				l_strContent = l_sbuf.substring(l_iStartContent, l_iPos).trim();

				l_map.computeIfAbsent(l_strTagName, $ -> new ArrayList<>()).add(l_strContent);

				// correct the parse position if necessary
				if (l_iPos < l_iLen && l_sbuf.charAt(l_iPos) == '@')
					l_iPos--;
			}
		}

		return l_map;
	}


	/**
	 * Remove whitespace (space or horizontal tabulator) from the
	 * beginning and the end of a line.
	 * <p>Line breaks are normalized to <code>LF</code>.</p>
	 * @param p_chseq The input.
	 * @return The given test with trimmed lines.
	 */
	public static StringBuilder trimLines(CharSequence p_chseq)
	{
		final int           l_iLen;
		final StringBuilder l_sbuf;
		boolean             l_bBeginningOfLine;

		l_iLen = p_chseq.length();
		l_sbuf = new StringBuilder(l_iLen);

		l_bBeginningOfLine = true;

		for (int l_iPos = 0; l_iPos < l_iLen; l_iPos++) // we do not need to parse codepoints here
		{
			final char l_ch;

			l_ch = p_chseq.charAt(l_iPos);

			if (_isWS(l_ch))
			{
				if (!l_bBeginningOfLine) // ignore whitespace at the beginning of a line
					l_sbuf.append(l_ch);
			}
			else if (l_ch == '\r')
			{
				// ignore it
			}
			else if (l_ch ==  '\n')
			{
				// trim whitespace at the end of the line
				_trimTrailingWhitespace(l_sbuf);

				l_sbuf.append(l_ch);

				l_bBeginningOfLine = true; // reset state
			}
			else
			{
				l_bBeginningOfLine = false;

				l_sbuf.append(l_ch);
			}
		}

		return _trimTrailingWhitespace(l_sbuf);
	}


	private static StringBuilder _trimTrailingWhitespace(StringBuilder p_sbuf)
	{
		while (p_sbuf.length() > 0 && _isWS(p_sbuf.charAt(p_sbuf.length() - 1)))
			p_sbuf.setLength(p_sbuf.length() - 1);

		return p_sbuf;
	}


	private static boolean _isWS(char p_ch)
	{
		return p_ch == ' ' || p_ch == '\t';
	}


	public static String replaceHtmlEntities(String p_str)
	{
		if (p_str.indexOf('&') == -1)
		{
			return p_str;
		}
		else
		{
			return p_str
				.replace("&auml;", "ä")
				.replace("&ouml;", "ö")
				.replace("&uuml;", "ü")
				.replace("&Auml;", "Ä")
				.replace("&Ouml;", "Ö")
				.replace("&Uuml;", "Ü")
				.replace("&szlig;", "ß");
		}
	}
}
