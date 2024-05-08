/*
 * Copyright 2000-2019 United Planet GmbH, Freiburg Germany All Rights Reserved.
 */


package de.uplanet.build;


import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;

import com.sun.source.doctree.DocTree;

import jdk.javadoc.doclet.Taglet;


/**
 * A Taglet for absorbing @up.help.text.
 * @author <a href="mailto:Alexander.Veit@unitedplanet.de">Alexander Veit</a>
 */
public final class UpTestTaglet implements Taglet
{
    private static final String NAME = "up.help.text";


    public UpTestTaglet()
    {
    }


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
		return "";
	}
}