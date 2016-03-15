package de.bs.cli.jpar.extractor;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.extractor.type.Type;
import de.bs.cli.jpar.process.Parameters;

public abstract class ExtractedOptionBaseTest {
	protected ExtractedOption testee;
	
	protected Option option;
	
	protected abstract String getName();
	protected abstract Class<?> getTargetType();
	protected abstract String getTargetName();
	protected abstract Class<?> getSourceType();
	protected abstract String getManualDecription();
	protected abstract boolean getRequired();
	
	
	@Test
	public void testGetELName() {
		String result = testee.getElName();
		
		assertThat(result, equalTo(getName().toUpperCase()));
	}
	
	@Test
	public void testGetName() {
		String result = testee.getName();
		
		assertThat(result, equalTo(getName()));
	}
	
	@Test
	public void testGetOptionName() {
		String result = testee.getOptionName();
		
		assertThat(result, equalTo("-" + getName()));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetTargetType() {
		Class<?> result = testee.getTargetType();
		
		assertThat(result, equalTo((Class)getTargetType()));
	}
	
	@Test
	public void testGetTargetName() {
		String result = testee.getTargetName();
		
		assertThat(result, equalTo(getTargetName().toUpperCase()));
	}
	
	@Test
	public void testGetOption() {
		Option result = testee.getOption();
		
		assertThat(result, equalTo(option));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetSourceType() {
		Class<?> result = testee.getSourceType();
		
		assertThat(result, equalTo((Class)getSourceType()));
	}
	
	@Test
	public void testGetManualDescription() {
		String result = testee.getManualDescription();
		
		assertThat(result, equalTo(getManualDecription()));
	}
	
	@Test
	public void testIsRequired() {
		boolean result = testee.isRequired();
		
		assertThat(result, equalTo(getRequired()));
	}
	
	@Test
	public void testGetType() {
		Type type = testee.getType();
		
		assertThat(type, notNullValue());
	}
	
	@Test
	public void testProcessArg() {
//		String argument = null;
//		Parameters args = new Parameters(new String[]{argument});
//		testee.processArg(this, "-" + getName(), argument, args);
		fail();
	}
}
