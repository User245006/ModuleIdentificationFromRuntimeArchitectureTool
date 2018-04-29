package AffectationsGeneration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class MethodsSignatures extends AbstractProcessor<CtType>{
	
	public static List<String> signatures = new ArrayList<>();
	public static List<String> signaturesExec = new ArrayList<>();

	@Override
	public void process(CtType cl) {
		
		Collection<CtMethod> meth = cl.getMethods();
		for(CtMethod m : meth)
		{
		signatures.add(m.getSignature());
		
		
		    List<CtParameter> methodParametersm= m.getParameters();
	        String invokedMethodPackNamem = m.getPosition().getCompilationUnit().getMainType().getPackage().getQualifiedName();
	        String invokedMethodClassNamem =  m.getPosition().getCompilationUnit().getMainType().getSimpleName();  
	  
	        String invokedParametersNames="";
		    for(int t =0; t<methodParametersm.size(); t++)
		    { 
		      invokedParametersNames = invokedParametersNames+methodParametersm.get(t).getType().getQualifiedName();
			  if(t != methodParametersm.size()-1)
		 		invokedParametersNames = invokedParametersNames+", ";
		    }
	        String invokeMethodFullNameWithParam = invokedMethodPackNamem+"."+invokedMethodClassNamem
				+"#"+m.getSimpleName()+"("+invokedParametersNames+")";
			signaturesExec.add(invokeMethodFullNameWithParam);
		}
		
		List<CtConstructor> cons =  cl.getElements(new TypeFilter(CtConstructor.class));
		for(CtConstructor c : cons)
		{
			signatures.add(c.getSignature());
		}
		}

}
