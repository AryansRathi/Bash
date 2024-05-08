/*
 * Copyright 2000-2018 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.build.io;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;


/**
 * @author <a href="mailto:alexander.veit@unitedplanet.de">Alexander Veit</a>
 */
public final class LfPrintWriter extends PrintWriter
{
	public LfPrintWriter(OutputStream p_out)
	{
		super(p_out, true, StandardCharsets.UTF_8);
	}


	@Override
	public void println()
	{
		write("\n");
	}
}
