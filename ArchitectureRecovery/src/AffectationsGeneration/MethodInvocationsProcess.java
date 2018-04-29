package AffectationsGeneration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import InstrumentationPhase.SystemInheritanceGraph;
import InstrumentationPhase.applicationClasses;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtNamedElement;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtTypeInformation;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.declaration.CtConstructorImpl;

public class MethodInvocationsProcess {
	
	public static void analyse(CtInvocation statement, CtExecutable element, String currentElementName, String className,
			String packageName) 
	{  System.out.println("invostatement: "+statement);
	   if(statement.getExecutable().getDeclaration() !=null)
	   { 
		if(MethodsSignatures.signatures.contains(statement.getExecutable().getDeclaration().getSignature()))
	     {      
				CtExpression target = statement.getTarget();
				
			    CtExecutable method;
			    if(statement.getExecutable().getDeclaration() instanceof CtConstructorImpl)
			    {
			    	method = (CtConstructorImpl) ((CtInvocation)statement).getExecutable().getDeclaration();
			    }
			    else
			    {
			    	 method = (CtMethod) ((CtInvocation)statement).getExecutable().getDeclaration();
			    }
			    
			    List<CtParameter> methodParameters = ((CtExecutable) method).getParameters();
		        String invokedMethodPackName = method.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
		        String invokedMethodClassName =  method.getPosition().getCompilationUnit().getMainType().getSimpleName();  
		        String invM = "";
			    if(target != null) // when a method invocation of super, the target is null
			    { 
			      if(applicationClasses.appClassesReferences.contains(target.getType()) )
			      { 
				       String invokedParametersNames="";
					    for(int t =0; t<methodParameters.size(); t++)
					    { 
					      invokedParametersNames = invokedParametersNames+methodParameters.get(t).getType().getQualifiedName();
						  if(t != methodParameters.size()-1)
					 		invokedParametersNames = invokedParametersNames+", ";
					    }
				       String invokeMethodFullNameWithParam = invokedMethodPackName+"."+invokedMethodClassName
							+"#"+((CtNamedElement) method).getSimpleName()+"("+invokedParametersNames+")";
				       
				       invM = invokeMethodFullNameWithParam;
					   String[] splitff = target.toString().split("\\.");
					           
					  String targetfieldSimpleName;
					  String targetfieldQualifiedName;
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
							chechScopesAndOutPuts(targetfieldQualifiedName);
							String sco;
						    if(VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size()>0)
							{   int si = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size();
								sco = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).get(si-1);
							}
						    else
						    {
						    	sco = ElementsDefaultScopes.allVariablesScopes.get(targetfieldQualifiedName);
								
						    }
						    // here we are only interested in getting the scope of the method
						    //
							chechScopesAndOutPuts(invokeMethodFullNameWithParam);
							VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).add(sco);
							if(method!=null)
							  { 
								 argumParamAffectations(method, statement,currentElementName,className,packageName,targetfieldQualifiedName, 0);
							  }
					       
					    }
				      else
				      {  
				    	     // the invocation does not contain a target (a method of the class)  	 
				    	  if(target.toString().equals(""))
					    	{   
					    		chechScopesAndOutPuts(invokeMethodFullNameWithParam);
					    		chechScopesAndOutPuts(packageName+"."+className);
					    		
					    		int sz = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
					    		if(sz>0)
					    		{
						    		String scope =  VariablesScopes.variablesScopes.get(packageName+"."+className).get(sz-1);
								    VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).add(scope);
								    String classNameScope = packageName+"."+className;
								    if(method!=null)
									{ 
										 argumParamAffectations(method, statement,currentElementName,className,packageName,classNameScope, 1);
									}
					    		}
					    	}
							
					   }
				      
				  analyseAllOverridedMethods((CtMethod)method);
				  Main.analyseBlock(method, invokedMethodClassName, invokedMethodPackName);
				  Main.staticCallGraph.add(invokeMethodFullNameWithParam);
			    }
			    else
			    {   
			    	if(applicationClasses.appClasses.contains(target.toString()))
			        {   
			    		CtMethod methodm = (CtMethod) ((CtInvocation)statement).getExecutable().getDeclaration();
					    List<CtParameter> methodParametersm= methodm.getParameters();
				        String invokedMethodPackNamem = methodm.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
				        String invokedMethodClassNamem =  methodm.getPosition().getCompilationUnit().getMainType().getSimpleName();  
				  
				        String invokedParametersNames="";
					    for(int t =0; t<methodParametersm.size(); t++)
					    { 
					      invokedParametersNames = invokedParametersNames+methodParametersm.get(t).getType().getQualifiedName();
						  if(t != methodParametersm.size()-1)
					 		invokedParametersNames = invokedParametersNames+", ";
					    }
				        String invokeMethodFullNameWithParam = invokedMethodPackNamem+"."+invokedMethodClassNamem
							+"#"+methodm.getSimpleName()+"("+invokedParametersNames+")";
				        invM = invokeMethodFullNameWithParam;
				    	chechScopesAndOutPuts(invokeMethodFullNameWithParam);
			    		
					    VariablesScopes.variablesScopes.get(invokeMethodFullNameWithParam).add(invokedMethodClassNamem);
					    String classNameScope = target.toString();
					    if(methodm!=null)
						  { 
							 argumParamAffectations(methodm, statement,currentElementName,className,packageName,invokedMethodClassNamem, 2);
						  }
					   analyseAllOverridedMethods((CtMethod)methodm);
			    	   Main.analyseBlock(methodm, invokedMethodClassNamem, invokedMethodPackNamem);
			    	   Main.staticCallGraph.add(invokeMethodFullNameWithParam);
			        }
			        else
			        { if(!target.toString().equals("super"))
			          {
				    	CollectionTypesMethods.collectCollectionTypes();
				    	System.out.println(statement);
				    	if(CollectionTypesMethods.collectionTypesNames.contains(target.getType().getQualifiedName()))
				    	{  
				    		CtExecutableReference meth = statement.getExecutable();
				    		for(CtExecutableReference refM : CollectionTypesMethods.collectionMethods)
				    		{   
				    			if(refM.getDeclaringType().getQualifiedName().toString().equals(meth.getDeclaringType().getQualifiedName().toString())
				    				&& refM.getParameters().size() == meth.getParameters().size()
				    				&& refM.getSimpleName().equals(meth.getSimpleName()))
				    			   {
				    				List<CtExpression> arguments = statement.getArguments();
				    				String [] splitt = target.toString().split("\\.");
				    				String targetSimpleName = splitt[splitt.length-1];
				    				String targetFullName;
				    				if(target instanceof CtFieldAccess)
				    				{ 
				    					targetFullName = targetSimpleName;
				    				}
				    				else
				    				{
				    					targetFullName = currentElementName+"."+targetSimpleName;
				    				}
				    				
				    				for(CtExpression arg : arguments)
				    				{  
				    				   String [] argSplit = arg.toString().split("\\.");
				    				   String argSimpleName = argSplit[argSplit.length-1];
				    				   String argFullName;
				    				   
				    					if(applicationClasses.appClassesReferences.contains(arg.getType()))
				    					{ 
				    						if(arg instanceof CtFieldAccess)
				    						{
				    							argFullName = argSimpleName;
				    						}
				    						else
				    						{
				    							argFullName = currentElementName+"."+argSimpleName;
				    						}
				    						
				    						chechScopesAndOutPuts(packageName+"."+className);
				    						
				    						String sco;
				    			    		if(VariablesScopes.variablesScopes.get(packageName+"."+className).size()>0)
				    			    		{
				    			    		  int s = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
				    			    		  sco =  VariablesScopes.variablesScopes.get(packageName+"."+className).get(s-1);
				    			    		}
				    			    		else
				    			    		{
				    			    			sco = ElementsDefaultScopes.allVariablesScopes.get(packageName+"."+className);
				    			    		}
				    			    		
				    			    		String instantiation = packageName+"."+sco+"."+targetFullName+" = "
				    			    				+packageName+"."+sco+"."+argFullName;
				    			    		if(!VariablesScopes.affectations.contains(instantiation))
				    			    		VariablesScopes.affectations.add(instantiation);
				    			    		
				    			      }
				    				}
				    			}
				    		}
				    	}
				       }
			        }
				    }
			    }
			
			    else
			    {    
			    	 if(method.getSimpleName().startsWith("super("))
                     {  
				    	CtTypeReference superclazz = ((CtTypeInformation) method.getParent()).getSuperclass();
				 		while(superclazz!= null)
				 		{  if(applicationClasses.appClassesReferences.contains(superclazz))
				 		   {
					 			int s = VariablesScopes.variablesScopes.get(packageName+"."+className).size();
					 			String scope = VariablesScopes.variablesScopes.get(packageName+"."+className).get(s-1);
					 			constructorsCcopes(superclazz, method, scope);
					 			superclazz = superclazz.getSuperclass();
				 		   }
				 		   else
				 		   {
				 			   superclazz = null;
				 		   }
				 		}
				 		 Main.analyseBlock(method, invokedMethodClassName, invokedMethodPackName);	
                     }
			    }
			}
	   }
		 else
		{   if(statement.toString().startsWith("super("))
		    {
			
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
	
	public static void argumParamAffectations(CtElement method, CtInvocation statement,String currentElementName, String className,String packageName, String targetfieldQualifiedName, int i)
	{   List<CtParameter> methodParameters= ((CtExecutable) method).getParameters();
		 // the invoked method parameters
	    List<CtParameter> parameters = ((CtExecutable) method).getParameters();
	    List<CtExpression> arguments = statement.getArguments();
	    String methodSimpleName = ((CtNamedElement) method).getSimpleName();
	    
	    String invokedMethodPackName = method.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
        String invokedMethodClassName =  method.getPosition().getCompilationUnit().getMainType().getSimpleName();  
  
        String invokedParametersNames="";
	    for(int t =0; t<methodParameters.size(); t++)
	    { 
	      invokedParametersNames = invokedParametersNames+methodParameters.get(t).getType().getQualifiedName();
		  if(t != methodParameters.size()-1)
	 		invokedParametersNames = invokedParametersNames+", ";
	    }
	    String invokeMethodFullNameWithParam = invokedMethodPackName+"."+invokedMethodClassName
				+"#"+((CtNamedElement) method).getSimpleName()+"("+invokedParametersNames+")";
	
	    
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
				if(i == 0)
				{
					int si = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size();
					if(VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).size()>0)
					{ 
						scope = VariablesScopes.variablesOutSets.get(targetfieldQualifiedName).get(si-1);
						
					}
				    else
				    {
				    	scope = ElementsDefaultScopes.allVariablesScopes.get(targetfieldQualifiedName);
						
				    }
				}
				else
				{  
					if(i == 1)
					{
						int si = VariablesScopes.variablesScopes.get(targetfieldQualifiedName).size();
					    if(VariablesScopes.variablesScopes.get(targetfieldQualifiedName).size()>0)
						{ 
							scope = VariablesScopes.variablesScopes.get(targetfieldQualifiedName).get(si-1);
							
						}
					    else
					    {
					    	scope = ElementsDefaultScopes.allVariablesScopes.get(targetfieldQualifiedName);
							
					    }
					}
					else
					{
					
						scope =  targetfieldQualifiedName;
					
					}
				}
				
				String sc;
				chechScopesAndOutPuts(arg);
				
				if(VariablesScopes.variablesScopes.get(arg).size()>0)
				{   
					int siz = VariablesScopes.variablesScopes.get(arg).size();
					sc = VariablesScopes.variablesScopes.get(arg).get(siz-1);
					
				}
			    else
			    {   
			    	sc = ElementsDefaultScopes.allVariablesScopes.get(arg);
					
			    }
				
				String instantiation = invokedMethodPackName+
			               "."+scope+"."+methodSimpleName+"."+parameterName+" = "+
			               packageName
			               +"."+sc+"."+argumentSimpleName;
		
				VariablesScopes.affectations.add(instantiation);
				
				// here we add parameters out puts
				
				String paramQuaName = invokedMethodPackName+"."+invokedMethodClassName+"."+
						((CtNamedElement) method).getSimpleName()+"."+parameterName;
				chechScopesAndOutPuts(paramQuaName);
				
				if(VariablesScopes.variablesOutSets.get(arg).size()>0)
				{   
					List<String> r  = VariablesScopes.variablesOutSets.get(arg);
					
					String t = r.get((r.size())-1);
					VariablesScopes.variablesOutSets.get(paramQuaName).add(t);
				}
				else
				{
				 String lefRet = argument.getType().getSimpleName();
				 VariablesScopes.variablesOutSets.get(paramQuaName).add(lefRet);
				}
				
				//adding scopes
				VariablesScopes.variablesScopes.get(paramQuaName).add(scope);
				
			}
           }
    }
	}
	
	public static void constructorsCcopes(CtTypeReference classe, CtExecutable constructor, String scope)
	{
		  
			String parametersNames = "";
			List<CtParameter> elementParameters = constructor.getParameters();
			for(int t =0; t<elementParameters.size(); t++)
			{
				parametersNames = parametersNames+elementParameters.get(t).getType().getQualifiedName();
				if(t != elementParameters.size()-1)
				parametersNames = parametersNames+", ";
				
				String currentParameterName = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
						+"."+classe.getSimpleName()+"."+classe.getSimpleName()+"."+elementParameters.get(t).getSimpleName();
				VariablesScopes.variablesScopes.get(currentParameterName).add(scope);
			}
			
			if(constructor.getBody()!=null)
			{
				Collection<CtLocalVariable> localVariables = constructor.getBody().getElements(new TypeFilter(CtLocalVariable.class));
			
				for(CtLocalVariable locVar : localVariables)
				{   
					String locVarName = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
							            +"."+classe.getSimpleName()+"."+constructor.getSimpleName()+"."
				                        +locVar.getSimpleName();
					VariablesScopes.variablesScopes.get(locVarName).add(scope);
				    
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
