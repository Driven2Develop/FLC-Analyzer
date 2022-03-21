package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * POJO class for Readability score and associated education level
 *
 * @author Iymen Abdella
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Readability {

    private long score;
    private String education_level;

    /**
     *
     * @param s for the score, required for constuctor, default is 999
     */
    public Readability(long s){
        this.score = s;
        this.education_level = geteducation_level(s);
    }

    public long getScore() {return score;}
    public void setScore(long score) {
        this.score = score;
    }

    /**
     * @author Iymen Abdella
     * default return is "Early"
     * static method to help calculate average education level
     * @return education level based on flesch index
     */
    public String geteducation_level() {return this.education_level;}

    public static String geteducation_level(long score) {

        if (score <= 0)
            return "Law School Graduate";
        else if (score > 0 && score < 31)
            return "College Graduate";
        else if (score > 30 && score < 51)
            return "College";
        else if (score > 50 && score < 61)
            return "High School";
        else if (score > 60 && score < 66)
            return "9th Grade";
        else if (score > 65 && score < 71)
            return "8th Grade";
        else if (score > 70 && score < 81)
            return "7th Grade";
        else if (score > 80 && score < 91)
            return "6th Grade";
        else if (score > 90 && score < 100)
            return "5th Grade";
        else
            return "Early";
    }

    public void seteducation_level(String education_level) {
        this.education_level = education_level;
    }
}
