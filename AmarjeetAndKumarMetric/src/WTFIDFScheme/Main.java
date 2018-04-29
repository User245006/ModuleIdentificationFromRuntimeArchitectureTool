package WTFIDFScheme;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;


public class Main extends AbstractProcessor<CtPackage>{
	
	
	//public static List<CtTypeReference> systemTypes = new ArrayList<>();
	
	public static HashMap<String, List<String>> packagesTypes = new HashMap<>();
	
	public static List<ClasseRelationShips> allRelationShips = new ArrayList<>();
	
	public void process(CtPackage packag) {
		
			System.out.println("package: "+packag.getQualifiedName());
			List<String> packTYpes = new ArrayList<>();
			Set<CtType<?>> types = packag.getTypes();
			
			for(CtType type : types)
			{ 
					if(!type.isAnonymous())
					{
						packTYpes.add(type.getQualifiedName());
						for(CtTypeReference classe : ApplicationClasses.appClassesReferences)
						{
							if(!type.getQualifiedName().equals(classe.getQualifiedName()))
							{   
							    if(!classe.isAnonymous())
							    {
							      collectStructuralRelationShips(type, classe.getDeclaration());
							    }
							}
						}
					}
			}
			
			packagesTypes.put(packag.getQualifiedName(), packTYpes);
			System.out.println("end package");

	}
	
	public static void collectStructuralRelationShips(CtType source, CtType target)
	{   
		ClasseRelationShips relationShips = new ClasseRelationShips(); 
	    relationShips.source = source;
		relationShips.target = target;
		
		if(source.getSuperclass()!=null)
		{
			if(source.getSuperclass().getQualifiedName().equals(target.getQualifiedName()))
			{   HashMap<String, Integer> extendRelationSHIP = new HashMap<>();
			    extendRelationSHIP.put("EX", 1);
				relationShips.relationShips.add(extendRelationSHIP);
			}
		}
		if(!ApplicationInterfaces.appinterfaces.contains(source.getQualifiedName()))
		{
			if(ApplicationInterfaces.appinterfaces.contains(target.getQualifiedName()))
			{   //System.out.println("oui:"+target.getQualifiedName());
				Set<CtTypeReference<?>> implementedInterfaces = source.getSuperInterfaces();
				  
					if(implementedInterfaces.contains(target.getReference()))
					{   HashMap<String, Integer> IMRelationSHIP = new HashMap<>();
						IMRelationSHIP.put("IM", 1);
						relationShips.relationShips.add(IMRelationSHIP);
					}
				
			}
		}
		else
		{
			if(ApplicationInterfaces.appinterfaces.contains(target.getQualifiedName()))
			{   //System.out.println("oui:"+target.getQualifiedName());
				Set<CtTypeReference<?>> implementedInterfaces = source.getSuperInterfaces();
				  
					if(implementedInterfaces.contains(target.getReference()))
					{   HashMap<String, Integer> EXRelationSHIP = new HashMap<>();
					    EXRelationSHIP.put("EX", 1);
						relationShips.relationShips.add(EXRelationSHIP);
					}
				
			}
		}
		
		CollectHasParameterRelationShip(source, target, relationShips);
		
		CollectReferenceAndCallRelationShip(source, target, relationShips);
		
		CollectIsTypeOfRelationShip(source, target, relationShips);
		
		CollectReturnTypeRelationShip(source, target, relationShips);
		
		CollectThrowsRelationShip(source, target, relationShips);
		
		
		/*System.out.println(relationShips.source.getQualifiedName());
		System.out.println(relationShips.target.getQualifiedName());
		System.out.println(relationShips.relationShips);
		System.out.println("******************");*/
		if(!relationShips.relationShips.isEmpty())
		{  allRelationShips.add(relationShips);	}
		
	}
	
	

	private static void CollectThrowsRelationShip(CtType source, CtType target, ClasseRelationShips relationShips)
	{   int throwsTarget = 0;
		Set<CtMethod> methods = source.getMethods();
		for(CtMethod method : methods)
		{
			if(method.getBody()!=null)
			{  
				Set<CtTypeReference> thrownTypes = method.getThrownTypes();
				for(CtTypeReference thrownType : thrownTypes)
				{
					if(thrownType.getQualifiedName().equals(target.getQualifiedName()))
					{
						
						throwsTarget++;
					}
				}
			}
		}
		if(throwsTarget!=0)
		{   HashMap<String, Integer> THRelationSHIP = new HashMap<>();
		    THRelationSHIP.put("TH", throwsTarget);
			relationShips.relationShips.add(THRelationSHIP);
		}
	}

