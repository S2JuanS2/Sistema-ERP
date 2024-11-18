package fi.uba.memo1.apirest.finanzas;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("fi.uba.memo1.apirest.finanzas")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "fi.uba.memo1.apirest.finanzas")
public class RunCucumberTest {

}