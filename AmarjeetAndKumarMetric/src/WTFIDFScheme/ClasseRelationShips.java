package WTFIDFScheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spoon.reflect.declaration.CtType;

public class ClasseRelationShips {
	
	public CtType source;
	
	public CtType target;
	
	public List<HashMap<String, Integer>> relationShips = new ArrayList<>();
	
	public float couplingValue ;

}
