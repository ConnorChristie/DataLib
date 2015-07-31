package com.tecno_wizard.tester;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import com.tecno_wizard.datalib.Database;
import com.tecno_wizard.datalib.DatabaseEntry;

public class TestPlugin extends JavaPlugin
{
	private Database database;
	
	public void onEnable()
	{
		database = new Database("tester");
		
		DatabaseEntry entry = database.getEntry("testDb");
		
		List<String> list = new ArrayList<String>();
		
		list.add("This is a string fools");
		list.add("ASasdsad This is a string fools");
		
		entry.set("myLetters", list);
		
		JSONObject obj = new JSONObject();
		obj.put("myLetters", "My letter to you");
		
		entry.set("me", obj);
		
		entry.save();
		
		System.out.println("Entry: " + entry.get("me.myLetters"));
		
		List<?> letters = (List<?>) entry.get("myLetters");
		
		System.out.println(StringUtils.join(letters, ", "));
	}
}