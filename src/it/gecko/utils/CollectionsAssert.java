package it.gecko.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

public class CollectionsAssert extends Assert
{
	public static <K, V> void assertMapsEquals(Map<K, V> expected, Map<K, V> actual)
	{
		assertEquals(expected.size(), actual.size());

		Iterator<Entry<K, V>> it1 = expected.entrySet().iterator();
		Iterator<Entry<K, V>> it2 = actual.entrySet().iterator();

		while (it1.hasNext() && it2.hasNext())
		{
			assertEquals(it1.next().getKey(), it2.next().getKey());
			assertEquals(it1.next().getValue(), it2.next().getValue());
		}
	}

}
