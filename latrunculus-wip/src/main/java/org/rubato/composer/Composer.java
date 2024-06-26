/*
 * Copyright (C) 2005 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.rubato.composer;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.WindowConstants;

import org.vetronauta.latrunculus.client.plugin.icons.Icons;
import org.rubato.composer.plugin.PluginManager;
import org.rubato.composer.preferences.UserPreferences;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;


public class Composer implements WindowStateListener {

    private static String version = "0.1";
    
    protected JComposer       frame;
    protected Splash          splash;
    
    private RubetteManager  rubetteManager;
    private PluginManager   pluginManager;
    private UserPreferences userPrefs = UserPreferences.getUserPreferences();
    private Logger          logger;
    private String[]        args;
    
    
    public Composer(String[] args) {
        logger = Logger.getLogger("org.rubato.composer.composer");
        logger.info("Starting Rubato Composer");
        this.args = args;
    }

    
    protected void quit() {
        frame.quit();
    }

    
    public void windowStateChanged(WindowEvent e) {
        boolean oldState = (e.getOldState() & Frame.MAXIMIZED_BOTH) != 0;
        boolean newState = (e.getNewState() & Frame.MAXIMIZED_BOTH) != 0;
        if (oldState != newState) {
            userPrefs.setMaximized(newState);
            userPrefs.save();
        }
    }

    private class Loader implements Runnable {
        
        public Loader(RubetteLoader loader) {
            this.loader = loader;
        }
        
        public void run() {
            loader.loadBuiltins();
            loader.loadPlugins();
            for (String error : loader.getErrors()) {
                frame.setStatusError(error);
            }
            frame.clearStatus();
            frame.setVisible(true);
            splash.hideSplash();
            handleArgs();
        }
        
        private RubetteLoader loader;
    }
    
    
    public void start() {
        splash = new Splash();
        splash.showSplash();
        
        frame = new JComposer();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowStateListener(this);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                quit();
            }
        });
        frame.setIconImage(Icons.rubatoIcon.getImage());
        frame.pack();

        //
        // set initial preferences
        //
        
        // set window attributes according to saved preferences
        if (userPrefs.getGeometrySaved()) {
            Rectangle r = userPrefs.getGeometry();
            frame.setBounds(r.x, r.y, r.width, r.height);
        }
        if (userPrefs.getMaximized()) {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
        Rational.setDefaultQuantization(userPrefs.getDefaultQuantization());

        // create a rubette manager
        rubetteManager = RubetteManager.getManager();
        rubetteManager.setComposer(frame);
        frame.setRubetteManager(rubetteManager);        

        // create a plugin manager
        pluginManager = PluginManager.getManager();
        pluginManager.addHomePluginDirectory();
        
        // load everything (builtins, plugins)
        RubetteLoader loader = new RubetteLoader();
        loader.setRubetteManager(rubetteManager);
        loader.setPluginManager(pluginManager);
        loader.setSplash(splash);
        Thread thread = new Thread(new Loader(loader));
        thread.start();
    }
    
    
    protected void handleArgs() {
        boolean opening = false;
        for (String arg : args) {
            if (arg.endsWith(".rbo")) {
                // open project file
                File file = new File(arg);
                if (file.canRead()) {
                    frame.open(file);                    
                    opening = true;
                    break;
                }
                else {
                    frame.showErrorDialog(ComposerMessages.getString("Composer.couldnotopen"), arg);
                    break;
                }
            }
            else if (arg.startsWith("--")) {
            }
        }
        if (!opening) {
            // open a fresh network when no project has been loaded
            frame.newJNetwork();
        }
        frame.setChanged(false);
    }
    
    
        private static void usage() {
        System.out.println("Usage: composer [OPTION]... [FILE]");
        System.out.println("Start Rubato Composer and optionally open the project FILE");
        System.out.println("       --help     display this help and exit");
        System.out.println("       --version  output version information and exit");
    }
    
    
        private static void version() {
        System.out.println("Rubato Composer version "+version);
        System.out.println("Copyright (C) 2006 Gerard Milmeister");
        System.out.println("This is free software; see the source for copying conditions.  There is NO");
        System.out.println("warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE,");
        System.out.println("to the extent permitted by law.");
    }
    
    
        public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("--help")) {
                usage();
                return;
            }
            else if (arg.equals("--version")) {
                version();
                return;
            }
        }
        Composer composer = new Composer(args);
        composer.start();
    }
}
