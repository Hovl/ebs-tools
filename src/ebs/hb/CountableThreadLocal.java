package ebs.hb;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Aleksey Dubov.
 * Date: 2011-04-22
 * Time: 18:13
 * Copyright (c) 2011
 */
public class CountableThreadLocal<T> extends AtomicReference<T> {
	private int counter = 0;

	public void setValue(T value) {
		counter = 0;
		super.set(value);
	}

	public T use() {
		counter++;
		return super.get();
	}

	public T release() {
		if(counter > 0) {
			counter--;
		}

		if(counter == 0) {
			return super.get();
		}
		return null;
	}
}
