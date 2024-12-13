package vttp.batch5.ssf.noticeboard.models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vttp.batch5.ssf.noticeboard.util.Util;

public class Notice {

    @NotNull(message = "Mandatory field.")
    @Size(min = 3, max = 128, message = "The notice title must contain between 3 to 128 characters.")
    private String title;

    @NotBlank(message = "Mandatory field.")
    @Email(message = "Must be a valid email.")
    private String poster;

    @NotNull(message = "Mandatory field.")
    @Future(message = "Publishing date must be in the future.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postDate;

    @NotBlank(message = "Mandatory field.")
    private String text;

    @Size(min = 1, message = "Mandatory field. Must include at least 1 category.")
    private List<String> categories;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        Long longDate = postDate.getTime();
        return title + Util.delimiter 
                + poster + Util.delimiter 
                + longDate + Util.delimiter 
                + text + Util.delimiter 
                + categories;
    }
    
}
