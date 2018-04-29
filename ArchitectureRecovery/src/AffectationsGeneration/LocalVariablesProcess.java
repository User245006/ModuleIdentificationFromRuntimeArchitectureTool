package AffectationsGeneration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import InstrumentationPhase.SystemInheritanceGraph;
import InstrumentationPhase.applicationClasses;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

public class LocalVariablesProcess {
	
	public static void analyse(CtLocalVariable localVariable, CtExecutable element ,String currentElementName, String className, String packageName)
	{   System.out.println(localVariable );
		int li = localVariable.getPosition().getLine();
		String lineNumber = localVariable.getPosition().getCompilationUnit().getMainType().getSimpleName()+": "+li;
		//cTorCall.getPosition().getCompilationUnit().getMainType().getSimpleName()+": "+cTorCall.getPosition().getLine()
		String locVarSimpleName = localVariable.getSimpleName();
		CtExpression rightPart = localVariable.getDefaultExpression();
		
		String elementParametersNames = "";
		 List<CtParameter> elementParameters = element.getParameters();
		 for(int t =0; t<elementParameters.size(); t++)
		  {
			elementParametersNames = elementParametersNames+elementParameters.get(t).getType().getQualifiedName();
			if(t != elementParameters.size()-1)
			elementParametersNames = elementParametersNames+", ";
		  }
		 String elementFullName;
		 if(element instanceof CtConstructor)
		 {
			 elementFullName = packageName+"."+className;
		 }
		 else
		 {
			 elementFullName = packageName+"."+className+"#"+currentElementName+"("+elementParametersNames+")";
		 }
		 String localVariableFullName = packageName+"."+className+"."+currentElementName+"."+locVarSimpleName;
		 String v ;

	     chechScopesAndOutPuts(localVariableFullName);
	     chechScopesAndOutPuts(packageName+"."+className);
	     chechScopesAndOutPuts(elementFullName);
		       
		   for(String s : VariablesScopes.variablesScopes.get(elementFullName))
			{
			  VariablesScopes.variablesScopes.get(localVariableFullName).add(s);
			}
	
		   int iiii = VariablesScopes.variablesScopes.get(localVariableFullName).size();
		   if(iiii>0)
		   {
		   v = VariablesScopes.variablesScopes.get(localVariableFullName).get(iiii-1);
		   }
		   else
			{ 
			   if(VariablesScopes.variablesScopes.get(packageName+"."+className).size()>0)
		       { int r = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
			     v = VariablesScopes.variablesScopes.get(packageName+"."+className).get(r-1);
		    	 }
		      else
		      {
	            	v = ElementsDefaultScopes.allVariablesScopes.get(elementFullName);
		      }
			}
			
		  if(rightPart instanceof CtConstructorCall)
		  {   
			  if(applicationClasses.appClassesReferences.contains(rightPart.getType()))
			  {
			   String instantiatedClassName = rightPart.getType().getSimpleName();
			   
			   String insantiatedClassPackage = rightPart.getType().getDeclaration().getPackage().getQualifiedName();
		       String instantiatedClassQualifiedName = insantiatedClassPackage+"."+instantiatedClassName;
		       chechScopesAndOutPuts(instantiatedClassQualifiedName);
		        String scope = VariablesScopes.scope((CtConstructorCall)rightPart, lineNumber);
		        
				String instantiation = packageName+
						               "."+v+"."+currentElementName+"."+locVarSimpleName+" = "+
						               insantiatedClassPackage
						               +"."+scope+"."+instantiatedClassName
						               +"."+"this";
				
				VariablesScopes.affectations.add(instantiation);
				VariablesScopes.collectedConstructorCalls.add(instantiatedClassName);
				VariablesScopes.variablesOutSets.get(localVariableFullName).add(scope);
				VariablesScopes.variablesScopes.get(instantiatedClassQualifiedName).add(scope);
				//VariablesScopes.variablesScopes.get(elementFullName).add(scope);
				
				// all the super classes of this class will have the same scope
				CtTypeReference superclass = rightPart.getType().getSuperclass();
				while(superclass!= null)
				{  if(applicationClasses.appClassesReferences.contains(superclass))
				   {
					String superclassQualifiedName = superclass.getDeclaration().getPackage().getQualifiedName()
							+"."+superclass.getSimpleName();
					chechScopesAndOutPuts(superclassQualifiedName);
					VariablesScopes.variablesScopes.get(superclassQualifiedName).add(scope);
					superclass = superclass.getSuperclass();
				   }
				   else
				   {
					superclass = null;
				   }
				}
				
				
				// to Do
				/*
				//each time a class is instantiated, we get its methods and we check if there is 
				//a method which is automatically executed like actionperformed
				CtType typeInstantiated = (CtType) ((CtConstructorCall)rightPart).getExecutable().getDeclaration().getPosition().getCompilationUnit().getMainType();
				Collection<CtMethod> typeMethods = (typeInstantiated).getAllMethods();
				for(CtMethod m : typeMethods)
				{
					if(m.getSimpleName().equals("actionPerformed"))
					{
						Main.analyseBlock(m, instantiatedClassName, insantiatedClassPackage);
					}
				}
				*/
				  // the instantiation has arguments
			    List<CtParameter>  constructorParameters = ((CtConstructorCall)rightPart).getExecutable().getDeclaration().getParameters();
			    List<CtExpression> constructorArguments = ((CtConstructorCall)rightPart).getArguments();
			    
				for(CtExpression argument : constructorArguments)
				{  if(applicationClasses.appClassesReferences.contains(argument.getType()))
				  {
				   int indexOfArgument = constructorArguments.indexOf(argument);
		           String parameterName = constructorParameters.get(indexOfArgument).getSimpleName();
		           String[] splitarg = argument.toString().split("\\.");
		           
				    
		           if(!(argument instanceof CtInvocation) && !(argument instanceof CtConstructorCall))
					{   
		        	    String leftPartSimple;
		        	    String argumentSimpleName;
		        	   
		        	    		
		        	    String paramQuaName = insantiatedClassPackage+"."+instantiatedClassName+"."+
								instantiatedClassName+"."+parameterName;
						chechScopesAndOutPuts(paramQuaName);
						
						leftPartSimple = currentElementName+"."+locVarSimpleName;

						String arg = packageName+"."+className+"."+leftPartSimple;
						
						
		        	    if(!(splitarg[splitarg.length-1].equals("this")))
		        	    {
							if(argument instanceof CtFieldAccess)
						    {  
								argumentSimpleName = splitarg[splitarg.length-1];
						    }
						    else
						    {  
						    	argumentSimpleName = currentElementName+"."+splitarg[splitarg.length-1];
						    }
							
							String argumentFullName = packageName+"."+className+"."+argumentSimpleName;
							if(VariablesScopes.variablesOutSets.get(arg).size()>0)
							{   int si = VariablesScopes.variablesOutSets.get(arg).size();
								scope = VariablesScopes.variablesOutSets.get(arg).get(si-1);
								
							}
						    else
						    {
						    	scope = ElementsDefaultScopes.allVariablesScopes.get(arg);
								
						    }
							
							String sco;
							if(VariablesScopes.variablesScopes.get(arg).size()>0)
							{   int size = VariablesScopes.variablesScopes.get(arg).size();
								sco = VariablesScopes.variablesScopes.get(arg).get(size-1);
								
							}
						    else
						    {
						    	sco = ElementsDefaultScopes.allVariablesScopes.get(arg);	
						    }
							
						    instantiation = insantiatedClassPackage+
						               "."+scope+"."+instantiatedClassName+"."+parameterName+" = "+
						               packageName
						               +"."+sco+"."+argumentSimpleName;
					
							VariablesScopes.affectations.add(instantiation);
							// adding scopes
							VariablesScopes.variablesScopes.get(paramQuaName).add(scope);
							// here we add parameters out puts
							chechScopesAndOutPuts(argumentFullName);
							if(VariablesScopes.variablesOutSets.get(argumentFullName).size()>0)
							{   
								List<String> r  = VariablesScopes.variablesOutSets.get(argumentFullName);
								VariablesScopes.variablesOutSets.get(paramQuaName).add(r.get(r.size()-1));
	                           // superConstructorParameters(rightPart,scope, r.get(r.size()-1), parameterName);
							}
							else
							{
							 String lefRet = argument.getType().getSimpleName();
							 VariablesScopes.variablesOutSets.get(paramQuaName).add(lefRet);
                             //superConstructorParameters(rightPart,scope, lefRet, parameterName);
							}
							
		        	    }
		        	    else
		        	    {   
		        	    	String thisObject = packageName+"."+className;
		        	    	
		        	    	int s = VariablesScopes.variablesScopes.get(thisObject).size();
		        	    	
		        	    	String currentObject = VariablesScopes.variablesScopes.get(thisObject).get(s-1);
		        	    	String withNumbers = currentObject.replaceAll("\\d","");
		        	    	argumentSimpleName = currentObject+"."+withNumbers+".this";
		        	    	
		        	    	
		        	    	if(VariablesScopes.variablesOutSets.get(arg).size()>0)
							{   int si = VariablesScopes.variablesOutSets.get(arg).size();
								scope = VariablesScopes.variablesOutSets.get(arg).get(si-1);
								
							}
						    else
						    {
						    	scope = ElementsDefaultScopes.allVariablesScopes.get(arg);
								
						    }
							
							
							instantiation = insantiatedClassPackage+
						               "."+scope+"."+instantiatedClassName+"."+parameterName+" = "+
						               packageName
						               +"."+argumentSimpleName;
							VariablesScopes.affectations.add(instantiation);
							VariablesScopes.variablesOutSets.get(paramQuaName).add(currentObject);
							VariablesScopes.variablesScopes.get(paramQuaName).add(scope);
							//superConstructorParameters(rightPart,scope, currentObject, parameterName);
		        	    
		        	    }
					}
		           
		           
		            
		      }
		    }
				Main.analyseBlock(((CtConstructorCall) rightPart).getExecutable().getDeclaration(), instantiatedClassName, insantiatedClassPackage);
		}
	    }
		else
		{ 
			if(rightPart instanceof CtInvocation)
			{
				CtMethod method = (CtMethod) ((CtInvocation) rightPart).getExecutable().getDeclaration();
				if(method !=null)
				{
						String sig = method.getSignature();
						if(MethodsSignatures.signatures.contains(sig))
						{
						List<CtParameter> methodParameters= method.getParameters();
					    
					    String invokedMethodPackName = method.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
					    String invokedMethodClassName =  method.getPosition().getCompilationUnit().getMainType().getSimpleName();  
					     
						CtExpression target = ((CtInvocation) rightPart).getTarget();
		
					    String[] splitff = target.toString().split("\\.");
				           
				        String targetfieldSimpleName;
				        String targetfieldQualifiedName = "";
				       
				        String mmm = "";
						if(applicationClasses.appClassesReferences.contains(target.getType()) && (! method.getType().isPrimitive()) && (applicationClasses.appClassesReferences.contains(method.getType())))
					    {  
							String invokedParametersNames="";
							 for(int t =0; t<methodParameters.size(); t++)
							  { 
							  invokedParametersNames = invokedParametersNames+methodParameters.get(t).getType().getQualifiedName();
								if(t != methodParameters.size()-1)
									invokedParametersNames = invokedParametersNames+", ";
							  }
						    
							String invokeMethodFullNameWithParam = invokedMethodPackName+"."+invokedMethodClassName
									+"#"+method.getSimpleName()+"("+invokedParametersNames+")";
						    mmm = invokeMethodFullNameWithParam;
						    String leftPartfieldSimpleName = currentElementName+"."+locVarSimpleName;
							String leftPartfieldQualifiedName = packageName+"."+className+"."+leftPartfieldSimpleName;
						    chechScopesAndOutPuts(leftPartfieldQualifiedName);
						   
						    if(!(target instanceof CtInvocation) && !target.toString().equals("this") && !target.toString().equals(""))
						    {
						        
									if(target instanceof CtFieldAccess)
									{   
									    targetfieldSimpleName = splitff[splitff.length-1];
						                targetfieldQualifiedName = packageName+"."+className+"."+targetfieldSimpleName;
									}
									else
									{   
										targetfieldSimpleName = currentElementName+"."+splitff[splitff.length-1];
							            targetfieldQualifiedName = packageName+"."+className+"."+targetfieldSimpleName;
									}
									System.out.println("target: "+targetfieldQualifiedName);
									chechScopesAndOutPuts(targetfieldQualifiedName);
									if(VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).size()>0)
									{
									    String scope;
									    if(VariablesScopes.variablesScopes.get(targetfieldQualifiedName).size()>0)
										{   int si = VariablesScopes.variablesScopes.get(targetfieldQualifiedName).size();
											scope = VariablesScopes.variablesScopes.get(targetfieldQualifiedName).get(si-1);
											
										}
									    else
									    {
									    	scope = ElementsDefaultScopes.allVariablesScopes.get(targetfieldQualifiedName);
											
									    }
									    
									    int size = VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).size();
									     v = VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).get(size-1);
									    
									   // Main.analyseBlock(method, invokedMethodClassName, invokedMethodPackName);
											
									    String instantiation = packageName+
									               "."+v+"."+leftPartfieldSimpleName+" = "+
									               invokedMethodPackName
									               +"."+scope+"."+method.getSimpleName() 
									               +"."+"return";
									    
								       
									   VariablesScopes.affectations.add(instantiation);
									   // the scope of the method
									   chechScopesAndOutPuts(invokeMethodFullNameWithParam);
									   VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).add(scope);
										
									}
								    else
									{   String leftPartscope;
								    	int s = VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).size();
								    	if(s>0)
								    	{
								    	leftPartscope = VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).get(s-1);
									    	
								    	}
								    	else
								    	{  
								    		    int e = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
									    		System.out.println(packageName+"."+className);
									    		if(e>0)
									    		{
									    		System.out.println(e);
									    		leftPartscope = VariablesScopes.variablesScopes.get(packageName+"."+className).get(e-1);
									    		}
									    		else
									    		{
									    			leftPartscope = className;
									    		}
								    		
								    		
								    	}
								    	chechScopesAndOutPuts(targetfieldQualifiedName);
									    String scope;
								    	if(VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size()>0)
										{   int si = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size();
											scope = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).get(si-1);
											
										}
									    else
									    {
									    	scope = ElementsDefaultScopes.allVariablesScopes.get(targetfieldQualifiedName);
											
									    }
								    	//Main.analyseBlock(method, invokedMethodClassName, invokedMethodPackName);
										
								    	
								    	String instantiation = packageName+
									               "."+leftPartscope+"."+leftPartfieldSimpleName+" = "+
									               invokedMethodPackName
									               +"."+scope+"."+method.getSimpleName() 
									               +"."+"return";
											VariablesScopes.affectations.add(instantiation);
											VariablesScopes.variablesOutSets.get(leftPartfieldQualifiedName).add(scope);
											chechScopesAndOutPuts(invokeMethodFullNameWithParam);
											VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).add(scope);
											   
									}
									
									// here we add the output of the left part which is the output of the return expression of the method
									String invokedMqName = invokedMethodPackName+"."+invokedMethodClassName+"."+
											method.getSimpleName()+".return";
									chechScopesAndOutPuts(leftPartfieldQualifiedName);
									if(VariablesScopes.variablesOutSets.containsKey(invokedMqName))
									{   
										List<String> r  = VariablesScopes.variablesOutSets.get(invokedMqName);
										int sss = r.size();
										if(sss > 0)
										{
										String oo = r.get(sss-1);
										VariablesScopes.variablesOutSets.get(leftPartfieldQualifiedName).add(oo);
										}
									}
									else
									{
									 String lefRet = ElementsDefaultScopes.allVariablesScopes.get(invokedMqName);
									 VariablesScopes.variablesOutSets.get(leftPartfieldQualifiedName).add(lefRet);
									}
									
								    
						    }
						    else
						    {
						    	// the method is invoked without target (it is a method of the current class)
						    	if(target.toString().equals(""))
						    	{   String scope;
						    		chechScopesAndOutPuts(invokeMethodFullNameWithParam);
						    		if(VariablesScopes.variablesScopes.containsKey(packageName+"."+className))
						    		{
						    		  int sz = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
						    		  if(sz>0)
						    		  {
						    		  scope =  VariablesScopes.variablesScopes.get(packageName+"."+className).get(sz-1);
						    		  }
						    		  else
						    		  {
						    			  scope = className;
						    		  }
						    		}
						    		else
						    		{
						    			scope = className;
						    		}
		                            VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).add(scope);
						    		
		                            //Main.analyseBlock(method, invokedMethodClassName, invokedMethodPackName);
									
						    		String instantiation = packageName+
								               "."+scope+"."+leftPartfieldSimpleName+" = "+
								               invokedMethodPackName
								               +"."+scope+"."+method.getSimpleName() 
								               +"."+"return";
								    
							       VariablesScopes.affectations.add(instantiation);
								   
								// here we add the output of the left part which is the output of the return expression of the method
									String invokedMqName = invokedMethodPackName+"."+invokedMethodClassName+"."+
											method.getSimpleName()+".return";
				
									chechScopesAndOutPuts(leftPartfieldQualifiedName);
									
									if(VariablesScopes.variablesOutSets.containsKey(invokedMqName))
									{   
										List<String> r  = VariablesScopes.variablesOutSets.get(invokedMqName);
										int sss = r.size();
										if(sss > 0)
										{
											String oo = r.get(sss-1);
											VariablesScopes.variablesOutSets.get(leftPartfieldQualifiedName).add(oo);
										}
									}
									else
									{
									 String lefRet = ElementsDefaultScopes.allVariablesScopes.get(invokedMqName);
									 VariablesScopes.variablesOutSets.get(leftPartfieldQualifiedName).add(lefRet);
									}
								   
						    	}
						    }
						    
		
							// when the invocation has arguments
						    if(method!=null)
							  {     // the invoked method parameters
								    List<CtParameter> parameters = method.getParameters();
								    List<CtExpression> arguments = ((CtInvocation)rightPart).getArguments();
								    String methodSimpleName = method.getSimpleName();
								    
									for(CtExpression argument : arguments)
									{  if(applicationClasses.appClassesReferences.contains(argument.getType()))
									  {
									   int indexOfArgument = arguments.indexOf(argument);
							           String parameterName = parameters.get(indexOfArgument).getSimpleName();
							           String[] splitarg = argument.toString().split("\\.");
									    
							           if(!(argument instanceof CtInvocation) && !(argument instanceof CtConstructorCall) && !(splitarg[splitarg.length-1].equals("this")))
										{   
							        	    String argumentSimpleName;
											if(argument instanceof CtFieldAccess)
										    {  
										       argumentSimpleName = splitarg[splitarg.length-1];
										       
										    }
										    else
										    {  
											   argumentSimpleName = currentElementName+"."+splitarg[splitarg.length-1];
											   
										    }
											String arg = packageName+"."+className+"."+argumentSimpleName;
											String scope;
		                                    
											if(VariablesScopes.variablesScopes.containsKey(invokeMethodFullNameWithParam))
										        {int si = VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).size();
												scope = VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).get(si-1);
												
											}
										    else
										    {
										    	scope = ElementsDefaultScopes.allVariablesScopes.get(invokeMethodFullNameWithParam);
												
										    }
											chechScopesAndOutPuts(arg);
											int siz = VariablesScopes.variablesScopes.get(arg).size();
											String sco;
											if(VariablesScopes.variablesScopes.get(arg).size()>0)
											{ 
												sco = VariablesScopes.variablesScopes.get(arg).get(siz-1);
												
											}
										    else
										    {
										    	sco = ElementsDefaultScopes.allVariablesScopes.get(arg);
												
										    }
											
											String instantiation = invokedMethodPackName+
										               "."+scope+"."+methodSimpleName+"."+parameterName+" = "+
										               packageName
										               +"."+sco+"."+argumentSimpleName;
									
											VariablesScopes.affectations.add(instantiation);
											
											// here we add parameters out puts
											
											String paramQuaName = invokedMethodPackName+"."+invokedMethodClassName+"."+
													method.getSimpleName()+"."+parameterName;
											
											chechScopesAndOutPuts(paramQuaName);
											chechScopesAndOutPuts(arg);
											if(VariablesScopes.variablesOutSets.get(arg).size()>0)
											{  
												List<String> r  = VariablesScopes.variablesOutSets.get(arg);
												VariablesScopes.variablesOutSets.get(paramQuaName).add(r.get(r.size()-1));
											}
											else
											{
											 String lefRet = argument.getType().getSimpleName();
											 VariablesScopes.variablesOutSets.get(paramQuaName).add(lefRet);
											}
											
		
											// adding scopes
											VariablesScopes.variablesScopes.get(paramQuaName).add(scope);
										}
							      }
							    }
							 }
						    
						    
				     }
						System.out.println(localVariable.getPosition());
						analyseAllOverridedMethods(method);
						Main.analyseBlock(method, invokedMethodClassName, invokedMethodPackName);
						Main.staticCallGraph.add(mmm);
						
					}
				    }
			        }
					else
					{
						// right part is a variable
						System.out.println("RIGHT PART: "+rightPart);
						if(rightPart != null && !rightPart.getType().isPrimitive() && applicationClasses.appClassesReferences.contains(rightPart.getType()))
						{
							if(!rightPart.toString().equals("null") && !rightPart.toString().startsWith("new "))
							  {  
								   String[] splitff = rightPart.toString().split("\\.");
								   String rightVarName = splitff[splitff.length-1];
								   
								   String rightPartFullName;
								   if(rightPart instanceof CtFieldAccess)
									{
									   rightPartFullName = rightVarName;
									}
									else
									{    
									   rightPartFullName = currentElementName+"."+rightVarName;
									}
								   
								   
								   String leftPartFullName = currentElementName+"."+locVarSimpleName;
								   
								   String scope;
								  
								   chechScopesAndOutPuts(packageName+"."+className+"."+rightPartFullName);
								   if(VariablesScopes.variablesScopes.get(packageName+"."+className+"."+rightPartFullName).size()>0)
									{   int si = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+rightPartFullName).size();
									    
										scope = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+rightPartFullName).get(si-1);
									}
								    else
								    {
								    	scope = ElementsDefaultScopes.allVariablesScopes.get(packageName+"."+className+"."+rightPartFullName);
										
								    }
								   
								    chechScopesAndOutPuts(packageName+"."+className+"."+leftPartFullName);
								    chechScopesAndOutPuts(packageName+"."+className+"."+rightPartFullName);
									String sco;
									
									if(VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).size()>0)
									{   int size = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).size();
										sco = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).get(size-1);
										
									}
								    else
								    {
								    	sco = ElementsDefaultScopes.allVariablesScopes.get(packageName+"."+className+"."+leftPartFullName);
								    }
									String instantiation = packageName+
								               "."+sco+"."+leftPartFullName+" = "+
								               packageName
								               +"."+scope+"."+rightPartFullName;
							
									VariablesScopes.affectations.add(instantiation);
									
									System.out.println(instantiation);
									System.out.println(localVariable.getPosition());
									int out = VariablesScopes.variablesOutSets.get(packageName+"."+className+"."+rightPartFullName).size();
									if(out>0)
									{
									String output = VariablesScopes.variablesOutSets.get(packageName+"."+className+"."+rightPartFullName).get(out-1);
									VariablesScopes.variablesOutSets.get(packageName+"."+className+"."+leftPartFullName).add(output);
									}
							   }
							else
							   {
								// right part is null
								 
								    String leftPartFullName = currentElementName+"."+locVarSimpleName;
								   
								    chechScopesAndOutPuts(packageName+"."+className+"."+leftPartFullName);
									String sco;
									
									if(VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).size()>0)
									{  int size = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).size();
										sco = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).get(size-1);
										
									}
								    else
								    {
								    	sco = ElementsDefaultScopes.allVariablesScopes.get(packageName+"."+className+"."+leftPartFullName);
								    }
								    String instantiation = packageName+
							               "."+sco+"."+leftPartFullName+" = null";
								  VariablesScopes.affectations.add(instantiation);
								
							  }
					}
					}
					}
		
	}
	
	
	public static void chechScopesAndOutPuts(String var)
	{
		if(!VariablesScopes.variablesScopes.keySet().contains(var))
		{
	    	VariablesScopes.variablesScopes.put(var, new ArrayList<String>());
		}
		
		if(!VariablesScopes.variablesOutSets.containsKey(var))
		{
			VariablesScopes.variablesOutSets.put(var, new ArrayList<>());
		}
	}
	
	public static void chechScopes(String var)
	{
		if(!VariablesScopes.variablesScopes.containsKey(var))
		{
			VariablesScopes.variablesScopes.put(var, new ArrayList<String>());
		}
	}
	
	public static void superConstructorParameters(CtExpression rightPart, String scope, String output, String parameterName)
	{
		// all the super classes of this class will have the same scope
		CtTypeReference superclazz = rightPart.getType().getSuperclass();
		while(superclazz!= null)
		{  if(applicationClasses.appClassesReferences.contains(superclazz))
		   {
			String superClassSimpleName = superclazz.getSimpleName();
			String parameterQName = superclazz.getPackage().getDeclaration().getQualifiedName()
					+"."+superClassSimpleName+"."+superClassSimpleName+"."+parameterName;
			chechScopesAndOutPuts(parameterQName);
			VariablesScopes.variablesOutSets.get(parameterQName).add(output);
			VariablesScopes.variablesScopes.get(parameterQName).add(scope);
			superclazz = superclazz.getSuperclass();
		   }
		   else
		   {
			   superclazz = null;
		   }
		}
	}
	
	public static List<CtTypeReference> getsubClasses(CtType classe)
	{   
		//HashMap<CtTypeReference, List<CtTypeReference>> implementingClasses = new HashMap();
		List<CtTypeReference> implClasses = new ArrayList<>();
		for (CtTypeReference clazz : SystemInheritanceGraph.inheritanceRelationShipRef.keySet())
		{  
		   
		   List<CtTypeReference> mmm = SystemInheritanceGraph.inheritanceRelationShipRef.get(clazz);
		   
			for(CtTypeReference cl : mmm)
			{   
				if(classe.getQualifiedName().equals(cl.getQualifiedName()))
				{   
					if(!implClasses.contains(clazz))
					implClasses.add((CtTypeReference) clazz);
				}
				
			}
			//implementingClasses.put(classe, implClasses);
		}
		return implClasses;
	}
	
	public static void analyseAllOverridedMethods(CtMethod method)
	{  
		List<CtTypeReference> sbcla = getsubClasses(method.getPosition().getCompilationUnit().getMainType());
		
		  for(CtTypeReference suc : sbcla)
		  {
			if(applicationClasses.appClasses.contains(suc.getQualifiedName()))
			{   
				Collection<CtMethod> methods = suc.getTypeDeclaration().getMethods();
				for(CtMethod m : methods)
				{  
					if(m.getSimpleName().equals(method.getSimpleName()))
					{
						if(InstrumentationPhase.applicationConstructorCalls.systemConstructorCalls.contains(suc.getQualifiedName()))
						{   
							Main.analyseBlock(m, suc.getSimpleName(), suc.getTypeDeclaration().getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName());
						}
					}
				   
				}
				
			}
		  }
	}

}
