/*
 * Copyright 2000-2018 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.build.util;


import java.util.List;
import java.util.Map;

import junit.framework.TestCase;


/**
 * @author <a href="mailto:alexander.veit@unitedplanet.de">Alexander Veit</a>
 */
public class TextUtilTestCase extends TestCase
{
	public void test00()
	{
		assertTrue(TextUtil.splitTags("aaa bbb\nccc ddd").isEmpty());
	}


	public void test01()
	{
		final Map<String, List<String>> l_tags;

		l_tags = TextUtil.splitTags("@a aaa");

		assertEquals(1, l_tags.size());
		assertEquals(1, l_tags.get("@a").size());
		assertEquals("aaa", l_tags.get("@a").get(0));
	}


	public void test02()
	{
		final Map<String, List<String>> l_tags;

		l_tags = TextUtil.splitTags(" @a aaa");

		assertEquals(1, l_tags.size());
		assertEquals(1, l_tags.get("@a").size());
		assertEquals("aaa", l_tags.get("@a").get(0));
	}


	public void test03()
	{
		final Map<String, List<String>> l_tags;

		l_tags = TextUtil.splitTags("@a aaa\n @b bbb\nbbb");

		assertEquals(2, l_tags.size());
		assertEquals(1, l_tags.get("@a").size());
		assertEquals("aaa", l_tags.get("@a").get(0));
		assertEquals(1, l_tags.get("@b").size());
		assertEquals("bbb\nbbb", l_tags.get("@b").get(0));
	}


	public void test04()
	{
		final Map<String, List<String>> l_tags;

		l_tags = TextUtil.splitTags("@a aaa\n@b bbb\nbbb\n@a AAA");

		assertEquals(2, l_tags.size());
		assertEquals(2, l_tags.get("@a").size());
		assertEquals("aaa", l_tags.get("@a").get(0));
		assertEquals("AAA", l_tags.get("@a").get(1));
	}
}
