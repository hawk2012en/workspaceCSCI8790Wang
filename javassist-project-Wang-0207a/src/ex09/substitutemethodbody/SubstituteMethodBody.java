package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
   static final String WORK_DIR      = System.getProperty("user.dir");
   static final String INPUT_PATH    = WORK_DIR + File.separator + "classfiles";

   static String className = null;
   static String methodName = null;
   static String paraIndex = null;
   static String paraValue = null;
   static HashSet<String> methods = new HashSet<String>();

   static String _L_ = System.lineSeparator();
   
   public static void main(String[] args) throws Throwable {
	  
	   try {    	  
	          while (true) {
		             UtilMenu.showMenuOptions();
		             switch (UtilMenu.getOption()) {
		             case 1:
		            		 System.out.println("Enter three parameters: ie. ComponentApp,move,1,0 or ServiceApp,fill,2,10");
		            		 String[] inputs = UtilMenu.getArguments();
		            		 if(inputs == null || inputs.length != 4) {
		            			 System.out.println("[WRN] Invalid Input!");
		            			 break;
		            		 }
		            		 else {	            				            			
		            			 className = inputs[0];	            			 
		            			 methodName = inputs[1];	            			 
		            			 paraIndex = inputs[2];	            			 
		            			 paraValue = inputs[3];
		            			 if(methods.contains(methodName)) {
		               				System.out.println("[WRN] This method \'" + methodName + "\' has been modified!!");
		               				break;
		               			}
		            		 }	    
				 
		            		 SubstituteMethodBody s = new SubstituteMethodBody();
		            	     Class<?> c = s.loadClass("target." + className);
		            	     Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
		            	     mainMethod.invoke(null, new Object[] { args });
		            	     methods.add(methodName);
		                     
		                     break;
		          	 default:
		                break;
		           }	           
	       }        

	   } catch (Exception e) {
	          e.printStackTrace();
	   }
   }

   private ClassPool pool;

   public SubstituteMethodBody() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(INPUT_PATH); // "target" must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         cc.instrument(new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException {
               String className2 = m.getClassName();
               String methodName2 = m.getMethodName();
 
               if (className2.equals("target." + className) && methodName2.equals(methodName)) {
                   System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: " + m.getLineNumber());
                   String block2 = "{" + _L_ //
                         + "System.out.println(\"\tReset param " + paraIndex + " to " + paraValue +  ".\"); " + _L_ //
                         + "$" + paraIndex + " = " + paraValue + "; " + _L_ //
                         + "$proceed($$); " + _L_ //
                         + "}";
                   System.out.println("[DBG] BLOCK2: " + block2);
                   System.out.println("------------------------");
                   m.replace(block2);
                }
            }
         });
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         e.printStackTrace();
         throw new ClassNotFoundException();
      }
   }
}
