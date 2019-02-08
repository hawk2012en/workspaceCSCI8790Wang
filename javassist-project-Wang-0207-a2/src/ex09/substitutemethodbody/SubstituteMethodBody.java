package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class SubstituteMethodBody extends ClassLoader {
   static final String WORK_DIR      = System.getProperty("user.dir");
   static final String INPUT_PATH    = WORK_DIR + File.separator + "classfiles";

   static String className = "ComponentApp";
   static String methodName = "move";
   static String paraIndex = "1";
   static String paraValue = "0";

   static String _L_ = System.lineSeparator();
   
   public static void main(String[] args) throws Throwable {
      SubstituteMethodBody s = new SubstituteMethodBody();
      Class<?> c = s.loadClass("target." + className);
      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
      mainMethod.invoke(null, new Object[] { args });
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
                         + "System.out.println(\"\tReset param $" + paraIndex + " to " + paraValue +  ".\"); " + _L_ //
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
