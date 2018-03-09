package org.opensrp.domain.postgres;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportMetadataExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public ReportMetadataExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
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

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andIdIsNull() {
			addCriterion("id is null");
			return (Criteria) this;
		}

		public Criteria andIdIsNotNull() {
			addCriterion("id is not null");
			return (Criteria) this;
		}

		public Criteria andIdEqualTo(Long value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(Long value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(Long value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(Long value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(Long value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(Long value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<Long> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<Long> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(Long value1, Long value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(Long value1, Long value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andReportIdIsNull() {
			addCriterion("report_id is null");
			return (Criteria) this;
		}

		public Criteria andReportIdIsNotNull() {
			addCriterion("report_id is not null");
			return (Criteria) this;
		}

		public Criteria andReportIdEqualTo(Long value) {
			addCriterion("report_id =", value, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdNotEqualTo(Long value) {
			addCriterion("report_id <>", value, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdGreaterThan(Long value) {
			addCriterion("report_id >", value, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdGreaterThanOrEqualTo(Long value) {
			addCriterion("report_id >=", value, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdLessThan(Long value) {
			addCriterion("report_id <", value, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdLessThanOrEqualTo(Long value) {
			addCriterion("report_id <=", value, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdIn(List<Long> values) {
			addCriterion("report_id in", values, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdNotIn(List<Long> values) {
			addCriterion("report_id not in", values, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdBetween(Long value1, Long value2) {
			addCriterion("report_id between", value1, value2, "reportId");
			return (Criteria) this;
		}

		public Criteria andReportIdNotBetween(Long value1, Long value2) {
			addCriterion("report_id not between", value1, value2, "reportId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdIsNull() {
			addCriterion("base_entity_id is null");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdIsNotNull() {
			addCriterion("base_entity_id is not null");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdEqualTo(String value) {
			addCriterion("base_entity_id =", value, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdNotEqualTo(String value) {
			addCriterion("base_entity_id <>", value, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdGreaterThan(String value) {
			addCriterion("base_entity_id >", value, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdGreaterThanOrEqualTo(String value) {
			addCriterion("base_entity_id >=", value, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdLessThan(String value) {
			addCriterion("base_entity_id <", value, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdLessThanOrEqualTo(String value) {
			addCriterion("base_entity_id <=", value, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdLike(String value) {
			addCriterion("base_entity_id like", value, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdNotLike(String value) {
			addCriterion("base_entity_id not like", value, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdIn(List<String> values) {
			addCriterion("base_entity_id in", values, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdNotIn(List<String> values) {
			addCriterion("base_entity_id not in", values, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdBetween(String value1, String value2) {
			addCriterion("base_entity_id between", value1, value2, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andBaseEntityIdNotBetween(String value1, String value2) {
			addCriterion("base_entity_id not between", value1, value2, "baseEntityId");
			return (Criteria) this;
		}

		public Criteria andServerVersionIsNull() {
			addCriterion("server_version is null");
			return (Criteria) this;
		}

		public Criteria andServerVersionIsNotNull() {
			addCriterion("server_version is not null");
			return (Criteria) this;
		}

		public Criteria andServerVersionEqualTo(Date value) {
			addCriterion("server_version =", value, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionNotEqualTo(Date value) {
			addCriterion("server_version <>", value, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionGreaterThan(Date value) {
			addCriterion("server_version >", value, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionGreaterThanOrEqualTo(Date value) {
			addCriterion("server_version >=", value, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionLessThan(Date value) {
			addCriterion("server_version <", value, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionLessThanOrEqualTo(Date value) {
			addCriterion("server_version <=", value, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionIn(List<Date> values) {
			addCriterion("server_version in", values, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionNotIn(List<Date> values) {
			addCriterion("server_version not in", values, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionBetween(Date value1, Date value2) {
			addCriterion("server_version between", value1, value2, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andServerVersionNotBetween(Date value1, Date value2) {
			addCriterion("server_version not between", value1, value2, "serverVersion");
			return (Criteria) this;
		}

		public Criteria andReportTypeIsNull() {
			addCriterion("report_type is null");
			return (Criteria) this;
		}

		public Criteria andReportTypeIsNotNull() {
			addCriterion("report_type is not null");
			return (Criteria) this;
		}

		public Criteria andReportTypeEqualTo(String value) {
			addCriterion("report_type =", value, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeNotEqualTo(String value) {
			addCriterion("report_type <>", value, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeGreaterThan(String value) {
			addCriterion("report_type >", value, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeGreaterThanOrEqualTo(String value) {
			addCriterion("report_type >=", value, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeLessThan(String value) {
			addCriterion("report_type <", value, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeLessThanOrEqualTo(String value) {
			addCriterion("report_type <=", value, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeLike(String value) {
			addCriterion("report_type like", value, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeNotLike(String value) {
			addCriterion("report_type not like", value, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeIn(List<String> values) {
			addCriterion("report_type in", values, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeNotIn(List<String> values) {
			addCriterion("report_type not in", values, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeBetween(String value1, String value2) {
			addCriterion("report_type between", value1, value2, "reportType");
			return (Criteria) this;
		}

		public Criteria andReportTypeNotBetween(String value1, String value2) {
			addCriterion("report_type not between", value1, value2, "reportType");
			return (Criteria) this;
		}

		public Criteria andProviderIdIsNull() {
			addCriterion("provider_id is null");
			return (Criteria) this;
		}

		public Criteria andProviderIdIsNotNull() {
			addCriterion("provider_id is not null");
			return (Criteria) this;
		}

		public Criteria andProviderIdEqualTo(String value) {
			addCriterion("provider_id =", value, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdNotEqualTo(String value) {
			addCriterion("provider_id <>", value, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdGreaterThan(String value) {
			addCriterion("provider_id >", value, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdGreaterThanOrEqualTo(String value) {
			addCriterion("provider_id >=", value, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdLessThan(String value) {
			addCriterion("provider_id <", value, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdLessThanOrEqualTo(String value) {
			addCriterion("provider_id <=", value, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdLike(String value) {
			addCriterion("provider_id like", value, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdNotLike(String value) {
			addCriterion("provider_id not like", value, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdIn(List<String> values) {
			addCriterion("provider_id in", values, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdNotIn(List<String> values) {
			addCriterion("provider_id not in", values, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdBetween(String value1, String value2) {
			addCriterion("provider_id between", value1, value2, "providerId");
			return (Criteria) this;
		}

		public Criteria andProviderIdNotBetween(String value1, String value2) {
			addCriterion("provider_id not between", value1, value2, "providerId");
			return (Criteria) this;
		}

		public Criteria andLocationIdIsNull() {
			addCriterion("location_id is null");
			return (Criteria) this;
		}

		public Criteria andLocationIdIsNotNull() {
			addCriterion("location_id is not null");
			return (Criteria) this;
		}

		public Criteria andLocationIdEqualTo(String value) {
			addCriterion("location_id =", value, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdNotEqualTo(String value) {
			addCriterion("location_id <>", value, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdGreaterThan(String value) {
			addCriterion("location_id >", value, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdGreaterThanOrEqualTo(String value) {
			addCriterion("location_id >=", value, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdLessThan(String value) {
			addCriterion("location_id <", value, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdLessThanOrEqualTo(String value) {
			addCriterion("location_id <=", value, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdLike(String value) {
			addCriterion("location_id like", value, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdNotLike(String value) {
			addCriterion("location_id not like", value, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdIn(List<String> values) {
			addCriterion("location_id in", values, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdNotIn(List<String> values) {
			addCriterion("location_id not in", values, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdBetween(String value1, String value2) {
			addCriterion("location_id between", value1, value2, "locationId");
			return (Criteria) this;
		}

		public Criteria andLocationIdNotBetween(String value1, String value2) {
			addCriterion("location_id not between", value1, value2, "locationId");
			return (Criteria) this;
		}

		public Criteria andTeamIsNull() {
			addCriterion("team is null");
			return (Criteria) this;
		}

		public Criteria andTeamIsNotNull() {
			addCriterion("team is not null");
			return (Criteria) this;
		}

		public Criteria andTeamEqualTo(String value) {
			addCriterion("team =", value, "team");
			return (Criteria) this;
		}

		public Criteria andTeamNotEqualTo(String value) {
			addCriterion("team <>", value, "team");
			return (Criteria) this;
		}

		public Criteria andTeamGreaterThan(String value) {
			addCriterion("team >", value, "team");
			return (Criteria) this;
		}

		public Criteria andTeamGreaterThanOrEqualTo(String value) {
			addCriterion("team >=", value, "team");
			return (Criteria) this;
		}

		public Criteria andTeamLessThan(String value) {
			addCriterion("team <", value, "team");
			return (Criteria) this;
		}

		public Criteria andTeamLessThanOrEqualTo(String value) {
			addCriterion("team <=", value, "team");
			return (Criteria) this;
		}

		public Criteria andTeamLike(String value) {
			addCriterion("team like", value, "team");
			return (Criteria) this;
		}

		public Criteria andTeamNotLike(String value) {
			addCriterion("team not like", value, "team");
			return (Criteria) this;
		}

		public Criteria andTeamIn(List<String> values) {
			addCriterion("team in", values, "team");
			return (Criteria) this;
		}

		public Criteria andTeamNotIn(List<String> values) {
			addCriterion("team not in", values, "team");
			return (Criteria) this;
		}

		public Criteria andTeamBetween(String value1, String value2) {
			addCriterion("team between", value1, value2, "team");
			return (Criteria) this;
		}

		public Criteria andTeamNotBetween(String value1, String value2) {
			addCriterion("team not between", value1, value2, "team");
			return (Criteria) this;
		}

		public Criteria andTeamIdIsNull() {
			addCriterion("team_id is null");
			return (Criteria) this;
		}

		public Criteria andTeamIdIsNotNull() {
			addCriterion("team_id is not null");
			return (Criteria) this;
		}

		public Criteria andTeamIdEqualTo(String value) {
			addCriterion("team_id =", value, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdNotEqualTo(String value) {
			addCriterion("team_id <>", value, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdGreaterThan(String value) {
			addCriterion("team_id >", value, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdGreaterThanOrEqualTo(String value) {
			addCriterion("team_id >=", value, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdLessThan(String value) {
			addCriterion("team_id <", value, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdLessThanOrEqualTo(String value) {
			addCriterion("team_id <=", value, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdLike(String value) {
			addCriterion("team_id like", value, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdNotLike(String value) {
			addCriterion("team_id not like", value, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdIn(List<String> values) {
			addCriterion("team_id in", values, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdNotIn(List<String> values) {
			addCriterion("team_id not in", values, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdBetween(String value1, String value2) {
			addCriterion("team_id between", value1, value2, "teamId");
			return (Criteria) this;
		}

		public Criteria andTeamIdNotBetween(String value1, String value2) {
			addCriterion("team_id not between", value1, value2, "teamId");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table core.report_metadata
	 * @mbg.generated  Fri Mar 09 15:24:01 EAT 2018
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table core.report_metadata
     *
     * @mbg.generated do_not_delete_during_merge Wed Mar 07 18:26:22 EAT 2018
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}