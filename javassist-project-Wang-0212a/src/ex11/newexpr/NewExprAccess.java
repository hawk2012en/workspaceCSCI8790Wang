package ex11.newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
   static String TARGET_MY_APP2 = null;
   static int numFields = 0;
   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws Throwable {
	   try {    	  
	          while (true) {
		             UtilMenu.showMenuOptions();
		             switch (UtilMenu.getOption()) {
		             case 1:
		            		 System.out.println("Enter three parameters: ie. target.ComponentApp,1 or target.ServiceApp,100");
		            		 String[] inputs = UtilMenu.getArguments();
		            		 if(inputs == null || inputs.length != 2) {
		            			 System.out.println("[WRN] Invalid Input!");
		            			 break;
		            		 }
		            		 else {	            				            			
		            			 TARGET_MY_APP2 = inputs[0];	            			 
		            			 numFields = Integer.parseInt(inputs[1]) ;	            			 		            			 		            			 
		            		 }	    
				 
		            		 NewExprAccess s = new NewExprAccess();
		            	     Class<?> c = s.loadClass(TARGET_MY_APP2);
		            	     Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
		            	     mainMethod.invoke(null, new Object[] { args });
		                     
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

   public NewExprAccess() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(CLASS_PATH); // TARGET must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         cc.instrument(new ExprEditor() {
            public void edit(NewExpr newExpr) throws CannotCompileException {
               try {
                  String longName = newExpr.getConstructor().getLongName();
                  if (longName.startsWith("java.")) {
                     return;
                  }
               } catch (NotFoundException e) {
                  e.printStackTrace();
               }
               String log = String.format(
                     "[Edited by ClassLoader] new expr: %s, " //
                           + "line: %d, signature: %s",
                     newExpr.getEnclosingClass().getName(), //
                     newExpr.getLineNumber(), newExpr.getSignature());
               System.out.println(log);
               
               CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields(); 
               if(numFields > fields.length) {
            	   numFields = fields.length;
               }
               String block1 = "{ " + _L_ //
                     + "  $_ = $proceed($$);" + _L_; //
               for(int i = 0; i < numFields; i++ ) {
            	   try {
            		   String fieldName = fields[i].getName();
            		   String fieldType = fields[i].getType().getName();
                       block1 += "  {"  + _L_ 
                               + "    String cName = $_.getClass().getName();" + _L_ //
                               + "    String fName = $_.getClass().getDeclaredFields()[" + i + "].getName();" + _L_//
                               + "    String fieldFullName = cName + \".\" + fName;" + _L_//
                               + "    " + fieldType + " fieldValue = $_." + fieldName + ";" + _L_//
                               + "    System.out.println(\"  [Instrument] \" + fieldFullName + \": \" + fieldValue);" + _L_//
                               + "  }"+ _L_;	   
             
            	   }catch (NotFoundException e) {
                       e.printStackTrace();
                   }
               }
               block1 += "}";
               System.out.println(block1);
               System.out.println("------------------------");
               newExpr.replace(block1.toString());
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