package demoapp.web.rest.vm;

public class PageResquests {

    private String title;
    private int page;
    private int size;
    private String field;
    private boolean predicate;

    public PageResquests() {
    }

    public PageResquests(int page, int size, String field, boolean predicate) {
        this.page = page;
        this.size = size;
        this.field = field;
        this.predicate = predicate;
    }

    public PageResquests(String title, int page, int size, String field, boolean predicate) {
        this.title = title;
        this.page = page;
        this.size = size;
        this.field = field;
        this.predicate = predicate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isPredicate() {
        return predicate;
    }

    public void setPredicate(boolean predicate) {
        this.predicate = predicate;
    }
}
