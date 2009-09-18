package org.workcraft.gui.propertyeditor;

import org.workcraft.Config;

public interface PersistentPropertyEditable extends PropertyEditable  {
	public void storePersistentProperties (Config config);
	public void loadPersistentProperties (Config config);

	public String getSection();
}
