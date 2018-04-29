package WTFIDFScheme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class ApplicationClasses extends AbstractProcessor<CtType>{
	
	public static  Collection<String> appClasses = new ArrayList<String>();
	public static  Collection<CtTypeReference> appClassesReferences = new ArrayList<CtTypeReference>();
	public static  HashMap <String, CtTypeReference> classNameReference = new HashMap<>();
	
	public void process(CtType e)
	{  //Collection<CtType<?>> classes=el.getElements(new TypeFilter(CtClass.class));
			if(!e.equals(null) && !e.isAnonymous() && e.isTopLevel())
			{   if(!appClasses.contains(e.getQualifiedName()))
			   {
				 this.appClasses.add(e.getQualifiedName());
				 appClassesReferences.add(e.getReference());
				 classNameReference.put(e.getQualifiedName(), e.getReference());
				 
			   }
			
			}
	}	
}
