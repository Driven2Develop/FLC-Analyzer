package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * POJO class for stats
 *
 * @author Bowen Cheng
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Stats {

    private String word;
    private int occurrence;

    public Stats() {
    }

    public Stats(String word, int occurrence) {
        this.word = word;
        this.occurrence = occurrence;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

}
