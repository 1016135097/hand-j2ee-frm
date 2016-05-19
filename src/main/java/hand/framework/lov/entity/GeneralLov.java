package hand.framework.lov.entity;

import hand.framework.entity.PagingEntity;

import java.util.ArrayList;
import java.util.List;

public class GeneralLov {
	
	private String lovId;
	
	private String sql;

	private GeneraLovConfig config;
	
	private String oldWhereClause;

	private String limitClause;

	private String nextLimitCluse;

	private List<String> params;

	private String queryWhereClause;
	
	private int start;
	
	private int end;
	
	private String moFlag;
	
	private boolean isPaging =false;
	
	private PagingEntity queryResult;
	

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

    
	public GeneraLovConfig getConfig() {
		return config;
	}

	public void setConfig(GeneraLovConfig config) {
		this.config = config;
	}

	public String getOldWhereClause() {
		return oldWhereClause;
	}

	public void setOldWhereClause(String oldWhereClause) {
		this.oldWhereClause = oldWhereClause;
	}

	public String getLimitClause() {
		return limitClause;
	}

	public void setLimitClause(String limitClause) {
		this.limitClause = limitClause;
	}

	public String getNextLimitCluse() {
		return nextLimitCluse;
	}

	public void setNextLimitCluse(String nextLimitCluse) {
		this.nextLimitCluse = nextLimitCluse;
	}

	public List<String> getParams() {
		return params;
	}
	
	public void setParams(List<String> params) {
		this.params = params;
	}
	
	public void addParams(String params) {
		if(this.params == null){
			this.params = new ArrayList<String>();
		}
		this.params.add(params);
	}

	public String getLovId() {
		return lovId;
	}

	public void setLovId(String lovId) {
		this.lovId = lovId;
	}


	public String getQueryWhereClause() {
		return queryWhereClause;
	}

	public void setQueryWhereClause(String queryWhereClause) {
		if(queryWhereClause == null){
			queryWhereClause = "";
		}
		this.queryWhereClause = queryWhereClause;
	}

    public void addQueryCondition(String search){
    	if(queryWhereClause == null){
    		queryWhereClause = "";
    	}
    	
    	queryWhereClause += " AND "+search+" like ? ";
    	
    }


	public PagingEntity getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(PagingEntity queryResult) {
		this.queryResult = queryResult;
	}

	public GeneralLov(String lovId,
					  String sql,
					  String moFlag,
					  String[] queryCondition,
					  String[] queryConditionDesc,
					  String[] resultDisplay,
					  String[] resultDisplayDesc,
					  String[] vReturn) {
		super();
		this.config = new GeneraLovConfig();
		this.queryResult = new PagingEntity();
		this.lovId = lovId;
		this.sql = sql;
		this.moFlag = moFlag;
		config.setQueryCondition(queryCondition);
		config.setQueryConditionDesc(queryConditionDesc);
		config.setResultDisplay(resultDisplay);
		config.setResultDisplayDesc(resultDisplayDesc);
		config.setvReturn(vReturn);
		this.queryWhereClause = "";

		String c = sql.toLowerCase().split("from")[0].split("select")[1].trim();

		String[] cs = c.split(",");
		String[] column = new String[cs.length];
		String[] alias = new String[cs.length];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = cs[i].trim();
//			System.out.println(cs[i] + " cs[i]");
			String[] sp = null;
			if (cs[i].contains(" as ")) {
				sp = cs[i].split(" as ");
			} else {
				sp = cs[i].split(" ");
			}
			column[i] = sp[0].trim();
			if (sp[sp.length - 1] == null
					|| "".equals(sp[sp.length - 1].trim())) {

				alias[i] = column[i];
			} else {
				alias[i] = sp[sp.length - 1].trim();
			}
		}
		
		config.setAlias(alias);
		config.setColumn(column);
	}
	
	public void setPage(int start,int limit){
		this.start = start + 1;
		this.end = start + limit;
		this.isPaging = true;
	}
	
	public boolean isPaging(){
		return this.isPaging;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getMoFlag() {
		return moFlag;
	}

	public void setMoFlag(String moFlag) {
		this.moFlag = moFlag;
	}
}
