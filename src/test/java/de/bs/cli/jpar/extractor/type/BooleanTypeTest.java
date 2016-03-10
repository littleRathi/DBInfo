package de.bs.cli.jpar.extractor.type;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import static org.hamcrest.core.StringContains.containsString;
import static de.bs.cli.jpar.extractor.type.BooleanType.FALSE;
import static de.bs.cli.jpar.extractor.type.BooleanType.TRUE;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.Arguments;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedValues;
import de.bs.cli.jpar.extractor.type.BooleanType;
import de.bs.cli.jpar.process.Parameters;

public class BooleanTypeTest {
	private BooleanType testee;
	
	private ExtractedOption extractedArgument;
	
	private static final String EXTRACTED_ARGUMENT_ARG_NAME = "-test:";
	private static final String WRONG_VALUE = "abc";
	
	private ExtractedValues mockExtractedValues() {
		String[][] values = new String[][]{{"first"}};
		
		ExtractedValues extractedValues = mock(ExtractedValues.class);
		when(extractedValues.getDelimiter()).thenReturn(";");
		
		return extractedValues;
	}
	
	private ExtractedOption mockExtractedArgument() {
		extractedArgument = mock(ExtractedOption.class);
		when(extractedArgument.getOptionName()).thenReturn(EXTRACTED_ARGUMENT_ARG_NAME);
		return extractedArgument;
	}
	
	@Before
	public void setupTest() {
		testee = new BooleanType(mockExtractedArgument());
	}
	
	// testcases:Type (superclass)
	@Test
	public void testGetExtractedArgument() {
		ExtractedOption returned = testee.getExtractedArgument();
		
		assertThat(returned, equalTo(extractedArgument));
	}
	
	@Test
	public void testGetTargetType() {
		@SuppressWarnings("unchecked") // TODO: seems strange
		Class<Boolean> returned = (Class<Boolean>)testee.getTargetType();
		
		assertThat(returned, equalTo(Boolean.class));
	}
	
	// testcases:BooleanType
	@Test
	public void testUsageDescription() {
		StringBuilder sb = new StringBuilder();
		testee.getUsageDescription(sb);
		String description = sb.toString();
		
		assertThat(description, containsString(EXTRACTED_ARGUMENT_ARG_NAME));
		assertThat(description, containsString(BooleanType.FALSE));
		assertThat(description, containsString(BooleanType.TRUE));
	}
	
	@Test
	public void testIsAssignableWithTrue() {
		Boolean returned = testee.isAssignable(TRUE);
		
		assertThat(returned, equalTo(Boolean.TRUE));
	}
	
	@Test
	public void testIsAssignableWithFalse() {
		Boolean returned = testee.isAssignable(FALSE);
		
		assertThat(returned, equalTo(Boolean.TRUE));
	}
	
	@Test
	public void testIsAssignableFalseObjects() {
		Object[] wrongObjects = new Object[]{new Object(), 16, 5.5, "abc"};
		
		for (Object wrongObject: wrongObjects) {
			Boolean returned = testee.isAssignable(wrongObject);
			
			assertThat(returned, equalTo(Boolean.FALSE));
		}
	}
	
	@Test
	public void testProcessArgsWithTrue() {
		String[] args = new String[]{EXTRACTED_ARGUMENT_ARG_NAME + TRUE};
		Parameters arguments = new Parameters(args);
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, TRUE, arguments);
		
		assertThat(result, equalTo((Object)Boolean.TRUE));
	}
	
	@Test
	public void testProcessArgsWithFalse() {
		String[] args = new String[]{EXTRACTED_ARGUMENT_ARG_NAME + FALSE};
		Parameters arguments = new Parameters(args);
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, FALSE, arguments);
		
		assertThat(result, equalTo((Object)Boolean.FALSE));
	}
	
	@Test(expected=JParException.class)
	public void testProcessArgsWithAnyString() {
		String[] args = new String[]{EXTRACTED_ARGUMENT_ARG_NAME + WRONG_VALUE};
		Parameters arguments = new Parameters(args);
		testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, args[0], arguments);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testProcessArgsWithAnythingButArgValues() {
		testee.setExtractedValues(mockExtractedValues());
		
		String[] args = new String[]{EXTRACTED_ARGUMENT_ARG_NAME + FALSE};
		Parameters arguments = new Parameters(args);
		testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, args[0], arguments);
		
		fail();
	}
}	
