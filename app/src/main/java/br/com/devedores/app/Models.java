package br.com.devedores.app;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Models {
    public static class Client {
        public String id=UUID.randomUUID().toString(), name="", cpf="", phone="", address="", notes="";
        public List<String> docs=new ArrayList<>();
        public List<Loan> loans=new ArrayList<>();
        public JSONObject toJson(){ try{JSONObject o=new JSONObject(); o.put("id",id);o.put("name",name);o.put("cpf",cpf);o.put("phone",phone);o.put("address",address);o.put("notes",notes); JSONArray d=new JSONArray(); for(String s:docs)d.put(s);o.put("docs",d);JSONArray l=new JSONArray();for(Loan x:loans)l.put(x.toJson());o.put("loans",l);return o;}catch(Exception e){return new JSONObject();}}
        public static Client fromJson(JSONObject o){Client c=new Client(); try{c.id=o.optString("id",c.id);c.name=o.optString("name");c.cpf=o.optString("cpf");c.phone=o.optString("phone");c.address=o.optString("address");c.notes=o.optString("notes"); JSONArray d=o.optJSONArray("docs");if(d!=null)for(int i=0;i<d.length();i++)c.docs.add(d.optString(i));JSONArray l=o.optJSONArray("loans");if(l!=null)for(int i=0;i<l.length();i++)c.loans.add(Loan.fromJson(l.getJSONObject(i)));}catch(Exception ignored){}return c;}
    }
    public static class Loan {
        public String id=UUID.randomUUID().toString(), title="Empréstimo";
        public double principal, interestPercent, interestValue, total, installmentAmount;
        public String frequency="Mensal"; public int installments=1; public long firstDue;
        public List<Payment> payments=new ArrayList<>();
        public double paid(){double s=0;for(Payment p:payments)s+=p.amount;return s;}
        public double balance(){return Math.max(0,total-paid());}
        public long dueAt(int n){ java.util.Calendar c=java.util.Calendar.getInstance();c.setTimeInMillis(firstDue);if("Semanal".equals(frequency))c.add(java.util.Calendar.DAY_OF_YEAR,7*n);else if("Quinzenal".equals(frequency))c.add(java.util.Calendar.DAY_OF_YEAR,14*n);else c.add(java.util.Calendar.MONTH,n);return c.getTimeInMillis();}
        public double paidForInstallment(int n){double before=0, all=paid();for(int i=0;i<n;i++)before+=installmentAmount;return Math.min(Math.max(0,all-before),installmentAmount);}
        public boolean installmentPaid(int n){return paidForInstallment(n)+0.005>=installmentAmount;}
        public JSONObject toJson(){try{JSONObject o=new JSONObject();o.put("id",id);o.put("title",title);o.put("principal",principal);o.put("interestPercent",interestPercent);o.put("interestValue",interestValue);o.put("total",total);o.put("installmentAmount",installmentAmount);o.put("frequency",frequency);o.put("installments",installments);o.put("firstDue",firstDue);JSONArray p=new JSONArray();for(Payment x:payments)p.put(x.toJson());o.put("payments",p);return o;}catch(Exception e){return new JSONObject();}}
        public static Loan fromJson(JSONObject o){Loan l=new Loan();try{l.id=o.optString("id",l.id);l.title=o.optString("title","Empréstimo");l.principal=o.optDouble("principal");l.interestPercent=o.optDouble("interestPercent");l.interestValue=o.optDouble("interestValue");l.total=o.optDouble("total");l.installmentAmount=o.optDouble("installmentAmount");l.frequency=o.optString("frequency","Mensal");l.installments=o.optInt("installments",1);l.firstDue=o.optLong("firstDue");JSONArray a=o.optJSONArray("payments");if(a!=null)for(int i=0;i<a.length();i++)l.payments.add(Payment.fromJson(a.getJSONObject(i)));}catch(Exception ignored){}return l;}
    }
    public static class Payment { public String id=UUID.randomUUID().toString(); public double amount; public long date; JSONObject toJson(){try{JSONObject o=new JSONObject();o.put("id",id);o.put("amount",amount);o.put("date",date);return o;}catch(Exception e){return new JSONObject();}} static Payment fromJson(JSONObject o){Payment p=new Payment();p.id=o.optString("id",p.id);p.amount=o.optDouble("amount");p.date=o.optLong("date");return p;} }
    public static class Reminder {public String id=UUID.randomUUID().toString(), title="Lembrete", details="";public long when; public JSONObject toJson(){try{JSONObject o=new JSONObject();o.put("id",id);o.put("title",title);o.put("details",details);o.put("when",when);return o;}catch(Exception e){return new JSONObject();}}static Reminder fromJson(JSONObject o){Reminder r=new Reminder();r.id=o.optString("id",r.id);r.title=o.optString("title","Lembrete");r.details=o.optString("details");r.when=o.optLong("when");return r;}}
}
