package ex03.define;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import ex03.util.UtilMenu;

public class DefineNewClass {
   static String WORK_DIR = System.getProperty("user.dir");
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   public static void main(String[] args) {
		  String first, second, superclassName, subclassName;
		  first = second = superclassName = subclassName = null;
	      try {
	          while (true) {
	             UtilMenu.showMenuOptions();
	             switch (UtilMenu.getOption()) {
	             case 1:	            	 
	            		 System.out.println("Enter two class names:");
	            		 String[] clazNames = UtilMenu.getArguments();
	            		 if(clazNames == null || clazNames.length != 2) {
	            			 System.out.println("[WRN] Invalid Input!");
	            			 break;
	            		 }
	            		 else {
		 		                first = clazNames[0];
				                second = clazNames[1];
	            		 }
	          	  	          		  		
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
	          	  
	          		  System.out.println("[DBG] Superclass Name: " + superclassName);
	          		  System.out.println("[DBG] Subclass Name: " + subclassName);       	          		  
	          		   
	          		  setSuperClass(subclassName, superclassName);	               
	          		  break;
	          	 default:
	                break;
	           }
	         }
	       } catch (Exception e) {
	          e.printStackTrace();
	       }
   
   }
   
   static void setSuperClass(String clazSub, String clazSuper) {
	   try {
		   
		   File directory = new File(OUTPUT_DIR);
		   if (! directory.exists()){
   			  directory.mkdir();
   		   }

	         ClassPool pool = ClassPool.getDefault();
	         pool.insertClassPath(OUTPUT_DIR);
	         System.out.println("[DBG] class path: " + OUTPUT_DIR);

	         CtClass ccSuper = pool.makeClass(clazSuper);
	         ccSuper.writeFile(OUTPUT_DIR);
	         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	         System.out.println("[DBG]\t new class: " + ccSuper.getName());

	         CtClass ccSub = pool.makeClass(clazSub);
	         ccSub.writeFile(OUTPUT_DIR);
	         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	         System.out.println("[DBG]\t new class: " + ccSub.getName());
	         
	         ccSub.defrost();
	         System.out.println("[DBG] modifications of the class definition will be permitted.");

	         ccSub.setSuperclass(ccSuper);
	         System.out.println("[DBG] set super class, " + ccSub.getName() + " -> " + ccSuper.getName());

	         ccSub.writeFile(OUTPUT_DIR);
	         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	         
	         ccSub.defrost();
	         ccSuper.defrost();
	      } catch (NotFoundException | CannotCompileException | IOException e) {
	         e.printStackTrace();
	      }
	   }
}
