package hand.framework.mybatis.criterion;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Criteria {
	  protected List<Criterion> criteria;

      protected Criteria() {
          super();
          criteria = new ArrayList<Criterion>();
      }

      public boolean isValid() {
          return criteria.size() > 0;
      }

      public List<Criterion> getAllCriteria() {
          return criteria;
      }

      public List<Criterion> getCriteria() {
          return criteria;
      }

      public void addCriterion(String condition) {
          if (condition == null) {
              throw new RuntimeException("Value for condition cannot be null");
          }
          criteria.add(new Criterion(condition));
      }

      public void addCriterion(String condition, Object value, String property) {
          if (value == null) {
              throw new RuntimeException("Value for " + property + " cannot be null");
          }
          criteria.add(new Criterion(condition, value));
      }

      public void addCriterion(String condition, Object value1, Object value2, String property) {
          if (value1 == null || value2 == null) {
              throw new RuntimeException("Between values for " + property + " cannot be null");
          }
          criteria.add(new Criterion(condition, value1, value2));
      }

      public void addCriterionForJDBCDate(String condition, Date value, String property) {
          if (value == null) {
              throw new RuntimeException("Value for " + property + " cannot be null");
          }
          addCriterion(condition, new java.sql.Date(value.getTime()), property);
      }

      public void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
          if (values == null || values.size() == 0) {
              throw new RuntimeException("Value list for " + property + " cannot be null or empty");
          }
          List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
          Iterator<Date> iter = values.iterator();
          while (iter.hasNext()) {
              dateList.add(new java.sql.Date(iter.next().getTime()));
          }
          addCriterion(condition, dateList, property);
      }

      public void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
          if (value1 == null || value2 == null) {
              throw new RuntimeException("Between values for " + property + " cannot be null");
          }
          addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
      }

	@Override
	public String toString() {
		return "Criteria [criteria=" + criteria + "]";
	}

}
