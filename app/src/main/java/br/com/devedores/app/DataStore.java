package br.com.devedores.app;
import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;import org.json.JSONObject;import java.util.*;
public class DataStore {
 private static final String P="devedores_db"; private final SharedPreferences sp; public List<Models.Client> clients=new ArrayList<>(); public List<Models.Reminder> reminders=new ArrayList<>();
 public DataStore(Context c){sp=c.getSharedPreferences(P,Context.MODE_PRIVATE);load();}
 public void load(){clients.clear();reminders.clear();try{JSONArray a=new JSONArray(sp.getString("clients","[]"));for(int i=0;i<a.length();i++)clients.add(Models.Client.fromJson(a.getJSONObject(i)));JSONArray r=new JSONArray(sp.getString("reminders","[]"));for(int i=0;i<r.length();i++)reminders.add(Models.Reminder.fromJson(r.getJSONObject(i)));}catch(Exception ignored){}}
 public void save(){try{JSONArray a=new JSONArray();for(Models.Client c:clients)a.put(c.toJson());JSONArray r=new JSONArray();for(Models.Reminder x:reminders)r.put(x.toJson());sp.edit().putString("clients",a.toString()).putString("reminders",r.toString()).apply();}catch(Exception ignored){}}
 public Models.Client client(String id){for(Models.Client c:clients)if(c.id.equals(id))return c;return null;}
 public Models.Loan loan(String id){for(Models.Client c:clients)for(Models.Loan l:c.loans)if(l.id.equals(id))return l;return null;}
}
