package classloader;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import util.UtilMenu;

public class SampleLoader extends ClassLoader {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String appName, fieldName;
   private ClassPool pool;

   public static void main(String[] args) throws Throwable {
	   String [] myArgs = new String[2]; 
   	while (true) {
  	  UtilMenu.showMenuOptions();
        switch (UtilMenu.getOption()) {
        case 1:
       		 System.out.println("Enter parameters as \"ComponentApp f1\" or \"ServiceApp f2\":");
       		 String[] clazNames = UtilMenu.getArguments();
      		 if(clazNames == null) {
      			 System.out.println("[WRN] Invalid Input!");
      			 break;
      		 }
      		 else if(clazNames.length != 2) {
      			 System.out.println("[WRN] Invalid Input!");
      			 break;
      		 }
      		 else {	            				            			
      			myArgs[0] = appName = clazNames[0];
      			myArgs[1] =	fieldName = clazNames[1];        			
      		 }
     	  	          		  		
         SampleLoader s = new SampleLoader();
         Class<?> c = s.loadClass(appName);
         c.getDeclaredMethod("main", new Class[] { String[].class }). //
               invoke(null, new Object[] { myArgs });          
     		  break;
     	 default:
           break;
      }
     }
   }

   public SampleLoader() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(INPUT_DIR); // Search MyApp.class in this path.
   }

   /* 
    * Find a specified class, and modify the bytecode.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
         CtClass cc = pool.get(name);
         if (name.equals(appName)) {
            CtField f = new CtField(CtClass.doubleType, fieldName, cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f);
         }
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         throw new ClassNotFoundException();
      }
   }
}
