package com.tecno_wizard.datalib;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.lang.System;
import java.lang.Throwable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ethan on 6/20/2015.
 */
public class DatabaseEntry
{
	protected JSONObject jsonObject;
	protected BufferedWriter writer;
	
	protected DatabaseEntry(JSONObject jsonObject, File file)
	{
		this.jsonObject = jsonObject;
		try
		{
			this.writer = new BufferedWriter(new FileWriter(file, false));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------------------------------
	// ---------------------------Getters---------------------------------
	// -------------------------------------------------------------------
	
	/**
	 * Gets the key set of the entry
	 * 
	 * @return key set of the entry
	 */
	public Set getKeySet()
	{
		return jsonObject.keySet();
	}
	
	/**
	 * Gets the value set of the entry
	 * 
	 * @return Collection of values
	 */
	public Collection getValueCollection()
	{
		return jsonObject.values();
	}
	
	protected void printJSON()
	{
		System.out.println(jsonObject.toJSONString());
	}
	
	/**
	 * Saves the DatabaseEntry.
	 */
	public void save()
	{
		try
		{
			System.out.println(jsonObject.toJSONString());
			writer.write(jsonObject.toJSONString());
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				writer.flush();
			} catch (IOException e)
			{
				e.printStackTrace();
				System.out.println("WARNING: FILE WAS NOT SAVED");
			}
		}
	}
	
	/**
	 * Changes a primitive to its JSON representation and maps it to the given
	 * key. If the key already exists, it will be replaced.
	 * 
	 * @param key
	 *            key to map
	 * @param value
	 *            primitive to map
	 */
	@SuppressWarnings("unchecked")
	public void set(String key, Object value)
	{
		if (value instanceof DatabaseSerializable)
		{
			jsonObject.put(key, ((DatabaseSerializable) value).serialize());
		} else if (value.getClass().isArray())
		{
			JSONArray arr = new JSONArray();
			arr.addAll(Arrays.asList(convertToObjectArray(value)));
			
			jsonObject.put(key, arr);
		} else if (value instanceof List)
		{
			JSONArray arr = new JSONArray();
			arr.addAll((List<?>) value);
			
			jsonObject.put(key, arr);
		} else
		{
			jsonObject.put(key, value);
		}
	}
	
	private Object[] convertToObjectArray(Object array)
	{
		Class ofArray = array.getClass().getComponentType();
		if (ofArray.isPrimitive())
		{
			List ar = new ArrayList();
			int length = Array.getLength(array);
			
			for (int i = 0; i < length; i++)
			{
				ar.add(Array.get(array, i));
			}
			
			return ar.toArray();
		} else
		{
			return (Object[]) array;
		}
	}
	
	/**
	 * Gets the value mapped to the key. Note that this is generic, meaning that
	 * all types are immediately casted to the desired type. If the mapped value
	 * does not exist or is not an instance of the intended class, null will be
	 * returned. For example, ChatColor color = entry.get("color") will return
	 * the ChatColor if it is a chat color, and null if it is not or the key
	 * "color" is not mapped to anything. Null can also signify that the key is
	 * explicitly mapped to null. Note that this does not elicit a
	 * deserialization for classes implementing DatabaseSerializable.
	 * 
	 * @param key
	 *            key to get the mapped value of
	 * @return
	 */
	public Object get(String key)
	{
		String[] spl = key.split("\\.");
		
		if (spl.length > 0)
		{
			Object parent = jsonObject.get(spl[0]);
			
			for (int i = 1; i < spl.length; i++)
			{
				if (i == spl.length - 1)
				{
					return ((JSONObject) parent).get(spl[i]);
				} else if (parent instanceof JSONObject)
				{
					JSONObject par = (JSONObject) parent;
					
					parent = par.get(spl[i]);
					
					if (!(parent instanceof JSONObject))
					{
						return null;
					}
				}
			}
			
			return null;
		}
		
		return null;
	}
	
	/**
	 * Gets the value mapped to the key. Note that this is generic, meaning that
	 * all types are immediately casted to the Class type If the mapped value
	 * does not exist or is not an instance of type, null will be returned with
	 * the exception of classes implementing DatabaseSerializable, which will be
	 * converted back from a map if possible.
	 * 
	 * @param key
	 *            key to get the mapped value of
	 * @param <T>
	 *            Class type to return.
	 * @param type
	 *            Class to attempt cast to
	 * @return
	 */
	public <T> T get(String key, Class<T> type)
	{
		Object obj = jsonObject.get(key);
		if (type.isInstance(obj))
			return (T) obj;
			
		// if obj is a map or the parsed data of obj is a map (obj is set to
		// parsed data)
		else if (obj instanceof Map || (obj instanceof String && (obj = JSONValue.parse((String) obj)) instanceof Map))
		{
			Class[] clazzes = type.getInterfaces();
			for (Class clazz : clazzes)
			{
				if (clazz.equals(DatabaseSerializable.class))
				{
					try
					{
						return type.getConstructor(Map.class).newInstance(obj);
					} catch (java.lang.Exception exception)
					{
						System.out.println("Error: Deserialization of class " + type.getName() + " was attempted but"
								+ " failed. DatabaseSerializable has not been implemented correctly or the key is not mapped" + " to the object that was specified for deserialization.");
						return null;
					}
				}
			}
		}
		return null;
	}
	
	// public void
	
	/**
	 * Removes the mapping of the given key
	 * 
	 * @param key
	 *            key to remove mapping of
	 * @return true if a field was removed, false if not.
	 */
	public boolean remove(Object key)
	{
		return jsonObject.remove(key) != null;
	}
	
	/**
	 * Gets whether or not the value mapped to var key is assignable from the
	 * var type.
	 * 
	 * @param type
	 *            Class to check for class equality
	 * @param key
	 *            key that the value is mapped to
	 * @return If type is the same class as the value mapped to var key, true,
	 *         else false
	 */
	public boolean isAssignableFrom(Class type, Object key)
	{
		return type.getClass().isAssignableFrom(jsonObject.get(key).getClass());
	}
	
	/**
	 * Gets whether or not the value mapped to var key is an instance of the var
	 * type.
	 * 
	 * @param type
	 *            Class to use for instanceof comparison
	 * @param key
	 *            key that the value is mapped to
	 * @return true key instanceof
	 */
	public boolean isInstanceOf(Class type, Object key)
	{
		return type.getClass().isInstance(jsonObject.get(key).getClass());
	}
	
	/**
	 * Gets whether or not a value is mapped to the specified key
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(Object key)
	{
		return jsonObject.get(key) != null;
	}
	
	protected void finalize() throws Throwable
	{
		super.finalize();
		writer.close();
	}
}
