package AffectationsGeneration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtExecutableReference;

public class CollectionTypesMethods {
	
	public static List<CtExecutableReference> collectionMethods = new ArrayList<>(); 
	public static List<String> collectionTypesNames = new ArrayList<>();
	
	public static void collectCollectionTypes()
	{
	
			CtType collection = new TypeFactory().get(Collection.class);
			Collection<CtExecutableReference<?>> methods = collection.getDeclaredExecutables();
			// ALL INHERITED CLASSES AND INTERFACES
			
			CtType set = new TypeFactory().get(Set.class);
			collectionTypesNames.add(set.getQualifiedName());
			
			
			CtType list = new TypeFactory().get(List.class);
			collectionTypesNames.add(list.getQualifiedName());
			Collection<CtExecutableReference<?>> listmethods = list.getDeclaredExecutables();
			
			CtType queue = new TypeFactory().get(Queue.class);
			collectionTypesNames.add(queue.getQualifiedName());
			
			CtType hashSet = new TypeFactory().get(HashSet.class);
			collectionTypesNames.add(hashSet.getQualifiedName());
			
			
			CtType linkedhashSet = new TypeFactory().get(LinkedHashSet.class);
			collectionTypesNames.add(linkedhashSet.getQualifiedName());
			
			
			
			CtType treeSet = new TypeFactory().get(TreeSet.class);
			collectionTypesNames.add(treeSet.getQualifiedName());
			
			CtType arrayList = new TypeFactory().get(ArrayList.class);
			collectionTypesNames.add(arrayList.getQualifiedName());
			
			
			CtType Vector = new TypeFactory().get(Vector.class);
			collectionTypesNames.add(Vector.getQualifiedName());
			Collection<CtExecutableReference<?>> vectormethods = Vector.getDeclaredExecutables();
			
			CtType linkedList = new TypeFactory().get(LinkedList.class);
			collectionTypesNames.add(linkedList.getQualifiedName());
			
			
			for(CtExecutableReference m : methods)
			{
				if(m.getSimpleName().equals("add") || (m.getSimpleName().equals("addAll")))
				{
					collectionMethods.add(m);
				}  
			}
			
			for(CtExecutableReference m : vectormethods)
			{
				if(m.getSimpleName().equals("add") || (m.getSimpleName().equals("addAll")))
				{
					collectionMethods.add(m);
				}  
			}
			
			for(CtExecutableReference m : listmethods)
			{
				if(m.getSimpleName().equals("add") || (m.getSimpleName().equals("addAll")))
				{
					collectionMethods.add(m);
				}  
			}
			
			
			CtType map = new TypeFactory().get(Map.class);
			Collection<CtExecutableReference<?>> mapmethods = map.getDeclaredExecutables();
			
			CtType hashMap = new TypeFactory().get(HashMap.class);
			collectionTypesNames.add(hashMap.getQualifiedName());
			CtType linkedHashMap = new TypeFactory().get(LinkedHashMap.class);
			collectionTypesNames.add(linkedHashMap.getQualifiedName());
			CtType treeMap = new TypeFactory().get(TreeMap.class);
			collectionTypesNames.add(treeMap.getQualifiedName());
			
			for(CtExecutableReference hm : mapmethods)
			{  
				if(hm.getSimpleName().equals("put") || (hm.getSimpleName().equals("putAll")))
				{
					collectionMethods.add(hm);
				}  
			}
			
	}

}
