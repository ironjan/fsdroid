package de.upb.fsmi.news.persistence;

import java.util.Date;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Content;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Description;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Guid;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class NoteItem extends Item{

	private String author, content, description, guid, link, title;
	private Date date;
	
	
	public NoteItem(Item item) {
		super();
		this.author			= item.getAuthor();
		this.content		= item.getContent().toString();
		this.description	= item.getDescription().toString();
		this.guid			= item.getGuid().toString();
		this.link			= item.getLink();
		this.title			= item.getTitle();
		this.date			= item.getPubDate();
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return super.getAuthor();
	}

	@Override
	public Content getContent() {
		// TODO Auto-generated method stub
		return super.getContent();
	}

	@Override
	public Description getDescription() {
		// TODO Auto-generated method stub
		return super.getDescription();
	}

	@Override
	public Guid getGuid() {
		// TODO Auto-generated method stub
		return super.getGuid();
	}

	@Override
	public String getLink() {
		// TODO Auto-generated method stub
		return super.getLink();
	}

	@Override
	public Date getPubDate() {
		// TODO Auto-generated method stub
		return super.getPubDate();
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return super.getTitle();
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return super.getUri();
	}

}
