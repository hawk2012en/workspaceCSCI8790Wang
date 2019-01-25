package javassistloader;

import java.io.File;
import java.lang.reflect.Method;
import util.UtilMenu;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;

public class JavassistLoaderExample {
   private static final String WORK_DIR = System.getProperty("user.dir");
   private static final String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   private static final String TARGET_POINT = "target.Point";
   private static final String TARGET_RECTANGLE = "target.Rectangle";

   public static void main(String[] args) {
	   String method1, method2, method3;
	   method1 = method2 = method3 = null;
	   boolean addStatusChanged = false;
	   boolean removeStatusChanged = false;
      try {
    	while (true) {
    	  UtilMenu.showMenuOptions();
          switch (UtilMenu.getOption()) {
          case 1:
         	 boolean isValid = false;
         	 do {
         		 System.out.println("Enter method names as \"add incX getX\" or \"remove incY getY\":");
         		 String[] clazNames = UtilMenu.getArguments();
        		 if(clazNames == null) {
        			 System.out.println("[WRN] Invalid Input!");
        		 }
        		 else if(clazNames.length != 3) {
        			 System.out.println("[WRN] Invalid Input!");
        		 }
        		 else {	            				            			
          			method1 = clazNames[0];
          			method2 = clazNames[1];
          			method3 = clazNames[2];
          			isValid = true;
          			if(method1.equals("add") && addStatusChanged) {
          				System.out.println("[WRN] This method \'add\' has been modified!!");
          				isValid = false;
          			}
          			if(method1.equals("remove") && removeStatusChanged) {
          				System.out.println("[WRN] This method \'remove\' has been modified!!");
          				isValid = false;
          			}
        		 }
         	  } while(!isValid);
       	  	          		  		
             ClassPool cp = ClassPool.getDefault();
             cp.insertClassPath(INPUT_DIR);
             System.out.println("[DBG] insert classpath: " + INPUT_DIR);

             CtClass cc = cp.get(TARGET_RECTANGLE);
             cc.setSuperclass(cp.get(TARGET_POINT));
             CtMethod m1 = cc.getDeclaredMethod(method1);
             m1.insertBefore("{ " //
                   + method2 + "();" //
                   + "System.out.println(\"[TR] "+ method3 + " result : \" + " + method3 + "()); }");

             Loader cl = new Loader(cp);
             Class<?> c = cl.loadClass(TARGET_RECTANGLE);
             Object rect = c.newInstance();
             System.out.println("[DBG] Created a Rectangle object.");

             Class<?> rectClass = rect.getClass();
             Method m = rectClass.getDeclaredMethod(method1, new Class[] {});
             System.out.println("[DBG] Called getDeclaredMethod.");
             Object invoker = m.invoke(rect, new Object[] {});
             System.out.println("[DBG] " + method3 + " result: " + invoker);
             
             if(method1.equals("add")) {
            	 addStatusChanged = true;
             }
             if(method1.equals("remove")) {
            	 removeStatusChanged = true;
             }
             cc.defrost();
            
       		  break;
       	 default:
             break;
        }
       }
      }catch (Exception e) {
         e.printStackTrace();
      }
   }

   /*static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = WORK_DIR + File.separator + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }*/
}
