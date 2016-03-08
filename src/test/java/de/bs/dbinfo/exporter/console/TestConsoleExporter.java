package de.bs.dbinfo.exporter.console;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import de.bs.dbinfo.exporter.console.ConsoleExporter;

public class TestConsoleExporter {
	private static final String IDENTIFIER = "myIdentifier";
	
	private ConsoleExporter testee;
	private PrintStream outStream;
	
	@Before
	public void setUp() {
		testee = new ConsoleExporter();
		outStream = new PrintStream(new ByteArrayOutputStream());
		testee.setOut(outStream);
	}
	
	@Test
	public void createExporter() {
		testee.setIdentifier(IDENTIFIER);
		
		assertEquals(IDENTIFIER, testee.getIdentifier());
	}
	
	@Test
	public void getDataBlock() {
		ConsoleExporter ce = new ConsoleExporter();
		ce.setOut(System.out);
		
		assertNotNull(ce.createDataBlock());
	}
}
