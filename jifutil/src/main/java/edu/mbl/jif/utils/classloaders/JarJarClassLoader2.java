/*
 * JarJarClassLoader22.java
 *
 * Created on January 5, 2007, 1:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.utils.classloaders;
/*
 * Copyright 2006 The MITRE Corporation. All rights reserved.
 * Written by Ayal Spitz
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1
 * (the "License"); you may not use this file except in compliance
 * with the License and the following two conditions:
 * 
* The US Government will not be charged any license fee and/or royalties 
* related to this software.
* 
* Neither name of The MITRE Corporation; nor the names of its contributors
* may be used to endorse or promote products derived from this software
* without specific prior written permission.
 
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/copyleft/lesser.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The JarJarClassLoader2 is a customized ClassLoader that loads classes from a
 * compressed file. In addition, this class loader is capable of dealing with 
 * Jar files inside of master Jar file. Using the JarJarClassLoader2 it's possible
 * to include a projects libraries inside a master Jar file.
 * 
 * @author 	Ayal Spitz
 * @version 1.1.6	- Dec 20,2005	- A little cleanup
 * version 1.1.5	- Dec 16,2005	- Changed the zip entry read mechanisim from 
 * 									1 byte at a time to an array of 1024 at a time
 * version 1.1.4	- Dec 16,2005	- Changed how we read entries from reading into a
 * 									set byte array to a ByteArrayOutputStream. Seems to work
 * version 1.1.3	- Dec 16,2005	- Removed buffering of input stream in constructor
 * 									One less thing to worry about
 * version 1.1.2	- Dec 16,2005	- New temp bug fix for zip entries with unknown size
 * 									We don't break anymore but check at the same time we
 * 									check if the entry is a directory
 * version 1.1.1	- Dec 15,2005 	- Updated 'main' to include use instructions
 * 									when there aren't enough 'args'
 * 									+ Added comments to 'main'
 * version 1.1 		- Dec 15,2005 	- Added new main1
 * 									+ Temp bug fix for zip entries with an unknown size
 * 									+ Access problem found : Not fixed
 * version	1.0 	- Dec 8,2005 	- Nice and Stable
 *
 */

public class JarJarClassLoader2 extends ClassLoader{
// ### Class Fields ###########################################################
	private String							src = null;

	private Hashtable<String,byte[]>		rawTable = null;
	private Set<JarJarClassLoader2>			childClassLoaderSet = null;

	private static JarJarClassLoader2Mgr		mgr = null;

// ############################################################################
	/**
	 * This is a wrapper 'main'. This main can be called uppon to activate
	 * other main classes of an application. This main reads the first two
	 * arguments of the passed in arguments and the passes the remaining 
	 * argumers to the called 'main'
	 */	
	public static void main(String... args){
		String[]			newArgs = null;
		
		if (args.length < 2){
			System.out.println("Not enough arguments passed in!");
			System.out.println("[srcJarFile] [targetClass] [targetArgs]");
			System.out.println("+ srcJarFile  : This is the Jar file in which" + 
				" the JarJarClassLoader2 is embedded");
			System.out.println("+ targetClass : This is the full qualified" +
				" target class name i.e. spitz.ayal.bob.MainClass");
			System.out.println("+ targetArgs  : This is a list of arguments to" +
				" be passed on to the targetClass");
			return;
		}
		
		newArgs = new String[args.length - 2];
		for(int i=2;i<args.length;i++){ newArgs[i-2] = args[i]; }

		if (args[0].equalsIgnoreCase("-C")){
			try{
				JarJarClassLoader2	loader = null;
				Class				clazz = null;

				loader = new JarJarClassLoader2(args[1]);
				clazz = loader.loadClass(args[2]);
				clazz.newInstance();
			}catch (Exception e){ e.printStackTrace(); }
		} else {
			try{
				main(args[0],args[1],newArgs);
			}catch (Exception e){ e.printStackTrace(); }
		}
	}
	
