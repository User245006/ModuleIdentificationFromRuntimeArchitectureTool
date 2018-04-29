package DataSetPreAnalysis;

public class Main {
	
	public static void main(String[] args) throws Exception {
		spoon.Launcher.main(new String[]
	    		 {"-p", "InstrumentationPhase.applicationClasses:"
	    			   +"DataSetPreAnalysis.AnonymousInstantiations:"
	        		   +"DataSetPreAnalysis.MethodsCallSequence:"
	        		   +"DataSetPreAnalysis.Main",
	              "-i", "/home/soumia/Bureau/Dropbox/Dossier de l'équipe Equipe MAREL/Thèse ZELLAGUI Soumia/WsE/MovieCatalogue/src/",
	              "--source-classpath","/home/soumia/Bureau/Dropbox/Dossier de l'équipe Equipe MAREL/Thèse ZELLAGUI Soumia/Spoon-5.1.0/spoon-core-5.1.0-jar-with-dependencies.jar:"
	              		+ "/home/soumia/Documents/jextJarFiles/ant-contrib-0.1.jar:"
						+"/home/soumia/Documents/jextJarFiles/jgoodies-looks-2.4.0.jar:"
						+"/home/soumia/Documents/jextJarFiles/jgoodies-plastic.jar:"
						+"/home/soumia/Documents/jextJarFiles/jSDG-stubs-jre1.5.jar:"
						+"/home/soumia/Documents/jextJarFiles/jython.jar:"
						+"/home/soumia/Documents/jextJarFiles/looks-2.0.4-sources.jar"
	        		});
		
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
		{   System.out.println(" ########################################################### ");
			System.out.println("Amazing, you can pass to the next step of the process");
			System.out.println(" ########################################################### ");
		}
    }

}
