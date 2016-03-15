package de.bs.cli.jpar.extractor;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.Arguments;
import de.bs.cli.jpar.Option;

// test only methods of the instance functionality
public class ExtractedArgumentsUsageTest {
	private static final String DELIMITER = ";";

	private ExtractedArguments testee;
	
	private Arguments arguments;
	private Option option;
	
	private static final String NAME = "test";
	private static final String OPTION_NAME = "-" + NAME;
	private static final String VALUE_FAIL0 = "fail0";
	private static final String VALUE_FAIL1 = "fail1";
	private static final String VALUE_VAL0 = "value1";
	private static final String VALUE_VAL1 = "value2";
	private static final String VALUE_VAL2 = "value3";
	private static final String VALUE_VAL3 = "value4";
	private static final String[] VALUES = new String[]{VALUE_VAL0, VALUE_VAL1, VALUE_VAL2, VALUE_VAL3};
	
	@SuppressWarnings("unused")
	private static String[][] mockField;
	
	private Field getMockField() throws NoSuchFieldException, SecurityException {
		return this.getClass().getDeclaredField("mockField");
	}
	
	private ExtractedArguments getArguments(final Arguments arguments, final Option option, final String optionName) throws NoSuchFieldException, SecurityException {
		this.testee = ExtractedArguments.getAnnotationOnField(arguments, option, optionName, getMockField());
		return this.testee;
	}
	
	public Option mockOption() {
		option = mock(Option.class);
		when(option.description()).thenReturn("The desciption for this mock option...");
		when(option.name()).thenReturn(NAME);
		when(option.required()).thenReturn(false);
		return option;
	}
	
	public Arguments mockArguments() {
		arguments = mock(Arguments.class);
		when(arguments.delimiter()).thenReturn(DELIMITER);
//		when(arguments.name()).thenReturn(NAME);
		when(arguments.values()).thenReturn(VALUES);
		return arguments;
	}
	
	@Before
	public void setupTest() throws Exception {
		testee = getArguments(mockArguments(), mockOption(), OPTION_NAME);
	}
	
	@After
	public void downTest() {
		testee = null;
	}

	@Test
	public void testGetDelimiter() {
		String result = testee.getDelimiter();
		
		assertThat(result, equalTo(DELIMITER));
	}
	
	@Test
	public void testGetValues() {
		String[][] result = testee.getValues();
		
		assertThat(result.length, equalTo(1));
		assertThat(result[0].length, greaterThan(0));
	}
	
	@Test
	public void testValidValueCorrect() {
		for (String val: VALUES) {
			assertThat(testee.validValue(val), equalTo(true));
		}
	}
	
	@Test
	public void testValidValueFail() {
		boolean result = testee.validValue(VALUE_FAIL0);
		
		assertThat(result, equalTo(false));
	}
	
	@Test
	public void testValidValueFailNull() {
		boolean result = testee.validValue(null);
		
		assertThat(result, equalTo(false));
	}
	
	@Test
	public void testValidValuesCorrectAll() {
		boolean result = testee.validValues(VALUES);
		
		assertThat(result, equalTo(true));
	}
	
	@Test
	public void testValidValuesCorrectHalf() {
		String[] toCheck = new String[]{VALUE_VAL0, VALUE_VAL1};
		
		boolean result = testee.validValues(toCheck);
		
		assertThat(result, equalTo(true));
	}
	
	@Test
	public void testValidValuesCorrectOne() {
		String[] toCheck = new String[]{VALUE_VAL2};
		
		boolean result = testee.validValues(toCheck);
		
		assertThat(result, equalTo(true));
	}
	
	@Test
	public void testValidValuesWrongMixFailCorrect() {
		String[] toCheck = new String[]{VALUE_FAIL0, VALUE_VAL0};
		
		boolean result = testee.validValues(toCheck);
		
		assertThat(result, equalTo(false));
	}
	
	@Test
	public void testValidValuesWrongMixCorrectFail() {
		String[] toCheck = new String[]{VALUE_VAL3, VALUE_FAIL1};
		
		boolean result = testee.validValues(toCheck);
		
		assertThat(result, equalTo(false));
	}
	
	@Test
	public void testValidValuesWrongValues() {
		String[] toCheck = new String[]{VALUE_FAIL0, VALUE_FAIL1};
		
		boolean result = testee.validValues(toCheck);
		
		assertThat(result, equalTo(false));
	}
	
	@Test
	public void testValidValuesWrongValue() {
		String[] toCheck = new String[]{VALUE_FAIL0};
		
		boolean result = testee.validValues(toCheck);

		assertThat(result, equalTo(false));
	}
	
	@Test
	public void testValidValuesWrongEmpty() {
		String[] toCheck = new String[]{};
		
		boolean result = testee.validValues(toCheck);

		assertThat(result, equalTo(false));
	}
	
	@Test
	public void testValidValuesWrongNull() {
		boolean result = testee.validValues(null);
		
		assertThat(result, equalTo(false));
	}
	// mock:
	// - Arguments everything set
	// - Option (emtpy should be enough()
	// tests for:
	// - getDelimiter() : String
	// - getValues() : String[][]
	// - validValue() : boolean
	// - validValues() : boolean
}
