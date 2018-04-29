package GraphsRecovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ExecutionTracesAnalysis {
	public static int numberOfTraces;
	public static List<String> traceLinesAsArray = new ArrayList<>();
	public static List<String> concatenatedLines = new ArrayList<>();
	public static List<String> finalTraceForms = new ArrayList<>();
	
	public  HashMap<String, Float> startTimeStamps ;
	public  HashMap<String, Float> endTimeStamps;
	
	public static List< HashMap <String, Float> > allTracesStartTS = new ArrayList<>();
	public static List< HashMap <String, Float> > allTracesEndTS = new ArrayList<>();
	
	public static List<Long> systemExecTimeLength = new ArrayList<>();
	public static List<Long> systemStartTime = new ArrayList<>();
	public static List<Long> systemEndTime = new ArrayList<>();
	public static int traceIndex = 0;
	
	public  HashMap<String, String> finalResultsStartAVG = new HashMap<>();
	public  HashMap<String, String> finalResultsEndAVG = new HashMap<>();
	public  HashMap<String, String> finalResultsLifeSpanAVG = new HashMap<>();
	public  HashMap<String, String> finalResultsProbability = new HashMap<>();
	
	public  HashMap<String, String> finalLabels = new HashMap<>();
	public  HashMap<String, Float> finalLenght = new HashMap<>();
	public  HashMap<String, String> classesNames = new HashMap<>();
	
	public  void listFilesForFolder(final File folder) throws IOException {
	    for (final File fileEntry : folder.listFiles()) 
	    { numberOfTraces++;
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	analyseTrace(fileEntry);
	        	clearTables();
	        }
	    }
	}
	
	public static void clearTables()
	{   System.out.println("Clearing tables ...");
		concatenatedLines.clear();
		traceLinesAsArray.clear();
		finalTraceForms.clear();
		//startTimeStamps.clear();
		//endTimeStamps.clear();
		
	}
	
	public  void analyseTrace(File file) throws IOException
	{   System.out.println("In Trace: "+traceIndex);
		//traceLinesAsArray.clear();
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line;
		while ((line = br.readLine()) != null)
		{
			traceLinesAsArray.add(line.toString());
		}
		
		//The first thing to do is to concatenate each two succesif lines different from the destruction lines
		int i = 0;
		int j = 0;
		String newLine ="";
		  while(i < traceLinesAsArray.size())
		   {
				if(!traceLinesAsArray.get(i).contains("fin:"))
				{   if(j<traceLinesAsArray.size()-1)
					{
						j = i+1;
						newLine = traceLinesAsArray.get(i) + ", "+traceLinesAsArray.get(j);
						i = i+2;
					}
					
				}
				else
				{
					newLine = traceLinesAsArray.get(i);
					i = i+1;
				}
				if(!concatenatedLines.contains(newLine))
				{
					concatenatedLines.add(newLine);
				}
		
		    }
		  
		  //after concatenation, second time stamp is removed
		  for(String conL : concatenatedLines)
		  {  if(!conL.contains("fin:"))
		     {System.out.println(conL);
			  String[] splConL = conL.split(",");
			  String newLR = splConL[0]+", "+splConL[1]+", "+splConL[2]+", "+splConL[4];
			  concatenatedLines.set(concatenatedLines.indexOf(conL), newLR);
		     }
		    
		  }
		 
		//Now, we get the trace Length
		 finalTraceForms = concatenatedLines;
		 systemExecTimeLength.add(getAppLength(traceLinesAsArray));  
		 
		//Now, we get the start timestamps, we need a hashmap 
		generateStartTimeStamps(concatenatedLines);
		//generateEndTimeStamps(finalTraceForms);
		 
		 traceIndex++;
	}
	
	public  void generateStartTimeStamps(List<String> traceLines)
	{   
		List<Boolean> alreadyToken = new ArrayList<>(Arrays.asList(new Boolean[finalTraceForms.size()]));
		Collections.fill(alreadyToken, Boolean.FALSE);
	    
		startTimeStamps = new HashMap<>();
		endTimeStamps = new HashMap<>();
	    int i = 0;
		for(String line : traceLines)
		{ 
			if(alreadyToken.get(i).booleanValue()==false)
		    {  alreadyToken.set(i, true);
			   if(!line.contains("fin: "))
			   {
				 // get the position value
				 String splitLine[] = line.split(",");
				 String position = splitLine[2];
				 String onlyPos[] = position.split("=");
				 String positionName = onlyPos[1];
				 
				 String className = splitLine[1];
				 String[] classNames = className.split("=");
				 String name = classNames[1].trim().replaceAll("\\d","");
				 
				 // here we get indexes of lines that represents the same instantiation (have same position value)
				 List<Integer> indexes = getOccurencesIndexes(onlyPos[1], traceLines);
				 
				 float AVGStartLifeSpan = 0;
				 float AVGEndLifeSpan = 0;
					 for(Integer ind : indexes)
					 {   
						 alreadyToken.set(ind, true);
						 
						 //starttimestamps collections
						 String occurence = traceLines.get(ind);
						 String splitOccurence[] = occurence.split(",");
						 String timeStampVal = splitOccurence[0];
						 
						 String[] valueE = timeStampVal.split("=");
						 String tmp2 = valueE[1].trim();
						 Long OccSTime = Long.valueOf(tmp2);
						 
						 AVGStartLifeSpan = AVGStartLifeSpan + ((float)(OccSTime - systemStartTime.get(traceIndex))/systemExecTimeLength.get(traceIndex));
					    
					 
					     //end time stamps collection
						// get the hashcode value
						 String hashCodeEntry[] = splitOccurence[3].split("=");
					     String hashCodeS = hashCodeEntry[1].trim();
					     Long hashCode = Long.valueOf(hashCodeS);
					   
					     Long hashCodeIndex = getHashCodeIndex(hashCode, traceLines);
					     AVGEndLifeSpan = AVGEndLifeSpan + ((float)(hashCodeIndex - systemStartTime.get(traceIndex))/systemExecTimeLength.get(traceIndex));
					 }
				 
			     float finalStartLifeSpan = (AVGStartLifeSpan/indexes.size())*100;
			     float finalEndLifeSpan = (AVGEndLifeSpan/indexes.size())*100;
			     
				 List<Float> allTracesPositionsStartTime = new ArrayList<>();   
				 allTracesPositionsStartTime.add(finalStartLifeSpan);
			       
				 startTimeStamps.put(positionName, finalStartLifeSpan);
				 classesNames.put(positionName, name);
			     List<Float> allTracesPositionsEndTime = new ArrayList<>();
			     allTracesPositionsEndTime.add(finalEndLifeSpan);
			     
				 endTimeStamps.put(positionName, finalEndLifeSpan);
				  
			   }
		  }
		
		  i++;
		}
		 // concatenating all traces
		
		allTracesStartTS.add(traceIndex, startTimeStamps);
		allTracesEndTS.add(traceIndex,endTimeStamps);
	}
	
	public static List<Integer> getOccurencesIndexes(String pos, List<String> traces)
	{
		List<Integer> indexes = new ArrayList<>();
		for(String t:traces)
		{
			if(!t.contains("fin: "))
			   {
				 String splitLine[] = t.split(",");
				 String position = splitLine[2];
				 String onlyPos[] = position.split("=");
				 String positionName = onlyPos[1];
				 if(positionName.equals(pos))
				 {   
					 indexes.add(traces.indexOf(t));
				 }
			   }
		}
		
		return indexes;
	}
	
	public static Long getHashCodeIndex(Long givenHC, List<String> traceLines)
	{ //System.out.println("number of lines: "+traceLines.size());
	  int i=0;
		for(String line : traceLines)
	    {   
			if(line.contains("fin: "))
			 {
				String lineSplit[] = line.split(",");
				String hashCodeEntry = lineSplit[1];
				String hcev[] = hashCodeEntry.split(":");
			    String hashCodeS = hcev[1].trim();
			    //System.out.println("line: "+line);
			    //System.out.println("Line number: "+i);
			    Long hashCode = Long.valueOf(hashCodeS);
			    
			    if(givenHC.equals(hashCode))
			    {
			    	String ts = lineSplit[0];
			    	String uu[] = ts.split("=");
			    	String tsvv = uu[1].trim();
				    Long tsv = Long.valueOf(tsvv);
				    return tsv;
			    }
			    i++; 
			 }
	    }
		return systemEndTime.get(traceIndex);
	}
	
	public static Long getAppLength(List<String> concatenatedLines)
	{
		  String lastLine = concatenatedLines.get((concatenatedLines.size())-1);
		  String firstLine = concatenatedLines.get(0);
		  //Start time
		  String[] splitstartTime = firstLine.split(",");
		  String startTimeS = splitstartTime[0];
		  String[] value = startTimeS.split("=");
		  String tmp = value[1].trim();
		  Long startTime = Long.valueOf(tmp);
		  systemStartTime.add(startTime);
		  
		  // End time
		  // here we have to cases, when we finish with an object creation and when we finish with a destruction
		  
		  String[] splitendTime = lastLine.split(",");
		  Long endTime;
		  String endTimeS = splitendTime[0];
		  String[] valueE = endTimeS.split("=");
		  String tmp2 = valueE[1].trim();
		  endTime = Long.valueOf(tmp2);
		  systemEndTime.add(endTime);
		  Long a = endTime - startTime;
		  return a;
	}
	
	public  void calculateAVGStart(List< HashMap <String, Float> > start)
	{ // BEFORE invoking this method, all traces were analyzed and added to a hashmap
	  //for each trace
	  for(HashMap hmp : start)
	  {
		  Iterator iterator = hmp.keySet().iterator();
	      while (iterator.hasNext()) {
	    	  String lifeSpansInterval;
	    	  String position = (String) iterator.next();
	    	  List<Float> startTimeValues = getOccurencesInOtherTraces(position, start);
	    	  float sumS = 0;
	    	  for(Float v : startTimeValues)
	    	  {
	    		  sumS = sumS + v;
	    	  }
	    	  
	    	  float AVGSTARTTIMEALLTRACES = (float)(sumS/(startTimeValues.size()));
	    	  lifeSpansInterval = Float.toString(AVGSTARTTIMEALLTRACES);
	    	  finalResultsStartAVG.put(position, lifeSpansInterval);
	    	  float nn = (float)numberOfTraces;
	    	  float probability = (float)(startTimeValues.size()/nn);
	    	  finalResultsProbability.put(position, Float.toString(probability));
	        } 
	  }
	}
	
	public  void calculateAVGEnd(List< HashMap <String, Float> > end)
	{ // BEFORE invoking this method, all traces were analyzed and added to a hashmap
	  // for each trace
	  for(HashMap hmp : end)
	  {
		  Iterator iterator = hmp.keySet().iterator();
	      while (iterator.hasNext()) {
	    	  String position = (String) iterator.next();
	    	  List<Float> endTimeValues = getOccurencesInOtherTraces(position, end);
	    	  float sumS = 0;
	    	  for(Float v : endTimeValues)
	    	  {
	    		  sumS = sumS + v;
	    	  }
	    	  
	    	  float AVGSTARTTIMEALLTRACES = (float)(sumS/(endTimeValues.size()));
	    	  String v = Float.toString(AVGSTARTTIMEALLTRACES);
	    	  finalResultsEndAVG.put(position, v);
	        } 
	  }
	}
	
	public static List<Float> getOccurencesInOtherTraces(String pos, List< HashMap <String, Float> > li)
	{   List<Float> TimeInAllTraces = new ArrayList<>();
		for(HashMap h : li)
		{
			Iterator iterator = h.keySet().iterator();
		      while (iterator.hasNext()) 
		      {
		    	  String position = (String) iterator.next();
		    	  if(position.equals(pos))
		    	  {
		    		  TimeInAllTraces.add((Float) h.get(position));
		    	  }
		      }
		}
		return TimeInAllTraces;
	}
	
	public void calculateLifeSpanAVGProba(HashMap<String, String> finalResultsStartAVG, HashMap<String, String> finalResultsEndAVG, 
	HashMap<String, String> finalResultsProbability)
    {
		Iterator iterator = finalResultsStartAVG.keySet().iterator();
		while (iterator.hasNext()) 
	      {
	    	  String position = (String) iterator.next();
	    	  String StartTimeStamp = finalResultsStartAVG.get(position);
	    	  String EndTimeStamp = finalResultsEndAVG.get(position);
	    	  String probability = finalResultsProbability.get(position);
	    	  
	    	  String label = "["+probability+", "+StartTimeStamp+", "+EndTimeStamp+"]";
	    	  finalLabels.put(position, label);
	    	  
	    	  float sts = Float.parseFloat(StartTimeStamp);
	    	  float ets = Float.parseFloat(EndTimeStamp);
	    	  finalLenght.put(position, (float)(ets-sts));
	      }
    }
	public static HashMap<String, String> CalculateLables(File folder)
	{  ExecutionTracesAnalysis execTa = new ExecutionTracesAnalysis();
		try {
			
			//File folder = new File("./OutPuts/Traces");
			execTa.listFilesForFolder(folder);
			execTa.calculateAVGStart(allTracesStartTS);
			execTa.calculateAVGEnd(allTracesEndTS);
			execTa.calculateLifeSpanAVGProba(execTa.finalResultsStartAVG, execTa.finalResultsEndAVG, execTa.finalResultsProbability);
			
			Iterator iterator = execTa.finalLabels.keySet().iterator();
			for (String key : execTa.finalLabels.keySet()) 
		    {
		      System.out.println(key+" -> "+execTa.finalLabels.get(key)+" -> "+execTa.classesNames.get(key));
		    }
			
			System.out.println("Lenghts: ");
			Iterator ite = execTa.finalLenght.keySet().iterator();
			for (String key : execTa.finalLenght.keySet()) 
		    {
		      System.out.println(key+" -> "+execTa.finalLenght.get(key)+" -> "+execTa.classesNames.get(key));
		    }
			
			// Objects disparition
			int tenPercent = 0;
			int twentyPercent = 0;
			int thirtyPercent = 0;
			int fourtyPercent = 0;
			int fiftyPercent = 0;
			int sixtyPercent = 0;
			int seventyPercent = 0;
			int eightyPercent = 0;
			int nintyPercent = 0;
			int hundredPercent = 0;
			
			Iterator it = execTa.finalLenght.keySet().iterator();
			for (String key : execTa.finalLenght.keySet()) 
		    {
		      if(execTa.finalLenght.get(key)>0 && execTa.finalLenght.get(key)<=10)
		      {
		    	  tenPercent++;
		      }
		      
		      if(execTa.finalLenght.get(key)>10 && execTa.finalLenght.get(key)<=20)
		      {
		    	  twentyPercent++;
		      }
		      if(execTa.finalLenght.get(key)>20 && execTa.finalLenght.get(key)<=30)
		      {
		    	  thirtyPercent++;
		      }
		      
		      if(execTa.finalLenght.get(key)>30 && execTa.finalLenght.get(key)<=40)
		      {
		    	  fourtyPercent++;
		      }
		      if(execTa.finalLenght.get(key)>40 && execTa.finalLenght.get(key)<=50)
		      {
		    	  fiftyPercent++;
		      }
		      
		      if(execTa.finalLenght.get(key)>50 && execTa.finalLenght.get(key)<=60)
		      {
		    	  sixtyPercent++;
		      }
		      if(execTa.finalLenght.get(key)>60 && execTa.finalLenght.get(key)<=70)
		      {
		    	  seventyPercent++;
		      }
		      
		      if(execTa.finalLenght.get(key)>70 && execTa.finalLenght.get(key)<=80)
		      {
		    	  eightyPercent++;
		      }
		      if(execTa.finalLenght.get(key)>80 && execTa.finalLenght.get(key)<=90)
		      {
		    	  nintyPercent++;
		      }
		      
		      if(execTa.finalLenght.get(key)>90 && execTa.finalLenght.get(key)<=100)
		      {
		    	  hundredPercent++;
		      }
		    }
			
			System.out.println("Number of objects whose lifespan lenght is less than 10%: "+tenPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 20%: "+twentyPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 30%: "+thirtyPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 40%: "+fourtyPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 50%: "+fiftyPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 60%: "+sixtyPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 70%: "+seventyPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 80%: "+eightyPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 90%: "+nintyPercent);
			System.out.println("Number of objects whose lifespan lenght is less than 100%: "+hundredPercent);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return execTa.finalLabels;
	}
}
