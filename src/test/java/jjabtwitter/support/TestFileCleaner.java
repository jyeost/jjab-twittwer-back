package jjabtwitter.support;

import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public interface TestFileCleaner {

    @AfterEach
    default void cleanStoreDirectory() {
        final File imageFolder = new File("src/test/resources/storetest/image");

        final FilenameFilter filter = (dir, name) -> !name.equals("dummy.txt");
        final File[] files = imageFolder.listFiles(filter);
        assert files != null;
        Arrays.stream(files).forEach(File::delete);
    }
}
