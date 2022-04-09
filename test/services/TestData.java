package services;

import models.Job;
import models.Project;
import models.Readability;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * helper class to provide data for other unit tests
 *
 * @author Mengqi Liu
 */
public class TestData {

    public static final long DEFAULT_USER_ID = 1L;
    public static final String TEST_TITLE_1 = "Test 1";
    public static final String TEST_TITLE_2 = "Test 2";
    public static final List<Job> TEST_JOBS_1 = List.of(new Job(7, "Java"), buildJob(8, "JavaScript"));
    public static final List<Job> TEST_JOBS_2 = List.of(new Job(9, "Java"), buildJob(10, "JavaScript"));
    public static final long TEST_SUBMITTIME_1 = 1647003600L;
    public static final long TEST_SUBMITTIME_2 = 1646139600L;
    public static final String DEFAULT_PROJECT_TYPE = "Default Project Type";
    public static final Readability DEFAULT_READABILITY_1 = new Readability(99);
    public static final String DEFAULT_TEST_PREVIEW_DESCRIPTION_1 = "This is a very hard project preview.";
    public static final String DEFAULT_TEST_PREVIEW_DESCRIPTION_2 = "This is an exceedingly challenging project description";
    public static final Readability DEFAULT_READABILITY_2 = new Readability(29);
    public static final Project TEST_PROJECT_1 = buildProject(TEST_TITLE_1, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, TEST_JOBS_1, TEST_SUBMITTIME_1, DEFAULT_READABILITY_1, DEFAULT_TEST_PREVIEW_DESCRIPTION_1);
    public static final Project TEST_PROJECT_2 = buildProject(TEST_TITLE_2, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, TEST_JOBS_2, TEST_SUBMITTIME_2, DEFAULT_READABILITY_2, DEFAULT_TEST_PREVIEW_DESCRIPTION_2);
    public static final CompletionStage<List<Project>> FUTURE_PROJECTS = CompletableFuture.completedStage(List.of(TEST_PROJECT_1, TEST_PROJECT_2));
    public static final Readability TEST_READABILITY = new Readability(50);
    public static final String[] TEST_PREVIEW_DESCRIPTION = {
            "This is simple.", //Early
            "This is good preview.", //5th grade
            "this is a very easy one.", //6th grade
            "This is a very hard project preview.", //7th grade
            "This is a project description.", //8th Grade
            "This is an even simpler project preview descript", //9th grade
            "This is a more challenging project description.", // High School
            "This is an incredibly hard preview description.", //College
            "This is an exceedingly challenging project description.", //College Graduate
            "Sophisticated understanding is needed for this preview description.", //Law School Graduate
            "Zero%Sentence%Case",
            "Zero%Word%Case"
    };


    /**
     * static method to build object <code>Project</code>
     *
     * @author Mengqi Liu
     */
    public static Project buildProject(String title, long ownerId, String jobType, List<Job> jobs, long time, Readability r, String s) {
        Project project = new Project();
        project.setTitle(title);
        project.setOwnerId(ownerId);
        project.setType(jobType);
        project.setJobs(jobs);
        project.setTimeSubmitted(time);
        project.setReadability(r);
        project.setPreviewDescription(s);
        return project;
    }

    public static Job buildJob(long id, String name) {
        Job job = new Job();
        job.setId(id);
        job.setName(name);
        return job;
    }
}
