import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import util.ConnectionManager;

import java.io.PrintWriter;

public class TestRunner {
    public static void main(String[] args) {
       Launcher launcher = LauncherFactory.create();
       LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
               .selectors(DiscoverySelectors.selectDirectory("target/test-classes")).build();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.execute(request,listener);
        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));

        ConnectionManager.terminate();
    }
}
