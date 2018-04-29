package AffectationsGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import InstrumentationPhase.OutPuts;
import InstrumentationPhase.applicationClasses;
import InstrumentationPhase.applicationInterfaces;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtLoop;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtTry;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtNamedElement;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.NameFilter;
import spoon.reflect.visitor.filter.TypeFilter;

public class Main extends AbstractProcessor<CtClass>{

	public static List<CtElement> instantiations = new ArrayList<CtElement>();
	public static LinkedList<CtElement> invocationsStack = new LinkedList<CtElement>(); 
	public static LinkedList<CtElement> methodsStack = new LinkedList<CtElement>(); 
	
	public static LinkedList<String> ifBlocks = new LinkedList<String>(); 
	
	public static List<String> staticCallGraph = new ArrayList<>();
	
	
	@Override
	public void process(CtClass classe) 
	{   
	    List<CtField> fields = classe.getElements(new TypeFilter(CtField.class));
			// if(!ApproachOutPut.alreadyPassedElements.contains(classe))
		    //{
				 
				for(CtField field : fields)
				{  if(applicationClasses.appClassesReferences.contains(field.getType())
						 || field.getType().toString().equals("java.util.Map") 
						 || field.getType().toString().equals("java.util.Vector")
						 ||field.getType().toString().equals("java.util.HashMap"))
				{
					
				}
					//FieldsScopesAndOutPuts.getFieldScopesAndOutputs(field, classe);
				}
		   // }
	   // ApproachOutPut.alreadyPassedElements.add(classe);	
	    
	    
		Collection<CtMethod> methods = classe.getMethods();
		for(CtMethod m : methods) {
			  int i = 0;
			  if(m.getSimpleName().equals("main") && m.hasModifier(ModifierKind.STATIC) && m.hasModifier(ModifierKind.PUBLIC)) 
			  { // get the main class. TODO: check cases where there is two or more mains
				
				String className = classe.getSimpleName();
				String packageName = classe.getPackage().getQualifiedName();
				System.out.println("the main class is : "+className);
				analyseBlock(m, m.getDeclaringType().getSimpleName(), m.getDeclaringType().getPackage().getQualifiedName()); // Analyse the main block
				
				i++;
				while(!invocationsStack.isEmpty()) {
					CtElement element = invocationsStack.removeFirst();
					analyseElement(element, className, packageName);
				}
				
		       }
			  
			  if(i == 1)
			  {
				  return;
			  }
	   }
    }
	
