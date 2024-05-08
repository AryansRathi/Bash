/*
 * Copyright 2000-2003 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.build.io;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


/**
 * Helps in handling files and their content.
 * @author <a href="mailto:Alexander.Veit@unitedplanet.de">Alexander Veit</a>
 */
public final class FileUtil
{
	private static final int BUFFER_SIZE = 4096;


	private FileUtil()
	{
	}


	public static ArrayList<Path> listDirectories(Path p_dir, DirectoryStream.Filter<Path> p_filter)
		throws IOException
	{
		final ArrayList<Path> l_list;

		l_list = new ArrayList<>();

		try (final DirectoryStream<Path> l_ds = Files.newDirectoryStream(p_dir))
		{
			for (final Path l_path : l_ds)
			{
				if (!Files.isDirectory(l_path))
					continue;

				if (l_path.getFileName().toString().startsWith("."))
					continue;

				if (p_filter.accept(l_path))
					l_list.add(l_path);
			}
		}

		return l_list;
	}


	public static void renameExact(Path p_file)
	{
		try
		{
			if (Files.isRegularFile(p_file))
			{
				final Path l_fileRealName;

				l_fileRealName = p_file.toRealPath().getFileName();

				if (!p_file.getFileName().equals(l_fileRealName))
				{
					final byte[] l_buf;

					l_buf = Files.readAllBytes(p_file);

					Files.delete(p_file);

					Files.write(p_file, l_buf);
				}
			}
		}
		catch (IOException l_e)
		{
			throw new UncheckedIOException(l_e);
		}
	}


	/**
	 * <p>Performs buffered copy from one stream to another.</p>
	 * <p>The destination buffer is being flushed when copying is ready.</p>
	 *
	 * @param p_src  The source stream.
	 * @param p_dest The destination stream.
	 * @exception java.io.IOException If an I/O-error occurs.
	 */
    public static void copyStream(OutputStream p_dest, InputStream p_src)
        throws IOException
    {
		byte[] l_buf = new byte[BUFFER_SIZE];

		int l_iRead = 0;

		while ((l_iRead = p_src.read(l_buf, 0, BUFFER_SIZE)) != -1)
			p_dest.write(l_buf, 0, l_iRead);

		p_dest.flush();
    }


	/**
	 * Get the content of a UTF-8 encoded stream.
	 */
	public static String getContent(InputStream p_in)
	{
		final Reader        l_reader;
		final StringBuilder l_sbuf;
		final char          l_buf[];

		l_reader = new InputStreamReader(p_in, StandardCharsets.UTF_8);
		l_sbuf   = new StringBuilder(1024);
		l_buf    = new char[1024];

		try
		{
			int l_iRead;

			while ((l_iRead = l_reader.read(l_buf, 0, 1024)) >= 0)
				l_sbuf.append(l_buf, 0, l_iRead);
		}
		catch (IOException l_e)
		{
			throw new UncheckedIOException(l_e);
		}

		return l_sbuf.toString();
	}
}