	/** 
	 * Wrapper 'main' provides a simple way to integrate the JarJarClassLoader2
	 * into an application, enabeling the use of a single uber jar file,
	 * with limited modification of the original code.
	 * 
	 * @param	srcFile			The source Jar file for the JarJarClassLoader2
	 * 							to use
	 * @param	targetClassName	The name of the class that contains the 'main'
	 * 							method to be called
	 * @param	args			An array of String containing the args passed in
	 * 							from the user. To be passed on to the 'main'
	 * 							method of the target class 
	 * @throws	IOException		Thrown if the JarJarClassLoader2 experiances an
	 * 							IOException
	 * @throws	ClassNotFoundException	Thrown if the JarJarClassLoader2 is unable
	 * 							to find the targetClassName
	 * @throws	NoSuchMethodException	Thrown if the target class does not
	 * 							contain a standard 'main' method
	 * @throws 	IllegalAccessException	Thrown if there is a security exception
	 * 							when attempting to invoke the main method of the
	 * 							target class
	 * @throws	InvocationTargetException	Throw if there is an exception when
	 * 							attempting to invoke the main method of the
	 * 							target class
	 */
	public static void main(String srcFile,String targetClassName,String... args)
		throws IOException, ClassNotFoundException, NoSuchMethodException,
		IllegalAccessException,InvocationTargetException{
		
		JarJarClassLoader2	loader = null;
		Class				clazz = null;
		Method				method = null;
		
			loader = new JarJarClassLoader2(srcFile);
			clazz = loader.loadClass(targetClassName);
			method = clazz.getMethod("main", new Class[]{String[].class});
			method.invoke(null, new Object[]{args});
	}
	
// ### Class Constructor ######################################################
	/**
	 * The primary constructor for JarJarClassLoader2. This constructor collects
	 * all the information and processes the passed in data. Should never be 
	 * called directly by the user
	 * 
	 * @param	parent			The parent class loader. Make sure we fit into
	 * 							the ClassLoader hierarchy
	 * @param	src				The name of the source of our data ie the name
	 * 							of the jar file
	 * @param	inStream		Thr source of the actual data to process
	 * 
	 * @throws	IOException		Thrown if an IOException is experianced while 
	 * 							processing the inStream
	 */
	private JarJarClassLoader2(ClassLoader parent,String src,InputStream inStream)
		throws IOException{
		
		// Make sure to call the ClassLoader init
		super(parent);

		// Initialize variables
		rawTable = new Hashtable<String,byte[]>();
		childClassLoaderSet = Collections.synchronizedSet(new HashSet<JarJarClassLoader2>());

		// Make sure we keep a pointer to the JAR file so that we can use it
		// later when we go to get resources
		this.src = src.replace('\\', '/');
		
		getMgr().registerClassLoader(this,this.src);
		
		ZipInputStream			zipIn = null;

		// Process the file
		try{
			// Create a ZipInputStream
			zipIn = new ZipInputStream(inStream);
			
			// Process the stream
			processZipInputStream(zipIn);
		// Make sure we close everything up/clean up after ourselves	
		}finally{
			zipIn.close();
		}
	}
	
// ----------------------------------------------------------------------------
	public JarJarClassLoader2(File srcFile,ClassLoader parent) throws IOException{
		this(parent,srcFile.getAbsolutePath(),new FileInputStream(srcFile));
	}
	
	public JarJarClassLoader2(File srcFile) throws IOException{
		this(srcFile,getSystemClassLoader());
	}
	
// ----------------------------------------------------------------------------
	public JarJarClassLoader2(String srcFile,ClassLoader parent) throws IOException{
		this(new File(srcFile),parent);		
	}
	
	public JarJarClassLoader2(String srcFile) throws IOException{
		this(new File(srcFile));
	}

// ### Class Methods ##########################################################
	
