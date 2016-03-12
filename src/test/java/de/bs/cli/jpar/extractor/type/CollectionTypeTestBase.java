package de.bs.cli.jpar.extractor.type;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedValues;
import de.bs.cli.jpar.process.Parameters;

@SuppressWarnings("rawtypes")
public abstract class CollectionTypeTestBase<T extends Collection> {
	private CollectionType testee;
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
	
	protected abstract Class<T> getCollectionType();
	
	protected abstract Collection getCollectionInstance();
	
	protected abstract Collection getWrongCollectionInstance();
		
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
		testee = new CollectionType(getCollectionType(), mockExtractedOption());
		testee.setValues(mockExtractedValuesDelimiterArguments());
	}
	
	// type check test
	@Test
	public void testCollectionTestClass() {
		Class<T> collectionClass = getCollectionType();
		
		assertThat("Class '" + collectionClass + "' is not a subclass of '" + Collection.class + "'.", 
				Collection.class.isAssignableFrom(collectionClass), equalTo(true));
		
	}
	
	// testcases:Type (superclass)
	@Test
	public void testGetOption() {
		ExtractedOption returned = testee.getOption();
		
		assertThat(returned, equalTo(option));
	}
	
	@SuppressWarnings({ "unchecked" })
	@Test
	public void testGetTargetType() {
		Class<T> returned = (Class<T>)testee.getTargetType();
		
		assertThat(returned, equalTo((Class)getCollectionType()));
	}
	
	@Test
	public void testWithSourceType() {
		Class returned = (Class)testee.getOption().getSourceType();
		
		assertThat(returned, equalTo((Class)SOURCE_TYPE));
	}
	
	@SuppressWarnings({ "unchecked", })
	@Test(expected=JParException.class)
	public void testWithoutSourceType() {
		option = mockExtractedOption();
		when(option.getSourceType()).thenReturn((Class)Void.class);
		
		testee = new CollectionType(getCollectionType(), option);
		
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
		Boolean returned = testee.isAssignable(getWrongCollectionInstance());
		
		assertThat(returned, equalTo(Boolean.FALSE));
	}
	
	@Test
	public void testIsAssignableWithSet() {
		Boolean returned = testee.isAssignable(getCollectionInstance());
		
		assertThat(returned, equalTo(Boolean.TRUE));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgs2AWithValues() {
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_2A});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_2A, args);

		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((T)result).size(), equalTo(2));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_A2));
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
		
		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((T)result).size(), equalTo(1));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_B0));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgs2AWithoutValuesProperty() {
		testee = new CollectionType(getCollectionType(), mockExtractedOption());
		testee.setValues(mockExtractedValuesDelimiterOnly());
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_2A});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_2A, args);
		
		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((T)result).size(), equalTo(2));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_A2));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgsABWithoutValuesProperty() {
		testee = new CollectionType(getCollectionType(), mockExtractedOption());
		testee.setValues(mockExtractedValuesDelimiterOnly());
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_AB});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_AB, args);
		
		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((T)result).size(), equalTo(2));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_B0));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgsBWithoutValuesProperty() {
		testee = new CollectionType(getCollectionType(), mockExtractedOption());
		testee.setValues(mockExtractedValuesDelimiterOnly());
		when(values.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_B});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ALLOWED_VALUE_B0, args);
		
		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((T)result).size(), equalTo(1));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_B0));
	}
}
