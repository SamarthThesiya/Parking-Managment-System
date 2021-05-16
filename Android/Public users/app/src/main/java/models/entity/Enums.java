package models.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Enums implements Serializable{

	@SerializedName("name")
	public String name;

	@SerializedName("id")
	public int id;

	@SerializedName("display_name")
	public String displayName;

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public String getDisplayName(){
		return displayName;
	}
}