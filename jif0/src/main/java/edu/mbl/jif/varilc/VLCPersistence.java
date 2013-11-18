/*
 * VLCPersistence
 */
package edu.mbl.jif.varilc;

/**
 *
 * @author GBH
 */
public interface VLCPersistence {

	String chooseFileLoad();

	String chooseFileSave();

	public void saveVLCSettings();

	public void loadVLCSettings();
}
