package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.PsqlStore;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.quartz.AlertRabbit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class SqlRuParse implements Parse {
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        for (int page = 1; page <= 5; page++) {
            try {
                Document doc = Jsoup.connect(link + "/" + page).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    if (
                            td.child(0).text().toLowerCase(Locale.ROOT).contains("java")
                            && !td.child(0).text().toLowerCase(Locale.ROOT).contains("javascript")
                    ) {
                        posts.add(detail(td.child(0).attr("href")));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".msgTable");
            String description = row.first().select(".msgBody").get(1).text();
            String title = row.first().select(".messageHeader").text();
            String created = row.last().select(".msgFooter").text();
            return new Post(title, link, description, dateTimeParser.parse(created));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
}