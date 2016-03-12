package de.bs.cli.jpar.extractor.type;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

@SuppressWarnings("rawtypes")
public class CollectionTypeTestForHashSet extends CollectionTypeTestBase<HashSet>{

	@Override
	protected Class<HashSet> getCollectionType() {
		return HashSet.class;
	}

	@Override
	protected Collection getCollectionInstance() {
		return new HashSet();
	}

	@Override
	protected Collection getWrongCollectionInstance() {
		return new LinkedList();
	}

}
