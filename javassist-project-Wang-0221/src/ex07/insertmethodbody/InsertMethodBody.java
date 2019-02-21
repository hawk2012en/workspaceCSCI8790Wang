package ex07.insertmethodbody;

import java.io.File;
import java.util.HashSet;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import util.UtilMenu;

public class InsertMethodBody {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   static String _L_ = System.lineSeparator();
   
   static String className = null;
   static String methodName = null;
   static int numPara = 0;
   static HashSet<String> methods = new HashSet<String>();

   public static void main(String[] args) {
      try {
    	  
          while (true) {
	             UtilMenu.showMenuOptions();
	             switch (UtilMenu.getOption()) {
	             case 1:
	            		 System.out.println("Enter three parameters: ie. ComponentApp,foo,2 or ServiceApp,bar,100");
	            		 String[] inputs = UtilMenu.getArguments();
	            		 if(inputs == null || inputs.length != 3) {
	            			 System.out.println("[WRN] Invalid Input!");
	            			 break;
	            		 }
	            		 else {	            				            			
	            			 className = inputs[0];	            			 
	            			 methodName = inputs[1];	            			 
	            			 numPara = Integer.parseInt(inputs[2]);	     
	            			 if(methods.contains(methodName)) {
		               				System.out.println("[WRN] Invalid input method!!");
		               				break;
		               		 }
	            			 if( (className.equals("ComponentApp") && !methodName.equals("foo")) || 
	            					(className.equals("ServiceApp")  && !methodName.equals("bar"))) {
	            					System.out.println("[WRN] Invalid input class!!");
		               				break;
	            			 }
	            		 }	    
			 
	            		 ClassPool pool = ClassPool.getDefault();
	                     pool.insertClassPath(INPUT_DIR);
	                     CtClass cc = pool.get("target." + className);
	                     CtMethod m = cc.getDeclaredMethod(methodName);
	                     String block1 = "";
	                     if(numPara > 3) {
	                    	 block1 +=  "{ " + _L_ //
	  	                           + "  System.out.println(\"[Inserted] target." + className + "." + methodName 
		                           + "\'s parm " + 3 + ": \" + $" + 3 + "); " + _L_ //
		                           +  "}";
	                     }
	                     else {
	                    	 for(int index = 1; index <= numPara; index++) {
	                    		 block1 +=  "{ " + _L_ //
	  	  	                           + "  System.out.println(\"[Inserted] target." + className + "." + methodName 
	  		                           + "\'s parm " + index + ": \" + $" + index + "); " + _L_ //
	  		                           +  "}";
	                    	 }
	                     }
	                     // System.out.println(block1);
	                     m.insertBefore(block1);
	                     cc.writeFile(OUTPUT_DIR);
	                     // System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	                     // System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
	                     Class<?> c = cc.toClass();         
	                     c.getDeclaredMethod("main", new Class[] { String[].class }). 
	                     invoke(null, new Object[] { args });
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
}
