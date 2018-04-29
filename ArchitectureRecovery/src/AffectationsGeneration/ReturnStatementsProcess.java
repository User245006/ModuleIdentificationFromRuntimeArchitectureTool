package AffectationsGeneration;

import java.util.ArrayList;
import java.util.List;

import InstrumentationPhase.OutPuts;
import InstrumentationPhase.applicationClasses;
import InstrumentationPhase.injector;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtParameter;

public class ReturnStatementsProcess {

	public static void analyse(CtReturn statement, CtExecutable element, String currentElementName, String className,
			String packageName) 
	
	  {
		String elementParametersNames = "";
		 List<CtParameter> elementParameters = element.getParameters();
		 for(int t =0; t<elementParameters.size(); t++)
		  {
			elementParametersNames = elementParametersNames+elementParameters.get(t).getType().getQualifiedName();
			if(t != elementParameters.size()-1)
			elementParametersNames = elementParametersNames+", ";
		  }
		 
		 String elementFullName = packageName+"."+className+"#"+currentElementName+"("+elementParametersNames+")";
		
		CtExpression returnedExpression = statement.getReturnedExpression();
		
		
		if(returnedExpression != null && applicationClasses.appClassesReferences.contains(returnedExpression.getType()))
		{ String varSimpleName;
		  String[] ff = returnedExpression.toString().split("\\.");
		  int z = ff.length;
			if(returnedExpression instanceof CtFieldAccess)
			{   
			    varSimpleName = ff[z-1];
			    
			}
			else
			{   
				varSimpleName = currentElementName+"."+ff[z-1];
				
			}
			
			 String returnedVarFullName = packageName+"."+className+"."+varSimpleName;
			 chechScopesAndOutPuts(elementFullName);
			 int size = VariablesScopes.variablesScopes.get(elementFullName).size();
			 String scope ;
			 if(size>0)
			 {
			      scope = VariablesScopes.variablesScopes.get(elementFullName).get(size-1);
			 }
			 else
			 {
				 scope = ElementsDefaultScopes.allVariablesScopes.get(returnedVarFullName);
			 }
			 String instantiation = packageName+"."+scope+"."+currentElementName+".return = "+
					               packageName+"."+scope+"."+varSimpleName;
			 VariablesScopes.affectations.add(instantiation);
			
			// add the outputs
			
			String returnStOutpu = packageName+"."+className+"."+currentElementName+".return";
			chechScopesAndOutPuts(returnStOutpu);
            chechScopesAndOutPuts(returnedVarFullName);
            
            int s = VariablesScopes.variablesOutSets.get(returnedVarFullName).size();
            if(s>0)
            {
				
			    String output = VariablesScopes.variablesOutSets.get(returnedVarFullName).get(s-1);
				VariablesScopes.variablesOutSets.get(returnStOutpu).add(output);
				System.out.println("Return statement 1 : "+returnStOutpu+": "+output);
				System.out.println(returnedVarFullName);
            }
            else
            {
            	String output = ElementsDefaultScopes.allVariablesScopes.get(returnedVarFullName);
            	VariablesScopes.variablesOutSets.get(returnStOutpu).add(output);
            	System.out.println("Return statement 2 : "+returnStOutpu+": "+output);
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
