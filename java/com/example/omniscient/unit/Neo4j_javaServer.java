package com.example.omniscient.unit;

import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Neo4j_javaServer{
    private final String URL="http://localhost:8081";
    public Neo4j_javaServer(){

    }
    @NonNull
    public String TEST(String s) throws IOException {
        Connection.Response res= Jsoup.connect(URL+"/Test")
                .data("test",s)
                .execute();
        if (res.statusCode()== HttpURLConnection.HTTP_OK) {
            return res.body();
        } else return "err";
    }
    @NonNull
    public boolean isSearched(@NonNull String str) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect(URL+"/Word/hasWord")
                .data("word",str)
                .timeout(3000)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            return Boolean.parseBoolean(res.body());
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Map<String,Object> getResult(@NonNull String str,Boolean includeIsSearch) throws IOException, JSONException {
        Map<String,Object> info=new HashMap<>();
        if(includeIsSearch){
            Connection.Response res= Jsoup.connect(URL+"/Word/hasWord")
                    .data("word",str)
                    .timeout(3000)
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            if(res.statusCode()==HttpURLConnection.HTTP_OK){
                info.put("InDataBase",Boolean.parseBoolean(res.body()));
            }else throw new IOException("网络错误");
        }
        Connection.Response res= Jsoup.connect(URL+"/Word/GetInfo")
                .data("word",str)
                .timeout(3000)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            List<String> temp=JSONArrayToList(object.getJSONArray("attr"));
            info.put("attr_list",temp);
            for (String s:temp){
                Map<String,Object> mp=new HashMap<>();
                mp.put("value",object.getJSONObject("info").get(s).toString());
                mp.put("relev",JSONArrayToList(object.getJSONObject("relev").getJSONArray(s)));
                info.put(s,mp);
            }
            return info;
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Map<String,Object> login(@NonNull String account,@NonNull String password) throws IOException, JSONException{
        Connection.Response res= Jsoup.connect(URL+"/User/Login")
                .header("Content-Type", "application/json")
                .requestBody("{\"id\":\"\",\"account\":\""+account+"\",\"username\":\"\",\"password\":\""+password+"\",\"haves\":[],\"pass\":\"\"}")
                .ignoreContentType(true)
                .timeout(3000)
                .method(Connection.Method.POST)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            System.out.println(object);
            Map<String,Object> info=new HashMap<>();
            Boolean temp=object.getBoolean("pass");
            info.put("pass",temp);
            if(temp){
                info.put("account",object.getString("account"));
                info.put("username",object.getString("username"));
                info.put("password",password);
            }
            return info;
        }
        throw new IOException("网络错误");
    }
    @NonNull
    public Map<String,Object> register(@NonNull String account,@NonNull String password,@NonNull String username) throws IOException, JSONException{
        Connection.Response res= Jsoup.connect(URL+"/User/Registered")
                .header("Content-Type", "application/json")
                .requestBody("{\"id\":\"\",\"account\":\""+account+"\",\"username\":\""+username+"\",\"password\":\""+password+"\",\"haves\":[],\"pass\":\"\"}")
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            Map<String,Object> info=new HashMap<>();
            boolean temp=object.getBoolean("hasError");
            info.put("res",!temp);
            if(temp){
                info.put("info",object.getString("data"));
            }
            return info;
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean setUserInfo(@NonNull String account,@NonNull Map<String,String> info) throws IOException, JSONException {
        Connection conn=Jsoup.connect(URL+"/User/setInfo");
        String requesetBody="{\"id\":\"\"," +
                "\"account\":\""+account+"\"," +
                "\"username\":\""+(info.get("username")!=null?info.get("username"):"")+"\"," +
                "\"password\":\""+(info.get("password")!=null?info.get("password"):"")+"\"," +
                "\"haves\":[]," +
                "\"pass\":\"\"}";
        //System.out.println(requesetBody);
        Connection.Response res= conn
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.PUT)
                .requestBody(requesetBody)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            //System.out.println(res.body());
            return Boolean.parseBoolean(res.body());
        }
        throw new IOException("网络错误");
    }


    @NonNull
    public List<String> getFavoriteFold(@NonNull String account) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect(URL+"/Favorite/AllFold")
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .data("account",account)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONArray t=new JSONArray(res.body());
            return JSONArrayToList(t,account.length());
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public List<String> getWordInFavoriteFold(@NonNull String account, @NonNull String foldName) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect(URL+"/Favorite/AllWord")
                .data("account",account)
                .data("foldName",foldName)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONArray object=new JSONArray(res.body());
            return JSONArrayToList(
                    object
            );
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean OperateWordInFavorite(@NonNull String account, @NonNull String foldName,@NonNull String op,@NonNull String word) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect(URL+"/Favorite/OperateWord")
                .data("account",account)
                .data("foldName",foldName)
                .data("do",op)
                .data("word",word)
                //.header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.PUT)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            return Boolean.parseBoolean(res.body());
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean delFavorite(@NonNull String account,@NonNull String foldName) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect(URL+"/Favorite/Del")
                .data("account",account)
                .data("foldName",foldName)
                //.header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .timeout(3000)
                .execute();
        return res.statusCode()==HttpURLConnection.HTTP_NO_CONTENT;
    }

    @NonNull
    public Boolean addFavorite(@NonNull String account,@NonNull String foldName) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect(URL+"/Favorite/CreateFold")
                .data("account",account)
                .data("foldName",foldName)
                //.header("Content-Type", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return Objects.equals(object.getString("name"), account + foldName);
        }
        throw new IOException("网络错误");
    }

    private List<String> attrs=new ArrayList<>();
    private List<String> values=new ArrayList<>();
    @NonNull
    public Boolean addWordInfo(@NonNull String finish,@NonNull String word,@NonNull String attr,@NonNull String info) throws IOException, JSONException {
        attrs.add(attr);
        values.add(info);
        if(finish.equals("0")){
            return true;
        }
        else if(finish.equals("1")){
            String requestBody="{\"id\":\"\"," +
                    "\"name\":\""+word+"\"," +
                    "\"attr\":"+ListTOJSONArray(attrs)+"," +
                    "\"value\":"+ListTOJSONArray(values)+"," +
                    "\"relevs\":[]}";
            //System.out.println(requestBody);
            Connection.Response res= Jsoup.connect(URL+"/Word/Create")
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .requestBody(requestBody)
                    .timeout(3000)
                    .execute();
            if(res.statusCode()==HttpURLConnection.HTTP_OK){
                attrs.clear();
                values.clear();
                return true;
            }
            return false;
        }
        throw new IOException("网络错误");
    }

    private Map<String,List<String>> relevs=new HashMap<>();
    @NonNull
    public Boolean addWordRel(@NonNull String finish,@NonNull String word,@NonNull String attr,@NonNull String relev) throws IOException, JSONException {
        relevs.put(attr, List.of(relev.split("_")));
        if(finish.equals("0")){
            return true;
        }else if(finish.equals("1")){
            String requestBody= "{\"word\":\""+word+"\"," +
                    "\"relev\":"+MapToJSONObject(relevs)+"}";
            System.out.println(requestBody);
            Connection.Response res= Jsoup.connect(URL+"/Word/CreateRel")
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .requestBody(requestBody)
                    .timeout(3000)
                    .execute();
            if(res.statusCode()==HttpURLConnection.HTTP_CREATED){
                relevs.clear();
                return true;
            }
            return false;
        }
        throw new IOException("网络错误");
    }
    private String MapToJSONObject(Map<String,List<String>> mp){
        StringBuilder res=new StringBuilder("{");
        boolean flag=true;
        for(Map.Entry<String,List<String>> entry:mp.entrySet()){
            String key="\""+entry.getKey()+"\"";
            String value=ListTOJSONArray(entry.getValue());
            if(flag) {
                flag=false;
                res.append(key).append(":").append(value);
            }else res.append(",").append(key).append(":").append(value);
        }
        res.append("}");
        return String.valueOf(res);
    }
    private List<String> JSONArrayToList(JSONArray j){
        return JSONArrayToList(j,0);
    }
    private List<String> JSONArrayToList(JSONArray j,int k){
        int num=1+k;
        String s1=j.toString();
        s1=s1.substring(1,s1.length()-1);
        List<String> result = new ArrayList<>();
        for(String s:s1.split(",")){
            if(s.length()>=1) result.add(s.substring(num,s.length()-1));
        }
        return result;
    }
    private String ListTOJSONArray(List<String> ls){
        StringBuilder res= new StringBuilder("[");
        for(int i=0;i<ls.size();i++){
            if(i==0) res.append("\"").append(ls.get(i)).append("\"");
            else res.append(",\"").append(ls.get(i)).append("\"");
        }
        res.append("]");
        return String.valueOf(res);
    }

}
