package com.example.kaelxin.esportnews;

class News {

    private final String mThumbnail;
    private final String headLine;
    private final String mPublicationDate;
    private final String author;
    private final String mUrl;
    private final String section;

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
