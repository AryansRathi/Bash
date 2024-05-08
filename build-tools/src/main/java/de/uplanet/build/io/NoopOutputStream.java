/*
 * Copyright 2000-2018 United Planet GmbH, Freiburg Germany All Rights Reserved.
 */


package de.uplanet.build.io;


import java.io.OutputStream;


/**
 * An output stream that does nothing.
 * <p>Simulates <code>/dev/null</code>.</p>
 *
 * @author <a href="mailto:Alexander.Veit@unitedplanet.de">Alexander Veit</a>
 */
public class NoopOutputStream extends OutputStream
{
	/**
	 * Construct a <code>NoopOutputStream</code>.
	 */
	public NoopOutputStream()
	{
	}


	/**
	 * <p>Does nothing</p>
	 */
	@Override
	public void close()
	{
	}


	/**
	 * <p>Does nothing</p>
	 */
	@Override
	public void flush()
	{
	}


	/**
	 * <p>Does nothing</p>
	 */
	@Override
	public void write(byte[] p_buf)
	{
	}


	/**
	 * <p>Does nothing</p>
	 */
	@Override
	public void write(byte[] p_buf, int p_iOffs, int p_iLen)
	{
	}


	/**
	 * <p>Does nothing</p>
	 */
	@Override
	public void write(int p_i)
	{
	}
}