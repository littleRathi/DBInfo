package de.bs.dbinfo.exporter.console;

import java.io.PrintStream;

import de.bs.dbinfo.exporter.DataBlock;
import de.bs.dbinfo.exporter.Exporter;

public class ConsoleExporter implements Exporter {
	private PrintStream out;
	
	private String identifier;
	
	public ConsoleExporter() {
		out = System.out;
	}
	
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
		
		out.println("[Console Exporter]");
		out.println(" - Export: " + this.identifier);
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public DataBlock createDataBlock() {
		return new ConsoleDataBlock(out);
	}

	@Override
	public void done() {
	}

	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}
}
