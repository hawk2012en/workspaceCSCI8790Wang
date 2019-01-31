package ex04.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import ex04.util.UtilMenu;

public class ToClass {
   public static void main(String[] args) {
	  String className, field1, field2;
	  className = field1 = field2 = null;
      try {
          while (true) {
	             UtilMenu.showMenuOptions();
	             switch (UtilMenu.getOption()) {
	             case 1:
	            		 System.out.println("Enter three parameters: ie. CommonServiceA, idA, nameA or CommonComponentB, idB, nameB");
	            		 String[] clazNames = UtilMenu.getArguments();
	            		 if(clazNames == null) {
	            			 System.out.println("[WRN] Invalid Input!");
	            			 break;
	            		 }
	            		 else if(clazNames.length != 3) {
	            			 System.out.println("[WRN] Invalid Input!");
	            			 break;
	            		 }
	            		 else {	            				            			
	            			 className = clazNames[0];
	            			 field1 = clazNames[1];
	            			 field2 = clazNames[2];
	            		 }	          	  	          		  		
	                 ClassPool cp = ClassPool.getDefault();
	                 CtClass cc = cp.get("target." + className);
	                 CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
	                 declaredConstructor.insertAfter("{ " //
	                       + "System.out.println(\"" + field1 + ": \" +" + field1 + "); "
	                       + "System.out.println(\"" + field2 + ": \" +" + field2 + "); }");	                 

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
