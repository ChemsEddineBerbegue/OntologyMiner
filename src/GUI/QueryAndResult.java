package GUI;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NsIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
//import org.apache.jena.atlas.io.IndentedWriter;


public class  QueryAndResult {
      Model m;
      String path;
      String	queryString ;
      Query query;
      OntModelSpec[] t={	OntModelSpec.OWL_DL_MEM,OntModelSpec.OWL_DL_MEM_RDFS_INF,OntModelSpec.OWL_DL_MEM_RULE_INF,
    			OntModelSpec.OWL_DL_MEM_TRANS_INF,OntModelSpec.OWL_LITE_MEM,
    			OntModelSpec.OWL_LITE_MEM_RDFS_INF,OntModelSpec.OWL_LITE_MEM_RULES_INF,OntModelSpec.OWL_LITE_MEM_TRANS_INF,
    			OntModelSpec.OWL_MEM,OntModelSpec.OWL_MEM_MICRO_RULE_INF,OntModelSpec.OWL_MEM_MINI_RULE_INF,
    			OntModelSpec.OWL_MEM_RDFS_INF,OntModelSpec.OWL_MEM_RULE_INF,OntModelSpec.OWL_MEM_TRANS_INF,
    			OntModelSpec.RDFS_MEM,OntModelSpec.RDFS_MEM_RDFS_INF,OntModelSpec.RDFS_MEM_TRANS_INF,
    			PelletReasonerFactory.THE_SPEC};

ArrayList<Model> lm=new ArrayList<Model>();	
int indice=0;
	
public QueryAndResult(int i) {
		m =ModelFactory.createOntologyModel(t[i]);
		
}

	public void setreq(String  s){queryString=s;}
    public void setpaths(File[] s){
   for(int i=0;i<s.length;i++){ 
	path =s[i].getPath().toLowerCase();
    path=  path.replace("\\", "/");System.out.println(path);
    System.out.println("parth = "+path);
	FileManager.get().readModel( m,path); 
   }
    lm.add(m);
    }	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public boolean exuteq(JTable jt){
		query = QueryFactory.create(queryString) ; 
		boolean b=false;
        QueryExecution qexec = QueryExecutionFactory.create(query, lm.get(this.indice)) ;
		
			ResultSet rs= qexec.execSelect();
			
			// Affichage des resultats 
			if(rs.hasNext()){System.out.println("not vide");b=true;
			 QuerySolution rb = rs.nextSolution() ; 
			 DefaultTableModel  tm=(DefaultTableModel)jt.getModel();
             tm.setColumnCount(0);
             tm.setRowCount(0);
			 ArrayList<String> ls=new ArrayList<String>();			
			 java.util.Iterator<String> i1 = rb.varNames(); 
			 String s;
			 while(i1.hasNext()){s=i1.next();tm.addColumn(s);ls.add(s);}
			for (int i=0 ; rb!=null ;i++ ){ 
			 tm.addRow(new Object[]{""});		  	
			 for(int j=0;j<ls.size();j++)
			 tm.setValueAt(rb.get(ls.get(j)), i, j);   
		if(rs.hasNext())rb = rs.nextSolution();else rb=null;   	
		}  
			} else System.out.println("vide ya chamsou");
			
		 qexec.close() ; return b;
		 }
//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean exask(){
		
		query = QueryFactory.create(queryString) ; 
        QueryExecution qexec = QueryExecutionFactory.create(query, lm.get(this.indice)) ;
        return  qexec.execAsk();
		
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public boolean exconst(){
		
		query = QueryFactory.create(queryString) ; 
        QueryExecution qexec = QueryExecutionFactory.create(query, lm.get(this.indice)) ;
        Model mm= qexec.execConstruct();
        lm.add(mm);
        if (mm!= null) return true;else return false;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public boolean exdes(){
	
	query = QueryFactory.create(queryString) ; 
    QueryExecution qexec = QueryExecutionFactory.create(query, lm.get(this.indice)) ;
    Model mm= qexec.execDescribe();
    lm.add(mm);
	if (mm!= null) return true;else return false;
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void remm(int i){
	lm.remove(i);
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void setmi(int i){
	this.indice=i;
	}
	
	public void getclasses(JTable jt,int j){
		DefaultTableModel  tm=(DefaultTableModel)jt.getModel();
		tm.setColumnCount(0);tm.setRowCount(0);
		ExtendedIterator classes=null;String className=null;NsIterator ns;
		switch(j){
		case 0:{tm.addColumn("Classes");
		         classes = ((OntModel) m).listClasses();
		        
		         break;}
		case 1:{tm.addColumn("propertys");
       classes = ((OntModel) m).listDatatypeProperties();
     
       break;}
		case 2:{tm.addColumn("Instances");
       classes = ((OntModel) m).listIndividuals();
       break;}
		case 3:{
			tm.addColumn("Associations");
			classes = ((OntModel) m).listObjectProperties();
		break;}
		   
		case 4:{tm.addColumn("Sypetriques propertys");
			classes = ((OntModel) m).listSymmetricProperties();
		break;}	
		case 5:{tm.addColumn("Transiticves transitive");
			classes = ((OntModel) m).listTransitiveProperties();
		break;}
		case 6:{tm.addColumn("ontology name space");
		classes = ((OntModel) m).listOntologies();
	      break;}
		case 7:{tm.addColumn("name spaces");
			ns =((OntModel) m).listNameSpaces();
			int ii=0;
		    while(ns.hasNext()){ 
			tm.addRow(new Object[]{""}); 
		    tm.setValueAt(ns.next(), ii, 0);
		    ii++;
			 }
			break;
		}
		}
	     int i=0;
	if(classes!=null)while (classes.hasNext()){
			 tm.addRow(new Object[]{""});	
			
			      Resource obj = (Resource) classes.next();
			           
			            className =obj.getLocalName();
			  
			  tm.setValueAt(className, i, 0);
	     i++; 
		 
		}
		
	}
		
		
	}
	
	
	
	
    
		
		
	