	/**
	 * processZipInputStream consumes a ZipInputStream and recursively extracts
	 * all files in the ZipInputStream. If any of the entries happen to be
	 * class files there names are converted to binary name formats. If any of
	 * the entries happen to be compressed files thouse compressed files are,
	 * themselves, processed. All the data extracted is put into a hashtable 
	 * to be processed on demand.
	 * 
	 * @param 	zipIn			A ZipInputStream
	 * @throws	IOException		Throws an IOException if there is a problem 
	 * 							with the ZipInputStream
	 */
	private void processZipInputStream(ZipInputStream zipIn) throws IOException{
		ZipEntry				zipEntry = null;

		String					entryName = null;
		ByteArrayOutputStream	byteOut = null;
		
		String					binaryName = null;
		
		// Allocate a ByteArrayOutputStream to be used while reading zipped entries
		byteOut = new ByteArrayOutputStream();

		// Loop threw all the entries in the ZipInputStream
		do{
			// Get the next ZipEntry
			zipEntry = zipIn.getNextEntry();
			// Make sure that the ZipEntry isn't null. If it is that means we've reached the end
			if (zipEntry != null){
				entryName = zipEntry.getName();
				// Make sure we ignore directories and files with unknown size
				if (!zipEntry.isDirectory()){					
					// Read zipped data from the stream, decompressed, make sure we get it all
					int		r = 0;
					byte[]	data = null;

					// Instead of reading a byte at a time we switched to 1024 bytes at a time
					data = new byte[1024];
					do{
						r = zipIn.read(data);
						if (r != -1){ byteOut.write(data,0,r); }
					} while(r != -1);
					
					// Transfer the data out of the ByteArrayOutputStream into an array
					data = byteOut.toByteArray();
					// Reset the ByteArrayOutputStream so we can re use it
					byteOut.reset();
					//System.out.println(entryName + " : " + data.length);
					
					// If the entry ends with '.jar' we process it as if we were processing another file
					if (entryName.endsWith(".jar")){
						ByteArrayInputStream	byteIn = null;
						JarJarClassLoader2		classLoader = null;
						String					src = null;
						
						// Convert the data byte array to an input stream
						byteIn = new ByteArrayInputStream(data);
						src = this.src + "|" + entryName;
						classLoader = new JarJarClassLoader2(this,src,byteIn);
						
						childClassLoaderSet.add(classLoader);

					// If the entry name ends with '.class' we convert the name and store the raw data
					} else if (entryName.endsWith(".class")){
						// Conver the name into a binary name, replace all '/' with '.'
						binaryName = entryName.replace('/', '.');
						// Strip out the '.class' ending
						binaryName = binaryName.substring(0, binaryName.length() - 6);
						// Store the raw data
						rawTable.put(binaryName, data);
					// Else store the raw data. This is probably a resource of some sort	
					} else {
						rawTable.put(entryName, data);
					}
				}
			}
			zipIn.closeEntry();
		// Keep looping until there aren't any more entries
		}while(zipEntry != null);
	}
	
	@Override
	protected Class<?> findClass(String binaryClassName) throws ClassNotFoundException{
		byte[]			data = null;
		
		data = rawTable.remove(binaryClassName);
		if (data != null){
			return defineClass(binaryClassName,data,0,data.length);
		} else {			
			Class<?>			clazz = null;

			synchronized(childClassLoaderSet){
				for(JarJarClassLoader2 loader:childClassLoaderSet){
					try{
						clazz = loader.findClass(binaryClassName);
						return clazz;
					}catch(ClassNotFoundException e){}
				}
			}
			
			throw new ClassNotFoundException();
		}		
	}
	
	@Override
	protected URL findResource(String resourceName) {
		URL			url = null;

		if (rawTable.containsKey(resourceName)){
			try{
				url = new URL("jarjar://" + src + "!" + resourceName);
			}catch(MalformedURLException e){}
		} else {
			synchronized(childClassLoaderSet){
				for(JarJarClassLoader2 loader:childClassLoaderSet){
					url = loader.findResource(resourceName);
					if (url != null){ return url; }
				}
			}
		}
		
		return url;
	}

	public InputStream findResourceAsStream(String resourceName){
		ByteArrayInputStream		byteArrayInStream = null;
		byte[]						data = null;
		
		data = rawTable.get(resourceName);
		if (data != null){ byteArrayInStream = new ByteArrayInputStream(data); }
		
		return byteArrayInStream;
	}
	
