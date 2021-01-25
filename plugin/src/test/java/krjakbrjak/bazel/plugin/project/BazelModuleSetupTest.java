package krjakbrjak.bazel.plugin.project;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import krjakbrjak.bazel.plugin.settings.BazelProjectSettings;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BazelModuleSetupTest extends LightJavaCodeInsightFixtureTestCase {
    public void testSettingGettingData() {
        var setup = BazelModuleSetup.getInstance();
        var listener = mock(BazelModuleSetupListener.class);
        setup.subscribeForChanges(listener);
        BazelProjectSettings settings = new BazelProjectSettings();

        setup.getData(settings);
        assertNull(settings.getExecutable());
        assertNull(settings.getPackages());
        assertEquals(settings.getCurrentPackage(), -1);
        assertNull(settings.getTargets());
        assertEquals(settings.getCurrentTarget(), -1);

        settings.setCurrentPackage(-10);
        assertEquals(-10, settings.getCurrentPackage());
        setup.setData(settings, true);
        BazelProjectSettings newSettings = new BazelProjectSettings();
        setup.getData(newSettings);
        assertNotEquals(settings, newSettings);

        settings = new BazelProjectSettings();
        settings.setExecutable(List.of("TEST"));
        setup.setData(settings, true);
        verifyNoMoreInteractions(listener);
        setup.getData(newSettings);
        assertEquals(settings, newSettings);

        settings.setPackages(List.of("PKG"));
        setup.setData(settings, true);
        verifyNoMoreInteractions(listener);
        setup.getData(newSettings);
        assertEquals(settings, newSettings);

        settings.setCurrentPackage(1);
        verifyNoMoreInteractions(listener);

        settings.setCurrentPackage(0);
        setup.setData(settings, true);
        verifyNoMoreInteractions(listener);
        setup.getData(newSettings);
        assertEquals(settings, newSettings);
    }
}
