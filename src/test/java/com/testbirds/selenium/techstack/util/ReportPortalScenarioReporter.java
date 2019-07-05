package com.testbirds.selenium.techstack.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.reportportal.cucumber.ScenarioReporter;

import cucumber.api.event.EventPublisher;

public class ReportPortalScenarioReporter extends ScenarioReporter {

    private static final Logger LOG = LoggerFactory.getLogger(ReportPortalScenarioReporter.class);

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        if (System.getProperty("rp.uuid") == null) {
            // support running without Report Portal as well
            LOG.warn("Property rp.uuid is not defined, will not publish to Report Portal");
            return;
        }

        super.setEventPublisher(publisher);
    }
}
