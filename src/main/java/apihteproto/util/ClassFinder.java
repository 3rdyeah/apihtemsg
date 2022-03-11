package apihteproto.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 通过给定的包名，来得到包下的全部CLASS类。
 */
public class ClassFinder {
	
	//类根目录地址
	private static String[] PATHS;
	
	static {
		//初始化类根目录地址
		initPaths();
	}

	/**
	 * 获取包目录下的全部CLASS
	 * @param packages
	 * @param clazzs
	 * @param result
	 */
	private static void find(Collection<String> packages,
			Collection<Class<?>> clazzs, Map<Class<?>, Set<Class<?>>> result) {
		try {
			//从指定的目录中查找目录、jar和.class的文件
			for (Entry<String, List<File>> en : findFiles(packages).entrySet()) {
				String packageName = en.getKey();
				for (File file : en.getValue()) {
					String fileName = file.getName();
					if (file.isDirectory()) {
						Set<String> pack = new HashSet<>();
						if (null == packageName || packageName.isEmpty()){
							pack.add(fileName);
						} else {
							pack.add(packageName + "." + fileName);
						}
						
						find(pack, clazzs, result);
					} else {//文件
						if (fileName.endsWith(".jar")) {//jar文件
							loadClassFromJar(file, packages, clazzs, result);
							continue;
						}
						
						//得到类名 比如将User.class变为User
						String classSimpleName = fileName.substring(0, fileName.lastIndexOf('.'));
						//得到类全名 比如xxx.User
						String className = packageName + "." + classSimpleName;
						tryAdd(className, clazzs, result);
					}
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void loadClassFromJar(File file, Collection<String> packages,
			Collection<Class<?>> clazzs, Map<Class<?>, Set<Class<?>>> result) {
		JarFile jar = null;
		try {
			jar = new JarFile(file);
			Enumeration<JarEntry> ens = jar.entries();
			while (ens.hasMoreElements()) {
				JarEntry en = ens.nextElement();
				if(en.isDirectory()) {
					continue;
				}
				if (!isClassFile(en.getName())) {
					continue;
				}
				String className = pathToPackage(en.getName());
				for (String packageName : packages) {
					if (className.startsWith(packageName)) {
						className = className.substring(0, className.lastIndexOf('.'));
						tryAdd(className, clazzs, result);
					}
				}
			}
		} catch(Throwable e) {
			e.printStackTrace();
		} finally {
			if (null != jar) {
				try {
					jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void tryAdd(String className, Collection<Class<?>> clazzs,
			Map<Class<?>, Set<Class<?>>> result) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
		for (Class<?> format : clazzs) {
			boolean add = false;
			if (format.isAnnotation()) {
				add = clazz.isAnnotationPresent((Class<? extends Annotation>) format);
			} else {
				add = format.isAssignableFrom(clazz);
			}
			
			if (!add) {
				continue;
			}
			
			Set<Class<?>> set = result.get(format);
			if (null == set) {
				set = new HashSet<Class<?>>();
				result.put(format, set);
			}
			if (set.add(clazz)) {
				System.out.println(format.getName() + " found class :" + clazz.getName());
			}
		}
	}
	
	/**
	 * 将从配置文件中读取的形如xxx.User转成xxx/User路径格式
	 * @param packageName
	 * @return
	 */
	private static String packageToPath(String packageName) {
		return packageName.replaceAll("\\.", "/");
	}
	
	private static String pathToPackage(String path) {
		return path.replaceAll("/", "\\.");
	}
	
	/**
	 * 获取符合包限制的文件
	 * @return
	 */
	private static Map<String, List<File>> findFiles(Collection<String> packages) {
		Map<String, List<File>> result = new HashMap<>();
		for(String path : PATHS) {
			if (path.endsWith(".jar")) {
				result.put(path, Arrays.asList(new File(path)));
				continue;
			}
			
			for (String packageName : packages) {
				//包对应的文件目录
				File dir = new File(path, packageToPath(packageName));
				//从指定的目录中查找目录和以.class结尾的文件
				File[] files = dir.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						if (pathname.isDirectory()) {
							return true;
						}
						return isClassFile(pathname.getName());
					}
				});
				
				if (files == null) {
					continue;
				}
				
				List<File> old = result.get(packageName);
				if (null == old) {
					old = new LinkedList<>();
					result.put(packageName, old);
				}
				old.addAll(Arrays.asList(files));
			}
		}
		
		return result;
	}
	
	private static boolean isClassFile(String fileName) {
		return fileName.matches(".*\\.class$");
	}
	
	/**
	 * 初始化类根目录地址
	 * @return
	 */
	private static void initPaths() {
		try {
			String pathStr = System.getProperty("java.class.path");
			if(isWin()) {
				PATHS = pathStr.split(";");
			} else {
				PATHS = pathStr.split(":");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	private static boolean isWin() {
		return System.getProperty("os.name").startsWith("Windows");
	}

	public interface Callback {
		void callback(Collection<Class<?>> clazzs);
	}
	
	public static class Builder {
		private final Set<String> packages = new HashSet<>();
		private final Map<Class<?>, Callback> callbacks = new LinkedHashMap<>();
		
		public Builder packige(String packige) {
			packages.add(packige);
			return this;
		}
		
		public Builder registry(Class<?> clazz, Callback callback) {
			callbacks.put(clazz, callback);
			return this;
		}
		
		public void build() {
			Map<Class<?>, Set<Class<?>>> finds = new HashMap<>();
			ClassFinder.find(packages, callbacks.keySet(), finds);
			for (Entry<Class<?>, Callback> en : callbacks.entrySet()) {
				Callback callback = en.getValue();
				if (null != callback) {
					Set<Class<?>> clazzs = finds.get(en.getKey());
					if (null == clazzs) {
						clazzs = Collections.emptySet();
					}
					callback.callback(clazzs);
				}
			}
		}
	}

	public static Builder create() {
		return new Builder();
	}
}