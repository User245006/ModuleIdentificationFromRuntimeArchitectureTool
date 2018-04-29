package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import AffectationsGeneration.VariablesScopes;
import DataSetPreAnalysis.AnonymousInstantiations;
import DataSetPreAnalysis.MethodsCallSequence;
import GraphsRecovery.ExecutionTracesAnalysis;
import GraphsRecovery.ObjectGraphRecoveryRefinedVersion;
import GraphsRecovery.ToIRecovery;
import InstrumentationPhase.OutPuts;
import InstrumentationPhase.SystemInheritanceGraph;
import InstrumentationPhase.SystemInheritanceGraph2;
import LanguarTarjanDominatorsAlgorithm.DominatorsRefinedVersion;
import LanguarTarjanDominatorsAlgorithm.JsonGraph;
import LanguarTarjanDominatorsAlgorithm.JsonNode;
import spoon.reflect.reference.CtTypeReference;

public class MainDialog {
	
	public static List<String> affectations = new ArrayList<>();
	public static HashMap<String, String> positions = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		spoon.Launcher.main(new String[]
	    		 {"-p", "InstrumentationPhase.applicationClasses:"
	    			    +"InstrumentationPhase.applicationInterfaces:"
	    			   //+"DataSetPreAnalysis.AnonymousInstantiations:"
	        		   //+"DataSetPreAnalysis.MethodsCallSequence:"
	    			   +"InstrumentationPhase.SystemInheritanceGraph2:"
	        		   +"InstrumentationPhase.applicationConstructorCalls:"
		               +"InstrumentationPhase.FieldsofUserDefinedClassType",
	              "-i", "/home/soumia/Documents/OSGiWorkspace/PMD6.2.0/src/",
	              "--source-classpath","/home/soumia/Bureau/Dropbox/Dossier de l'équipe Equipe MAREL/Thèse ZELLAGUI Soumia/Spoon-5.1.0/spoon-core-5.1.0-jar-with-dependencies.jar:"
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
		
		// Test if the input source code respects certain structure
		if(AnonymousInstantiations.anonymousInstancePositions.size() != 0 || MethodsCallSequence.methodCallsSequencePositions.size() != 0)
		{   System.out.println(" ########################################################### ");
			System.out.println("Your input system does not respect our tool rules. Please modify statements od the above positions: ");
			System.out.println(" ########################################################### ");
			for(String instance : AnonymousInstantiations.anonymousInstancePositions)
		    {
		    	System.out.println(instance);
		    }
		    
		    for(String methodCall : MethodsCallSequence.methodCallsSequencePositions)
		    {
		    	System.out.println(methodCall);
		    }
		}
		else
		{   
			
			System.out.println(" ########################################################### ");
			System.out.println("Amazing, the input source code respects the required structure, you can pass to the next step of the process");
			System.out.println(" ########################################################### ");
			
			// Now we start by collecting affectations
			/*
			affectations = AffectationsGeneration.Main.affectationsGeneration();
			for(String aff : affectations)
		     {
		        System.out.println(aff);
		     }
		     affectations.clear();
			*/
			
			File file= new File("/home/soumia/Documents/conferencesDifferentVersions/ASE2018/1/Experiments/PMD/PMDAff2.txt");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		    //get the first line to calculate application lenght
			String line = null;
			
			while ((line = br.readLine()) != null) 
			{
			 affectations.add(line);
			}
			positions = VariablesScopes.objectsIdentifiers;
			InstrumentationPhase.FieldsofUserDefinedClassType.varNames = refineFields(InstrumentationPhase.FieldsofUserDefinedClassType.varNames);
			
			System.out.println(InstrumentationPhase.FieldsofUserDefinedClassType.varNames);
			// object graph recovery
	        JsonGraph graph = ObjectGraphRecoveryRefinedVersion.draw(affectations, positions);
	        
	        //Dominance Tree recovery
	  		DominatorsRefinedVersion.calculateDominators(graph.nodes, graph.edges);
	  		System.out.println("object graph size: "+graph.nodes.size());
	  		// Tree of creation recovery
	  		ToIRecovery.TreeOfCreationRecovery(affectations);
	  		
	  		for(String obj : ToIRecovery.objectCreators.keySet())
	  		{
	  			System.out.println("Object: "+obj+" , Creator: "+ToIRecovery.objectCreators.get(obj));
	  		}
	  		//Test if the ToI is equal to ToCS
	  		if(ToIRecovery.compareToIwithToCS(ToIRecovery.objectCreators, DominatorsRefinedVersion.dominators) == true)
	  		{
	  			System.out.println("ToI and ToCs are Isomorphes");
	  		}
	  		else
	  		{
	  			System.out.println("ToI and ToCs are not Isomorphes");
	  		}
	  		
			for(CtTypeReference entry: SystemInheritanceGraph2.inheritanceRelationShip.keySet())
			{
	  		  System.out.println("Class: "+entry.getSimpleName()+ "  Extends: "+SystemInheritanceGraph2.inheritanceRelationShip.get(entry));
			}
	  		
	  		// Traces Analysis
	  		
	  		//File folder = new File("./OutPuts/Traces");
	  		//HashMap<String, String> labels = ExecutionTracesAnalysis.CalculateLables(folder);
	  		
	  		
	  	   // addLabels(graph, labels);
	      //  graph.generateJSONEntries();
	     
		}
    }
	
	public static void addLabels(JsonGraph graph, HashMap<String, String> labels)
	{  
		for(JsonNode node : graph.nodes)
		{
			String position = node.position;
			//System.out.println("position: "+position);
			Set keys = labels.keySet();
		    Iterator it = keys.iterator();
		    while (it.hasNext())
		    {
		       Object k = it.next(); 
		       Object line = labels.get(k); 
		       System.out.println("***: "+position);
		       System.out.println("***: "+k);
		       if(k.toString().trim().equals(position.trim()))
				{   
				    String newLabel = labels.get(k).replaceAll("\\[", "");
				    newLabel = newLabel.replaceAll("]", "");
					node.label = newLabel;
				}
		    }
		}
	}
	
	
	public static List<String> refineFields(List<String> varNames)
	{
		int sz = varNames.size();
		for(int k =0; k < sz; k++) 
		{
		  String var = varNames.get(k); 
		  
		   String newValue = "";
			if(var.contains("$"))
			{
				String[] splittedVar = var.split("\\.");
				int size = splittedVar.length;
				String v = splittedVar[size-2];
				if(v.contains("$"))
				{   
					String[] splittedV = v.split("\\$");
					if(splittedV.length>0)
					{
						v = splittedV[1];
					}
				}
			   splittedVar[size-2] = v;
			   
				for(int i=0; i<size; i++)
				{
					if(i!=(size-1))
					{   String temp = splittedVar[i]+".";
					    newValue = newValue.concat(temp);
					}
					else
					{
						String temp = splittedVar[i];
						newValue = newValue.concat(temp);
					}
				}
				
				if(!varNames.contains(newValue))
				varNames.add(newValue);
			}
			
		}
		
		return varNames;
	}

}
