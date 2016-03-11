package de.bs.cli.jpar.extractor.type;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.StringContains.containsString;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.isNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedValues;
import de.bs.cli.jpar.process.Parameters;

public class CollectionTypeForSetTest {
	private CollectionType testee;
	@SuppressWarnings("rawtypes")
	private static final Class<Set> TESTEE_TARGET_TYPE = Set.class;
	@SuppressWarnings("rawtypes")
	private static final Class SOURCE_TYPE = String.class;
	
	private ExtractedOption option;
	private ExtractedValues values;
	
	private static final String EXTRACTED_ARGUMENT_ARG_NAME = "-test:";
	
	private static final String ASSIGNABLE_STRING = "abcdefg";
	private static final Object ASSIGNABLE_OBJECT = new Object();
	
	private static final String ARGUMENT_2A = "a00;a02";
	private static final String ARGUMENT_AB = "a00;b00";
	private static final String ARGUMENT_B = "b00";
	
	private static final String DELIMITER = ";";
	private static final String ALLOWED_VALUE_A0 = "a00";
	private static final String ALLOWED_VALUE_A1 = "a01";
	private static final String ALLOWED_VALUE_A2 = "a02";
	private static final String ALLOWED_VALUE_B0 = "b00";
	private static final String[][] ALLOWED_VALUES = new String[][]{
		{ALLOWED_VALUE_A0, ALLOWED_VALUE_A1, ALLOWED_VALUE_A2},{ALLOWED_VALUE_B0}};
	
	private ExtractedValues mockExtractedValuesDelimiterArguments() {
		values = mock(ExtractedValues.class);
		when(values.getDelimiter()).thenReturn(DELIMITER);
		when(values.getOption()).thenReturn(option);
		when(values.getValues()).thenReturn(ALLOWED_VALUES);
		
		return values;
	}
	
	private ExtractedValues mockExtractedValuesDelimiterOnly() {
		values = mock(ExtractedValues.class);
		when(values.getDelimiter()).thenReturn(DELIMITER);
		when(values.getOption()).thenReturn(option);
		
		return values;
	}
	
	@SuppressWarnings("unchecked")
	private ExtractedOption mockExtractedOption() {
		option = mock(ExtractedOption.class);
		when(option.getOptionName()).thenReturn(EXTRACTED_ARGUMENT_ARG_NAME);
		when(option.getManuelDescription()).thenReturn("Some meaningless text");
		when(option.getDelimiter()).thenReturn(DELIMITER);
		when(option.getSourceType()).thenReturn(SOURCE_TYPE);
		
		return option;
	}
	
	@Before
	public void setupTest() {
		testee = new CollectionType(TESTEE_TARGET_TYPE, mockExtractedOption());
		testee.setValues(mockExtractedValuesDelimiterArguments());
	}
	
	// testcases:Type (superclass)
	@Test
	public void testGetOption() {
		ExtractedOption returned = testee.getOption();
		
		assertThat(returned, equalTo(option));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetTargetType() {
		@SuppressWarnings("rawtypes")
		Class<Set> returned = (Class<Set>)testee.getTargetType();
		
		assertThat(returned, equalTo(TESTEE_TARGET_TYPE));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testWithSourceType() {
		Class returned = (Class)testee.getOption().getSourceType();
		
		assertThat(returned, equalTo((Class)SOURCE_TYPE));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(expected=JParException.class)
	public void testWithoutSourceType() {
		option = mockExtractedOption();
		when(option.getSourceType()).thenReturn((Class)Void.class);
		
		testee = new CollectionType(TESTEE_TARGET_TYPE, option);
		
		fail();
	}
	
	// testcases:CollectionType
	@Test
	public void testUsageDescription() {
		StringBuilder sb = new StringBuilder();
		testee.getManualDescription(sb);
		String description = sb.toString();
		
		assertThat(description, containsString(EXTRACTED_ARGUMENT_ARG_NAME));
		assertThat(description, containsString(SOURCE_TYPE.getSimpleName()));
		assertThat(description, containsString(DELIMITER));
	}
	
	@Test
	public void testIsAssignableWithString() {
		Boolean returned = testee.isAssignable(ASSIGNABLE_STRING);
		
		assertThat(returned, equalTo(Boolean.FALSE));
	}
	
	@Test
	public void testIsAssignableWithObject() {
		Boolean returned = testee.isAssignable(ASSIGNABLE_OBJECT);
		
		assertThat(returned, equalTo(Boolean.FALSE));
	}
	
	@Test
	public void testIsAssignableWithList() {
		Boolean returned = testee.isAssignable(new ArrayList<String>());
		
		assertThat(returned, equalTo(Boolean.FALSE));
	}
	
	@Test
	public void testIsAssignableWithSet() {
		Boolean returned = testee.isAssignable(new HashSet<String>());
		
		assertThat(returned, equalTo(Boolean.TRUE));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgs2AWithValues() {
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_2A});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_2A, args);

		assertThat(result, instanceOf(Set.class));
		assertThat(((Set<String>)result).size(), equalTo(2));
		assertThat(((Set<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_A2));
	}
	
	@Test(expected=JParException.class)
	public void testProcessArgsABWithValues() {
		when(values.validValues((String[])isNotNull())).thenReturn(false);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_AB});
		testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_AB, args);
		
		fail();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgsBWithValues() {
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_B});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_B, args);
		
		assertThat(result, instanceOf(Set.class));
		assertThat(((Set<String>)result).size(), equalTo(1));
		assertThat(((Set<String>)result), hasItems(ALLOWED_VALUE_B0));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgs2AWithoutValuesProperty() {
		testee = new CollectionType(TESTEE_TARGET_TYPE, mockExtractedOption());
		testee.setValues(mockExtractedValuesDelimiterOnly());
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_2A});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_2A, args);
		
		assertThat(result, instanceOf(Set.class));
		assertThat(((Set<String>)result).size(), equalTo(2));
		assertThat(((Set<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_A2));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgsABWithoutValuesProperty() {
		testee = new CollectionType(TESTEE_TARGET_TYPE, mockExtractedOption());
		testee.setValues(mockExtractedValuesDelimiterOnly());
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_AB});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_AB, args);
		
		assertThat(result, instanceOf(Set.class));
		assertThat(((Set<String>)result).size(), equalTo(2));
		assertThat(((Set<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_B0));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgsBWithoutValuesProperty() {
		testee = new CollectionType(TESTEE_TARGET_TYPE, mockExtractedOption());
		testee.setValues(mockExtractedValuesDelimiterOnly());
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_B});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ALLOWED_VALUE_B0, args);
		
		assertThat(result, instanceOf(Set.class));
		assertThat(((Set<String>)result).size(), equalTo(1));
		assertThat(((Set<String>)result), hasItems(ALLOWED_VALUE_B0));
	}
}
