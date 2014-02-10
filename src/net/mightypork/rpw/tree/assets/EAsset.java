package net.mightypork.rpw.tree.assets;


import java.io.File;

import net.mightypork.rpw.utils.files.FileUtils;


/**
 * Enum of asset filetypes
 * 
 * @author MightyPork
 */
public enum EAsset {

	//@formatter:off
	
	/* sound */
	SOUND("ogg"),
	
	/* images */
	IMAGE("png"),
	
	/* config files (ini based) */
	TEXT("txt"), LANG("lang"), PROPERTIES("properties"),
	INI("ini"), CFG("cfg"), // config files
	
	/* json based */
	MCMETA("mcmeta"), JSON("json"),
	
	/* vertex shader, fragment shader */
	VSH("vsh"), FSH("fsh"),
	
	/* unknown type */
	UNKNOWN("");
	
	//@formatter:on

	private EAsset(String extension) {

		this.extension = extension;
	}

	private String extension;


	public String getExtension() {

		return this.extension;
	}


	public boolean isText() {

		switch (this) {
			case TEXT:
			case LANG:
			case PROPERTIES:
			case INI:
			case CFG:
			case JSON:
				return true;
			default:
				return false;
		}
	}


	public boolean isImage() {

		return this == IMAGE;
	}


	public boolean isSound() {

		return this == SOUND;
	}


	public boolean isMeta() {

		return this == MCMETA;
	}


	public boolean isJson() {

		return this == JSON;
	}


	public boolean isShader() {

		return this == VSH || this == FSH;
	}


	public boolean isAsset() {

		return isText() || isImage() || isSound() || isJson();
	}


	/**
	 * Get if type is asset or meta<br>
	 * Used to filter out files not to be extracted from vanilla
	 * 
	 * @return is asset, meta or shader
	 */
	public boolean isAssetOrMeta() {

		// Shader is considered meta, because it has the same name
		// as the json file, only different extension. Which is a
		// common trait with mcmeta files.
		//
		// Future version may add shader editor to deal with them.

		return isMeta() || isAsset() || isShader();
	}


	public boolean isUnknown() {

		return this == UNKNOWN;
	}


	public static EAsset forExtension(String ext) {

		for (EAsset a : EAsset.values()) {
			if (a.extension.equalsIgnoreCase(ext)) {
				return a;
			}
		}

		return UNKNOWN;
	}


	public static EAsset forFile(File file) {

		return forExtension(FileUtils.getExtension(file));
	}


	public static EAsset forFile(String file) {

		return forExtension(FileUtils.getExtension(file));
	}
}
