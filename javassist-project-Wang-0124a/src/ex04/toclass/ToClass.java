package ex04.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import ex04.util.UtilMenu;

public class ToClass {
   public static void main(String[] args) {
	  String className = null;
      try {
          while (true) {
	             UtilMenu.showMenuOptions();
	             switch (UtilMenu.getOption()) {
	             case 1:
	            	 boolean isValid = false;
	            	 do {
	            		 System.out.println("Enter one class names:");
	            		 String[] clazNames = UtilMenu.getArguments();
	            		 if(clazNames == null) {
	            			 System.out.println("[WRN] Invalid Input!");
	            		 }
	            		 else if(clazNames.length != 1) {
	            			 System.out.println("[WRN] Invalid Input!");
	            		 }
	            		 else {	            				            			
	            			 className = clazNames[0];
		            		 isValid = true;
	            		 }
	            	  } while(!isValid);	          	  	          		  		

	                 ClassPool cp = ClassPool.getDefault();
	                 CtClass cc = cp.get("target." + className);
	                 CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
	                 declaredConstructor.insertAfter("{ " //
	                       + "System.out.println(\"id: \" + id); }");
	                 declaredConstructor.insertAfter("{ " //
		                       + "System.out.println(\"year: \" + year); }");

	                 Class<?> c = cc.toClass();
	                 c.newInstance();
	          		  break;
	          	 default:
	                break;
	           }
          }

      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException e) {
         System.out.println(e.toString());
      }
   }
}
