/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.utils.progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.TileCache;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.option.GroupImpl;
import org.apache.commons.cli2.util.HelpFormatter;

/**
 * @author Simone Giannecchini, GeoSolutions.
 * 
 *
 *
 * @source $URL$
 */
public abstract class BaseArgumentsManager extends ProgressManager {
	/**
	 * Default tile cache size.
	 */
	public static final long DEFAULT_TILE_CACHE_SIZE = 128 * 1024 * 1024;

	/**
	 * Default priority for the underlying {@link Thread}.
	 */
	public static final int DEFAULT_PRIORITY = Thread.NORM_PRIORITY;

	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(BaseArgumentsManager.class.toString());

	/**
	 * Options for the command line.
	 */
	private final List<Option> cmdOpts = Collections
			.synchronizedList(new ArrayList<Option>());

	private final Parser cmdParser = new Parser();

	protected final ArgumentBuilder argumentBuilder = new ArgumentBuilder();

	protected final DefaultOptionBuilder optionBuilder = new DefaultOptionBuilder();

	private Group optionsGroup;

	private CommandLine cmdLine;

	private final Option helpOpt;

	private final Option priorityOpt;

	private final Option versionOpt;

	/** Commons-cli option for the tile cache size to use. */
	private final Option tileCacheSizeOpt;

	/** Default tile cache size. */
	private long tileCacheSize = DEFAULT_TILE_CACHE_SIZE;

	/**
	 * Default priority for the underlying {@link Thread}.
	 */
	private int priority = DEFAULT_PRIORITY;

	private String toolName;

	private String version;

	/**
	 * Default imageio caching behaviour.
	 */
	public final boolean DEFAULT_IMAGEIO_CACHING_BEHAVIOUR = false;

	/** ImageIO caching behvaiour controller. */
	private boolean useImageIOCache = DEFAULT_IMAGEIO_CACHING_BEHAVIOUR;

	/**
	 * 
	 */
	public BaseArgumentsManager(final String name, final String version) {
		super();
		toolName = name;
		this.version = version;
		versionOpt = optionBuilder.withShortName("v")
				.withLongName("versionOpt").withDescription(
						"print the versionOpt.").create();

		helpOpt = optionBuilder.withShortName("h").withShortName("?")
				.withLongName("helpOpt").withDescription("print this message.")
				.create();

		tileCacheSizeOpt = optionBuilder.withShortName("c").withLongName(
				"cache_size").withArgument(
				argumentBuilder.withName("c").withMinimum(0).withMaximum(1)
						.create()).withDescription("tile cache size")
				.withRequired(false).create();

		priorityOpt = optionBuilder.withShortName("p").withLongName(
				"thread_priority").withArgument(
				argumentBuilder.withName("priority").withMinimum(0)
						.withMaximum(1).create()).withDescription(
				"priority for the underlying thread").withRequired(false)
				.create();
		cmdOpts.add(versionOpt);
		cmdOpts.add(helpOpt);
		cmdOpts.add(tileCacheSizeOpt);
		cmdOpts.add(priorityOpt);
	}

	protected void addOption(Option opt) {
		if (cmdLine != null)
			throw new IllegalStateException();
		synchronized (cmdOpts) {
			cmdOpts.add(opt);
		}

	}

	protected boolean removeOption(Option opt) {
		if (cmdLine != null)
			throw new IllegalStateException();
		synchronized (cmdOpts) {
			return cmdOpts.remove(opt);
		}
	}

	protected boolean removeOptions(List<Option> opts) {
		if (cmdLine != null)
			throw new IllegalStateException();
		synchronized (cmdOpts) {
			return cmdOpts.remove(opts);
		}
	}

	protected void addOptions(List<Option> opts) {
		if (cmdLine != null)
			throw new IllegalStateException();
		synchronized (cmdOpts) {
			cmdOpts.addAll(opts);
		}

	}

	protected void finishInitialization() {
		// /////////////////////////////////////////////////////////////////////
		//
		// Help Formatter
		//
		// /////////////////////////////////////////////////////////////////////
		final HelpFormatter cmdHlp = new HelpFormatter("| ", "  ", " |", 75);
		cmdHlp.setShellCommand(getToolName());
		cmdHlp.setHeader("Help");
		cmdHlp.setFooter(new StringBuffer(getToolName()
				+ " - GeoSolutions S.a.s (C) 2006 - v ").append(getVersion())
				.toString());
		cmdHlp
				.setDivider("|-------------------------------------------------------------------------|");

		// /////////////////////////////////////////////////////////////////////
		//
		// Close Parser
		//
		// /////////////////////////////////////////////////////////////////////
		optionsGroup = new GroupImpl(cmdOpts, "Options", "All the options", 1,
				cmdOpts.size());
		cmdParser.setGroup(optionsGroup);
		cmdParser.setHelpOption(helpOpt);
		cmdParser.setHelpFormatter(cmdHlp);

	}

	public boolean parseArgs(String[] args) {

		cmdLine = cmdParser.parseAndHelp(args);
		if (cmdLine == null)
			return false;

		if (cmdLine.hasOption(versionOpt)) {
			System.out.print(new StringBuffer(getToolName()).append(
					" - GeoSolutions S.a.s (C) 2006 - v").append(getVersion())
					.toString());
			System.exit(0);

		}
		// //
		//
		// Tile cache size
		//
		// //
		// index name
		if (cmdLine.hasOption(tileCacheSizeOpt))
			try {
				tileCacheSize = Integer.parseInt((String) cmdLine
						.getValue(tileCacheSizeOpt));
			} catch (Throwable e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
				return false;
			}

		// //
		//
		// Thread priority
		//
		// //
		// index name
		if (cmdLine.hasOption(priorityOpt))
			try {
				priority = Integer.parseInt((String) cmdLine
						.getValue(priorityOpt));
			} catch (Throwable e) {
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
				return false;
			}
		setJAIHints();
		return true;

	}

	public boolean hasOption(Option opt) {
		if (cmdLine == null)
			throw new IllegalStateException();
		return this.cmdLine.hasOption(opt);
	}

	public boolean hasOption(String optName) {
		if (cmdLine == null)
			throw new IllegalStateException();
		return this.cmdLine.hasOption(optName);
	}

	public Object getOptionValue(Option opt) {
		if (cmdLine == null)
			throw new IllegalStateException();
		return this.cmdLine.getValue(opt);
	}

	public Object getOptionValue(String optName) {
		if (cmdLine == null)
			throw new IllegalStateException();
		return this.cmdLine.getValue(optName);
	}

	public int getPriority() {
		return priority;
	}

	public long getTileCacheSize() {
		return tileCacheSize;
	}

	public String getToolName() {
		return toolName;
	}

	public String getVersion() {
		return version;
	}

	public void setUseImageIOCache(boolean useImageIOCache) {
		this.useImageIOCache = useImageIOCache;
	}

	/**
	 * 
	 * This method is a utlity method for setting various JAi wide hints we will
	 * use here and afterwards.
	 * 
	 * 
	 */
	private void setJAIHints() {

		// //
		//
		// JAI cache fine tuning
		//
		// //
		final JAI jaiDef = JAI.getDefaultInstance();
		// setting the tile cache
		final TileCache cache = jaiDef.getTileCache();
		cache.setMemoryCapacity(tileCacheSize * 1024 * 1024);

		// //
		// Imageio caching behaviour in case it is ever needed.
		// //
		ImageIO.setUseCache(useImageIOCache);

	}

	public boolean isUseImageIOCache() {
		return useImageIOCache;
	}

	public void setTileCacheSize(long tileCacheSize) {
		this.tileCacheSize = tileCacheSize;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
