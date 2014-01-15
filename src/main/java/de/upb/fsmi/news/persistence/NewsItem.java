package de.upb.fsmi.news.persistence;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.*;
import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

import java.util.*;

@DatabaseTable(tableName = "newsItems")
public class NewsItem implements Comparable<NewsItem> {

	public static final String COLUMN_LINK = "link";

	public static final String COLUMN_ID = "_id";

	public static final String COLUMN_DATE = "date";

	@DatabaseField(columnName = COLUMN_ID, generatedId = true)
	private long _id;

	public long get_id() {
		return _id;
	}

	public void set_id(long p_id) {
		_id = p_id;
	}

	@DatabaseField(columnName = "author")
	private String author;

	@DatabaseField(columnName = "content")
	private String content;

	@DatabaseField(columnName = "description")
	private String description;

	@DatabaseField(columnName = COLUMN_LINK)
	private String link;

	@DatabaseField(columnName = "title")
	private String title;

	@DatabaseField(columnName = COLUMN_DATE)
	private Date date;

	public NewsItem() {
		super();
	}

    public NewsItem(String author, String content, String description, String link, String title, Date date) {
        this.author = author;
        this.content = content;
        this.description = description;
        this.link = link;
        this.title = title;
        this.date = date;
    }

    public NewsItem(Item item) {
        super();
		this.author = item.getAuthor();
		this.content = item.getContent().getValue();
		this.description = item.getDescription().getValue();
		this.link = item.getLink();
		this.title = item.getTitle();
		this.date = item.getPubDate();
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String pAuthor) {
		author = pAuthor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String pContent) {
		content = pContent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String pDescription) {
		description = pDescription;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String pLink) {
		link = pLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String pTitle) {
		title = pTitle;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date pDate) {
		date = pDate;
	}

	@Override
	public int compareTo(NewsItem pAnother) {
        if (date != null && pAnother.date != null) {
            int ascending = date.compareTo(pAnother.date);
		int descending = -ascending;

            return descending ;
        }

        return title.compareTo(pAnother.title);
    }

}
