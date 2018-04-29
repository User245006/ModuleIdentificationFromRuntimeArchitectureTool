package GraphsRecovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import InstrumentationPhase.FieldsofUserDefinedClassType;
import LanguarTarjanDominatorsAlgorithm.Dominators;
import LanguarTarjanDominatorsAlgorithm.DominatorsRefinedVersion;
import LanguarTarjanDominatorsAlgorithm.JsonEdge;
import LanguarTarjanDominatorsAlgorithm.JsonGraph;
import LanguarTarjanDominatorsAlgorithm.JsonNode;

public class Main {

	
	public static void main(String[] args) throws Exception {
		spoon.Launcher.main(new String[]
	    		 {"-p", "InstrumentationPhase.applicationClasses:"
	    			   +"InstrumentationPhase.SystemInheritanceGraph:"
		               +"InstrumentationPhase.FieldsofUserDefinedClassType:"
		               +"GraphsRecovery.Main",
	              "-i", "/home/soumia/Bureau/jext/src/",
	              "--source-classpath","/home/soumia/Bureau/Dropbox/Dossier de l'équipe Equipe MAREL/Thèse ZELLAGUI Soumia/Spoon-5.1.0/spoon-core-5.1.0-jar-with-dependencies.jar:"
	              		+ "/home/soumia/Documents/jextJarFiles/ant-contrib-0.1.jar:"
						+"/home/soumia/Documents/jextJarFiles/jgoodies-looks-2.4.0.jar:"
						+"/home/soumia/Documents/jextJarFiles/jgoodies-plastic.jar:"
						+"/home/soumia/Documents/jextJarFiles/jSDG-stubs-jre1.5.jar:"
						+"/home/soumia/Documents/jextJarFiles/jython.jar:"
						+"/home/soumia/Documents/jextJarFiles/looks-2.0.4-sources.jar"
	        		});
		
		
		System.out.println(FieldsofUserDefinedClassType.varNames);
		
		List<String> affectations = new ArrayList<String>();
	    List<String> refinedAffectations = new ArrayList<String>();
	
		/*try {
		BufferedReader in = new BufferedReader(new FileReader("/home/soumia/Documents/OSGiWorkspace/instrumentationPhase/OutPuts/Affectation.log"));
		String str;
		
		
        while((str = in.readLine()) != null){
        	   if(!affectations.contains(str))
        	   {
			    affectations.add(str);
        	   }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// remove reptitions
		try { 
			  File refinedAffFile = new File("/home/soumia/Documents/OSGiWorkspace/instrumentationPhase/OutPuts/RefinedAffectation.log");
			  PrintWriter out;
			  
	          out = new PrintWriter(new FileWriter(refinedAffFile));
	       
	          
		    for(String affectation:affectations)
		    {
		    	   out.println(affectation);
		    }
		
	       }
		catch(IOException e)
		{
			e.printStackTrace();
		}
		*/
		// reload refined affectations after manual validation by the architect
		try {
			BufferedReader in = new BufferedReader(new FileReader("./OutPuts/RefinedAffectation.log"));
			String str;
			
			
	        while((str = in.readLine()) != null){
	        	  refinedAffectations.add(str);
	   
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
		
		// Graphs Recovery
		
		// object flow graph recovery
		//ObjectFlowGraphRecovery.draw(refinedAffectations);
		
		// object graph recovery
        //JsonGraph graph = ObjectGraphRecoveryRefinedVersion.draw(refinedAffectations);
        
       
        //System.out.println("nodes: "+graph.nodes);
        
        //Dominance Tree recovery
  		//DominatorsRefinedVersion.calculateDominators(graph.nodes, graph.edges);
  		
  		
  		// Traces Analysis
  		
  		//File folder = new File("./OutPuts/Traces");
  		//HashMap<String, String> labels = ExecutionTracesAnalysis.CalculateLables(folder);
        //graph.generateJSONEntries();
        
        
        
	}
}
