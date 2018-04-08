package nanodegree.florinbacu.com.newmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
  public void test_find_image_link() throws IOException, JSONException {
        Document doc = Jsoup.connect("https://www.moma.org/visit/calendar/film_screenings/28379").get();
        Elements scripts = doc.getElementsByTag("script");
        int i;
        for(i=0;i<scripts.size();i++)
        {
            if(scripts.get(i).attr("type").equals("application/ld+json"))
            {
             String json=scripts.get(i).html().replaceAll("(\\n| |\\t)","");
                final String regex = "\"workPresented\":\\[.*\\]";
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(json);
                matcher.find();
                JSONArray array=new JSONArray(matcher.group(0).split(":")[1]);
                String image = array.getJSONObject(0).getString("image");
                System.out.println(image);
                break;

            }
        }
    }
}