package com.tecno_wizard.tester;

import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

import com.tecno_wizard.datalib.DatabaseSerializable;

/**
 * Created by Ethan Zeigler on 7/20/2015 for BukkitPlayerDatabase.
 */
public class Tester implements DatabaseSerializable
{
	private String one;
	private int two;
	private boolean three;
	
	public Tester(String one, int two, boolean three)
	{
		this.one = one;
		this.two = two;
		this.three = three;
	}
	
	@Override
	public Map<String, Object> serialize()
	{
		Map map = new HashMap<String, Object>();
		map.put("Class name", this.getClass().getName());
		map.put("one", one);
		map.put("two", two);
		map.put("three", three);
		return map;
	}
	
	public static Tester deserialize(Map<String, Object> serializedData)
	{
		if (serializedData.get("Class name").equals(Tester.class.getName()))
		{
			return new Tester((String) serializedData.get("one"), (Integer) serializedData.get("two"), (java.lang.Boolean) serializedData.get("three"));
		} else
		{
			return null;
		}
	}
	
	public static boolean isValueOf(Map<String, Object> serializedData)
	{
		return serializedData.get("Class name").equals(Tester.class.getName());
	}
}
