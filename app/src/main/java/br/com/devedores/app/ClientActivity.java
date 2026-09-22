package br.com.devedores.app;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientActivity extends Activity {
    DataStore ds; Models.Client c; LinearLayout root, docsBox, loansBox; String pendingFolder="Outros";

    @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(Ui.BG);getWindow().setNavigationBarColor(Ui.BG);ds=new DataStore(this);load();}
    void load(){c=ds.client(getIntent().getStringExtra("id"));if(c==null){finish();return;}build();}
    @Override protected void onResume(){super.onResume();if(ds!=null){ds.load();c=ds.client(getIntent().getStringExtra("id"));if(c!=null)build();}}
    String money(double x){return String.format(Locale.getDefault(),"R$ %.2f",x);}

    void build(){
        root=Ui.col(this);ScrollView sc=new ScrollView(this);sc.setFillViewport(true);sc.setVerticalScrollBarEnabled(false);sc.addView(root);setContentView(sc);

        LinearLayout top=Ui.row(this);
        Button back=Ui.btnDark(this,"‹  Voltar");back.setOnClickListener(v->finish());top.addView(back,new LinearLayout.LayoutParams(Ui.dp(this,88),Ui.dp(this,44)));
        LinearLayout meta=Ui.col(this);meta.setPadding(Ui.dp(this,10),0,0,0);meta.addView(Ui.title(this,c.name,22));meta.addView(Ui.label(this,"Perfil e pasta do cliente"));top.addView(meta,new LinearLayout.LayoutParams(0,Ui.dp(this,52),1));
        Button edit=Ui.btnDark(this,"Editar");edit.setOnClickListener(v->editClient());top.addView(edit,new LinearLayout.LayoutParams(Ui.dp(this,76),Ui.dp(this,44)));root.addView(top);Ui.gap(this,root,14);

        double paid=0,bal=0;int overdue=0;for(Models.Loan l:c.loans){paid+=l.paid();bal+=l.balance();for(int i=0;i<l.installments;i++)if(!l.installmentPaid(i)&&l.dueAt(i)<System.currentTimeMillis())overdue++;}
        LinearLayout hero=Ui.heroCard(this,overdue>0?Ui.RED:(bal>0?Ui.GOLD:Ui.GREEN));
        LinearLayout hr=Ui.row(this);TextView av=Ui.avatar(this,c.name);hr.addView(av,new LinearLayout.LayoutParams(Ui.dp(this,54),Ui.dp(this,54)));
        LinearLayout htxt=Ui.col(this);htxt.setPadding(Ui.dp(this,12),0,0,0);htxt.addView(Ui.eyebrow(this,overdue>0?"ATENÇÃO":bal>0?"CONTRATO EM ABERTO":"TUDO QUITADO"));htxt.addView(Ui.title(this,money(bal),27));htxt.addView(Ui.label(this,"saldo atual"));hr.addView(htxt,new LinearLayout.LayoutParams(0,Ui.dp(this,80),1));hero.addView(hr);
        LinearLayout hf=Ui.row(this);hf.addView(Ui.label(this,"Recebido: "+money(paid)),new LinearLayout.LayoutParams(0,Ui.dp(this,30),1));hf.addView(Ui.pill(this,c.loans.size()+" contrato(s)",Ui.BLUE,Ui.WHITE));hero.addView(hf);root.addView(hero);Ui.gap(this,root,12);

        LinearLayout quick=Ui.row(this);Button loan=Ui.btn(this,"+ Empréstimo");loan.setOnClickListener(v->{Intent i=new Intent(this,AddLoanActivity.class);i.putExtra("clientId",c.id);startActivity(i);});Button note=Ui.btnDark(this,"Anotações");note.setOnClickListener(v->editNote());quick.addView(loan,new LinearLayout.LayoutParams(0,Ui.dp(this,48),1));Ui.gap(this,quick,6);quick.addView(note,new LinearLayout.LayoutParams(0,Ui.dp(this,48),1));root.addView(quick);Ui.gap(this,root,16);

        root.addView(Ui.sectionTitle(this,"Dados do cliente"));Ui.gap(this,root,5);
        LinearLayout info1=Ui.row(this);info1.addView(Ui.infoTile(this,"CPF",c.cpf.isEmpty()?"Não informado":c.cpf,Ui.BLUE),new LinearLayout.LayoutParams(0,Ui.dp(this,70),1));Ui.gap(this,info1,6);info1.addView(Ui.infoTile(this,"RG",c.rg.isEmpty()?"Não informado":c.rg,Ui.PURPLE),new LinearLayout.LayoutParams(0,Ui.dp(this,70),1));root.addView(info1);Ui.gap(this,root,6);
        LinearLayout info2=Ui.row(this);info2.addView(Ui.infoTile(this,"TELEFONE",c.phone.isEmpty()?"Não informado":c.phone,Ui.GREEN),new LinearLayout.LayoutParams(0,Ui.dp(this,70),1));Ui.gap(this,info2,6);info2.addView(Ui.infoTile(this,"ARQUIVOS",c.documents.size()+" documento(s)",Ui.GOLD),new LinearLayout.LayoutParams(0,Ui.dp(this,70),1));root.addView(info2);Ui.gap(this,root,6);
        LinearLayout addr=Ui.card(this);addr.addView(Ui.eyebrow(this,"ENDEREÇO"));addr.addView(Ui.text(this,c.address.isEmpty()?"Não informado":c.address,14));root.addView(addr);Ui.gap(this,root,16);

        LinearLayout foldersHeader=Ui.sectionHeader(this,"Pastas e documentos","+ Pasta",v->createFolder());root.addView(foldersHeader);root.addView(Ui.label(this,"Cada pasta fica separada dentro do perfil do cliente."));Ui.gap(this,root,8);
        LinearLayout grid=new LinearLayout(this);grid.setOrientation(LinearLayout.VERTICAL);
        java.util.List<String> folders=new java.util.ArrayList<>(c.folders);if(folders.isEmpty())java.util.Collections.addAll(folders,"RG","CPF","Comprovante","Contrato","Fotos","Outros");
        for(int r=0;r<folders.size();r+=2){LinearLayout rr=Ui.row(this);for(int col=0;col<2;col++){int idx=r+col;if(idx>=folders.size()){rr.addView(new Space(this),new LinearLayout.LayoutParams(0,Ui.dp(this,78),1));break;}String folder=folders.get(idx);Button b=Ui.btnDark(this,folder+"\n"+countFolder(folder)+" arquivo(s)");b.setGravity(Gravity.CENTER_LEFT);b.setPadding(Ui.dp(this,14),0,Ui.dp(this,8),0);b.setOnClickListener(v->pickDoc(folder));rr.addView(b,new LinearLayout.LayoutParams(0,Ui.dp(this,78),1));if(col==0)Ui.gap(this,rr,6);}grid.addView(rr);Ui.gap(this,grid,6);}root.addView(grid);Ui.gap(this,root,14);

        root.addView(Ui.sectionHeader(this,"Arquivos recentes",null,null));Ui.gap(this,root,4);docsBox=Ui.col(this);docsBox.setPadding(0,0,0,0);root.addView(docsBox);renderDocs();Ui.gap(this,root,16);

        root.addView(Ui.sectionHeader(this,"Empréstimos","+ Novo",v->{Intent i=new Intent(this,AddLoanActivity.class);i.putExtra("clientId",c.id);startActivity(i);}));Ui.gap(this,root,4);loansBox=Ui.col(this);loansBox.setPadding(0,0,0,0);root.addView(loansBox);renderLoans();Ui.gap(this,root,16);

        root.addView(Ui.sectionTitle(this,"Anotações internas"));EditText notes=Ui.field(this,"Informações importantes sobre este cliente");notes.setText(c.notes);root.addView(notes,new LinearLayout.LayoutParams(-1,Ui.dp(this,115)));Ui.gap(this,root,7);Button sn=Ui.btn(this,"Salvar anotações");sn.setOnClickListener(v->{c.notes=notes.getText().toString();ds.save();Toast.makeText(this,"Anotações salvas",Toast.LENGTH_SHORT).show();});root.addView(sn);Ui.gap(this,root,16);
        Button del=Ui.btnDanger(this,"Excluir cliente e arquivos");del.setOnClickListener(v->confirmDelete());root.addView(del);
    }

    void createFolder(){EditText e=Ui.field(this,"Nome da pasta (ex.: Contratos 2026)");new AlertDialog.Builder(this).setTitle("Nova pasta").setView(e).setPositiveButton("Criar",(d,w)->{String n=e.getText().toString().trim();if(n.isEmpty())return;if(c.folders==null)c.folders=new java.util.ArrayList<>();for(String f:c.folders)if(f.equalsIgnoreCase(n)){Toast.makeText(this,"Já existe uma pasta com esse nome",Toast.LENGTH_SHORT).show();return;}c.folders.add(n);ds.save();build();}).setNegativeButton("Cancelar",null).show();}
    int countFolder(String folder){int n=0;for(Models.Document d:c.documents)if(folder.equals(d.folder))n++;return n;}
    void renderDocs(){docsBox.removeAllViews();if(c.documents.isEmpty()){LinearLayout empty=Ui.softCard(this,Ui.BLUE);empty.addView(Ui.label(this,"Nenhum documento adicionado ainda."));docsBox.addView(empty);return;}for(Models.Document d:c.documents){LinearLayout row=Ui.card(this);LinearLayout t=Ui.row(this);TextView av=Ui.iconBadge(this,"▣");t.addView(av,new LinearLayout.LayoutParams(Ui.dp(this,36),Ui.dp(this,36)));LinearLayout tx=Ui.col(this);tx.setPadding(Ui.dp(this,10),0,0,0);tx.addView(Ui.title(this,d.name,14));tx.addView(Ui.label(this,d.folder+"  •  "+new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault()).format(new Date(d.addedAt))));t.addView(tx,new LinearLayout.LayoutParams(0,Ui.dp(this,56),1));Button x=Ui.btnDanger(this,"Excluir");x.setOnClickListener(v->deleteDoc(d));row.addView(t,new LinearLayout.LayoutParams(0,Ui.dp(this,60),1));row.addView(x,new LinearLayout.LayoutParams(Ui.dp(this,78),Ui.dp(this,44)));row.setOnClickListener(v->openDoc(d));docsBox.addView(row);Ui.gap(this,docsBox,6);}}
    void renderLoans(){loansBox.removeAllViews();if(c.loans.isEmpty()){loansBox.addView(Ui.label(this,"Nenhum empréstimo cadastrado."));return;}for(Models.Loan l:c.loans){LinearLayout card=Ui.card(this);LinearLayout h=Ui.row(this);h.addView(Ui.title(this,l.title,16),new LinearLayout.LayoutParams(0,Ui.dp(this,32),1));h.addView(Ui.pill(this,l.balance()>0?"Em aberto":"Quitado",l.balance()>0?Ui.BLUE:Ui.GREEN,Ui.WHITE));card.addView(h);double pct=l.total<=0?0:(l.paid()/l.total*100.0);card.addView(Ui.label(this,"Recebido "+String.format(Locale.getDefault(),"%.0f",pct)+"%"));card.addView(Ui.progress(this,(int)pct,100),new LinearLayout.LayoutParams(-1,Ui.dp(this,7)));Ui.gap(this,card,5);card.addView(Ui.text(this,"Total "+money(l.total)+"  •  Pago "+money(l.paid())+"\nSaldo "+money(l.balance())+"  •  "+l.installments+" parcelas "+l.frequency,13));card.setOnClickListener(v->{Intent i=new Intent(this,LoanActivity.class);i.putExtra("clientId",c.id);i.putExtra("loanId",l.id);startActivity(i);});loansBox.addView(card);Ui.gap(this,loansBox,7);}}
    void pickDoc(String folder){pendingFolder=folder;Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("*/*");i.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"image/*","application/pdf","text/*","application/octet-stream"});i.addCategory(Intent.CATEGORY_OPENABLE);startActivityForResult(i,44);}
    @Override protected void onActivityResult(int r,int res,Intent data){super.onActivityResult(r,res,data);if(r==44&&res==RESULT_OK&&data!=null&&data.getData()!=null){try{Uri u=data.getData();String path=DocumentManager.copyToClient(this,c.id,pendingFolder,u);Models.Document d=new Models.Document();d.folder=pendingFolder;d.name=DocumentManager.displayName(getContentResolver(),u);if(d.name==null)d.name="Documento";d.uriOrPath=path;d.mime=getContentResolver().getType(u);if(d.mime==null)d.mime="application/octet-stream";c.documents.add(d);ds.save();build();Toast.makeText(this,"Arquivo salvo em "+pendingFolder,Toast.LENGTH_SHORT).show();}catch(Exception e){Toast.makeText(this,"Não foi possível salvar o arquivo",Toast.LENGTH_LONG).show();}}}
    void openDoc(Models.Document d){try{Intent i=new Intent(Intent.ACTION_VIEW);Uri u=d.uriOrPath.startsWith("content://")?Uri.parse(d.uriOrPath):DocumentManager.uriForFile(this,d.uriOrPath);i.setDataAndType(u,d.mime);i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);startActivity(i);}catch(Exception e){Toast.makeText(this,"Nenhum aplicativo pode abrir esse arquivo",Toast.LENGTH_SHORT).show();}}
    void deleteDoc(Models.Document d){new AlertDialog.Builder(this).setTitle("Excluir arquivo?").setMessage(d.name).setPositiveButton("Excluir",(a,w)->{DocumentManager.delete(d.uriOrPath);c.documents.remove(d);ds.save();build();}).setNegativeButton("Cancelar",null).show();}
    void editNote(){EditText e=Ui.field(this,"Anotações");e.setText(c.notes);new AlertDialog.Builder(this).setTitle("Anotações do cliente").setView(e).setPositiveButton("Salvar",(d,w)->{c.notes=e.getText().toString();ds.save();build();}).setNegativeButton("Cancelar",null).show();}
    void editClient(){LinearLayout p=Ui.col(this);p.setPadding(0,0,0,0);EditText n=Ui.field(this,"Nome");n.setText(c.name);EditText cp=Ui.field(this,"CPF");cp.setText(c.cpf);EditText rg=Ui.field(this,"RG");rg.setText(c.rg);EditText ph=Ui.field(this,"Telefone");ph.setText(c.phone);EditText ad=Ui.field(this,"Endereço");ad.setText(c.address);p.addView(n);Ui.gap(this,p,6);p.addView(cp);Ui.gap(this,p,6);p.addView(rg);Ui.gap(this,p,6);p.addView(ph);Ui.gap(this,p,6);p.addView(ad);new AlertDialog.Builder(this).setTitle("Editar cliente").setView(p).setPositiveButton("Salvar",(d,w)->{c.name=n.getText().toString();c.cpf=cp.getText().toString();c.rg=rg.getText().toString();c.phone=ph.getText().toString();c.address=ad.getText().toString();ds.save();build();}).setNegativeButton("Cancelar",null).show();}
    void confirmDelete(){new AlertDialog.Builder(this).setTitle("Excluir cliente?").setMessage("O cliente, empréstimos e arquivos serão apagados deste aparelho.").setPositiveButton("Excluir",(d,w)->{for(Models.Loan l:c.loans)AlarmScheduler.cancelLoanAlarms(this,l.id,l.installments);for(Models.Document doc:c.documents)DocumentManager.delete(doc.uriOrPath);ds.removeClient(c.id);finish();}).setNegativeButton("Cancelar",null).show();}
}
