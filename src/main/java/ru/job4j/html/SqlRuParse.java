package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;

public class SqlRuParse {
    public Post postData(String url) {
        SqlRuDateTimeParser sq = new SqlRuDateTimeParser();
        Post post = new Post();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".msgTable");
            String description = row.first().select(".msgBody").get(1).text();
            String title = row.first().select(".messageHeader").text();
            String created = row.last().select(".msgFooter").text();
            return new Post(title, url, description, sq.parse(created));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

    public static void main(String[] args) throws Exception {
        for (int page = 1; page <= 5; page++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + page).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String date = td.parent().child(5).text();
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(date);
            }
        }
    }
}