import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({UserTest.class, CourseTest.class, ModuleTest.class,ContentTest.class, InscriptionTest.class, ContentUserTest.class, ModuleUserTest.class, CommentTest.class, DiplomaTest.class})
public class RunTest {

}
