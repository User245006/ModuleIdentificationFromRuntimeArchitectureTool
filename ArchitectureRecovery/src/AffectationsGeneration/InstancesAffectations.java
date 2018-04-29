package AffectationsGeneration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import InstrumentationPhase.OutPuts;
import InstrumentationPhase.applicationClasses;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class InstancesAffectations extends AbstractProcessor<CtConstructorCall> {

	@Override
	public void process(CtConstructorCall cTorCall) {
		// the role of this class is only to get the methods to process when begening from the main
		
		String typeOfClass = cTorCall.getType().getQualifiedName();
	    Collection<String> ret = applicationClasses.appClasses;
	
	    System.out.println(cTorCall.getPosition());
		if(typeOfClass != null && ret.contains(typeOfClass))
		{ 
		  CtElement gparent = cTorCall.getParent();
		  while(!(gparent instanceof CtAssignment) && (!(gparent instanceof CtLocalVariable)) && (!(gparent instanceof CtReturn) && (!(gparent instanceof CtClass))) && (!(gparent instanceof CtInterface)))
		   {  
			  gparent = gparent.getParent();
		   }
		CtElement statement =  gparent;
		String affectation = "";
		if(statement instanceof CtAssignment)
		{
			CtExpression leftPart = ((CtAssignment) statement).getAssigned();
			CtExpression rightPart = ((CtAssignment) statement).getAssignment();
			
			
			if(leftPart instanceof CtFieldAccess)
			{   
				String PackageName = cTorCall.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
			    String className = cTorCall.getPosition().getCompilationUnit().getMainType().getSimpleName();
			    
			    String[] splitff = leftPart.toString().split("\\.");
			    String fieldSimpleName = splitff[splitff.length-1];
			    
			    String insantiatedClassPackage = cTorCall.getType().getPosition().getCompilationUnit().getDeclaredPackage().getQualifiedName();
			    String instantiatedClass = cTorCall.getType().getSimpleName();
			    
			    OutPuts.outPuts.put(PackageName+"."+className+"."+fieldSimpleName, cTorCall.getType().getQualifiedName());
			    
			    if(!OutPuts.alloutPuts.containsKey(PackageName+"."+className+"."+fieldSimpleName))
			    {  List<String> l = new ArrayList<>();
			        l.add(cTorCall.getType().getQualifiedName());
			       OutPuts.alloutPuts.put(PackageName+"."+className+"."+fieldSimpleName,l);
			    }
			    else
			    {   if(!OutPuts.alloutPuts.get(PackageName+"."+className+"."+fieldSimpleName).contains(cTorCall.getType().getQualifiedName()))
			    	
			    	OutPuts.alloutPuts.get(PackageName+"."+className+"."+fieldSimpleName).add(cTorCall.getType().getQualifiedName());
			    }
				
			}
			else
			{  
			    CtElement parent = leftPart.getParent();
			    while((!(parent instanceof CtConstructor)) && (!(parent instanceof CtMethod)) && (!(parent instanceof CtAnonymousExecutable)))
			    {
			    	parent = parent.getParent();
			    }
			   
				String PackageName = cTorCall.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
			    String className = cTorCall.getPosition().getCompilationUnit().getMainType().getSimpleName();
			    
			    String parentName ="";
			    if((parent instanceof CtConstructor) || (parent instanceof CtAnonymousExecutable))
			    {
			    	parentName = cTorCall.getPosition().getCompilationUnit().getMainType().getSimpleName();
			    }
			    
			    if(parent instanceof CtMethod)
			    {
			    	parentName = ((CtMethod) parent).getSimpleName();
			    }
			    String variableSimpleName = parentName+"."+leftPart.toString();
			    
			    //System.out.println(cTorCall);
			    //System.out.println(cTorCall.getType().getPosition().getCompilationUnit().getDeclaredPackage().getQualifiedName());
			    String insantiatedClassPackage = cTorCall.getType().getPosition().getCompilationUnit().getDeclaredPackage().getQualifiedName();
			    String instantiatedClass = cTorCall.getType().getSimpleName();
			    
			    if(!OutPuts.alloutPuts.containsKey(PackageName+"."+className+"."+variableSimpleName))
			    {  List<String> l = new ArrayList<>();
			        l.add(cTorCall.getType().getQualifiedName());
			       OutPuts.alloutPuts.put(PackageName+"."+className+"."+variableSimpleName,l);
			    }
			    else
			    {  if(!OutPuts.alloutPuts.get(PackageName+"."+className+"."+variableSimpleName).contains(cTorCall.getType().getQualifiedName()))
			    	
			    	OutPuts.alloutPuts.get(PackageName+"."+className+"."+variableSimpleName).add(cTorCall.getType().getQualifiedName());
			    }
				
			}
		}
		
		if(statement instanceof CtLocalVariable)
		{   CtLocalVariable locVar = (CtLocalVariable) statement;
			
			CtElement parent = locVar.getParent();
		    while((!(parent instanceof CtConstructor)) && (!(parent instanceof CtMethod)) && (!(parent instanceof CtAnonymousExecutable)))
		    {  
		    	parent = parent.getParent();
		    }
		   
		    String PackageName = cTorCall.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
		    String className = cTorCall.getPosition().getCompilationUnit().getMainType().getSimpleName();
		    
		    String parentName ="";
		    if(parent instanceof CtConstructor)
		    {
		    	parentName = cTorCall.getPosition().getCompilationUnit().getMainType().getSimpleName();
		    }
		    
		    if(parent instanceof CtMethod)
		    {
		    	parentName = ((CtMethod) parent).getSimpleName();
		    }
		    
		    if(parent instanceof CtAnonymousExecutable)
		    {
		    	parentName = cTorCall.getPosition().getCompilationUnit().getMainType().getSimpleName();
		    }
		    String variableSimpleName = parentName+"."+locVar.getSimpleName();
		    
		    OutPuts.outPuts.put(PackageName+"."+className+"."+variableSimpleName, cTorCall.getType().getQualifiedName());	
		   
		    if(!OutPuts.alloutPuts.containsKey(PackageName+"."+className+"."+variableSimpleName))
		    {  List<String> l = new ArrayList<>();
		        l.add(cTorCall.getType().getQualifiedName());
		       OutPuts.alloutPuts.put(PackageName+"."+className+"."+variableSimpleName,l);
		    }
		    else
		    {   if(!OutPuts.alloutPuts.get(PackageName+"."+className+"."+variableSimpleName).contains(cTorCall.getType().getQualifiedName()))
		    	OutPuts.alloutPuts.get(PackageName+"."+className+"."+variableSimpleName).add(cTorCall.getType().getQualifiedName());
		    }
		
		}
		
		
		if(statement instanceof CtReturn)
		{
			
		}
		
		if(statement instanceof CtClass)
		{
			
		}
		
		}
	}
	
}
