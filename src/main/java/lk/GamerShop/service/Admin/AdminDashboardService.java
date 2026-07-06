package lk.GamerShop.service.Admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.GamerShop.DTO.AuthorDTO;
import lk.GamerShop.Entities.Author;
import lk.GamerShop.Entities.Book;
import lk.GamerShop.Entities.Status;
import lk.GamerShop.Entities.User;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardService {


    public String fetchSummaryMetrics(String filter) {
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 1. Calculate Date Boundaries dynamically based on the current time
            LocalDate today = LocalDate.now();
            LocalDateTime startDateTime = null;

            if (filter.equals("today")) {
                startDateTime = today.atStartOfDay(); // Today at 00:00
            } else if (filter.equals("this month")) {
                startDateTime = today.withDayOfMonth(1).atStartOfDay(); // 1st of this month
            } else if (filter.equals("this year")) {
                startDateTime = today.withDayOfYear(1).atStartOfDay(); // Jan 1st of this year
            }

            StringBuilder revenueHql = new StringBuilder("SELECT SUM(o.total) FROM Order o");
            StringBuilder userHql = new StringBuilder("SELECT COUNT(u.id) FROM User u");
            StringBuilder bookHql = new StringBuilder("SELECT COUNT(b.id) FROM OrderItem b WHERE 1=1");

            if (startDateTime != null) {
                revenueHql.append(" WHERE o.createdAt >= :startDate");
                userHql.append(" WHERE u.createdAt >= :startDate");
                bookHql.append(" AND b.createdAt >= :startDate");
            }

            // Execute Dynamic Revenue
            Query<Double> revQuery = session.createQuery(revenueHql.toString(), Double.class);
            if (startDateTime != null) revQuery.setParameter("startDate", startDateTime);
            Double calculatedRevenue = revQuery.getSingleResult();
            double totalRevenue = (calculatedRevenue != null) ? calculatedRevenue : 0.0;

            // Execute Dynamic Users Count
            Query<Long> userQuery = session.createQuery(userHql.toString(), Long.class);
            if (startDateTime != null) userQuery.setParameter("startDate", startDateTime);
            long totalUsers = userQuery.getSingleResult();

            // Execute Dynamic Books Count (Books added in this specific period)
            Query<Long> bookQuery = session.createQuery(bookHql.toString(), Long.class);
            if (startDateTime != null) bookQuery.setParameter("startDate", startDateTime);
            long totalBooks = bookQuery.getSingleResult();

            response.addProperty("totalUsers", totalUsers);
            response.addProperty("totalBooks", totalBooks);
            response.addProperty("totalRevenue", totalRevenue);

            // 3. Dynamic Spotlight Book (Most Sold in the selected period)
            StringBuilder topBookHql = new StringBuilder(
                    "SELECT oi.ebook.title, oi.ebook.coverimagepath, COUNT(oi.id) FROM OrderItem oi "
            );
            if (startDateTime != null) {
                topBookHql.append(" WHERE oi.order.createdAt >= :startDate ");
            }
            topBookHql.append(" GROUP BY oi.ebook.id, oi.ebook.title, oi.ebook.coverimagepath ORDER BY COUNT(oi.id) DESC");

            Query<Object[]> topBookQuery = session.createQuery(topBookHql.toString(), Object[].class).setMaxResults(1);
            if (startDateTime != null) topBookQuery.setParameter("startDate", startDateTime);
            List<Object[]> topBookList = topBookQuery.getResultList();

            JsonObject featuredBookJson = new JsonObject();
            if (!topBookList.isEmpty()) {
                Object[] row = topBookList.get(0);
                featuredBookJson.addProperty("title", (String) row[0]);
                featuredBookJson.addProperty("coverPath", (String) row[1]);
                featuredBookJson.addProperty("unitsSold", (Long) row[2]);
            } else {
                featuredBookJson.addProperty("title", "No sales tracked");
                featuredBookJson.addProperty("coverPath", "");
                featuredBookJson.addProperty("unitsSold", 0);
            }
            response.add("featuredBook", featuredBookJson);

            // 4. Chart Timeline Axis Formatting with Real Data
            JsonArray revLabels = new JsonArray();
            JsonArray revData = new JsonArray();
            JsonArray signupLabels = new JsonArray();
            JsonArray signupData = new JsonArray();

            if (filter.equals("today")) {
                // Group real revenue by Hour of the day (0 - 23)
                List<Object[]> hourlyRevenue = session.createQuery(
                                "SELECT HOUR(o.createdAt), SUM(o.total) FROM Order o " +
                                        "WHERE o.createdAt >= :startDate " +
                                        "GROUP BY HOUR(o.createdAt) " +
                                        "ORDER BY HOUR(o.createdAt) ASC", Object[].class)
                        .setParameter("startDate", startDateTime)
                        .getResultList();

                for (Object[] row : hourlyRevenue) {
                    int hour = (Integer) row[0];
                    double sum = (Double) row[1];
                    revLabels.add(String.format("%02d:00", hour));
                    revData.add(sum);
                }

                // Group user signups by Hour
                List<Object[]> hourlySignups = session.createQuery(
                                "SELECT HOUR(u.createdAt), COUNT(u.id) FROM User u " +
                                        "WHERE u.createdAt >= :startDate " +
                                        "GROUP BY HOUR(u.createdAt) " +
                                        "ORDER BY HOUR(u.createdAt) ASC", Object[].class)
                        .setParameter("startDate", startDateTime)
                        .getResultList();

                for (Object[] row : hourlySignups) {
                    signupLabels.add(String.format("%02d:00", (Integer) row[0]));
                    signupData.add((Long) row[1]);
                }

            } else if (filter.equals("this month")) {
                // Group real revenue by the specific Day of Month (1 - 31)
                List<Object[]> dailyRevenue = session.createQuery(
                                "SELECT DAY(o.createdAt), SUM(o.total) FROM Order o " +
                                        "WHERE o.createdAt >= :startDate " +
                                        "GROUP BY DAY(o.createdAt) " +
                                        "ORDER BY DAY(o.createdAt) ASC", Object[].class)
                        .setParameter("startDate", startDateTime)
                        .getResultList();

                for (Object[] row : dailyRevenue) {
                    revLabels.add("Day " + row[0]);
                    revData.add((Double) row[1]);
                }

                // Group user signups by Day of Month
                List<Object[]> dailySignups = session.createQuery(
                                "SELECT DAY(u.createdAt), COUNT(u.id) FROM User u " +
                                        "WHERE u.createdAt >= :startDate " +
                                        "GROUP BY DAY(u.createdAt) " +
                                        "ORDER BY DAY(u.createdAt) ASC", Object[].class)
                        .setParameter("startDate", startDateTime)
                        .getResultList();

                for (Object[] row : dailySignups) {
                    signupLabels.add("Day " + row[0]);
                    signupData.add((Long) row[1]);
                }

            } else if (filter.equals("this year")) {
                // Group real revenue by Month (1 = Jan, 12 = Dec)
                List<Object[]> monthlyRevenue = session.createQuery(
                                "SELECT MONTH(o.createdAt), SUM(o.total) FROM Order o " +
                                        "WHERE o.createdAt >= :startDate " +
                                        "GROUP BY MONTH(o.createdAt) " +
                                        "ORDER BY MONTH(o.createdAt) ASC", Object[].class)
                        .setParameter("startDate", startDateTime)
                        .getResultList();

                String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                for (Object[] row : monthlyRevenue) {
                    int monthNum = (Integer) row[0];
                    revLabels.add(months[monthNum]);
                    revData.add((Double) row[1]);
                }

                // Group user signups by Month
                List<Object[]> monthlySignups = session.createQuery(
                                "SELECT MONTH(u.createdAt), COUNT(u.id) FROM User u " +
                                        "WHERE u.createdAt >= :startDate " +
                                        "GROUP BY MONTH(u.createdAt) " +
                                        "ORDER BY MONTH(u.createdAt) ASC", Object[].class)
                        .setParameter("startDate", startDateTime)
                        .getResultList();

                for (Object[] row : monthlySignups) {
                    signupLabels.add(months[(Integer) row[0]]);
                    signupData.add((Long) row[1]);
                }

            } else {
                // "total" configuration mode: Group by Year (e.g. 2024, 2025, 2026)
                List<Object[]> yearlyRevenue = session.createQuery(
                                "SELECT YEAR(o.createdAt), SUM(o.total) FROM Order o " +
                                        "GROUP BY YEAR(o.createdAt) " +
                                        "ORDER BY YEAR(o.createdAt) ASC", Object[].class)
                        .getResultList();

                for (Object[] row : yearlyRevenue) {
                    revLabels.add(row[0].toString());
                    revData.add((Double) row[1]);
                }

                // Group user signups by Year
                List<Object[]> yearlySignups = session.createQuery(
                                "SELECT YEAR(u.createdAt), COUNT(u.id) FROM User u " +
                                        "GROUP BY YEAR(u.createdAt) " +
                                        "ORDER BY YEAR(u.createdAt) ASC", Object[].class)
                        .getResultList();

                for (Object[] row : yearlySignups) {
                    signupLabels.add(row[0].toString());
                    signupData.add((Long) row[1]);
                }
            }

            response.add("revenueTimelineLabels", revLabels);
            response.add("revenueTimelineData", revData);
            response.add("signupTimelineLabels", signupLabels);
            response.add("signupTimelineData", signupData);

            response.add("revenueTimelineLabels", revLabels);
            response.add("revenueTimelineData", revData);
            response.add("signupTimelineLabels", signupLabels);
            response.add("signupTimelineData", signupData);

            status = true;
            message = "Dashboard metrics calculated successfully.";

        } catch (Exception e) {
            e.printStackTrace();
            message = "Error rendering period calculations: " + e.getMessage();
        } finally {
            session.close();
        }

        response.addProperty("status", status);
        response.addProperty("message", message);
        return AppUtil.GSON.toJson(response);
    }
    }