	private static void CollectReturnTypeRelationShip(CtType source, CtType target, ClasseRelationShips relationShips)
	{   int returnedTarget = 0;
		Set<CtMethod> methods = source.getMethods();
		for(CtMethod method : methods)
		{
			if(method.getType()!=null  && !method.getType().toString().equals("void") && !method.getType().isPrimitive())
			{  
			   if(ApplicationClasses.appClassesReferences.contains(method.getType()))
			   {
			    String TargetName = target.getQualifiedName();
				if(method.getType().getQualifiedName().equals(TargetName))
				{
					returnedTarget++;
				}
			   }
			}
		}
		
		if(returnedTarget!=0)
		{   HashMap<String, Integer> RTRelationSHIP = new HashMap<>();
		    RTRelationSHIP.put("RT", returnedTarget);
			relationShips.relationShips.add(RTRelationSHIP);
		}
	}

	private static void CollectIsTypeOfRelationShip(CtType source, CtType target, ClasseRelationShips relationShips) 
	{  int IsTypeOffields = 0;
		List<CtField> fields = source.getFields();
		for(CtField field : fields)
		{
			if(field.getType().getQualifiedName().equals(target.getQualifiedName()))
			{
				IsTypeOffields++;
			}
		}
		
		if(IsTypeOffields!=0)
		{   HashMap<String, Integer> ITRelationSHIP = new HashMap<>();
		    ITRelationSHIP.put("IT", IsTypeOffields);
			relationShips.relationShips.add(ITRelationSHIP);
		}
		
	}

	private static void CollectHasParameterRelationShip(CtType source, CtType target,ClasseRelationShips relationShips) 
	{   int nParamOfTargetType = 0;
	
		Set<CtMethod> methods = source.getMethods();
		for(CtMethod method : methods)
		{
			List<CtParameter> methodParameters = method.getParameters();
			for(CtParameter param : methodParameters)
			{
				if(param.getType().equals(target))
				{
					nParamOfTargetType++;
				}
			}
		}
		
		Collection<CtExecutableReference<?>> constructors = source.getAllExecutables();
		for(CtExecutableReference constructor : constructors)
		{
			if(constructor instanceof CtConstructor)
			{
				List<CtParameter> constructorParameters = constructor.getParameters();
				for(CtParameter param : constructorParameters)
				{
					if(param.getType().equals(target))
					{
						nParamOfTargetType++;
					}
				}
			}
		}
		
		if(nParamOfTargetType!=0)
		{   HashMap<String, Integer> HPRelationSHIP = new HashMap<>();
		    HPRelationSHIP.put("HP", nParamOfTargetType);
			relationShips.relationShips.add(HPRelationSHIP);
		}
	}

	

