package de.bs.cli.jpar.extractor.type;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

public class VoidTypeTest {
	private VoidType testee;
	
	private ExtractedOption option;
	
	private static final String EXTRACTED_ARGUMENT_ARG_NAME = "-test";
	
	private ExtractedOption mockExtractedOption() {
		option = mock(ExtractedOption.class);
		when(option.getOptionName()).thenReturn(EXTRACTED_ARGUMENT_ARG_NAME);
		when(option.getManuelDescription()).thenReturn("Einfache Beschreibung zu VoidType");
		
		return option;
	}
	
	private ExtractedArguments mockExtractedArguments() {
		ExtractedArguments arguments = mock(ExtractedArguments.class);
		
		return arguments;
	}
	
	@Before
	public void setupTest() {
		testee = new VoidType(mockExtractedOption(), null);
	}
	
	// testcases:Type (superclass)
	@Test
	public void testGetOption() {
		ExtractedOption returned = testee.getOption();
		
		assertThat(returned, equalTo(option));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetTargetType() {
		Class returned = (Class)testee.getTargetType();
		
		assertThat(returned, equalTo((Class)Void.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(expected=JParException.class)
	public void testWithSourceType() {
		option = mockExtractedOption();
		when(option.getSourceType()).thenReturn((Class)String.class);
		
		testee = new VoidType(option, null);
		
		fail();
	}
	
	// testcases:VoidType
	@Test
	public void testUsageDescription() {
		StringBuilder sb = new StringBuilder();
		testee.getManualDescription(sb);
		String description = sb.toString();
		
		assertThat(description, containsString(EXTRACTED_ARGUMENT_ARG_NAME));
	}
	
	@Test
	public void testProcessArgsWith() {
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, "", args);
		
		assertThat(result, nullValue());
	}
	
	@Test(expected=JParException.class)
	public void testConstructorWithExtractedArguments() {
		testee = new VoidType(mockExtractedOption(), mockExtractedArguments());
		
		fail();
	}
}
