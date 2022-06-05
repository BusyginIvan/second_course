import org.apache.tools.ant.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DiffTask extends Task {
    private final Git git;
    private String commitClassesFileName;

    public DiffTask() throws IOException {
        git = Git.open(new File("."));
    }

    public void setCommitClassesFileName(String commitClassesFileName) { this.commitClassesFileName = commitClassesFileName; }

    @Override
    public void execute() throws BuildException {
        try (BufferedReader br = new BufferedReader(new FileReader(commitClassesFileName))) {
            boolean thereAreUncommittedChanges = false;
            String line;
            while ((line = br.readLine()) != null) {
                git.add().addFilepattern(line).call();
                if (git.status().addPath(line).call().hasUncommittedChanges())
                    thereAreUncommittedChanges = true;
            }
            if (thereAreUncommittedChanges)
                git.commit().setMessage("auto commit").call();
        } catch (IOException|GitAPIException e) {
            e.printStackTrace();
        }
    }
}
