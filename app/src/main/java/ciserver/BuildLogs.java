package ciserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuildLogs {

    public static final String BUILD_LOGS_PATH = "ci-server/buildlogs/";


    /**
     * Gets the build log output from a given SHA of the commit.
     *
     * @param uniqueId SHA of the commit.
     * @return The content of the build logs.
     * @throws IOException
     */
    public static String getBuildLog(String uniqueId) throws IOException {
        return Files.readString(Path.of(BUILD_LOGS_PATH + "/" + uniqueId));
    }

    /**
     * List the names of all the commits that have been logged.
     *
     * @return List all the logs that are available
     */
    public static String getBuildLogs() {
        File folder = new File(BUILD_LOGS_PATH);
        File[] filesList = folder.listFiles();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < filesList.length; i++) {
            stringBuilder.append("Build " + i + ": " + filesList[i].getName() + "\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Creates a log file given a unique ID.
     *
     * @param uniqueID a unique ID for the file name.
     * @return log File
     */
    public static File createLogFile(String uniqueID) throws IOException {
        File output = new File(BUILD_LOGS_PATH + uniqueID);
        output.getParentFile().mkdirs();
        output.createNewFile();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = format.format(new Date());
        try (FileWriter writer = new FileWriter(BUILD_LOGS_PATH + uniqueID)) {
            writer.write("BUILD DATE: " + date + "\n" + "BUILD IDENTIFIER: " + uniqueID + "\n"+"BUILD LOG: \n");        }
        return output;
    }
}
