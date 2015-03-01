package io.macgyver.plugin.jython;

import org.python.core.Options;

public class ImportSiteWorkaround {

	public ImportSiteWorkaround() {
		Options.importSite=false;
	}
}
