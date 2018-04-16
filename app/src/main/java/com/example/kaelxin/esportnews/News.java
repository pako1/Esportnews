package com.example.kaelxin.esportnews;

public class News {

    private String mThumbnail;
    private String headLine;
    private String mPublicationDate;
    private String author;
    private String mUrl;
    private String section;

     News(String mThumbnail, String headLine, String mPublicationDate, String author, String mUrl, String section) {

        this.mThumbnail = mThumbnail;
        this.headLine = headLine;
        this.mPublicationDate = mPublicationDate;
        this.author = author;
        this.mUrl = mUrl;
        this.section = section;

    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getmPublicationDate() {
        return mPublicationDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getSection() {
        return section;
    }

}
