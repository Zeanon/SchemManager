package de.zeanon.schemmanager.globalutils;

import com.rylinaux.plugman.util.PluginUtil;
import de.zeanon.schemmanager.SchemManager;

class PlugManUtils {

    static void plugmanReload() {
        PluginUtil.reload(SchemManager.getInstance());
    }
}
