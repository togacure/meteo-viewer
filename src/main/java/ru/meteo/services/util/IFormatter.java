package ru.meteo.services.util;

public interface IFormatter<T> {

	String formatAsText(T input);
	
}
