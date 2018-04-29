package LanguarTarjanDominatorsAlgorithm;

public class Pair<S, T> {
	  
	  private final S first;

	  
	  private final T second;

	  
	  public Pair(final S first, final T second) {
	    this.first = first;
	    this.second = second;
	  }

	  public static <S, T> Pair<S, T> make(final S first, final T second) {
	    return new Pair<S, T>(first, second);
	  }

	  @Override
	  public boolean equals(final Object obj) {
	    if (!(obj instanceof Pair)) {
	      return false;
	    }

	    final Pair<?, ?> p = (Pair<?, ?>) obj;

	    return (((p.first != null) && p.first.equals(first)) || ((p.first == null) && (first == null)))
	        && (((p.second != null) && p.second.equals(second)) || ((p.second == null) && (second == null)));
	  }

	  public S first() {
	    return first;
	  }

	  @Override
	  public int hashCode() {
	    return (first == null ? 1 : first.hashCode()) * (second == null ? 1 : second.hashCode());
	  }

	  public T second() {
	    return second;
	  }

	  @Override
	  public String toString() {
	    return "< " + first + ", " + second + ">";
	  }
	}
