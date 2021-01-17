package krjakbrjak.bazel.plugin.project;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.VfsTestUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BazelProjectImportProviderTest extends LightJavaCodeInsightFixtureTestCase {
    public void testCanImportFile() throws IOException {
        TemporaryFolder tempDir = new TemporaryFolder();
        tempDir.create();

        VirtualFile file = mock(VirtualFile.class);
        var importProvider = new BazelProjectImportProvider(mock(BazelProjectImportBuilder.class));

        when(file.getName()).thenReturn("a");
        assertFalse(importProvider.canImportFromFile(file));
        assertFalse(importProvider.canImport(file, null));

        when(file.getName()).thenReturn(BazelProjectImportProvider.WORKSPACE_FILENAME);
        assertTrue(importProvider.canImportFromFile(file));
        assertTrue(importProvider.canImport(file, null));

        when(file.isDirectory()).thenReturn(true);
        File bazelProjectDir = tempDir.newFolder("bazelProject");
        assertNotNull(bazelProjectDir);

        var bazelDir = VfsUtil.findFileByIoFile(bazelProjectDir, false);
        assertNotNull(bazelDir);

        assertFalse(importProvider.canImport(bazelDir, null));

        VfsTestUtil.createFile(bazelDir, BazelProjectImportProvider.WORKSPACE_FILENAME);
        assertTrue(importProvider.canImport(bazelDir, null));

        VfsTestUtil.deleteFile(bazelDir);

        tempDir.delete();
    }
}
