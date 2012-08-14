package it.gecko.utils;

import java.util.Map.Entry;

public class MapEntry<K, V> implements Entry<K, V>
{
	private K key;
	private V value;

	public MapEntry(K key)
	{
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getKey()
	 */
	@Override
	public K getKey()
	{
		return key;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getValue()
	 */
	@Override
	public V getValue()
	{
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#setValue(java.lang.Object)
	 */
	@Override
	public V setValue(V object)
	{
		value = object;
		return value;
	}

}
