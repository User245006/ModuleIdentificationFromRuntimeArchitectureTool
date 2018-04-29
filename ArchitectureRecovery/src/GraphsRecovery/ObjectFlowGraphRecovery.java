package GraphsRecovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ObjectFlowGraphRecovery {
	
	
	
	
	public static List<String> affectationsLeftParts = new ArrayList<>();
	public static List<String> affectationsRightParts = new ArrayList<>();
	public static List<String> nonRepeatedAffectations = new ArrayList<>();
	public static List<String> nonRepeatedUncertainAffectations = new ArrayList<>();
	public static List<String> uncertainAffectationsLeftParts = new ArrayList<>();
	public static List<String> uncertainAffectationsRightParts = new ArrayList<>();
	
	
	public static void draw(List<String> affectations)
	{
		
		for(String affectation : affectations)
		  {  if(!nonRepeatedAffectations.contains(affectation))
		      {
			  nonRepeatedAffectations.add(affectation);
		      }  
		  }
		 
		  
		  for(String a : nonRepeatedUncertainAffectations)
		  {
			  String[] affectationParts = a.split(" = ");
			  uncertainAffectationsLeftParts.add(affectationParts[0]);
			  uncertainAffectationsRightParts.add(affectationParts[1]);
		  }
		  
		  for(String a : nonRepeatedAffectations)
		  {
			  String[] affectationParts = a.split(" = ");
			  affectationsLeftParts.add(affectationParts[0]);
			  affectationsRightParts.add(affectationParts[1]);
		  }
		  
		  PrintWriter out;
	      File fich = new File("/home/soumia/Bureau/OutPut/OFG.dot");
	      try {
	          out = new PrintWriter(new FileWriter(fich));
	          String bgcolor = "bgcolor=\"antiquewhite3\"";
	          out.write("digraph OFG {"+bgcolor+"");
	          out.println();
	          
	          
	          for(int s=0; s<affectationsRightParts.size(); s++)
	          {  
	        	  out.write("\""+affectationsRightParts.get(s)+ "\"->\""+ affectationsLeftParts.get(s)+"\"");
	              out.println();
	          }

	          out.write("}");
	          out.close();
	      }  catch (IOException ex) {
	          System.out.println("Erreur : "+ex.getMessage());
	  }

	      try {
	          Runtime rt = Runtime.getRuntime();
	          //Process pr = rt.exec("cmd /c dir");
	          Process pr = rt.exec("dot -Tpng -O /home/soumia/Bureau/OutPut/OFG.dot");
	          BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	          String line=null;
	          while((line=input.readLine()) != null) {
	              System.out.println(line);
	          }
	          
	          int exitVal = pr.waitFor();
	          System.out.println("Exited with error code "+exitVal);
	          
	          
	          
	          Process pr02 = rt.exec("xdg-open /home/soumia/Bureau/OutPut/OFG.dot.png");
	          BufferedReader input02 = new BufferedReader(new InputStreamReader(pr02.getInputStream()));
	          String line02=null;
	          while((line02=input02.readLine()) != null) {
	              System.out.println(line02);
	          }
	          int exitVal02 = pr02.waitFor();
	          System.out.println("Exited with error code "+exitVal02);
	      } catch(Exception e) {
	          System.out.println(e.toString());
	          e.printStackTrace();
	      }
		     
		}
}
