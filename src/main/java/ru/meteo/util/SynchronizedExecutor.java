package ru.meteo.util;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import com.google.common.collect.Sets;


public class SynchronizedExecutor<L, R> {

	private final Set<L> objects = Sets.newHashSet();
	
	private final Lock lock = new ReentrantLock();
	
	protected R executeOnce(L lockObject, Function<Void, ? extends R> success, Function<Void, ? extends R> decline) {
		try {
			if (!acqure(lockObject)) {
				return Optional.ofNullable(success).map((s) -> {return s.apply(null);}).orElse(null);
			} else {
				return Optional.ofNullable(decline).map((s) -> {return s.apply(null);}).orElse(null);
			}
		} finally {
			release(lockObject);
		}
	}
	
	private final boolean acqure(L lockObject) {
		lock.lock();
		try {
			if (objects.contains(lockObject)) {
				return true;
			} else {
				objects.add(lockObject);
				return false;
			}
		} finally {
			lock.unlock();
		}
	}
	
	private final void release(L lockObject) {
		lock.lock();
		try {
			objects.remove(lockObject);
		} finally {
			lock.unlock();
		}
	}
	
}