	public static void analyseElement(CtElement element, String classeName, String packageName) 
	{
		if(element instanceof CtCreateObject) { // We reached a indicator indicating that all statements 
			                                    //in the constructor have been analyzed and that the object
			                                    //can now be instantiated
			instantiations.add(((CtCreateObject) element).element);
		}
		else if(element instanceof CtConstructorCall) { // The element is a constructor call, we need to analyse its statements
			
			invocationsStack.addFirst(new CtCreateObject(element)); // add an indicator
			CtExecutable exeConstructor = ((CtConstructorCall) element).getExecutable().getDeclaration();
			if(exeConstructor != null) {
			CtBlock block = exeConstructor.getBody();
			analyseBlock(exeConstructor, classeName, packageName); // analyse statements in the constructor block
			}
		} 
		else if(element instanceof CtInvocation ) 
		{   
			
			if(!methodsStack.contains(((CtInvocation) element).getExecutable()))
			{       methodsStack.add(((CtInvocation) element).getExecutable());
					CtExecutable exeMethod = ((CtInvocation) element).getExecutable().getDeclaration();
					if(exeMethod != null) { // fix for when we don't have code (e.g. super() of Object); TODO: check implicit vs explicit fix
						CtBlock block =null;
						CtElement parent = element.getParent();
						
						// here i did not treated the case when we have if and else that contain 2 different instances
						if(applicationInterfaces.appinterfaces.contains(((CtInvocation) element).getTarget().getType().getQualifiedName()))
						{
							
							CtElement target = ((CtInvocation) element).getTarget();
							CtMethod method = (CtMethod) ((CtInvocation) element).getExecutable().getDeclaration();
							String targetName;
							if(target instanceof CtFieldAccess)
							{  
								targetName = packageName+"."+classeName+"."+target.toString();
								
						    }
							else
							{  // modify the element
								
								while(! (parent instanceof CtMethod) && !(parent instanceof CtConstructor))
								  {
									parent = parent.getParent();
								  }
								targetName = packageName+"."+classeName+"."+((CtNamedElement) parent).getSimpleName()+"."+target.toString();
							}
							String methodOrConsName = ((CtInvocation) element).getExecutable().getSimpleName();
							
							List<String> types = OutPuts.alloutPuts.get(targetName);
							for(String type : types)
							{  
								CtTypeReference clazz  = applicationClasses.classNameReference.get(type);
								List<CtMethod> classMethods = clazz.getDeclaration().getElements(new TypeFilter(CtMethod.class));
								
								for(CtMethod m : classMethods)
								{  
									if(m.getSimpleName().equals(methodOrConsName))
	                               {  
		                              block = m.getBody(); 
		                              CtExecutable exec = (CtExecutable)m;
		  							   analyseBlock(exec, classeName, packageName);
		                              
	                               }
								}
							}
			             }
						else
						{
							block = exeMethod.getBody();
							analyseBlock(exeMethod, classeName, packageName);
						}
						
						
					}
					
					List<CtExpression> arguments = ((CtInvocation) element).getArguments();
					List<CtConstructorCall> conCall = element.getElements(new TypeFilter(CtConstructorCall.class));
					for(CtExpression argument : arguments)
					{ CtTypeReference type;
						for (CtConstructorCall cons : conCall)
						{ if(applicationClasses.appClassesReferences.contains(cons.getType()))
						  {
							if(cons.toString().equals(argument.toString()))
							{  analyseElement(argument, cons.getType().getSimpleName(), cons.getType().getPackage().getDeclaration().getQualifiedName());
							}
						  }
						}
					}
			}
		}
	}
	
	
	public static void analyseBlock(CtExecutable element, String className, String packageName) 
	{  String currentElementName = "";
	    
	   
	   if(!element.getSimpleName().toString().equals("<init>"))
		 {  currentElementName = element.getSimpleName();
		    
		 }
		 else
		 { currentElementName = element.getParent().getPosition().getCompilationUnit().getMainType().getSimpleName();
		 }
		 
		 
		 CtBlock block = element.getBody();
		 if(block != null)
		 {    
			 AnalyseBlockBody(block, element, currentElementName, className, packageName)  ; 
		 }
	}

