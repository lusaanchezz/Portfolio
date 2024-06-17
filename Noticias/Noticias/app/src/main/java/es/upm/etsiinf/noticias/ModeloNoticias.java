package es.upm.etsiinf.noticias;

import java.util.ArrayList;

public class ModeloNoticias {
    private int totalResults;
    private String status;
    private ArrayList<Articulos> articles;

    public ModeloNoticias(int totalResults, String status, ArrayList<Articulos> articles) {
        this.totalResults = totalResults;
        this.status = status;
        this.articles = articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Articulos> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Articulos> articles) {
        this.articles = articles;
    }
}
