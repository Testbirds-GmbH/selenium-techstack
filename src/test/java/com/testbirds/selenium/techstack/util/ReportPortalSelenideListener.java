package com.testbirds.selenium.techstack.util;

import static com.epam.reportportal.service.ReportPortal.emitLog;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEvent.EventStatus;
import com.codeborne.selenide.logevents.LogEventListener;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.google.common.io.ByteSource;
import com.google.common.io.MoreFiles;

import rp.com.google.common.base.Function;

public class ReportPortalSelenideListener implements LogEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ReportPortalSelenideListener.class);

    private SaveLogRQ createLogRQ(final String itemId, final String level, final String message, final Path filePath) {
        final SaveLogRQ rq = new SaveLogRQ();
        rq.setLevel(level);
        rq.setLogTime(Calendar.getInstance().getTime());
        rq.setTestItemId(itemId);
        rq.setMessage(message);

        try {
            if (filePath != null) {
                final ByteSource data = MoreFiles.asByteSource(filePath);
                final SaveLogRQ.File file = new SaveLogRQ.File();
                final String fileName = filePath.getFileName().toString();
                file.setContent(data.read());
                file.setName(fileName);
                if (fileName.endsWith(".html")) {
                    file.setContentType("text/html");
                } else if (fileName.endsWith(".png")) {
                    file.setContentType("image/png");
                } else {
                    file.setContentType("");
                }
                rq.setFile(file);
            }
        } catch (final IOException e) {
            LOG.error("IO exception during Selenide report file loading", e);
        }

        return rq;
    }

    private void log(final LogEvent currentLog) {
        if (System.getProperty("rp.uuid") == null) {
            // support running without reportportal as well
            return;
        }

        emitLog(new Function<String, SaveLogRQ>() {
            @Override
            public SaveLogRQ apply(String itemId) {
                final String level = currentLog.getStatus() == EventStatus.FAIL ? "ERROR" : "DEBUG";
                Path file = null;

                try {
                    if (currentLog.getError() instanceof UIAssertionError) {
                        final UIAssertionError error = (UIAssertionError) currentLog.getError();
                        if (!"".equals(error.getScreenshot())) {
                            file = Paths.get(new URI(error.getScreenshot()));

                            // check for extra DOM dump, if screenshot is PNG
                            final String fileName = file.getFileName().toString();
                            if (fileName.endsWith(".png")) {
                                final Path extraDomDump = file.resolveSibling(
                                        fileName.substring(0, fileName.length() - ".png".length()) + ".html");
                                if (extraDomDump.toFile().exists()) {
                                    emitLog((itemId2) -> createLogRQ(itemId2, level,
                                            "Selenide provided an extra DOM dump with the screenshot:", extraDomDump));
                                }
                            }
                        }
                    }
                } catch (final Exception e) {
                    LOG.error("Error during Selenide Screenshot loading", e);
                }

                return createLogRQ(itemId, level, currentLog.toString(), file);
            }
        });
    }

    @Override
    public final void afterEvent(final LogEvent currentLog) {
        switch (currentLog.getStatus()) {
        case FAIL:
        case PASS:
            log(currentLog);
            break;
        case IN_PROGRESS:
        default:
            break;
        }
    }

    @Override
    public final void beforeEvent(final LogEvent currentLog) {
    }
}
