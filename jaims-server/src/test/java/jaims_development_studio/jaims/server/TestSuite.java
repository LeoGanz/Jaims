package jaims_development_studio.jaims.server;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author WilliGross
 */
@RunWith(Suite.class)
@SuiteClasses({ AccountManagerTest.class,
	ProfileAndSettingsManagerTest.class,
	SendableAndUserTest.class,
	UserManagerTest.class,
	ContactsTest.class })

public class TestSuite {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(TestSuite.class);
		
		for (Failure failure : result.getFailures())
			System.out.println(failure.toString());
		System.out.println("TestSuite successful: " + result.wasSuccessful());
	}
}
