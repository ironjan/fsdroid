package de.upb.fsmi.fsdroid.sync.entities;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

import java.text.*;
import java.util.*;

import de.upb.fsmi.fsdroid.sync.*;

@DatabaseTable(tableName = NewsItemContract.NEWS_ITEMS_TABLE)
public class NewsItem implements Comparable<NewsItem> {


    @DatabaseField(columnName = NewsItemContract.NewsItemColumns.COLUMN_ID, generatedId = true)
    private long _id;

    public long get_id() {
        return _id;
    }

    public void set_id(long p_id) {
        _id = p_id;
    }

    @DatabaseField(columnName = NewsItemContract.NewsItemColumns.COLUMN_AUTHOR)
    private String author;

    @DatabaseField(columnName = NewsItemContract.NewsItemColumns.COLUMN_CONTENT)
    private String content;

    @DatabaseField(columnName = NewsItemContract.NewsItemColumns.COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = NewsItemContract.NewsItemColumns.COLUMN_LINK)
    private String link;

    @DatabaseField(columnName = NewsItemContract.NewsItemColumns.COLUMN_TITLE)
    private String title;

    @DatabaseField(columnName = NewsItemContract.NewsItemColumns.COLUMN_DATE, dataType = DataType.DATE_STRING, format = "yyyy-MM-dd'T'HH:mm:ss.SSS")
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

            return descending;
        }

        return title.compareTo(pAnother.title);
    }

}
