package ex04.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import target.Hello;
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
	            		 if(clazNames.length != 1) {	            			 
	            			 System.out.println("[WRN] Invalid Input!");
	            		 }
	            		 else {
	            			className = clazNames[0];
	            			isValid = true;
	            		 }
	            	  } while(!isValid);
	          	  	          		  		
	                 // Hello orig = new Hello(); // java.lang.LinkageError

	                 ClassPool cp = ClassPool.getDefault();
	                 CtClass cc = cp.get("target.Hello");
	                 CtMethod m = cc.getDeclaredMethod("say");
	                 m.insertBefore("{ System.out.println(\"[TR] Hello.say: \" + i); }");

	                 CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
	                 declaredConstructor.insertAfter("{ " //
	                       + "System.out.println(\"[TR] After calling a constructor: \" + i); }");

	                 Class<?> c = cc.toClass();
	                 Hello h = (Hello) c.newInstance();
	                 h.say();
	               
	          		  break;
	          	 default:
	                break;
	           }

      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException e) {
         System.out.println(e.toString());
      }
   
}
