/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  dewddtran.tr
 *  dprint.r
 *  org.bukkit.Server
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package dewddgravity;

import dewddgravity.DigEventListener2;
import dewddtran.tr;
import dprint.r;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin {
    Logger log; 
    DigEventListener2 ax = new DigEventListener2();

    public void onDisable() {
        this.getServer().getPluginManager().disablePlugin((Plugin)this);
        r.printAll((String)("ptdew&dewdd : " + tr.gettr((String)"unloaded_plugin") + "  dewdd gravity"));
    }
    
    public void onEnable() {
        this.log = this.getLogger();
        DigEventListener2.ac = this;
        this.getServer().getPluginManager().registerEvents((Listener)this.ax, (Plugin)this);
        r.printAll((String)("ptdew&dewdd : " + tr.gettr((String)"loaded_plugin") + "  dewdd gravity"));
    }
}