	/**
	 * This method returns the a set of string that represent the absolute class
	 * name (com.someco.className) given a partial class name (className). This
	 * method uses the String classes 'endsWith' method to compare every absolute
	 * class name the class loader knows about with the partial class name
	 * passed in
	 * 
	 * @param 	className		An incomplete class name
	 * 	
	 * @return	A Set of Strings that represent the absolute class name that
	 * 			match the partial class name passed in
	 */
	public Set<String> getAbsoluteClassName(String className){
		Set<String>		rsrcSet = null;
		Set<String>		absClassNameSet = null;
		
		rsrcSet = Collections.synchronizedSet(rawTable.keySet());
		absClassNameSet = new HashSet<String>();
		
		synchronized(rsrcSet){
			for(String rsrcName:rsrcSet){
				if (rsrcName.endsWith(className)){
					absClassNameSet.add(rsrcName);
				}
			}
		}

		return absClassNameSet;
	}

	/**
	 * Gets a class based on a partial class name. This method will call
	 * getAbsoluteClassName to get a set of absoulte class names that match
	 * the partial class name passed in and then attempt to load each absolute
	 * class name returning the first successful class load
	 * 
	 * @param	canonicalClassName	A partial class name
	 * @return	The first class from the set of absolue class names that was
	 * 			succesfuly loaded
	 * @throws	ClassNotFoundException	Thrown when the set of absolute class
	 * 			names is empty
	 */
	public Class<?> getCanonicalClass(String canonicalClassName) throws ClassNotFoundException{
		Set<String>			absClassNameSet = null;
		Iterator<String>	iterator = null;
		String				absClassName = null;
		
		absClassNameSet = getAbsoluteClassName(canonicalClassName);
		iterator = absClassNameSet.iterator();

		if (iterator.hasNext()){
			absClassName = iterator.next();
			return loadClass(absClassName);
		} else {
			throw new ClassNotFoundException();
		}
	}

	private JarJarClassLoader2Mgr getMgr(){
		if (mgr == null){ mgr = new JarJarClassLoader2Mgr(); }
		return mgr;
	}

	public String toString(){ return src; }

// ### Inner Classes ##########################################################
	/**
	 * These classes are glue code to handel connecting the JarJarClassLoader2
	 * to a getResource call with a URL.
	 */
	
	/**
	 * The JarJarClassLoader2Mgr keeps track of all the JarJarClassLoader2s and
	 * manages the getResource(URL) method calls
	 */
	private class JarJarClassLoader2Mgr{
	// === Class Fields =======================================================
		private Hashtable<String,JarJarClassLoader2>	classLoaderTable = null;
		
	// === Class Constructor ==================================================
		private JarJarClassLoader2Mgr(){
			classLoaderTable = new Hashtable<String,JarJarClassLoader2>();
			URL.setURLStreamHandlerFactory(new Handler());
		}
	
	// === Classs Methods =====================================================
		protected void registerClassLoader(JarJarClassLoader2 loader,String src){
			classLoaderTable.put(src, loader);
		}
		
		protected InputStream getResource(URL url){
			String				urlStr = null;
			String				src = null;
			String				rsrcName = null;
			int					index = 0;
			JarJarClassLoader2	classLoader = null;
			
			urlStr = url.toString();
			index = urlStr.lastIndexOf('!');
			src = urlStr.substring(9,index);
			rsrcName = urlStr.substring(index + 1,urlStr.length());
					
			classLoader = classLoaderTable.get(src);
			if (classLoader != null){
				return classLoader.findResourceAsStream(rsrcName);
			}
			
			return null;
		}
	}
	
	private class Handler implements URLStreamHandlerFactory{
		public URLStreamHandler createURLStreamHandler(String protocol){
			URLStreamHandler		handler = null;
			
			if (protocol.equals("jarjar")){
				handler = new JarJarURLStreamHandler();
			}

			return handler;
		}		
	}

	private class JarJarURLStreamHandler extends URLStreamHandler{	
		@Override
		protected URLConnection openConnection(URL url) throws IOException {
			return new JarJarURLConnection(url);
		}
	}

	private class JarJarURLConnection extends URLConnection{
		public JarJarURLConnection(URL url){ super(url); }
		
		@Override
		public void connect() throws IOException{}

		@Override
		public InputStream getInputStream() throws IOException{
			return getMgr().getResource(url);
		}
	}
}