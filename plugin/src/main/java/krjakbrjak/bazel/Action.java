package krjakbrjak.bazel;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class Action extends AnAction {
    private static final NotificationGroup STICKY_GROUP =
            new NotificationGroup("krjakbrjak.notifications.bazel_version",
                    NotificationDisplayType.STICKY_BALLOON, true);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Notification msg = new Notification(
                STICKY_GROUP.getDisplayId(), null,
                "Bazel", "Version", "0.0.0",
                NotificationType.INFORMATION, null);
        Notifications.Bus.notify(msg);
    }
}
