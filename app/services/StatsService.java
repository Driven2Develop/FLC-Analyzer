package services;

import com.google.inject.Inject;
import models.Project;
import models.Stats;
import wsclient.MyWSClient;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static helpers.JsonUtil.parseResultJsonNode;

/**
 * Service layer class for processing stats
 */
public class StatsService {

    private MyWSClient myWSClient;
    private static final String PROJECT_SEARCH_URL = "/projects/0.1/projects/all/?offset=0&limit=250&sort_field=time_submitted&job_details=true";

    /**
     * Constructor for DI
     *
     * @param myWSClient myWSClient
     * @author Bowen Cheng
     */
    @Inject
    public StatsService(MyWSClient myWSClient) {
        this.myWSClient = myWSClient;
    }

    /**
     * Get global word status for top 250 projects based on description preview text<br>
     * Implemented using Stream API
     *
     * @param searchTerms search text
     * @return List of status
     * @throws Exception in case of processing error
     * @author Bowen Cheng
     */
    public List<Stats> getGlobalStats(String searchTerms) throws Exception {
        List<Project> results = parseResultJsonNode(this.myWSClient.initRequest(PROJECT_SEARCH_URL + "&query=" + searchTerms).get());
        Stream<String> stringStream = results.stream().map(Project::getPreviewDescription);
        return getStatsFromStringStream(stringStream);
    }

    /**
     * Get word stats for a single project<br>
     * Implemented using Stream API
     *
     * @param projectDesc project description to extract word stats from
     * @return List of status
     * @author Bowen Cheng
     */
    public List<Stats> getProjectStats(String projectDesc) {
        Stream<String> stringStream = Stream.of(projectDesc);
        return getStatsFromStringStream(stringStream);
    }

    /**
     * Common method to extract word stats and sort results by word occurrence
     *
     * @param stringStream Pre-processed stream of single words
     * @return A list of stats object based on the input words
     * @author Bowen Cheng
     */
    private static List<Stats> getStatsFromStringStream(Stream<String> stringStream) {
        Map<String, Integer> occurrenceMap = new HashMap<>();

        stringStream.map(desc -> desc.replaceAll("\\p{Punct}", ""))
                .map(desc -> desc.replaceAll("\\d+", ""))
                .map(desc -> desc.split(" "))
                .flatMap(Arrays::stream)
                .filter(desc -> (!"".equals(desc)))
                .forEach(word -> occurrenceMap.put(word, occurrenceMap.getOrDefault(word, 0) + 1));

        List<Stats> resultStats = occurrenceMap.entrySet()
                .stream()
                .map(entry -> new Stats(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(Stats::getOccurrence).reversed()) // sort desc
                .collect(Collectors.toList());
        System.out.println("Num of unique words is: " + resultStats.size());
        return resultStats;

    }

}
