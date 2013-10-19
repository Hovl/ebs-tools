package ebs.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dubov Aleksey
 * Date: Feb 26, 2010
 * Time: 5:02:27 PM
 * Company: EBS (c) 2009
 */

public class ListTools<E, T> {
	public interface Transformer<E, T> {
		public T transform(E src);
	}

	public interface Filter<E> {
		public boolean filter(E src);
	}

	private List<E> list;
	private List<T> transformedList;
	private List<E> filteredList;

	public ListTools(List<E> list) {
		this.list = list;
	}

	public List<E> getList() {
		return list;
	}

	public List<T> getTransformedList() {
		return transformedList;
	}

	public List<E> getFilteredList() {
		return filteredList;
	}

	public ListTools<E, T> transform(Transformer<E, T> t) {
		transformedList = new ArrayList<T>(list.size());

		for(E src : list) {
			this.transformedList.add(t.transform(src));
		}

		return this;
	}

	public static <E, T> List<T> transform(List<E> list, Transformer<E, T> t) {
		return new ListTools<E, T>(list).transform(t).transformedList;
	}

	public ListTools<E, T> filter(Filter<E> f) {
		filteredList = new ArrayList<E>(filteredList);

		for(E src : list) {
			if(f.filter(src)) {
				filteredList.add(src);
			}
		}

		return this;
	}

	public static <E> List<E> filter(List<E> list, Filter<E> f) {
		return new ListTools<E, E>(list).filter(f).filteredList;
	}
}
