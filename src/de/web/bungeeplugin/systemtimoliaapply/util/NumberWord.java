package de.web.bungeeplugin.systemtimoliaapply.util;

public enum NumberWord {
	
	EINS(1, "eins", "ein"),
	ZWEI(2, "zwei", null),
	DREI(3, "drei", null),
	VIER(4, "vier", null),
	FUENF(5, "fünf", null),
	SECHS(6, "sechs", null),
	SIEBEN(7, "sieben", null),
	ACHT(8, "acht", null),
	NEUN(9, "neun", null),
	ZEHN(10, "zehn", null),
	ELF(11, "elf", null),
	ZWOELF(12, "zwölf", null);
	
	private int integer;
	private String number;
	private String singular;
	
	private NumberWord(int integer, String number, String singular) {
		this.integer = integer;
		this.number = number;
		this.singular = singular;
	}
	
	public int getInteger() {
		return integer;
	}
	
	public String getWord() {
		return number;
	}
	
	public String getSingular() {
		return singular;
	}
	
	public boolean isSingular() {
		return (singular != null);
	}
	
	public static NumberWord getByInteger(int integer) {
		for (NumberWord numberword : values()) {
			if (numberword.integer == integer) {
				return numberword;
			}
		}
		return null;
	}
	
}
