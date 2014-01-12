package de.upb.fsmi.cards.entities;

public class Library {
	private final String license;
	private final String name;
	private final String description;
	private String link;

	public Library(String name, String link, String license, String description) {
		this.link = link;
		this.license = license;
		this.name = name;
		this.description = description;
	}

	public String getLicense() {
		return license;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String pLink) {
		link = pLink;
	}

}
