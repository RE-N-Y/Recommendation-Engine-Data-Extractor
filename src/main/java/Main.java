import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main{

    public static void main(String[] args){
        anime();
    }

    private static void duplicateRemover() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/inputs/merged.json"));
            Set<String> lines = new HashSet<String>(2000000);
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/outputs/anime.json"));
            for (String unique : lines) {
                writer.write(unique);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void anime_extractor() {
        File folder = new File("src/main/inputs");
        File[] listOfFiles = folder.listFiles();
        try {
            for (int i = 0; i < listOfFiles.length; i++) {
                if(!listOfFiles[i].toString().equals("src/main/inputs/.DS_Store")) {
                    System.out.println(listOfFiles[i].toString());
                    File tempFile = new File("src/main/outputs/"+((i+1)+".json"));
                    FileReader inputStream = new FileReader(listOfFiles[i].getPath());
                    FileWriter outputStream = new FileWriter(tempFile);
                    int c;
                    StringBuilder sb = new StringBuilder();
                    while ((c = inputStream.read()) != -1) {
                        if((char)c=='\n') {
                            JSONObject ob = new JSONObject(sb.toString()).getJSONObject("myanimelist");
                            if(!ob.has("anime")) {
                                sb = new StringBuilder();
                                continue;
                            }
                            Object animes = ob.get("anime");
                            if(animes instanceof JSONArray) {
                                for(Object anime : (JSONArray) animes) {
                                    outputStream.write(((JSONObject) anime).get("series_animedb_id").toString()+"\n");
                                }
                            }
                            sb = new StringBuilder();
                        }
                        sb.append((char)c);
                    }
                    inputStream.close();
                    outputStream.close();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void parser() {
        try {
            File folder = new File("src/main/inputs");
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File tempFile = new File(("src/main/outputs/"+(i+1)+".json"));
                BufferedReader reader = new BufferedReader(new FileReader(listOfFiles[i].getPath()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                String currentLine;
                System.out.println(listOfFiles[i]);
                while((currentLine = reader.readLine()) != null) {
                    try {
                        String json = XML.toJSONObject(currentLine).toString();
                        if(json.equals("{}") || json.equals("{\"myanimelist\":\"\"}")) {
                            continue;
                        }
                        writer.write(json+"\n");
                    } catch (JSONException e) {
                        continue;
                    }
                }

                writer.close();
                reader.close();

            }
        } catch(IOException e) {
            e.printStackTrace();
        }



    }

    private static void finalizer() {
        File folder = new File("src/main/inputs/");
        File[] listOfFiles = folder.listFiles();
        try {
            for (int i = 0; i < listOfFiles.length; i++) {
                System.out.println(listOfFiles[i].toString());
                File tempFile = new File("src/main/outputs/"+((i+1)+".xml"));
                FileReader inputStream = new FileReader(listOfFiles[i].getPath());
                FileWriter outputStream = new FileWriter(tempFile);
                int c;
                StringBuilder sb = new StringBuilder();
                while ((c = inputStream.read()) != -1) {
                    if((char)c=='\n') {
                        if(!sb.toString().contains("</myanimelist>")) {
                            System.out.println(sb.toString());
                            continue;
                        } else {
                            outputStream.write(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                    sb.append((char)c);
                }
                inputStream.close();
                outputStream.close();
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private  static void extracter() {
        ArrayList<String> usernames = new ArrayList<String>();
        Path path = FileSystems.getDefault().getPath("user.txt");
        try {
            Writer output = new BufferedWriter(new FileWriter("data.xml"));
            Files.lines(path).forEach(e->usernames.add(e));
            for (int i=78032;i<usernames.size();i++) {
                run(usernames.get(i),output);
            }

            output.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void remover () {
        try {
            String lineToRemove = "</user_name><user_watching>0</user_watching><user_completed>0</user_completed><user_onhold>0</user_onhold><user_dropped>0</user_dropped><user_plantowatch>0</user_plantowatch><user_days_spent_watching>0.00</user_days_spent_watching></myinfo></myanimelist>";
            String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            File folder = new File("src/main/inputs");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println(listOfFiles[i].toString());
                    File tempFile = new File(("src/main/outputs/"+(i+1)+".xml"));
                    BufferedReader reader = new BufferedReader(new FileReader(listOfFiles[i].getPath()),8*1024);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                    String currentLine;

                    while((currentLine = reader.readLine()) != null) {
                        String trimmedLine = currentLine.trim();
                        if(trimmedLine.contains(lineToRemove)) continue;
                        if(trimmedLine.contains(xmlHeader)) continue;
                        writer.append(currentLine+"\n");
                    }
                    writer.close();
                    reader.close();
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void formatter() {
        File folder = new File("src/main/inputs/");
        File[] listOfFiles = folder.listFiles();

        try {
            for (int i = 0; i < listOfFiles.length; i++) {
                File tempFile = new File("src/main/outputs/"+((i+1)+".xml"));
                FileReader inputStream = new FileReader(listOfFiles[i].getPath());
                FileWriter outputStream = new FileWriter(tempFile);
                System.out.println(listOfFiles[i].toString());
                int c;
                ArrayList<Character> dataEnd = new ArrayList<Character>();
                ArrayList<Character> XMLHeader = new ArrayList<Character>();
                while ((c = inputStream.read()) != -1) {
                    if (dataEnd.size()>13) {
                        dataEnd.remove(0);
                    }
                    if (XMLHeader.size()>37) {
                        XMLHeader.remove(0);
                    }
                    dataEnd.add((char)c);
                    XMLHeader.add((char)c);
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    for (char ch : dataEnd) {
                        sb1.append(ch);
                    }
                    for (char ch : XMLHeader) {
                        sb2.append(ch);
                    }
                    outputStream.write(c);
                    if(sb1.toString().equals("</myanimelist>")) {
                        outputStream.write('\n');
                    }
                    if(sb2.toString().equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                        outputStream.write('\n');
                    }
                }
                inputStream.close();
                outputStream.close();
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void run(String username, Writer output){
        try {
            TimeUnit.MILLISECONDS.sleep(600);
            System.out.println(username);
            String https_url = "https://myanimelist.net/malappinfo.php?u="+username+"&status=all&type=anime";
            URL url;
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            if(con!=null) {
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                String input;
                while ((input = br.readLine()) != null){
                    output.append(input+"\n");
                }
                br.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray toArray(String str) {
        JSONArray arr = new JSONArray();
        if(!str.equals("None found, add some")) {
            String[] temp = str.split(", ");
            for(String s : temp) {
                arr.put(s);
            }
        }
        return arr;
    }

    private static void anime() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/outputs/animeList4.json"));
            Path path = FileSystems.getDefault().getPath("src/main/inputs/anime.json");
            ArrayList<String> animeIds = new ArrayList<String>();
            Files.lines(path).forEach(e->animeIds.add(e));
            for (int i=(2225+44+7887+822); i<animeIds.size();i++) {
                if(animeIds.get(i).equals("34606")) continue;
                TimeUnit.MILLISECONDS.sleep(900);
                URL url = new URL("https://myanimelist.net/anime/"+animeIds.get(i));
                HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
                System.out.println(animeIds.get(i));
                if(con!=null) {
                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));
                    String input;
                    String result = "";
                    while ((input = br.readLine()) != null){
                        result+=input;
                    }
                    JSONObject user_data = new JSONObject();
                    Document doc = Jsoup.parse(result);
                    Element description = doc.select("[itemprop=\"description\"]").first();
                    user_data.put("Title",doc.select("[itemprop=\"name\"]").first().text().toString());
                    if(description != null) {
                        user_data.put("Description",doc.select("[itemprop=\"description\"]").first().text().toString());
                    }
                    user_data.put("Description","");
                    Elements info = doc.select("div.js-scrollfix-bottom h2 ~ div");
                    Elements staff = doc.select("div.detail-characters-list a[href~=people]");
                    JSONArray staffs = new JSONArray();

                    for (Element e : info) {
                        int index = e.text().indexOf(':');
                        if(index>0) {
                            String key = e.text().substring(0,index);
                            String value = e.text().substring(index+2,e.text().length());
                            if(key.equals("Synonyms") || key.equals("Japanese") || key.equals("English") || key.equals("Broadcast") || key.equals("Licensors")) {
                                continue;
                            } else if(key.equals("Popularity")) {
                                value = value.substring(1,value.length());
                            } else if(key.equals("Rating") || key.equals("Duration")) {
                                Pattern pattern = Pattern.compile("[0-9]+");
                                Matcher matcher = pattern.matcher(value);
                                if(matcher.find()) value = matcher.group();
                            } else if(key.equals("Score")) {
                                value = value.substring(0,4);
                            } else if(key.equals("Genres")) {
                                JSONArray genres = new JSONArray();
                                String[] temp = value.split(", ");
                                for(String genre : temp) {
                                    genres.put(genre);
                                }
                                user_data.put(key,genres);
                                continue;
                            } else if(key.equals("Ranked")) {
                                String num = value.substring(1,value.indexOf(' '));
                                value = num.substring(0,num.length()-1);
                            } else if(key.equals("Members") || key.equals("Favorites")){
                                value = value.replaceAll(",","");
                            } else if(key.equals("Aired")) {
                                String[] temp = value.split("to");
                                user_data.put("Started_Airing",temp[0].trim());
                                if(temp.length > 1) {
                                    user_data.put("Ended_Airing",temp[1].trim());
                                }
                                continue;
                            } else if(key.equals("Producers")) {
                                user_data.put(key,toArray(value));
                                continue;
                            } else if(key.equals("Studios")) {
                                user_data.put(key,toArray(value));
                                continue;
                            } else if(key.equals("Source")) {
                                if(value.equals("Unkwown")) value = "";
                            }
                            user_data.put(key,value);
                        }
                    }
                    for (Element e : staff) {
                        if(!e.text().equals("")) staffs.put(e.text());
                    }
                    user_data.put("Staffs",staffs);
                    writer.append(user_data.toString()+"\n");
                    br.close();
                }

            }
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}