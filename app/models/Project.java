package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.util.List;

import static helpers.DateUtil.parseDate;

/**
 * POJO class for Project object
 *
 * @author Mengqi Liu
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Project {

    private long ownerId;
    private LocalDate timeSubmitted;
    private String title;
    private String type;
    private List<Job> jobs;
    private String previewDescription;

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDate getTimeSubmitted() {
        return timeSubmitted;
    }

    public void setTimeSubmitted(long timeSubmitted) {
        this.timeSubmitted = parseDate(timeSubmitted);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public String getPreviewDescription() {
        return previewDescription;
    }

    public void setPreviewDescription(String previewDescription) {
        this.previewDescription = previewDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        return new EqualsBuilder().append(ownerId, project.ownerId).append(timeSubmitted, project.timeSubmitted).append(title, project.title).append(type, project.type).append(jobs, project.jobs).append(previewDescription, project.previewDescription).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(ownerId).append(timeSubmitted).append(title).append(type).append(jobs).append(previewDescription).toHashCode();
    }

    @Override
    public String toString() {
        return "Project{" +
                "ownerId=" + ownerId +
                ", timeSubmitted=" + timeSubmitted +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", jobs=" + jobs +
                ", previewDescription='" + previewDescription + '\'' +
                '}';
    }
}
