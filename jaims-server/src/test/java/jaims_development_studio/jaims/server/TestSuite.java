package jaims_development_studio.jaims.server;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DAOTest.class, SendableAndUserTest.class })
public class TestSuite {
	
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(TestSuite.class);

		for (Failure failure : result.getFailures())
			System.out.println(failure.toString());
		System.out.println("TestSuite successful: " + result.wasSuccessful());
	}
}
