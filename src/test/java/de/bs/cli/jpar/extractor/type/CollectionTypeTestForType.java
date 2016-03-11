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

import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;

import org.junit.Test;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;

public class CollectionTypeTestForType {
	private CollectionType testee;
	
	private ExtractedOption option;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ExtractedOption mockExtractedOption(final Class sourceType) {
		ExtractedOption option = mock(ExtractedOption.class);
		when(option.getSourceType()).thenReturn(sourceType);
		when(option.getDelimiter()).thenReturn(";");
		return option;
	}
	
	// check target type
	@Test(expected=JParException.class)
	public void testTargetTypeVoid() {
		testee = new CollectionType(Void.class, mockExtractedOption(String.class));
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testTargetTypevoid() {
		testee = new CollectionType(void.class, mockExtractedOption(String.class));
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testTargetTypeObject() {
		testee = new CollectionType(Object.class, mockExtractedOption(String.class));
		
		fail();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testTargetTypeHashSet() {
		testee = new CollectionType(HashSet.class, mockExtractedOption(String.class));
		
		assertThat(testee.getTargetType(), equalTo((Class)HashSet.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testTargetTypeSet() {
		testee = new CollectionType(Set.class, mockExtractedOption(String.class));
		
		assertThat(testee.getTargetType(), equalTo((Class)Set.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testTargetTypeArrayList() {
		testee = new CollectionType(ArrayList.class, mockExtractedOption(String.class));
		
		assertThat(testee.getTargetType(), equalTo((Class)ArrayList.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testTargetTypeLinkedList() {
		testee = new CollectionType(LinkedList.class, mockExtractedOption(String.class));
		
		assertThat(testee.getTargetType(), equalTo((Class)LinkedList.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testTargetTypeList() {
		testee = new CollectionType(List.class, mockExtractedOption(String.class));
		
		assertThat(testee.getTargetType(), equalTo((Class)List.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testTargetTypePriorityQueue() {
		testee = new CollectionType(PriorityQueue.class, mockExtractedOption(String.class));
		
		assertThat(testee.getTargetType(), equalTo((Class)PriorityQueue.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testTargetTypeCollection() {
		testee = new CollectionType(Collection.class, mockExtractedOption(String.class));
		
		assertThat(testee.getTargetType(), equalTo((Class)Collection.class));
	}
	
	// check source type
	@SuppressWarnings("rawtypes")
	@Test
	public void testSourceTypeString() {
		testee = new CollectionType(Set.class, mockExtractedOption(String.class));
		
		assertThat(testee.getOption().getSourceType(), equalTo((Class)String.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testSourceTypeFile() {
		testee = new CollectionType(Set.class, mockExtractedOption(File.class));
		
		assertThat(testee.getOption().getSourceType(), equalTo((Class)File.class));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testSourceTypeInteger() {
		testee = new CollectionType(Set.class, mockExtractedOption(Integer.class));
		
		assertThat(testee.getOption().getSourceType(), equalTo((Class)Integer.class));
	}
	
	@Test(expected=JParException.class)
	public void testSourceTypeObject() {
		testee = new CollectionType(Set.class, mockExtractedOption(Object.class));
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testSourceTypeList() {
		testee = new CollectionType(Set.class, mockExtractedOption(List.class));
		
		fail();
	}
}
