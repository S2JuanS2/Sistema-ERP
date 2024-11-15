package fi.uba.memo1.apirest.finanzas;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("fi/uba/memo1/apirest/finanzas")
public class RunCucumberTest {

}