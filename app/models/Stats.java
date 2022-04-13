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

    private final String word;
    private final int occurrence;

    public Stats(String word, int occurrence) {
        this.word = word;
        this.occurrence = occurrence;
    }

    public String getWord() {
        return word;
    }

    public int getOccurrence() {
        return occurrence;
    }

}
