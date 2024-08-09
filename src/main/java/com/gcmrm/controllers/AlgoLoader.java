package com.gcmrm.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;

import com.gcmrm.models.Algo;

/**
 * Controller class used to import, load and manage algorithms.
 */
public class AlgoLoader {
	
	/***
	 * Load the jar implementing interface Algo
	 * @param pathToScan path to the directory containing the jar
	 * @return an ArrayList of all the found Algo
	 */
	public static ArrayList<Algo> getAlgoList(String pathToScan) {
		
		ArrayList<Algo> plugins = new ArrayList<Algo>();
		
		//directory where we search the plugins
		File pluginDirectory=new File(pathToScan); 
	    if(!pluginDirectory.exists())pluginDirectory.mkdir();
	    
	    File[] files=pluginDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
	    
	    if(files!=null && files.length>0)
	    {
	    	//all the .class files 
	        ArrayList<String> classes=new ArrayList<>();

	        //path of the jar files
	        URL url=null;
	        
	        for(File file:files)
	        {
	        	//for each jar we look for the .class files 
	        	//then we select the ones implementing interface Algo
	            JarFile jar;
				try {

					jar = new JarFile(file);
					jar.stream().forEach(jarEntry -> {
		                if(jarEntry.getName().endsWith(".class"))
		                {	
		                	classes.add(jarEntry.getName()); 
		                }
		            });
					url=file.toURI().toURL();
		            
		            jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//look the .class files and if they implement Algo => we store them
				URLClassLoader urlClassLoader=new URLClassLoader(new URL [] {url});
				
	            classes.forEach(className->{
	                try
	                {
	                    Class<?> cls=urlClassLoader.loadClass(className.replaceAll("/",".").replace(".class",""));
	                    Class<?>[] interfaces=cls.getInterfaces();
	                    for(Class<?> intface:interfaces)
	                    {
	                    	
	                        if(intface.equals(Algo.class)) //compare the path of both interfaces 
	                        {
	                            Algo plugin=(Algo) cls.getDeclaredConstructor().newInstance();
	                            plugins.add(plugin);
	                        }
	                    }
	                }
	                catch (Exception e){e.printStackTrace();}
	            });
	            
	            try {
					urlClassLoader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            //we delete the .class file we found and go check the next jar
	            classes.clear();
	            
	        }
	    }
	    
	    return plugins;
	}
}
