package core;

import java.util.ArrayList;

import repositoryhandler.Commit;
import repositoryhandler.CommitFilter;
import repositoryhandler.GitRepositoryHandler;

public class Maestro {
	
	WorkingSet ws;

	public Maestro() {
		this.ws = new WorkingSet();		
	}
	
	public void makeMusic(){
		GitRepositoryHandler handler = this.ws.getRepoHandler();
		
		ArrayList<Commit> commits = new ArrayList<Commit>();
		
		for(CommitFilter filtro : ws.getFilters()){
			commit			
		}
		
		handler.getRevisions(filter)   handler.getAllRevisions(true);
	}
}