	public static void AnalyseBlockBody(CtBlock block, CtExecutable element, String currentElementName,String className, String packageName  )
	{
		LinkedList<CtElement> localInvocationsStack = new LinkedList<CtElement>();
		//System.out.println("///////: "+block);
		if(block!=null)
		{
		List<CtStatement> statements = block.getStatements(); // Get all statement in the block
		
		
		// to show what instantiations are in the stack
		for(CtStatement statement : statements)
		{ 
		  if(statement instanceof CtAssignment)
		  {
			
			AssignmentsProcess.analyse((CtAssignment)statement, element,currentElementName, className, packageName);
		  }
		  
		  if(statement instanceof CtLocalVariable)
		  {
			  
			  LocalVariablesProcess.analyse((CtLocalVariable) statement, element, currentElementName, className, packageName);
		  }
		  
		  if(statement instanceof CtInvocation)
		  {   
			  
			  if(!statement.isImplicit())
			  { 
			    MethodInvocationsProcess.analyse((CtInvocation)statement, element, currentElementName, className, packageName);
			  }
		      
			  
		  }
		  
		  if(statement instanceof CtReturn)
		  {
			  ReturnStatementsProcess.analyse((CtReturn) statement, element, currentElementName, className, packageName);
		  }
		  
		  if(statement instanceof CtIf)
		  {   System.out.println("ifblock");
		  
	          CtBlock ifblock= ((CtIf)statement).getThenStatement();
	          if(!ifBlocks.contains(ifblock.toString()))
	          {
	            AnalyseBlockBody(ifblock, element, currentElementName, className, packageName);
	            ifBlocks.add(ifblock.toString());
	          }
	          
		      CtBlock elseblock= ((CtIf)statement).getElseStatement();
		      if(elseblock!=null)
	          {  if(!ifBlocks.contains(elseblock.toString()))
	            {
		          AnalyseBlockBody(elseblock, element, currentElementName, className, packageName);
		          ifBlocks.add(elseblock.toString());
	            }
	          }
		      
		  }
		  
		  
		  if(statement instanceof CtLoop)
		  {
			  CtStatement loopBlock = ((CtLoop)statement).getBody();
			  AnalyseBlockBody((CtBlock) loopBlock, element, currentElementName, className, packageName);
		  }
		  
		  if(statement instanceof CtTry)
		  {
			  CtBlock tryBlock = ((CtTry) statement).getBody();
			  AnalyseBlockBody((CtBlock) tryBlock, element, currentElementName, className, packageName);
			  
			  
			  List<CtCatch> catchBlock = ((CtTry) statement).getCatchers();
			  for(CtCatch catcher : catchBlock)
			  {
				  CtBlock catcherr = catcher.getBody();
				  AnalyseBlockBody((CtBlock) catcherr, element, currentElementName, className, packageName);
			  }
			  
			  CtBlock finBlock = ((CtTry) statement).getFinalizer();
			  AnalyseBlockBody((CtBlock) finBlock, element, currentElementName, className, packageName);
		  
		  }
		  
		  if(statement instanceof CtSwitch)
		  {
			  List<CtCase> cases = ((CtSwitch) statement).getCases();
			  for(CtCase casee : cases)
			  {
				  AnalyseBlockBody((CtBlock) casee, element, currentElementName, className, packageName);
			  }
		  }
		  
			// 1. Test if it is a Constructor call
			List<CtConstructorCall> constructors = statement.getElements(new TypeFilter(CtConstructorCall.class));
			if(!constructors.isEmpty()) {
				for(CtConstructorCall cons : constructors)
				{
				   localInvocationsStack.add(cons);
				}
			}
			
			// 2. Test if it is a method invocation
			List<CtInvocation> invocations = statement.getElements(new TypeFilter(CtInvocation.class));
			if(!invocations.isEmpty()) {
				for(CtInvocation invoc : invocations)
				{
					CtElement target = invoc.getTarget();
					localInvocationsStack.add(invoc);
				}
			}
			
		}
		
		// here the affectations are generated
		
		/*while(!localInvocationsStack.isEmpty()) {
				CtElement elem = localInvocationsStack.removeLast();
				
				if(!invocationsStack.contains(elem))
				{
				invocationsStack.addFirst(elem);
				}
			}
			*/
		}
			
	}
	public static List<String> affectationsGeneration() throws Exception   {
		
		 //launcher = new Launcher();
	     spoon.Launcher.main(new String[]
	    		 {
	        "-p",   "InstrumentationPhase.applicationClasses:"
	                +"InstrumentationPhase.applicationInterfaces:"
	                +"AffectationsGeneration.InstancesAffectations:"
	                +"AffectationsGeneration.ElementsDefaultScopes:"
	                +"InstrumentationPhase.SystemInheritanceGraph:"
	        		   +"InstrumentationPhase.applicationConstructorCalls:"
	                + "AffectationsGeneration.MethodsSignatures:"
	        	    + "AffectationsGeneration.Main",
	        	    "-i", "/home/soumia/Bureau/Dropbox/Dossier de l'équipe Equipe MAREL/Thèse ZELLAGUI Soumia/WsE/JGraphx/src/",
		              "--source-classpath","/home/soumia/Bureau/Dropbox/Dossier de l'équipe Equipe MAREL/Thèse ZELLAGUI Soumia/Spoon-5.1.0/spoon-core-5.1.0-jar-with-dependencies.jar:"
		              +"/home/soumia/Bureau/PMDLibs/apache-ant-1.8.2.jar:"
		              +"/home/soumia/Bureau/PMDLibs/asm-5.1.jar:"
		              +"/home/soumia/Bureau/PMDLibs/com.springsource.org.objectweb.asm_2.2.3.jar:"
		              +"/home/soumia/Bureau/PMDLibs/commons-io-2.4.jar:"
		              +"/home/soumia/Bureau/PMDLibs/commons-lang3-3.0.jar:"
		              +"/home/soumia/Bureau/PMDLibs/gson-2.2.2.jar:"
		              +"/home/soumia/Bureau/PMDLibs/jaxen-1.1.2.jar:"
		              +"/home/soumia/Bureau/PMDLibs/jcommander-1.30.jar:"
		              +"/home/soumia/Bureau/PMDLibs/org.apache.commons.io.jar:"
		              +"/home/soumia/Bureau/PMDLibs/org.objectweb.asm-3.2.0.jar:"
		              +"/home/soumia/Bureau/PMDLibs/saxon-9.1.0.8.jar:"
		              +"/home/soumia/Bureau/PMDLibs/saxon-he-9.3.0.5.jar:"
		            //jhotdraw
		              +"/home/soumia/Téléchargements/jdo.jar:"
		              +"/home/soumia/Téléchargements/batik-svggen-1.7.jar:"
		              +"/home/soumia/Téléchargements/batik-dom-1.7.jar:"
		        		});
	    
	     return VariablesScopes.affectations;
	}     
}
