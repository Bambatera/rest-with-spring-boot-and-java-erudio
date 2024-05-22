package br.com.erudio.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.springframework.hateoas.RepresentationModel;

@JsonPropertyOrder({"id", "author", "launchDate", "price", "title"})
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Integer key;
    
    private String author;
    private Date launchDate;
    private Double price;
    private String title;

    public BookVO() {
    }

    public BookVO(Date launchDate, Double price) {
        this.launchDate = launchDate;
        this.price = price;
    }

    public BookVO(Integer key, String author, Date launchDate, Double price, String title) {
        this.key = key;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
        this.title = title;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.key);
        hash = 37 * hash + Objects.hashCode(this.author);
        hash = 37 * hash + Objects.hashCode(this.launchDate);
        hash = 37 * hash + Objects.hashCode(this.price);
        hash = 37 * hash + Objects.hashCode(this.title);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BookVO other = (BookVO) obj;
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        if (!Objects.equals(this.launchDate, other.launchDate)) {
            return false;
        }
        return Objects.equals(this.price, other.price);
    }

}
