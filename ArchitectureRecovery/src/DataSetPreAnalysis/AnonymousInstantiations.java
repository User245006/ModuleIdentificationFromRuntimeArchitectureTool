package DataSetPreAnalysis;

import java.util.ArrayList;
import java.util.List;

import InstrumentationPhase.applicationClasses;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;

public class AnonymousInstantiations extends AbstractProcessor<CtConstructorCall>{

	public static List<String> anonymousInstancePositions = new ArrayList<>();
	
	public void process(CtConstructorCall consCall) {
		
		CtElement element = consCall.getParent();
		
		if(applicationClasses.appClassesReferences.contains(consCall.getType()))
		{
			if(element instanceof CtConstructorCall || element instanceof CtReturn || element instanceof CtInvocation) 
			{  
				anonymousInstancePositions.add(element+" in: "+element.getPosition().toString());
			}
		}
	}
		
}