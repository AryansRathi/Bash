/*
 * Copyright 2000-2010 United Planet GmbH, Freiburg Germany
 * All Rights Reserved.
 */


package de.uplanet.build;


import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;

import com.sun.source.doctree.DocTree;

import jdk.javadoc.doclet.Taglet;


/**
 * @author <a href="mailto:Stefan.Menner@unitedplanet.de">Stefan Menner</a>
 */
public final class SystemPropertyTaglet implements Taglet
{
	private static final String NAME = "up.sysprop";


	/**
     * Return the name of this custom tag.
     */
    @Override
	public String getName()
    {
		return NAME;
    }


    /**
     * Will return false since <code>@up.help.text</code>
     * is not an inline tag.
     * @return <code>false</code> since <code>@up.help.text</code>
     *    is not an inline tag.
     */
    @Override
	public boolean isInlineTag()
    {
        return false;
    }


    @Override
	public Set<Location> getAllowedLocations()
	{
		return EnumSet.allOf(Location.class);
	}


	@Override
	public String toString(List<? extends DocTree> p_tags, Element p_element)
	{
		// FIXME restore the previous implementation using the new API
		return "";
	}
}
