package de.bs.cli.jpar.extractor.type;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class CollectionTypeTestForSet extends CollectionTypeTestBase<Set>{

	@Override
	protected Class<Set> getCollectionType() {
		return Set.class;
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
