package services;

import com.google.inject.Inject;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.util.stream.DoubleStream;
import models.Project;
import models.Readability;
import wsclient.MyWSClient;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;


/**
 * Service layer class for processing projects
 *
 * @author Mengqi Liu
 * @author Yvonne Lee
 */
public class ProjectService {

    private MyWSClient myWSClient;
    private static final String PROJECT_SEARCH_URL = "/projects/0.1/projects/all/?offset=0&limit=10&sort_field=time_submitted&job_details=true";
    private static final String PROJECT_LIST_URL = "/projects/0.1/projects?limit=10";

    private HashMap<Long, CompletionStage<List<Project>>> projectsByOwnerIdCache = new HashMap<>();
    private HashMap<String, CompletionStage<List<Project>>> searchProjectsCache = new HashMap<>();
    private HashMap<Long, CompletionStage<List<Project>>> projectsByJobIdCache = new HashMap<>();
    private HashMap<Long, CompletionStage<List<Project>>> averageReadabilityCache = new HashMap<>();

    /**
     * Constructor for DI
     *
     * @author Mengqi Liu
     */
    @Inject
    public ProjectService(MyWSClient myWSClient) {
        this.myWSClient = myWSClient;
    }

    /**
     * Search latest projects by search terms calling Freelancer API<br/>
     * Implemented using Stream API
     *
     * @param searchTerms the terms user inputs for searching related projects
     * @return Latest ten projects searched by the search terms calling Freelancer API
     * @author Mengqi Liu
     */
    public CompletionStage<List<Project>> searchLatestTenProjects(String searchTerms) {
        return this.myWSClient.initRequest(PROJECT_SEARCH_URL + "&query=" + searchTerms.trim())
                .getListResults(searchProjectsCache, searchTerms, Project.class, "projects");
    }

    /**
     * Search latest projects by jobId calling Freelancer API<br/>
     * Implemented using Stream API
     *
     * @param jobId id for one job defined in Freelancer API
     * @return Latest ten projects searched by the job id calling Freelancer API
     * @author Mengqi Liu
     */
    public CompletionStage<List<Project>> findProjectsByJobId(long jobId) {
        return this.myWSClient.initRequest(PROJECT_SEARCH_URL + "&jobs[]=" + jobId)
                .getListResults(projectsByJobIdCache, jobId, Project.class, "projects");
    }

    /**
     * Get readability from description
     * Implemented using Stream API
     *
     * @param previewDescription project description to compute average
     * @return List of readability objects
     * @author Iymen Abdella
     */
    public List<Readability> computeProjectReadability(String previewDescription) {

        //null case, empty case, blank case, no letters case
        if (previewDescription == null || previewDescription.isBlank() || previewDescription.chars().allMatch(Character::isLetter) ){

            return new ArrayList<Readability>(Arrays.asList(new Readability(999)));
        }

        //else calculate preview description readability
        String[] sentences = previewDescription.split("\\.|\\!|\\?|:|;"); // get all the sentences
        List <String> words = Stream.of(previewDescription.trim().split(" ")).map(w -> w.toLowerCase()).collect(Collectors.toList()); //get words
        words.removeAll(Arrays.asList(""," ", null, ".", "!","?",":", ";")); //remove blank words or false positives

        //count the number of syllables for each word
        //case 1: words with three letters or less count as one syllable

        long syllable_count = Stream.of(words).filter(w -> w.size() < 4 ).count();

        // case 2: each vowel in a word is one syllable, consecutive vowels count as one syllable
        // -es, -ed, -e (except -le) endings are ignored
        // regex will be used
        Pattern regex = Pattern.compile("[aiou][aeiou]*|e[aeiou]*(?!d?|s?|\\b)");

        List <Matcher> matches = words.stream().filter(w -> w.length() > 3 ) //only words larger than length 3
                .map(w -> regex.matcher(w) ) //map word to the number of syllables it has
                .collect(Collectors.toList()); // map matcher to number of good matches

        // count the number of matches
        for (Matcher m : matches) {
            while(m.find()){
                syllable_count++;
            }
        }

        var sentence_count = sentences.length;
        var word_count = words.size();
        long score;

        //print description to console:
        System.out.println("DESCRIPTION: " + previewDescription);

        //print results to console:
        System.out.println("Number of sentences: " + sentence_count + ". Number of words: " + word_count + ". Number of syllables: " + syllable_count + ".");

        //error case to avoid dividing by zero.
        if (sentence_count == 0){
            System.out.println("No sentences detected. Description may be lacking correct punctuation. Default sentence count of 1 will be used.");
            sentence_count = 1;
        }
        if (word_count == 0){
            System.out.println("No words detected. Description may be lacking correct spacing. Default word count of 1 will be used.");
            word_count = 1;
        }

        score = (long) (206.835 - (84.6 * syllable_count/word_count) - (1.015 * word_count / sentence_count) );
        System.out.println("score: " + score);

        return new ArrayList<Readability>(Arrays.asList(new Readability(score)));
    }

    /**
     * Get projects by owner ID<br>
     * Implemented using Stream API
     *
     * @param ownerId owner ID to retrieve projects from
     * @return List of projects
     * @author Yvonne Lee
     */
    public CompletionStage<List<Project>> findProjectsByOwnerId(long ownerId) {
        return this.myWSClient.initRequest(PROJECT_LIST_URL + "&owners[]=" + ownerId)
                .getListResults(projectsByOwnerIdCache, ownerId, Project.class, "projects");
    }

}
