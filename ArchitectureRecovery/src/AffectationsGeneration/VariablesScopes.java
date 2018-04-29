package AffectationsGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.declaration.CtElement;

public class VariablesScopes {
	
	public static List<String> collectedConstructorCalls = new ArrayList<>();
	public static HashMap<String, List<String>> variablesScopes = new HashMap<String, List<String>>();
	public static HashMap<String, List<String>> variablesOutSets = new HashMap<String, List<String>>();
	public static HashMap<String, String> objectsIdentifiers = new HashMap<String, String>();
	public static HashMap<String, List<String>> constrcutorsScopes = new HashMap<String, List<String>>();
	
	public static List<String> affectations = new ArrayList<>();
	
	public static String scope(CtConstructorCall constructor, String lineNumber)
	{ int i = 1;
	  String scope;
		for(String instantiation :  VariablesScopes.collectedConstructorCalls)
		{
			if(instantiation.equals(constructor.getType().getSimpleName()))
			{
				i++;
			}
		}
		
	 scope = constructor.getType().getSimpleName()+i;
	 objectsIdentifiers.put(scope, lineNumber);
		return scope;
	}

}
