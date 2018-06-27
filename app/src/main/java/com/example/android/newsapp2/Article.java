package com.example.android.newsapp2;

/**
 * Created by egi-megi on 07.06.18.
 */

public class Article {

    private String mTitle;

    private String mSectionName;

    private String mPublicationDate;

    private String mWebUrl;

    private String mAuthors;

    public Article(String title, String sectionName, String publicationDate, String webUrl, String authors) {
        mTitle = title;
        mSectionName = sectionName;
        mPublicationDate = publicationDate;
        mWebUrl = webUrl;
        mAuthors = authors;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getAuthors() {
        return mAuthors;
    }
}

