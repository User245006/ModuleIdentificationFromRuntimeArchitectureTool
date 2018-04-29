package WTFIDFScheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

public class CohesionAndCoupling {
	
	public static List<ClasseRelationShips> allRefinedRelationShips = new ArrayList<>();
	public static HashMap<String, Float> packageCohesion = new HashMap<>();
	public static HashMap<String, Float> packageCoupling = new HashMap<>();
	
	
	public void SCSValueCalculation(List<ClasseRelationShips> allRelationShips)
	{
		for(ClasseRelationShips r : allRelationShips)
		{
			CtType type = r.source;
			//bast
			float CiCj = relationShipsValues(r.relationShips);
			
			//makam
			float CkCj = 0;
			int ncj = 0;
			for(ClasseRelationShips l : allRelationShips)
			{
				if(l.target.getQualifiedName().equals(r.target.getQualifiedName()))
				{
					CkCj = CkCj + relationShipsValues(l.relationShips);
					ncj++;
				}
			}
			
			float CjCi = 0;
			
			for(ClasseRelationShips ll : allRelationShips)
			{
				if(ll.target.getQualifiedName().equals(type.getQualifiedName()) &&
						ll.source.getQualifiedName().equals(r.target.getQualifiedName()))
				{
					CjCi = CjCi + relationShipsValues(ll.relationShips);
				}
			}
			
			float CkCi = 0;
			int nci = 0;
			for(ClasseRelationShips lll : allRelationShips)
			{
				if(lll.target.getQualifiedName().equals(type.getQualifiedName()))
						
				{
					CkCi = CkCi + relationShipsValues(lll.relationShips);
					nci++;
				}
			}
			
			float rightPart = (float)((float)(CiCj/CkCj)*Math.log10((float)(ApplicationClasses.appClassesReferences.size())/(ncj)));
			
			float leftPart = 0;
			if(CjCi !=0)
			{
			  leftPart = (float)((float)(CjCi/CkCi)*Math.log10((float)(ApplicationClasses.appClassesReferences.size())/(nci)));
			}
			else
			{
				leftPart = 0;
			}
			
			r.couplingValue = rightPart+leftPart;
			
			allRefinedRelationShips.add(r);
		}
	}
	
	public void cohesionCalculation(HashMap<String, List<String>> packagesTypes,
			List<ClasseRelationShips> allRelationShips)
	{ packageCohesion.clear();
		for(String PackageName : packagesTypes.keySet())
		{   float cohesion = 0;
			for(String type : packagesTypes.get(PackageName))
			{
				for(ClasseRelationShips crs : allRelationShips)
				{
					if(crs.source.getQualifiedName().equals(type))
					{
						if(packagesTypes.get(PackageName).contains(crs.target.getQualifiedName()))
						{
							cohesion = cohesion + crs.couplingValue;
						}
					}
				}
				
			}
			
			packageCohesion.put(PackageName, cohesion);
		}
	}
	
	public void couplingCalculation(HashMap<String, List<String>> packagesTypes,
			List<ClasseRelationShips> allRelationShips)
	{ packageCoupling.clear();
		for(String PackageName : packagesTypes.keySet())
		{   float coupling = 0;
			for(String type : packagesTypes.get(PackageName))
			{
				for(ClasseRelationShips crs : allRelationShips)
				{
					if(crs.source.getQualifiedName().equals(type))
					{
						if(!packagesTypes.get(PackageName).contains(crs.target.getQualifiedName()))
						{
							coupling = coupling + crs.couplingValue;
						}
					}
				}
			}
			
			packageCoupling.put(PackageName, coupling);
		}
	}
	
	public float relationShipsValues(List<HashMap<String, Integer>> relationShips)
	{  float SCSValue = 0;
		for(HashMap<String, Integer> relations : relationShips)
		{
			for(String element : relations.keySet())
			{
				if(element.equals("EX"))
				{
					SCSValue = SCSValue + (float)(8.5 * relations.get(element));
				}
				
				if(element.equals("HP"))
				{
					SCSValue = SCSValue + (float)(3.5 * relations.get(element));
				}
				
				if(element.equals("RE"))
				{
					SCSValue = SCSValue + (float)(3 * relations.get(element));
				}
				
				if(element.equals("CA"))
				{
					SCSValue = SCSValue + (float)(2.5 * relations.get(element));
				}
				if(element.equals("IM"))
				{
					SCSValue = SCSValue + (float)(2 * relations.get(element));
				}
				if(element.equals("IT"))
				{
					SCSValue = SCSValue + (float)(2 * relations.get(element));
				}
				if(element.equals("RT"))
				{
					SCSValue = SCSValue + (float)(2 * relations.get(element));
				}
				if(element.equals("TH"))
				{
					SCSValue = SCSValue + (float)(1 * relations.get(element));
				}
			}
		}
		return SCSValue;
	}

}
