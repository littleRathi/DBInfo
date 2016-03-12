package de.bs.cli.jpar.extractor.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("rawtypes")
public class CollectionTypeTestForArrayList extends CollectionTypeTestBase<ArrayList> {

	@Override
	protected Class<ArrayList> getCollectionType() {
		return ArrayList.class;
	}

	@Override
	protected Collection getCollectionInstance() {
		return new ArrayList();
	}

	@Override
	protected Collection getWrongCollectionInstance() {
		return new HashSet();
	}

}
