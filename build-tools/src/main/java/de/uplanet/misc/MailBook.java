/*
 * Copyright 2000-2003 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.misc;


import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * @author <a href="mailto:Alexander.Veit@unitedplanet.de">Alexander Veit</a>
 */
public final class MailBook
{
	private final static Map<String, String> MB;

	static
	{
		MB = new HashMap<String, String>();

		MB.put("detlev.struebig@unitedplanet.de",   "alexander.veit@unitedplanet.de");
		MB.put("juergen.dick@unitedplanet.de",      "alexander.veit@unitedplanet.de");
		MB.put("augustinus.topor@unitedplanet.de",  "alexander.veit@unitedplanet.de");
		MB.put("dieter.kaltenbach@unitedplanet.de", "alexander.veit@unitedplanet.de");
		MB.put("oliver.lingg@unitedplanet.de",      "karin.moedinger@unitedplanet.de");
		MB.put("karsten.medlin@unitedplanet.de",    "karin.moedinger@unitedplanet.de");
		MB.put("kerstin.gersonde@unitedplanet.de",  "tobias.seng@unitedplanet.de");
		MB.put("michael.schulte@unitedplanet.de",   "tobias.seng@unitedplanet.de");
		MB.put("markus.birkhofer@unitedplanet.de",  "manfred.stetz@unitedplanet.de");
		MB.put("thomas.buehler@unitedplanet.de",    "martin.keller@unitedplanet.de");
		MB.put("xun.xu@unitedplanet.de",            "martin.keller@unitedplanet.de");
	}


	/**
	 * Map mail addresses to existing internal recipients.
	 */
	public static String map(String p_strAddress)
	{
		String                l_strAddress;
		final StringTokenizer l_stok;
		final StringBuffer    l_sbuf;


		l_stok = new StringTokenizer(p_strAddress.replaceAll(",", ";"), ";");
		l_sbuf = new StringBuffer(256);

		while (l_stok.hasMoreTokens())
		{
			l_strAddress = l_stok.nextToken();
			l_strAddress = l_strAddress.trim();
			l_strAddress = l_strAddress.toLowerCase();

			if (MB.containsKey(l_strAddress))
				l_strAddress = MB.get(l_strAddress);

			if (!l_strAddress.endsWith("@unitedplanet.de"))
				l_strAddress = "alexander.veit@unitedplanet.de";

			if (l_strAddress.length() > 3) // minimalistic email address is a@b
			{
				l_sbuf.append(l_strAddress);
				l_sbuf.append(";");
			}
		}

		if (l_sbuf.length() > 0)
			l_sbuf.setLength(l_sbuf.length() - 1);

		return l_sbuf.toString();
	}
}
