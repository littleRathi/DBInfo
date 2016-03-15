package de.bs.cli.jpar.extractor.type;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.process.Parameters;

public class StringTypeTest {
	private StringType testee;
	
	private static final Class<?> TARGET_TYPE = String.class;
	
	private ExtractedOption option;
	private ExtractedArguments arguments;

	private static final String EXTRACTED_ARGUMENT_ARG_NAME = "-test:";
	private static final String VALID_VALUE_A00 = "a00";
	private static final String VALID_VALUE_A01 = "a01";
	private static final String WRONG_VALUE = "abc";
	private static final String[][] ALLOWED_VALUES = {
			{VALID_VALUE_A00, VALID_VALUE_A01}
	};
	
	private ExtractedOption mockExtractedOption() {
		option = mock(ExtractedOption.class);
		when(option.getOptionName()).thenReturn(EXTRACTED_ARGUMENT_ARG_NAME);
		when(option.getManualDescription()).thenReturn("description for the string type...");
		
		return option;
	}
	
	private ExtractedArguments mockExtractedValues() {
		arguments = mock(ExtractedArguments.class);
		when(arguments.getValues()).thenReturn(ALLOWED_VALUES);
		
		return arguments;
	}
	
	@Before
	public void setupTest() {
		testee = new StringType(mockExtractedOption(), null);
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
		
		assertThat(returned, equalTo((Class)TARGET_TYPE));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetSourceType() {
		Class returned = (Class)testee.getOption().getSourceType();
		
		assertThat(returned, equalTo(null));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(expected=JParException.class)
	public void TestGetSourceTypeWrong() {
		option = mockExtractedOption();
		when(option.getSourceType()).thenReturn((Class)String.class);
		testee = new StringType(option, null);
		
		testee.getOption().getSourceType();
		
		fail();
	}
	
	// testcases:StringType
	@Test
	public void testUsageDescription() {
		StringBuilder sb = new StringBuilder();
		testee.getManualDescription(sb);
		String description = sb.toString();
		
		assertThat(description, containsString(EXTRACTED_ARGUMENT_ARG_NAME));
		assertThat(description, containsString("<STRING>"));
	}
	
	@Test
	public void testProcessArgsWithA00() {
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + VALID_VALUE_A00});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, VALID_VALUE_A00, args);
		
		assertThat(result, instanceOf(String.class));
		assertThat((String)result, equalTo(VALID_VALUE_A00));
	}
	
	@Test
	public void testProcessArgsWithA00WithValues() {
		testee = new StringType(mockExtractedOption(), mockExtractedValues());
		when(arguments.validValue((String)isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + VALID_VALUE_A00});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, VALID_VALUE_A00, args);
		
		assertThat(result, instanceOf(String.class));
		assertThat((String)result, equalTo(VALID_VALUE_A00));
	}
	
	@Test(expected=JParException.class)
	public void testProcessArgsWithWrongWithValues() {
		testee = new StringType(mockExtractedOption(), mockExtractedValues());
		when(arguments.validValue((String)isNotNull())).thenReturn(false);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + WRONG_VALUE});
		testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, WRONG_VALUE, args);
		
		fail();
	}
}
