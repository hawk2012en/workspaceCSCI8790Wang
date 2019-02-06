package ex07.insertmethodbody;

import java.io.File;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import util.UtilFile;
import util.UtilMenu;

public class InsertMethodBody {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   static String _L_ = System.lineSeparator();
   
   static String className = null;
   static String methodName = null;
   static String index = null;

   public static void main(String[] args) {
      try {
    	  
          while (true) {
	             UtilMenu.showMenuOptions();
	             switch (UtilMenu.getOption()) {
	             case 1:
	            		 System.out.println("Enter three parameters: ie. ComponentApp,foo,1 or ServiceApp,bar,2");
	            		 String[] inputs = UtilMenu.getArguments();
	            		 if(inputs == null || inputs.length != 3) {
	            			 System.out.println("[WRN] Invalid Input!");
	            			 break;
	            		 }
	            		 else {	            				            			
	            			 className = inputs[0];	            			 
	            			 methodName = inputs[1];	            			 
	            			 index = inputs[2];	            			 
	            		 }	    
			 
	            		 ClassPool pool = ClassPool.getDefault();
	                     pool.insertClassPath(INPUT_DIR);
	                     CtClass cc = pool.get("target." + className);
	                     CtMethod m = cc.getDeclaredMethod(methodName);
	                     String block1 = "{ " + _L_ //
	                           + "System.out.println(\"[Inserted] target." + className + "." + methodName + "\'s parm " + index + ": \" + $" + index + "); " + _L_ //
	                           +  "}";
	                     System.out.println(block1);
	                     m.insertBefore(block1);
	                     cc.writeFile(OUTPUT_DIR);
	                     System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	                     System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
	                     Class<?> c = cc.toClass();         
	                     c.getDeclaredMethod("main", new Class[] { String[].class }). 
	                     invoke(null, new Object[] { args });
	                     
	                     break;
	          	 default:
	                break;
	           }	           
       }        

      } catch (Exception e) {
          e.printStackTrace();
       }
   }
}
