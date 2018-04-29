package WTFIDFScheme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

public class ApplicationInterfaces extends AbstractProcessor<CtInterface>{
	
	public static  Collection<String> appinterfaces = new ArrayList<String>();
	public static  Collection<CtTypeReference> appinterfacesReferences = new ArrayList<CtTypeReference>();
	
	public void process(CtInterface e)
	{  //Collection<CtType<?>> classes=el.getElements(new TypeFilter(CtClass.class));
			if(!e.equals(null))
			{   if(!appinterfaces.contains(e.getQualifiedName()))
			   {
				this.appinterfaces.add(e.getQualifiedName());
				appinterfacesReferences.add(e.getReference());
				
			   }
			/*if(!appClassesReferences.contains(e.getReference()))
			  {
				appClassesReferences.add(e.getReference()); 
			  }
			*/	
			}
	}	
}
