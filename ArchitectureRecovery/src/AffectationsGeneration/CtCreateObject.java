package AffectationsGeneration;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import spoon.reflect.code.CtComment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.ParentNotInitializedException;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtVisitor;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.ReferenceFilter;
import spoon.reflect.visitor.chain.CtFunction;
import spoon.reflect.visitor.chain.CtQuery;

public class CtCreateObject implements CtElement {
	public CtElement element;
	
	public CtCreateObject(CtElement element) {
		this.element = element;
	}

	@Override
	public Factory getFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFactory(Factory arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accept(CtVisitor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <E extends CtElement> E addAnnotation(CtAnnotation<? extends Annotation> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <E extends CtElement> List<E> getAnnotatedChildren(Class<? extends Annotation> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A extends Annotation> CtAnnotation<A> getAnnotation(CtTypeReference<A> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CtAnnotation<? extends Annotation>> getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocComment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> List<E> getElements(Filter<E> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMetadata(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getMetadataKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CtElement getParent() throws ParentNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <P extends CtElement> P getParent(Class<P> arg0) throws ParentNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E getParent(Filter<E> arg0) throws ParentNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SourcePosition getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CtTypeReference<?>> getReferencedTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends CtReference> List<T> getReferences(ReferenceFilter<T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasParent(CtElement arg0) throws ParentNotInitializedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isImplicit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isParentInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <E extends CtElement> E putMetadata(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAnnotation(CtAnnotation<? extends Annotation> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void replace(CtElement arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <E extends CtElement> E setAnnotations(List<CtAnnotation<? extends Annotation>> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E setDocComment(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E setImplicit(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E setParent(E arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E setPosition(SourcePosition arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E setPositions(SourcePosition arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAllParentsBelow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends CtElement> CtQuery<T> filterChildren(Filter<T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <I, R> CtQuery<R> map(CtFunction<I, R> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E addComment(CtComment arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CtElement clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CtComment> getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends CtReference> List<T> getReferences(Filter<T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShortRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E removeComment(CtComment arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends CtElement> E setComments(List<CtComment> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	

}