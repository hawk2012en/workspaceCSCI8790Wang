package ex13.newfield;

import java.io.File;
import java.lang.annotation.Annotation;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import target.Author;
import util.UtilMenu;

public class AnnotatedFieldExample3 {
   static String workDir = System.getProperty("user.dir");
   static String inputDir = workDir + File.separator + "classfiles";
   static String outputDir = workDir + File.separator + "output";
   static String className = null;
   static String annoName = null;

   public static void main(String[] args) {
	   try {    	  
	          while (true) {
		             UtilMenu.showMenuOptions();
		             switch (UtilMenu.getOption()) {
		             case 1:
		            		 System.out.println("Enter two parameters: ie. target.ComponentApp,Column or target.ServiceApp,Row");
		            		 String[] inputs = UtilMenu.getArguments();
		            		 if(inputs == null || inputs.length != 2) {
		            			 System.out.println("[WRN] Invalid Input!");
		            			 break;
		            		 }
		            		 else {	            				            			
		            			 className = inputs[0];	            			 
		            			 annoName = inputs[1];	            			 		            			 		            			 
		            		 }	    
				 
		                     ClassPool pool = ClassPool.getDefault();
		                     pool.insertClassPath(inputDir);
		                     
		                     CtClass ct = pool.get(className);         
		                     CtField[] cfs = ct.getFields();
		                     for(int i = 0; i < cfs.length; i++) {  
//		                    	 System.out.println(cfs[i].getName());
		                    	 Object[] annoList = cfs[i].getAnnotations();
		                    	 if(checkAnno(annoList)) {
		                    		 process(annoList);
		                    	 }
		                     }
		                     
		                     break;
		          	 default:
		                break;
		           }	           
	       }        

	   } catch (Exception e) {
	          e.printStackTrace();
	   }     
   }

   static boolean checkAnno(Object[] annoList) {
	      for (int i = 0; i < annoList.length; i++) {
	     	 Class<? extends Annotation> type = ((Annotation) annoList[i]).annotationType();
//         	 System.out.println("Annotation Type: " + type.getName());	    	  
	         if (type.getName().endsWith(annoName)) {
	            return true;	            
	         } 	         
	      }
	      return false;
	   }
   
   static void process(Object[] annoList) {
      for (int i = 0; i < annoList.length; i++) {
         if (annoList[i] instanceof Author) {
            Author author = (Author) annoList[i];
            System.out.println("Name: " + author.name() + ", Year: " + author.year());
         }
      }
   }
}
