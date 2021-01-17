package krjakbrjak.bazel;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

public class Fixture extends LightJavaCodeInsightFixtureTestCase {
    public void testAction()
    {
        myFixture.testAction(new Action());
        assertTrue(true);
    }
}
