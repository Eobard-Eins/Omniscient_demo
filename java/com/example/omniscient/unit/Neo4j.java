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

public final class Neo4j{
    public Neo4j(){

    }
    @NonNull
    public String TEST(String s) throws IOException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Test")
                .data("test",s)
                .execute();
        if (res.statusCode()== HttpURLConnection.HTTP_OK) {
            return res.body();
        } else return "err";
    }
    @NonNull
    public boolean isSearched(@NonNull String str) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Word/hasWord")
                .data("word",str)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return object.getBoolean("InDataBase");
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Map<String,Object> getResult(@NonNull String str,Boolean includeIsSearch) throws IOException, JSONException {
        Map<String,Object> info=new HashMap<>();
        if(includeIsSearch){
            Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Word/hasWord")
                    .data("word",str)
                    .timeout(3000)
                    .execute();
            if(res.statusCode()==HttpURLConnection.HTTP_OK){
                JSONObject object = new JSONObject(res.body());
                info.put("InDataBase",object.getBoolean("InDataBase"));
            }else throw new IOException("网络错误");
        }
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Word/GetInfo")
                .data("word",str)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body()).getJSONObject("info");
            List<String> temp=JSONArrayToList(object.getJSONArray("attr_list"));
            info.put("attr_list",temp);
            for (String s:temp){
                Map<String,Object> mp=new HashMap<>();
                mp.put("value",object.getJSONObject(s).get("value").toString());
                mp.put("relev",JSONArrayToList(object.getJSONObject(s).getJSONArray("relev")));
                info.put(s,mp);
            }
            return info;
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Map<String,Object> login(@NonNull String account,@NonNull String password) throws IOException, JSONException{
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/User/Login")
                .data("account",account)
                .data("pw",password)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
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
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/User/Registered")
                .data("account",account)
                .data("pw",password)
                .data("username",username)
                .data("isAdmin","0")
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            Map<String,Object> info=new HashMap<>();
            Boolean temp=object.getBoolean("res");
            info.put("res",temp);
            if(!temp){
                info.put("info",object.getString("info"));
            }
            return info;
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean setUserInfo(@NonNull String account,@NonNull Map<String,String> info) throws IOException, JSONException {
        Connection conn=Jsoup.connect("http://39.101.66.194:8888/User/setInfo");
        conn.data("account",account);
        if(info.get("password") != null) conn.data("pw",info.get("password"));
        if(info.get("username") != null) conn.data("username",info.get("username"));
        Connection.Response res= conn.timeout(3000).execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return object.getBoolean("res");
        }
        throw new IOException("网络错误");
    }


    @NonNull
    public List<String> getFavoriteFold(@NonNull String account) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Favorite/AllFold")
                .data("account",account)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return JSONArrayToList(
                    object.getJSONArray("favorite"),account.length()
            );
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public List<String> getWordInFavoriteFold(@NonNull String account, @NonNull String foldName) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Favorite/AllWord")
                .data("account",account)
                .data("foldName",foldName)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return JSONArrayToList(
                    object.getJSONArray("include")
            );
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean OperateWordInFavorite(@NonNull String account, @NonNull String foldName,@NonNull String op,@NonNull String word) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Favorite/OperateWord")
                .data("account",account)
                .data("foldName",foldName)
                .data("do",op)
                .data("word",word)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return object.getBoolean("res");
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean delFavorite(@NonNull String account,@NonNull String foldName) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Favorite/Del")
                .data("account",account)
                .data("foldName",foldName)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return object.getBoolean("res");
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean addFavorite(@NonNull String account,@NonNull String foldName) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Favorite/CreateFold")
                .data("account",account)
                .data("foldName",foldName)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return object.getBoolean("res");
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean addWordInfo(@NonNull String finish,@NonNull String word,@NonNull String attr,@NonNull String info) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Word/Create")
                .data("finish",finish)
                .data("word",word)
                .data("attr",attr)
                .data("info",info)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return object.getBoolean("res");
        }
        throw new IOException("网络错误");
    }

    @NonNull
    public Boolean addWordRel(@NonNull String finish,@NonNull String word,@NonNull String attr,@NonNull String relev) throws IOException, JSONException {
        Connection.Response res= Jsoup.connect("http://39.101.66.194:8888/Word/CreateRel")
                .data("finish",finish)
                .data("word",word)
                .data("attr",attr)
                .data("relev",relev)
                .timeout(3000)
                .execute();
        if(res.statusCode()==HttpURLConnection.HTTP_OK){
            JSONObject object = new JSONObject(res.body());
            return object.getBoolean("res");
        }
        throw new IOException("网络错误");
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
}