	private static void CollectReferenceAndCallRelationShip(CtType source, CtType target, ClasseRelationShips relationShips) 
	{   int fieldsOfTargetType = 0;
	    int methodsOfTargetType = 0;
		List<CtConstructorCall> constructorCalls = source.getElements(new TypeFilter(CtConstructorCall.class));
		for(CtConstructorCall constructorCall : constructorCalls)
		{
			if(constructorCall.getType().getQualifiedName().equals(target.getQualifiedName()))
			{  fieldsOfTargetType++;
			   methodsOfTargetType++;
				/*List<CtFieldAccess> fieldAccesses = source.getElements(new TypeFilter(CtFieldAccess.class));
				for(CtFieldAccess fieldAccess : fieldAccesses)
				{
					if(fieldAccess.getVariable().getDeclaringType().getQualifiedName().equals(target.getQualifiedName()))
					{
						fieldsOfTargetType++;
					}
				}
				
				List<CtInvocation> methodCalls = source.getElements(new TypeFilter(CtInvocation.class));
				for(CtInvocation methodCall: methodCalls)
				{
					if(methodCall.getTarget()!=null && !methodCall.getTarget().toString().equals("super"))
					{ 
						if(methodCall.getTarget().getType().getQualifiedName().equals(target.getQualifiedName()))
						{
							methodsOfTargetType++;
						}
					}
				}
				*/

			}
		}

		if(fieldsOfTargetType!=0)
		{   HashMap<String, Integer> RERelationSHIP = new HashMap<>();
		    RERelationSHIP.put("RE", fieldsOfTargetType);
			relationShips.relationShips.add(RERelationSHIP);
		}	
		
		if(methodsOfTargetType!=0)
		{   HashMap<String, Integer> CARelationSHIP = new HashMap<>();
		    CARelationSHIP.put("CA", methodsOfTargetType);
			relationShips.relationShips.add(CARelationSHIP);
		}	
				
	}
	public static void main(String[] args) throws Exception {
		spoon.Launcher.main(new String[]
	    		 {"-p", "WTFIDFScheme.ApplicationClasses:"
	    		 		  +"WTFIDFScheme.ApplicationInterfaces:"
	    			     +"WTFIDFScheme.Main:",
	              "-i", "/home/soumia/Bureau/Dropbox/Dossier de l'équipe Equipe MAREL/Thèse ZELLAGUI Soumia/WsE/MiniDrawComponents/src/",
	              "--source-classpath","/home/soumia/Bureau/Dropbox/Dossier de l'équipe Equipe MAREL/Thèse ZELLAGUI Soumia/Spoon-5.1.0/spoon-core-5.1.0-jar-with-dependencies.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-aop-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-aop-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-aop-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-aspects-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-aspects-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-aspects-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-beans-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-beans-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-beans-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-indexer-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-indexer-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-indexer-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-support-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-support-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-context-support-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-core-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-core-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-core-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-expression-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-expression-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-expression-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-instrument-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-instrument-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-instrument-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jcl-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jcl-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jcl-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jdbc-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jdbc-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jdbc-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jms-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jms-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-jms-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-messaging-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-messaging-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-messaging-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-orm-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-orm-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-orm-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-oxm-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-oxm-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-oxm-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-test-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-test-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-test-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-tx-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-tx-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-tx-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-web-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-web-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-web-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-webflux-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-webflux-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-webflux-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-webmvc-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-webmvc-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-webmvc-5.0.2.RELEASE-sources.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-websocket-5.0.2.RELEASE.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-websocket-5.0.2.RELEASE-javadoc.jar:"
	            		  +"/home/soumia/Documents/spring-framework-5.0.2.RELEASE/libs/spring-websocket-5.0.2.RELEASE-sources.jar:"
	              +"/home/soumia/Bureau/PMDLibs/apache-ant-1.8.2.jar:"
	              +"/home/soumia/Bureau/PMDLibs/asm-5.1.jar:"
	              +"/home/soumia/Bureau/PMDLibs/com.springsource.org.objectweb.asm_2.2.3.jar:"
	              +"/home/soumia/Bureau/PMDLibs/commons-io-2.4.jar:"
	              +"/home/soumia/Bureau/PMDLibs/commons-lang3-3.0.jar:"
	              +"/home/soumia/Bureau/PMDLibs/gson-2.2.2.jar:"
	              +"/home/soumia/Bureau/PMDLibs/jaxen-1.1.2.jar:"
	              +"/home/soumia/Bureau/PMDLibs/jcommander-1.30.jar:"
	              +"/home/soumia/Bureau/PMDLibs/org.apache.commons.io.jar:"
	              +"/home/soumia/Bureau/PMDLibs/org.objectweb.asm-3.2.0.jar:"
	              +"/home/soumia/Bureau/PMDLibs/saxon-9.1.0.8.jar:"
	              +"/home/soumia/Bureau/PMDLibs/saxon-he-9.3.0.5.jar:"
	              //jhotdraw
	              +"/home/soumia/Téléchargements/jdo.jar:"
	              +"/home/soumia/Téléchargements/batik-svggen-1.7.jar:"
	              +"/home/soumia/Téléchargements/batik-dom-1.7.jar:"
	        });
		
		
		
		CohesionAndCoupling cohCoupl = new CohesionAndCoupling();
		
		
		cohCoupl.SCSValueCalculation(allRelationShips);
		for(ClasseRelationShips re : cohCoupl.allRefinedRelationShips)
		{   System.out.println("********************************");
			System.out.println(re.source.getQualifiedName());
			System.out.println(re.target.getQualifiedName());
			System.out.println(re.relationShips);
			System.out.println(re.couplingValue);
			System.out.println("********************************");
		}
		cohCoupl.cohesionCalculation(packagesTypes, cohCoupl.allRefinedRelationShips);
		cohCoupl.couplingCalculation(packagesTypes, cohCoupl.allRefinedRelationShips);
		
		System.out.println(cohCoupl.packageCohesion);
		System.out.println(cohCoupl.packageCoupling);
		
	}

}

