package de.bs.cli.jpar.extractor.type;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

@SuppressWarnings("rawtypes")
public class CollectionTypeTestForLinkedList extends CollectionTypeTestBase<LinkedList> {

	@Override
	protected Class<LinkedList> getCollectionType() {
		return LinkedList.class;
	}

	@Override
	protected Collection getCollectionInstance() {
		return new LinkedList();
	}

	@Override
	protected Collection getWrongCollectionInstance() {
		return new HashSet();
	}

}
