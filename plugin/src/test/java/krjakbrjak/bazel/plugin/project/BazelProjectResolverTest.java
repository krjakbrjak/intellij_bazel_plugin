package krjakbrjak.bazel.plugin.project;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.ExternalSystemException;
import com.intellij.openapi.externalSystem.model.project.ModuleSdkData;
import com.intellij.openapi.externalSystem.model.project.ProjectData;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskType;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import krjakbrjak.bazel.BazelCommands;
import krjakbrjak.bazel.Handle;
import krjakbrjak.bazel.plugin.services.BazelCommandsTest;
import krjakbrjak.bazel.plugin.settings.BazelExecutionSettings;
import krjakbrjak.bazel.plugin.util.BazelConstants;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BazelProjectResolverTest extends LightJavaCodeInsightFixtureTestCase {

    private BazelExecutionSettings mockExecutionSettings() {
        BazelExecutionSettings exeSettings = mock(BazelExecutionSettings.class);
        when(exeSettings.getIdeProjectPath()).thenReturn("TestIdeProjectPath");
        return exeSettings;
    }

    public void testResolveProjectInfo() throws IOException {
        var bazelCommands = ((BazelCommandsTest) ServiceManager.getService(BazelCommands.class)).getMockBazel();
        Handle<Optional<String>> handle = (Handle<Optional<String>>) mock(Handle.class);
        when(bazelCommands.queryLocalJdk(any(), any())).thenReturn(handle);
        when(handle.onExit()).thenReturn(
                CompletableFuture.completedFuture(Optional.of("/test/jdk"))
        );

        Handle<List<String>> handle2 = (Handle<List<String>>) mock(Handle.class);
        when(bazelCommands.queryAllDependencies(any(), any(), any())).thenReturn(handle2);
        when(handle2.onExit()).thenReturn(
                CompletableFuture.completedFuture(List.of("/test/jdk"))
        );

        ExternalSystemTaskNotificationListener listener = mock(ExternalSystemTaskNotificationListener.class);

        var resolver = new BazelProjectResolver();

        DataNode<ProjectData> ret = resolver.resolveProjectInfo(
                ExternalSystemTaskId.create(
                        BazelConstants.SYSTEM_ID,
                        ExternalSystemTaskType.RESOLVE_PROJECT,
                        getProject()
                ),
                "test",
                false,
                null,
                listener
        );
        assertNull(ret);

        BazelExecutionSettings exeSettings = mock(BazelExecutionSettings.class);
        ret = resolver.resolveProjectInfo(
                ExternalSystemTaskId.create(
                        BazelConstants.SYSTEM_ID,
                        ExternalSystemTaskType.RESOLVE_PROJECT,
                        getProject()
                ),
                "test",
                false,
                exeSettings,
                listener
        );
        assertNull(ret);

        when(exeSettings.getIdeProjectPath()).thenReturn("TestIdeProjectPath");
        ret = resolver.resolveProjectInfo(
                ExternalSystemTaskId.create(
                        BazelConstants.SYSTEM_ID,
                        ExternalSystemTaskType.RESOLVE_PROJECT,
                        getProject()
                ),
                "test",
                false,
                exeSettings,
                listener
        );
        assertNotNull(ret);
        var children = ret.getChildren();
        assertEquals(1, children.size());
        children.forEach(node -> {
            var nodes = node.getChildren().stream().filter(dataNode -> dataNode.getKey() == ModuleSdkData.KEY).collect(Collectors.toList());
            assertEquals(1, nodes.size());
            String jdkName = nodes.get(0).getData(ModuleSdkData.KEY).getSdkName();
            assertEquals("test jdk", jdkName);
        });
    }

    public void testExecutionSettings() {
        var resolver = new BazelProjectResolver();
        assertFalse(resolver.areExecutionSettingsValid(null));

        var settings = mock(BazelExecutionSettings.class);

        assertFalse(resolver.areExecutionSettingsValid(settings));

        when(settings.getIdeProjectPath()).thenReturn("");
        assertFalse(resolver.areExecutionSettingsValid(settings));

        when(settings.getIdeProjectPath()).thenReturn("k");
        assertTrue(resolver.areExecutionSettingsValid(settings));
    }

    public void testWrongExecutableReturn() throws IOException {
        var bazelCommands = ((BazelCommandsTest) ServiceManager.getService(BazelCommands.class)).getMockBazel();
        Handle<Optional<String>> handle = (Handle<Optional<String>>) mock(Handle.class);
        when(bazelCommands.queryLocalJdk(any(), any())).thenThrow(IOException.class);
        when(handle.onExit()).thenReturn(
                CompletableFuture.completedFuture(Optional.empty())
        );

        Handle<List<String>> handle2 = (Handle<List<String>>) mock(Handle.class);
        when(bazelCommands.queryAllDependencies(any(), any(), any())).thenReturn(handle2);
        when(handle2.onExit()).thenReturn(
                CompletableFuture.completedFuture(List.of())
        );

        BazelExecutionSettings exeSettings = mockExecutionSettings();
        ExternalSystemTaskNotificationListener listener = mock(ExternalSystemTaskNotificationListener.class);

        var resolver = new BazelProjectResolver();

        assertThrows(ExternalSystemException.class, () -> resolver.resolveProjectInfo(
                ExternalSystemTaskId.create(
                        BazelConstants.SYSTEM_ID,
                        ExternalSystemTaskType.RESOLVE_PROJECT,
                        getProject()
                ),
                "test",
                false,
                exeSettings,
                listener
        ));
    }
}
