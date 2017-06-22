package com.capture.core;

import com.beust.jcommander.Parameter;

public class Parameters {

	@Parameter(names = { "-c",
			"--caption" }, order = 0, description = "Window caption name(s), regex expression (matched = captured)", required = true)
	public String caption;

	@Parameter(names = { "-p", "--path" }, order = 1, description = "Path for screenshots")
	public String path = ".";

	@Parameter(names = { "-e", "--extension" }, order = 2, description = "Screenshot file extension")
	public String extension = "png";

	@Parameter(names = { "-w", "--wait" }, order = 3, description = "Seconds to wait before taking a screenshot")
	public Integer wait = 5;

	@Parameter(names = { "-h", "--help" }, order = 4, help = true, description = "Help")
	public boolean help = false;

	@Override
	public String toString() {
		return "Parameters [caption=" + caption + ", path=" + path + ", extension=" + extension + ", wait=" + wait
				+ ", help=" + help + "]";
	}

}
