package org.pknudev.common;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;

import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import lombok.Setter;
import org.ocpsoft.prettytime.PrettyTime;

@Setter
public class PrettyTimeTag extends SimpleTagSupport {
    private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("MM/dd HH:mm");
    private PrettyTime prettyTime = new PrettyTime(new Locale("ko"));
    private Timestamp date;

    @Override
    public void doTag() throws IOException {
        JspWriter out = getJspContext().getOut();
        boolean isRelativelyNew = date.toInstant().plusSeconds(3600).isAfter(Instant.now());
        if (isRelativelyNew) {
            out.println(prettyTime.format(date));
        } else {
            out.println(defaultDateFormat.format(date));
        }
    }
}
