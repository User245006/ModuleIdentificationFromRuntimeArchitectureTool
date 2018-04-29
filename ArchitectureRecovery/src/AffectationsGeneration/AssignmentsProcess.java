package AffectationsGeneration;

import java.util.ArrayList;
import java.util.List;

import InstrumentationPhase.applicationClasses;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;

public class AssignmentsProcess {
	
	public static void analyse(CtAssignment assignment, CtExecutable element ,String currentElementName, String className, String packageName)
	{   System.out.println(assignment.getPosition());
	    System.out.println(VariablesScopes.variablesScopes);
		String lineNumber = assignment.getPosition().getCompilationUnit().getMainType().getSimpleName()+": "+assignment.getPosition().getLine();
		CtExpression leftPart = assignment.getAssigned();
		CtExpression rightPart = assignment.getAssignment();
		//System.out.println("assignment: "+assignment);
		if(element instanceof CtConstructor)
		{  System.out.println(packageName+"."+className);
		   int r = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
	       String sc = VariablesScopes.variablesScopes.get(packageName+"."+className).get(r-1);
   	       String temp = sc.replaceAll("\\d","");
			currentElementName = temp;
		}
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
		 chechScopesAndOutPuts(elementFullName);
	    if(applicationClasses.appClassesReferences.contains(rightPart.getType()))
	    {  
	      if(rightPart instanceof CtConstructorCall)
	    	  
	      {   
		        String instantiatedClassName = rightPart.getType().getSimpleName();
		        String insantiatedClassPackage = rightPart.getType().getDeclaration().getPackage().getQualifiedName();
	           String instantiatedClassQualifiedName = insantiatedClassPackage+"."+instantiatedClassName;
	    
			if(leftPart instanceof CtFieldAccess)
			{   
				String[] splitff = leftPart.toString().split("\\.");
			    String fieldSimpleName = splitff[splitff.length-1];
			    
			    
			    String fieldQualifiedName = packageName+"."+className+"."+fieldSimpleName;
			    
			    chechScopesAndOutPuts(fieldQualifiedName);
			    chechScopes(instantiatedClassQualifiedName);
			   
			    String scope = VariablesScopes.scope((CtConstructorCall)rightPart, lineNumber);
			       
					for(String s : VariablesScopes.variablesScopes.get(elementFullName))
					{
					   if(!VariablesScopes.variablesScopes.get(fieldQualifiedName).contains(s))
						   VariablesScopes.variablesScopes.get(fieldQualifiedName).add(s);
					}
							
						
					    
					String v;
					if(VariablesScopes.variablesScopes.get(fieldQualifiedName).size()>0)
					{  
					    int size = VariablesScopes.variablesScopes.get(fieldQualifiedName).size();
					    
					     v = VariablesScopes.variablesScopes.get(fieldQualifiedName).get(size-1);
							
					}
					else
					{
						 v = ElementsDefaultScopes.fieldsScopes.get(fieldQualifiedName);
					}
			       
				   
			    String instantiation = packageName+
			               "."+v+"."+splitff[splitff.length-1]+" = "+
			               insantiatedClassPackage
			               +"."+scope+"."+instantiatedClassName
			               +"."+"this";
				
				VariablesScopes.affectations.add(instantiation);
				VariablesScopes.collectedConstructorCalls.add(instantiatedClassName);
				VariablesScopes.variablesOutSets.get(fieldQualifiedName).add(scope);
				VariablesScopes.variablesScopes.get(instantiatedClassQualifiedName).add(scope);
				
				// all the super classes of this class will have the same scope
				CtTypeReference superclass = rightPart.getType().getSuperclass();
				while(superclass!= null)
				{  if(applicationClasses.appClassesReferences.contains(superclass))
				   {
					String superclassQualifiedName = superclass.getPackage().getDeclaration().getQualifiedName()
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
			  
			}
			else
			{  // the leftPart is not an access to a field
			    String variableSimpleName = currentElementName+"."+leftPart.toString();
			    String variableFullName = packageName+"."+className+"."+variableSimpleName;
			    
			    chechScopesAndOutPuts(variableFullName);
			    chechScopes(instantiatedClassQualifiedName);
			    
			    String scope;
			    String v;
			    
				if(VariablesScopes.variablesScopes.containsKey(elementFullName))
				{
					for(String s : VariablesScopes.variablesScopes.get(elementFullName))
					{
					   if(!VariablesScopes.variablesScopes.get(variableFullName).contains(s))
						   VariablesScopes.variablesScopes.get(variableFullName).add(s);
					}
				  
				}
			    scope = VariablesScopes.scope((CtConstructorCall)rightPart, lineNumber);
			    
			    if(VariablesScopes.variablesScopes.get(variableFullName).size()>0)
				{
				    int size = VariablesScopes.variablesScopes.get(variableFullName).size();
				    
				    v = VariablesScopes.variablesScopes.get(variableFullName).get(size-1);
				}
				else
				{
				   v = ElementsDefaultScopes.localVariablesScopes.get(variableFullName);
				}
				
							
				String instantiation = packageName+
						               "."+v+"."+variableSimpleName+" = "+
						               insantiatedClassPackage
						               +"."+scope+"."+instantiatedClassName
						               +"."+"this";
				
				VariablesScopes.affectations.add(instantiation);
				VariablesScopes.collectedConstructorCalls.add(instantiatedClassName);
				VariablesScopes.variablesOutSets.get(variableFullName).add(scope);
				VariablesScopes.variablesScopes.get(instantiatedClassQualifiedName).add(scope);
				// all the super classes of this class will have the same scope
				CtTypeReference superclass = rightPart.getType().getSuperclass();
				while(superclass!= null)
				{  if(applicationClasses.appClassesReferences.contains(superclass))
				   {
					String superclassQualifiedName = superclass.getPackage().getDeclaration().getQualifiedName()
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
			}
			
			
			  // the instantiation has arguments
		    List<CtParameter>  constructorParameters = ((CtConstructorCall)rightPart).getExecutable().getDeclaration().getParameters();
		    List<CtExpression> constructorArguments = ((CtConstructorCall)rightPart).getArguments();
		    
			for(CtExpression argument : constructorArguments)
			{  if(applicationClasses.appClassesReferences.contains(argument.getType()))
			  {
			   int indexOfArgument = constructorArguments.indexOf(argument);
	           String parameterName = constructorParameters.get(indexOfArgument).getSimpleName();
	           String[] splitarg = argument.toString().split("\\.");
	           String lp[] = leftPart.toString().split("\\.");
			    
	           if(!(argument instanceof CtInvocation) && !(argument instanceof CtConstructorCall) && !(splitarg[splitarg.length-1].equals("this")))
				{   
	        	    String leftPartSimple;
					if(leftPart instanceof CtFieldAccess)
				    {  
						leftPartSimple = lp[lp.length-1];
				       
				    }
				    else
				    {  
				    	leftPartSimple = currentElementName+"."+lp[lp.length-1];
					   
				    }
					
					String argumentSimpleName;
					if(argument instanceof CtFieldAccess)
				    {  
						argumentSimpleName = splitarg[splitarg.length-1];
				       
				    }
				    else
				    {  
				    	argumentSimpleName = currentElementName+"."+splitarg[splitarg.length-1];
					   
				    }
					String arg = packageName+"."+className+"."+leftPartSimple;
					String scope;
					int si = VariablesScopes.variablesOutSets.get(arg).size();
					if(VariablesScopes.variablesOutSets.get(arg).size()>0)
					{ 
						scope = VariablesScopes.variablesOutSets.get(arg).get(si-1);
						
					}
				    else
				    {
				    	scope = ElementsDefaultScopes.allVariablesScopes.get(arg);
						
				    }
					
					String sco;
					if(VariablesScopes.variablesScopes.get(arg).size()>0)
					{  int size = VariablesScopes.variablesScopes.get(arg).size();
						sco = VariablesScopes.variablesScopes.get(arg).get(size-1);
						
					}
				    else
				    {
				    	if(VariablesScopes.variablesScopes.get(packageName+"."+className).size()>0)
				    	{
				    		int ee = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
				    		sco = VariablesScopes.variablesScopes.get(packageName+"."+className).get(ee-1);
				    	}
				    	else
				    	{
				    	sco = ElementsDefaultScopes.allVariablesScopes.get(arg);
				    	}
				    }
					
					String instantiation = insantiatedClassPackage+
				               "."+scope+"."+instantiatedClassName+"."+parameterName+" = "+
				               packageName
				               +"."+sco+"."+argumentSimpleName;
			
					VariablesScopes.affectations.add(instantiation);
					
					// here we add parameters out puts
					String paramQuaName = insantiatedClassPackage+"."+instantiatedClassName+"."+
							instantiatedClassName+"."+parameterName;
					chechScopesAndOutPuts(paramQuaName);
					chechScopesAndOutPuts(arg);
					VariablesScopes.variablesScopes.get(paramQuaName).add(scope);
					if(VariablesScopes.variablesOutSets.containsKey(arg))
					{   
						List<String> r  = VariablesScopes.variablesOutSets.get(arg);
						VariablesScopes.variablesOutSets.get(paramQuaName).add(r.get(r.size()-1));
					}
					else
					{
					 String lefRet = argument.getType().getSimpleName();
					 VariablesScopes.variablesOutSets.get(paramQuaName).add(lefRet);
					}
					
				}
	      }
	    }
			Main.analyseBlock(((CtConstructorCall) rightPart).getExecutable().getDeclaration(), instantiatedClassName, insantiatedClassPackage);
	   }
	
	else
	{       String[] spl = leftPart.toString().split("\\.");
			String leftPartfieldSimpleName;
			String leftPartfieldQualifiedName;
			if(leftPart instanceof CtFieldAccess)
			{   
			    leftPartfieldSimpleName = spl[spl.length-1];
			    leftPartfieldQualifiedName = packageName+"."+className+"."+leftPartfieldSimpleName;
			}
			else
			{
				leftPartfieldSimpleName = currentElementName+"."+spl[spl.length-1];
				leftPartfieldQualifiedName = packageName+"."+className+"."+leftPartfieldSimpleName;
			}
		if(rightPart instanceof CtInvocation)
		{   String methIn = "";
			CtMethod method = (CtMethod) ((CtInvocation) rightPart).getExecutable().getDeclaration();
			System.out.println(rightPart.getPosition());
			List<CtParameter> methodParameters= method.getParameters();
		    
		    String invokedMethodPackName = method.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
		    String invokedMethodClassName =  method.getPosition().getCompilationUnit().getMainType().getSimpleName();  
		    
			CtExpression target = ((CtInvocation) rightPart).getTarget();
			

	        String[] splitff = target.toString().split("\\.");
	           
	        String targetfieldSimpleName;
	        String targetfieldQualifiedName ="";
	        
			if(applicationClasses.appClassesReferences.contains(target.getType()))
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
				 methIn = invokeMethodFullNameWithParam;
			    if(!(target instanceof CtInvocation) && !target.toString().equals("this"))
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
						
					    chechScopesAndOutPuts(leftPartfieldQualifiedName);
						if(VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).size()>0)
						{
						    int size = VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).size();
						    int si = VariablesScopes.variablesScopes.get(targetfieldQualifiedName).size();
						    String v = VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).get(size-1);
						    String scope;
						    if(VariablesScopes.variablesScopes.get(targetfieldQualifiedName).size()>0)
							{ 
								scope = VariablesScopes.variablesScopes.get(targetfieldQualifiedName).get(si-1);
								
							}
						    else
						    {
						    	scope = ElementsDefaultScopes.allVariablesScopes.get(targetfieldQualifiedName);
								
						    }
						    String instantiation = packageName+
						               "."+v+"."+spl[spl.length-1]+" = "+
						               invokedMethodPackName
						               +"."+scope+"."+method.getSimpleName() 
						               +"."+"return";
						    
					       
						   VariablesScopes.affectations.add(instantiation);
						   
						   // the scope of the method
						   chechScopesAndOutPuts(invokeMethodFullNameWithParam);
						   VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).add(scope);
							
						}
					    else
						{   
					    	String leftPartscope = ElementsDefaultScopes.allVariablesScopes.get(leftPartfieldQualifiedName);
					    	int size = VariablesScopes.variablesScopes.get(leftPartfieldQualifiedName).size();
					    	chechScopesAndOutPuts(targetfieldQualifiedName);
						    int si = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size();
						    String scope;
					    	if(VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size()>0)
							{ 
								scope = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).get(si-1);
								
							}
						    else
						    {
						    	scope = ElementsDefaultScopes.allVariablesScopes.get(targetfieldQualifiedName);
								
						    }
					    	String instantiation = packageName+
						               "."+leftPartscope+"."+spl[spl.length-1]+" = "+
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
						if(VariablesScopes.variablesOutSets.containsKey(invokedMqName))
						{   
							List<String> r  = VariablesScopes.variablesOutSets.get(invokedMqName);
							VariablesScopes.variablesOutSets.get(leftPartfieldQualifiedName).add(r.get(r.size()-1));
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
			    	{   
			    		chechScopesAndOutPuts(invokeMethodFullNameWithParam);
			    		
			    		int sz = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
			    		String scope =  VariablesScopes.variablesScopes.get(packageName+"."+className).get(sz-1);
			    		String instantiation = packageName+
					               "."+scope+"."+leftPartfieldSimpleName+" = "+
					               invokedMethodPackName
					               +"."+scope+"."+method.getSimpleName() 
					               +"."+"return";
					    
				       
					   VariablesScopes.affectations.add(instantiation);
					   
					   VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).add(scope);
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
								int si = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size();
								if(VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size()>0)
								{ 
									scope = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).get(si-1);
									
								}
							    else
							    {
							    	scope = ElementsDefaultScopes.allVariablesScopes.get(targetfieldQualifiedName);
									
							    }
								int size = VariablesScopes.variablesScopes.get(arg).size();
								String sco;
								if(VariablesScopes.variablesScopes.get(arg).size()>0)
								{ 
									sco = VariablesScopes.variablesScopes.get(arg).get(size-1);
									
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
								VariablesScopes.variablesScopes.get(paramQuaName).add(scope);
								if(VariablesScopes.variablesOutSets.containsKey(arg))
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
			Main.analyseBlock(method, invokedMethodClassName, invokedMethodPackName);
			Main.staticCallGraph.add(methIn);
		}
		else
		{    
			  if(rightPart != null)
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
						if(rightPart.toString().equals("this"))
						{  int r = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
					      String sc = VariablesScopes.variablesScopes.get(packageName+"."+className).get(r-1);
				    	   String temp = sc.replaceAll("\\d","");
							rightPartFullName = temp+".this";
						}
						else
						{
							rightPartFullName = currentElementName+"."+rightVarName;
						}
					}
				   
				   String leftPartFullName;
				   String[] splp = leftPart.toString().split("\\.");
				   String leftVarName = splp[splp.length-1];
				   if(leftPart instanceof CtFieldAccess)
					{
					   leftPartFullName = leftVarName;
					}
				   else
					{
					   leftPartFullName = currentElementName+"."+leftVarName;
					}
				   
				   chechScopesAndOutPuts(packageName+"."+className+"."+leftPartFullName);
				   chechScopesAndOutPuts(packageName+"."+className+"."+rightPartFullName);
				   String scope;
				   
				   if(VariablesScopes.variablesScopes.get(packageName+"."+className+"."+rightPartFullName).size()>0)
					{   int si = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+rightPartFullName).size();
						scope = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+rightPartFullName).get(si-1);
						
					}
				    else
				    { chechScopesAndOutPuts(packageName+"."+className);
				    	if(VariablesScopes.variablesScopes.get(packageName+"."+className).size()>0)
					       { int r = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
						     scope = VariablesScopes.variablesScopes.get(packageName+"."+className).get(r-1);
					       }
					      else
					      {
				         	scope = ElementsDefaultScopes.allVariablesScopes.get(packageName+"."+className+"."+rightPartFullName);
					      }
				    	
				    	
				    }
				   
					String sco;
					if(VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).size()>0)
					{  int size = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).size();
						sco = VariablesScopes.variablesScopes.get(packageName+"."+className+"."+leftPartFullName).get(size-1);
						
					}
				    else
				   {  chechScopesAndOutPuts(packageName+"."+className);
				    	if(VariablesScopes.variablesScopes.get(packageName+"."+className).size()>0)
					       { int r = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
						     sco = VariablesScopes.variablesScopes.get(packageName+"."+className).get(r-1);
					    	 }
					      else
					      {
				       	sco = ElementsDefaultScopes.allVariablesScopes.get(packageName+"."+className+"."+leftPartFullName);
					      }
				   }
					String instantiation = packageName+
				               "."+sco+"."+leftPartFullName+" = "+
				               packageName
				               +"."+scope+"."+rightPartFullName;
			
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
}
