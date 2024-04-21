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

package org.rubato.composer.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.core.util.TextUtils;

public class PluginManager {

    public static PluginManager getManager() {
        if (manager == null) {
            manager = new PluginManager();
        }
        return manager;
    }
    
    private PluginManager() {}
    
    
    public void addSearchDirectory(String dirName) {
        File file = new File(dirName);
        if (file.canRead() && file.isDirectory()) {
            searchPath.add(file);
        }
    }
    
    
    public void addHomePluginDirectory() {
        String home = System.getProperty("user.home"); 
        String sep = System.getProperty("file.separator"); 
        addSearchDirectory(home+sep+".rubato"+sep+"plugins"); 
        addSearchDirectory(home+sep+"Rubato"+sep+"Plugins"); 
    }
    
    
    public void loadPlugins() {
        for (File dir : searchPath) {
            File[] files = dir.listFiles(jarFilter);
            for (File file : files) {
                loadPluginJar(file);
            }
        }
    }
    
    public void displaySearchPath() {
        for (File file : searchPath) {
            System.out.println(file.getAbsolutePath());
        }
    }

    
    private void loadPluginJar(File file) {
        try {
            JarClassLoader loader = new JarClassLoader(file, getClass().getClassLoader());
            String[] plgs = loader.getListAttribute("Plugin"); 
            for (String plugin : plgs) {
                addPlugin(file, loader, plugin);
            }
        }
        catch (IOException e) {
            setError(PluginMessages.getString("PluginManager.couldnotloadfile"), file.getName());
        }
    }
    
    
    private void addPlugin(File file, ClassLoader loader, String className) {
        String fileName = file.getName();
        try {
            Class<?> cls = loader.loadClass(className);
            if (cls != null) {
                Object obj = cls.newInstance();
                if (obj != null) {
                    if (obj instanceof Rubette) {
                        rubettes.add((Rubette)obj);
                    }
                    else if (obj instanceof OldPlugin) {
                        addPlugin((OldPlugin)obj);
                    }
                    else {
                        setError(PluginMessages.getString("PluginManager.notaplugin"), fileName, className);
                    }
                }
                else {
                    setError(PluginMessages.getString("PluginManager.notaplugin"), fileName, className);
                }
            }
        }
        catch (ClassNotFoundException e) {
            setError(PluginMessages.getString("PluginManager.classnotfound"), fileName, className);
        }
        catch (IllegalAccessException e) {
            setError(PluginMessages.getString("PluginManager.illegalaccess"), fileName, className);
        }
        catch (InstantiationException e) {
            setError(PluginMessages.getString("PluginManager.couldnotinst"), fileName, className);
        }
        catch (Exception e) {
            setError(PluginMessages.getString("PluginManager.couldnotinst"), fileName, className);
        }
    }
    
    
    private void addPlugin(OldPlugin plugin) {
        plugins.add(plugin);
        if (plugin instanceof ModuleMorphismOldPlugin) {
            moduleMorphismPlugins.add((ModuleMorphismOldPlugin)plugin);
        }
    }
    
    
    private void setError(String string, Object ... objects) {
        errors.add(TextUtils.replaceStrings(string, objects));
    }
    
    
    public void clearErrors() {
        errors.clear();
    }
    
    
    public List<String> getErrors() {
        return errors;
    }
    
    
    public List<Rubette> getRubettes() {
        return rubettes;
    }
    
    
    public List<OldPlugin> getPlugins() {
        return plugins;
    }


    public List<ModuleMorphismOldPlugin> getModuleMorphismPlugins() {
        return moduleMorphismPlugins;
    }
    

    private LinkedList<File>    searchPath = new LinkedList<File>();
    private LinkedList<Rubette> rubettes = new LinkedList<Rubette>();
    private LinkedList<OldPlugin>  plugins = new LinkedList<OldPlugin>();
    private LinkedList<ModuleMorphismOldPlugin>  moduleMorphismPlugins = new LinkedList<ModuleMorphismOldPlugin>();
    private LinkedList<String>  errors = new LinkedList<String>();
    
    private static final FilenameFilter jarFilter = new FilenameFilter() {
        public boolean accept(File file, String name) {
            return name.endsWith(".jar"); 
        }
    };
    
    private static PluginManager manager;    
}
