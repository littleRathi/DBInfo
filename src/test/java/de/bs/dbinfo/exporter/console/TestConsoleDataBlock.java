package de.bs.dbinfo.exporter.console;

import static org.junit.Assert.*;
import static org.hamcrest.core.StringContains.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import de.bs.dbinfo.exporter.DataBlock;

public class TestConsoleDataBlock {
	private static final String HEADER = "testheader";
	
	private static final String HEADER_ROW_PREFIX = "rowheader";
	private static final String[] HEADER_ROW_DATA = new String[]{"headercol0", "headercol1"};
	
	private static final String DATA_ROW_PREFIX = "rowdata";
	private static final String[] DATA_ROW_DATA = new String[]{"datacol0", "datacol1"};

	private DataBlock testee;
	
	private ConsoleExporter exporter;
	
	private PrintStream printStream;
	private ByteArrayOutputStream outStream;
	
	@Before
	public void setUpConsole() {
		exporter = new ConsoleExporter();
		outStream = new ByteArrayOutputStream();
		printStream = new PrintStream(outStream);
		exporter.setOut(printStream);
		testee = null;
	}
	
	@Test
	public void setHeader() {
		testee = exporter.createDataBlock();
		testee.setHeader(HEADER);

		String output = outStream.toString();
		assertEquals(HEADER, testee.getHeader());
		assertThat(output, containsString(HEADER));
	}
	
	@Test
	public void headerRow() {
		testee = exporter.createDataBlock();
		testee.addHeaderRow(HEADER_ROW_PREFIX, HEADER_ROW_DATA);
		
		String output = outStream.toString();
		assertThat(output, containsString(HEADER_ROW_PREFIX));
		for (String rowData: HEADER_ROW_DATA) {
			assertThat(output, containsString(rowData));
		}
	}
	
	@Test
	public void dataRow() {
		testee = exporter.createDataBlock();
		testee.addRow(DATA_ROW_PREFIX, DATA_ROW_DATA);
		
		String output = outStream.toString();
		assertThat(output, containsString(DATA_ROW_PREFIX));
		for (String rowData: DATA_ROW_DATA) {
			assertThat(output, containsString(rowData));
		}
	}
}
