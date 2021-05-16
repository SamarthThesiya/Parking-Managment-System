package models.responseModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class DocumentResponse implements BaseModel {

	@SerializedName("extension")
	private String extension;

	@SerializedName("document_type_id")
	private String documentTypeId;

	@SerializedName("owner_id")
	private int ownerId;

	@SerializedName("file_name")
	private String fileName;

	@SerializedName("external_storage_url")
	private String externalStorageUrl;

	@SerializedName("id")
	private int id;

	@SerializedName("file_size")
	private int fileSize;

	public String getExtension(){
		return extension;
	}

	public String getDocumentTypeId(){
		return documentTypeId;
	}

	public int getOwnerId(){
		return ownerId;
	}

	public String getFileName(){
		return fileName;
	}

	public String getExternalStorageUrl(){
		return externalStorageUrl;
	}

	public int getId(){
		return id;
	}

	public int getFileSize(){
		return fileSize;
	}
}