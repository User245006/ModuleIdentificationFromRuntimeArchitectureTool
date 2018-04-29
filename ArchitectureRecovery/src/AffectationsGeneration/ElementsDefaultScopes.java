package AffectationsGeneration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.visitor.filter.TypeFilter;

public class ElementsDefaultScopes extends AbstractProcessor<CtClass>{

	
	public static HashMap<String, String> methodsScopes = new HashMap<String, String>();
	public static HashMap<String, String> fieldsScopes = new HashMap<String, String>();
	public static HashMap<String, String> localVariablesScopes = new HashMap<String, String>();
	public static HashMap<String, String> methodParametersScopes = new HashMap<String, String>();
	
	public static HashMap<String, String> allVariablesScopes = new HashMap<String, String>();
	
	
	public void process(CtClass classe) {
		
		Collection<CtMethod> methods  = classe.getMethods();
		
		for(CtMethod method : methods)
		{  
			String parametersNames = "";
			List<CtParameter> elementParameters = method.getParameters();
			for(int t =0; t<elementParameters.size(); t++)
			{
				parametersNames = parametersNames+elementParameters.get(t).getType().getQualifiedName();
				if(t != elementParameters.size()-1)
				parametersNames = parametersNames+", ";
				
				String currentParameterName = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
						+"."+classe.getSimpleName()+"."+method.getSimpleName()+"."+elementParameters.get(t).getSimpleName();
				methodParametersScopes.put(currentParameterName, classe.getSimpleName());
				allVariablesScopes.put(currentParameterName, classe.getSimpleName());
			}
			
			String methodName = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
					            +"."+classe.getSimpleName()+"."+method.getSimpleName()+"("+parametersNames+")";
			methodsScopes.put(methodName, classe.getSimpleName());
			allVariablesScopes.put(methodName, classe.getSimpleName());
			//ApproachOutPut.variablesScopes.put(methodName, new ArrayList<>());
			//ApproachOutPut.variablesScopes.get(methodName).add(classe.getSimpleName());
			if(method.getBody()!=null)
			{
				Collection<CtLocalVariable> localVariables = method.getBody().getElements(new TypeFilter(CtLocalVariable.class));
			
				for(CtLocalVariable locVar : localVariables)
				{   
					String locVarName = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
							            +"."+classe.getSimpleName()+"."+method.getSimpleName()+"."
				                        +locVar.getSimpleName();
					localVariablesScopes.put(locVarName, classe.getSimpleName());
					allVariablesScopes.put(locVarName, classe.getSimpleName());
					//ApproachOutPut.variablesScopes.put(locVarName, new ArrayList<>());
					//ApproachOutPut.variablesScopes.get(locVarName).add(classe.getSimpleName());
				}
				String retuStat = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
			            +"."+classe.getSimpleName()+"."+method.getSimpleName()+".return";
				List<CtReturn> ret =  ((CtElement)method).getElements(new TypeFilter(CtReturn.class));
				for(CtReturn r : ret)
				{   if(r != null)
				    {
					  if(r.getReturnedExpression() != null)
					  {
					  String re = r.getReturnedExpression().getType().getSimpleName();
					  allVariablesScopes.put(retuStat, re);
					  }
				    }
				}
			
			}
		}
		
		
		Collection<CtField> fields = classe.getFields();
		for(CtField field : fields)
		{   String fieldName = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()+"."+classe.getSimpleName()+"."+field.getSimpleName();
			fieldsScopes.put(fieldName, classe.getSimpleName());
			allVariablesScopes.put(fieldName, classe.getSimpleName());
		}
		
		
		Collection<CtConstructor> constructors  = classe.getConstructors();
		for(CtConstructor constructor : constructors)
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
				methodParametersScopes.put(currentParameterName, classe.getSimpleName());
				allVariablesScopes.put(currentParameterName, classe.getSimpleName());
				
			}
			
			String methodName = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
					            +"."+classe.getSimpleName()+"."+constructor.getSimpleName()+"("+parametersNames+")";
			methodsScopes.put(methodName, classe.getSimpleName());
			allVariablesScopes.put(methodName, classe.getSimpleName());
			//ApproachOutPut.variablesScopes.put(methodName, new ArrayList<>());
			//ApproachOutPut.variablesScopes.get(methodName).add(classe.getSimpleName());
			if(constructor.getBody()!=null)
			{
				Collection<CtLocalVariable> localVariables = constructor.getBody().getElements(new TypeFilter(CtLocalVariable.class));
			
				for(CtLocalVariable locVar : localVariables)
				{   
					String locVarName = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
							            +"."+classe.getSimpleName()+"."+constructor.getSimpleName()+"."
				                        +locVar.getSimpleName();
					localVariablesScopes.put(locVarName, classe.getSimpleName());
					allVariablesScopes.put(locVarName, classe.getSimpleName());
					//ApproachOutPut.variablesScopes.put(locVarName, new ArrayList<>());
					//ApproachOutPut.variablesScopes.get(locVarName).add(classe.getSimpleName());
				}
				String retuStat = classe.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName()
			            +"."+classe.getSimpleName()+"."+constructor.getSimpleName()+".return";
				List<CtReturn> ret =  ((CtElement)constructor).getElements(new TypeFilter(CtReturn.class));
				for(CtReturn r : ret)
				{   if(r != null)
				    {
					  if(r.getReturnedExpression() != null)
					  {
					  String re = r.getReturnedExpression().getType().getSimpleName();
					  allVariablesScopes.put(retuStat, re);
					  }
				    }
				}
			
			}
		}
		
		
	}
}
