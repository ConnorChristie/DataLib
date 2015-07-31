package com.tecno_wizard.datalib;

import java.lang.Object;import java.lang.String;import java.util.Map;

/**
 * DatabaseSerializable is the interface necessary needed to serialize the object in a database entry.
 *
 * In addition to implementing serialize, the implementing class <b><I>must</I></b> also have a constructor accepting a Map of the generic String, Object.
 * It is also recommended that a static method accepting a Map of the generic <String, Object> and returning whether the serialized data is of the class named <b><i>isValueOf</i></b> is implemented. A simple
 * way to check this is to have index 0 of the map hold the class name as a String (Ex. com.name.project.ClassName) mapped to the Object's hash code. This
 * value is not used by DataLib, but may be in the future.</li></ul>
 *
 * <p>Not sure if you have done this correctly? Go to <a href="url">Tecno_Wizard's site</a> for a detailed explanation of this process.</p>
 *
 * <p>This is necessary because Java Objects cannot be directly serialized into/deserialized out of JSON. Their values as a map is the only valid input.
 * java.lang is exempt.</p>
 */ //TODO update to contain testing class and correct URL
public interface DatabaseSerializable {
    public Map<String, Object> serialize();
}
