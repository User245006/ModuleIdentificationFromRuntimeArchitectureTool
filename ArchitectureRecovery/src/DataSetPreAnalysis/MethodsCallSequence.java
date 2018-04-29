package DataSetPreAnalysis;


import java.util.ArrayList;
import java.util.List;

import InstrumentationPhase.applicationClasses;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;

public class MethodsCallSequence extends AbstractProcessor<CtInvocation>{

	public static List<String> methodCallsSequencePositions = new ArrayList<>();
	
	public void process(CtInvocation methodInvocation) {
		
		if(applicationClasses.appClassesReferences.contains(methodInvocation.getType()))
		{
			CtExpression target  = methodInvocation.getTarget();
			if(target instanceof CtInvocation || methodInvocation.getParent() instanceof CtConstructorCall || methodInvocation.getParent() instanceof CtInvocation)
			{
				methodCallsSequencePositions.add(methodInvocation+" in: "+methodInvocation.getPosition().toString());
			}
		}
	}

}
