package ex01.setsuper;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
/*import javassist.ClassClassPath;*/
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
/*import target.Rectangle;*/

public class SetSuperclass {
   static String _S = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + _S + "output";

   public static void main(String[] args) {
	  String first, second, superclassName, subclassName;
	  first = second = superclassName = subclassName = "";

	  if(args.length != 2) {
		  System.out.println("The number of command-line arguments is not 2! Exit");
		  System.exit(0);
	  }
	  else {
		  first = args[0];
		  second = args[1];
		  if(first.startsWith("Common") && !second.startsWith("Common")) {
			  superclassName = first;
			  subclassName = second;
		  }
		  else if(!first.startsWith("Common") && second.startsWith("Common")) {
			  superclassName = second;
			  subclassName = first;
		  }
		  else if(first.startsWith("Common") && second.startsWith("Common")) {
			  if(first.length() >= second.length()) {
				  superclassName = first;
				  subclassName = second;
			  }
			  else {
				  superclassName = second;
				  subclassName = first;
			  }
		  }
		  else {
			  superclassName = first;
			  subclassName = second;
		  }
	  }
	  
	  System.out.println("[DBG] Superclass Name: " + superclassName);
	  System.out.println("[DBG] Subclass Name: " + subclassName);	  
	  
      try {
         ClassPool pool = ClassPool.getDefault();

         /*boolean useRuntimeClass = true;
         if (useRuntimeClass) {
            insertClassPathRunTimeClass(pool);
         } else {*/
         insertClassPath(pool);
         /*}*/

         CtClass cc = pool.get("target." + subclassName);
         setSuperclass(cc, "target." + superclassName, pool);
         cc.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   /*static void insertClassPathRunTimeClass(ClassPool pool) throws NotFoundException {
      ClassClassPath classPath = new ClassClassPath(new Rectangle().getClass());
      pool.insertClassPath(classPath);
      System.out.println("[DBG] insert classpath: " + classPath.toString());
   }*/

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = workDir + _S + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }

   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
      curClass.setSuperclass(pool.get(superClass));
      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
            ", subclass: " + curClass.getName());
   }
}
