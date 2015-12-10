/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.service.schedule;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.ScheduleExpression;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpressionParser {
    private final Map<String, ScheduleExpression> scheduleAliases = new HashMap<>();

    @PostConstruct
    private void init() {
        scheduleAliases.put("hourly", new ScheduleExpression().hour("*"));
        scheduleAliases.put("daily", new ScheduleExpression());
        scheduleAliases.put("weekly", new ScheduleExpression().dayOfWeek("*"));
        scheduleAliases.put("monthly", new ScheduleExpression().dayOfMonth(1));
        scheduleAliases.put("annually", new ScheduleExpression().dayOfMonth(1).month(1));
    }

    public ScheduleExpression parse(final String cronExpression) {
        if ("never".equals(cronExpression)) {
            return null;
        }

        // parsing it more or less like cron does, at least supporting same fields (+ seconds)
        final String[] parts = cronExpression.split(" ");
        final ScheduleExpression scheduleExpression;
        if (parts.length != 6 && parts.length != 5) {
            scheduleExpression = scheduleAliases.get(cronExpression);
            if (scheduleExpression == null) {
                throw new IllegalArgumentException(cronExpression + " doesn't have 6 segments as excepted");
            }
            return scheduleExpression;
        } else if (parts.length == 6) { // enriched cron with seconds
            return new ScheduleExpression()
                    .minute(parts[0])
                    .hour(parts[1])
                    .second(parts[2])
                    .dayOfMonth(parts[3])
                    .month(parts[4])
                    .dayOfWeek(parts[5]);
        }
        // cron
        return new ScheduleExpression()
                .minute(parts[0])
                .hour(parts[1])
                .dayOfMonth(parts[2])
                .month(parts[3])
                .dayOfWeek(parts[4]);
    }
}
