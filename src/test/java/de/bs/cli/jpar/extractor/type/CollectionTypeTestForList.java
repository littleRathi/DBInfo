package de.bs.cli.jpar.extractor.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CollectionTypeTestForList extends CollectionTypeTestBase<List> {

	@Override
	protected Class<List> getCollectionType() {
		return List.class;
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
