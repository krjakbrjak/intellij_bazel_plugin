package krjakbrjak.bazel.plugin.settings;

import com.intellij.openapi.externalSystem.settings.ExternalSystemSettingsListener;
import com.intellij.util.messages.Topic;

import java.util.List;

public interface BazelSettingsListener extends ExternalSystemSettingsListener<BazelProjectSettings> {
    Topic<BazelSettingsListener> TOPIC = new Topic<>(BazelSettingsListener.class, Topic.BroadcastDirection.NONE);

    void onBazelExecutableChanged(String linkedProjectPath, List<String> oldExecutable, List<String> newExecutable);

    void onTargetChanged(String linkedProjectPath, int oldTarget, int newTarget);
}
