package com.tecno_wizard.datalib;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tecno_wizard.tester.Tester;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.lang.System;

/*
file naming structure:
a-k/
l-z/
0-9/
templates/filename
 */

/**
 * Created by Ethan on 6/20/2015.
 */
public class Database
{
	private JSONObject template;
	private final String databasePath;
	private FileReader reader;
	private JSONParser parser = new JSONParser();
	
	public Database(String name)
	{
		this.databasePath = "database/" + name + "/";
		checkFiles();
	}
	
	private void checkFiles()
	{
		File file = new File(databasePath);
		if (!file.exists() || !file.isDirectory())
			file.mkdirs();
	}
	
	/**
	 * Gets the database entry with the file name. All that is needed it the
	 * name of the file, not the path. If the entry does not yet exist, one will
	 * be created.
	 * 
	 * @param fileName
	 *            file name
	 * @return DatabaseEntry with the file name. If it does not exist, it will
	 *         be created.
	 */
	public DatabaseEntry getEntry(String fileName)
	{
		File file = null;
		fileName += ".json";
		try
		{
			file = new File(databasePath + fileName);
			System.out.println(file.exists());
			if (!file.exists())
				file.createNewFile();
			reader = new FileReader(databasePath + fileName);
			return new DatabaseEntry((JSONObject) parser.parse(reader), file);
		} catch (IOException e)
		{
			return new DatabaseEntry(new JSONObject(), file);
		} catch (ParseException e)
		{
			return new DatabaseEntry(new JSONObject(), file);
		} finally
		{
			try
			{
				reader.close();
			} catch (IOException e)
			{
			}
		}
	}
	
	public void setDefaultTemplate()
	{
	
	}
	
	public void getDefaultTemplate()
	{
	
	}
	
	public static void main(String[] args)
	{
		Database base = new Database("tester");
		DatabaseEntry entry = base.getEntry("1");
		entry.set("test", new Tester("one", 2, true));
	}
}
