package de.bs.dbinfo.exporter.console;

import java.io.PrintStream;

import de.bs.dbinfo.exporter.DataBlock;

class ConsoleDataBlock implements DataBlock {
	private PrintStream out;
	
	private String header;
	
	public ConsoleDataBlock(final PrintStream out) {
		this.out = out;
	}

	@Override
	public String getHeader() {
		return header;
	}

	@Override
	public void setHeader(final String header) {
		this.header = header;
		
		out.println("###" + header);
	}

	@Override
	public void addHeaderRow(final String prefix, final String[] data) {
		out.print("\t" + (prefix != null ? prefix + ":" : ""));
		for (int i = 0; i < data.length; i++) {
			out.print(data[i] + "\t");
		}
		out.println();
	}

	@Override
	public void addRow(final String prefix, final String[] data) {
		out.print("\t" + (prefix != null ? prefix + ":" : ""));
		for (int i = 0; i < data.length; i++) {
			out.print(data[i] + "\t");
		}
		out.println();
	}
}
