/*
 * Copyright 2000-2003 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.build;


import java.io.File;

import javax.tools.DocumentationTool;
import javax.tools.ToolProvider;

import junit.framework.TestCase;


/**
 * Tests for the online help doclet.
 *
 * @author <a href="mailto:Alexander.Veit@unitedplanet.de">Alexander Veit</a>
 * @up.help.text Dies ist ein Text
 *     der in der Hilfe erscheinen
 *     soll.
 */
public final class HelpDocletTestCase extends TestCase
{
	public void test()
	{
		File                    l_fileSrc;
		final DocumentationTool l_dt;
		final int               l_iExitCode;

		System.setProperty("up.help.dir", "work");

		l_fileSrc = new File("src/test/java");

		l_dt = ToolProvider.getSystemDocumentationTool();

		l_iExitCode = l_dt.run(System.in,
		                       System.out,
		                       System.err,
		                       "-doclet",
		                       "de.uplanet.build.OnlineHelpDoclet",
		                       "-sourcepath",
		                       l_fileSrc.getAbsolutePath(),
		                       "de.uplanet.testcases.lucy.client.appdesigner"
		                       );

		System.out.println(l_iExitCode);
	}
}
