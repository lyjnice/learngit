package com.yanxiu.util.database;

import com.yanxiu.workselect.api.commons.vo.Pagination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基础dao类
 * @author yueting
 * @version 创建时间:2015年9月14日
 *
 */
@SuppressWarnings({ "unchecked" })
public abstract class MysqlBaseDAO {

	private static final Log LOG = LogFactory.getLog(MysqlBaseDAO.class);

	protected JdbcTemplate jdbcTemplate;
	
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * sql中的in 条件使用NamedParameterJdbcTemplate实现，禁止拼接sql
	 * @return
	 */
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		
		if(this.namedParameterJdbcTemplate == null){
			this.createNamedParameterJdbcTemplate();
		}
		return this.namedParameterJdbcTemplate;
	}
	
	private synchronized void createNamedParameterJdbcTemplate(){
		
		if(this.namedParameterJdbcTemplate == null){
			this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		}
	}

//	@SuppressWarnings("unused")
//	private <T> Class<T> getPoClass() {
//
//		Type type = getClass().getGenericSuperclass();
//		Class<T> poClass = null;
//		if (type instanceof ParameterizedType) {
//			ParameterizedType pType = (ParameterizedType) type;
//			poClass = (Class<T>) pType.getActualTypeArguments()[0];
//		}
//		return poClass;
//	}

	/**
	 * 查询唯一对象
	 * 
	 * @param sql
	 * @param rowMapper
	 * @param args
	 * @return
	 */
	protected <T> T queryUnique(String sql, RowMapper<T> rowMapper, Object... args) {
		try {
			return jdbcTemplate.queryForObject(sql, rowMapper, args);
		} catch (EmptyResultDataAccessException e) {
			//LOG.debug("queryUnique is null");
		}
		return null;
	}


	/**
	 * 查询唯一对象，返回map
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	protected Map<String, Object> queryUnique(String sql, Object... args) {

		try {
			return jdbcTemplate.queryForMap(sql, args);
		} catch (EmptyResultDataAccessException e) {
			//LOG.debug("queryUnique is null");
		}
		return null;
	}

	/**
	 * 查询第一个对象
	 * 
	 * @param sql
	 * @param rowMapper
	 * @param args
	 * @return
	 */
	protected <T> T queryFirst(String sql, RowMapper<T> rowMapper, Object... args) {

		sql += " limit 1";
		List<T> list = jdbcTemplate.query(sql, rowMapper, args);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}


	/**
	 * 查询第一个对象，返回map
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	protected Map<String, Object> queryFirst(String sql, Object... args) {
		
		sql += " limit 1";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 查询List数据对象
	 * 
	 * @param sql
	 * @param rowMapper
	 * @param args
	 * @return
	 */
	protected <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... args) {

		return jdbcTemplate.query(sql, rowMapper, args);
	}


	/**
	 * 查询List数据对象，返回map
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	protected List<Map<String, Object>> queryForList(String sql, Object... args) {

		return jdbcTemplate.queryForList(sql, args);
	}

	protected <T> List<T> queryForLimitListFromZero(String sql, int dataNum, RowMapper<T> rowMapper, Object... args) {

		return this.queryForLimitList(sql, 0, dataNum, rowMapper, args);
	}


	/**
	 * 查询List对象，指定返回数量map
	 * 
	 * @param sql
	 * @param dataNum
	 * @param args
	 * @return
	 */
	protected List<Map<String, Object>> queryForLimitListFromZero(String sql, int dataNum, Object... args) {

		return this.queryForLimitList(sql, 0, dataNum, args);
	}

	/**
	 * 查询List对象，指定返回数量
	 * 
	 * @param sql
	 * @param firstResult
	 * @param dataNum
	 * @param rowMapper
	 * @param args
	 * @return
	 */
	protected <T> List<T> queryForLimitList(String sql, int firstResult, int dataNum, RowMapper<T> rowMapper,
			Object... args) {

		sql += " limit " + firstResult + "," + dataNum;
		return jdbcTemplate.query(sql, rowMapper, args);
	}


	/**
	 * 查询List对象，指定返回数量map，根据表字段名自动映射VO中的属性
	 * 
	 * @param sql
	 * @param firstResult
	 * @param dataNum
	 * @param args
	 * @return
	 */
	protected List<Map<String, Object>> queryForLimitList(String sql, int firstResult, int dataNum, Object... args) {

		sql += " limit " + firstResult + "," + dataNum;
		return jdbcTemplate.queryForList(sql, args);
	}


	/**
	 * 查询结果数量
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	protected long queryForCount(String sql, Object... args) {

		return jdbcTemplate.queryForObject(sql, args, Long.class);
	}

	/**
	 * 更新
	 *
	 * @param sql
	 * @param args
	 */
	protected int update(String sql, Object... args) {

		return jdbcTemplate.update(sql, args);
	}

	/**
	 * 删除
	 *
	 * @param sql
	 * @param args
	 */
	protected int delete(String sql, Object... args) {

		return jdbcTemplate.update(sql, args);
	}

	/**
	 * 分页查询返回封装map对象
	 * 
	 * @param sql
	 * @param offset	与pageNum二选一
	 * @param pageSize
	 * @param pageNum	与offset二选一
	 * @param args
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected <T> Pagination<T> queryPaginationMap(String sql, Integer offset, Integer pageSize, Integer pageNum, Object... args) {

		Pagination<T> pagination = new Pagination<T>();
		sql = this.initPaginationSql(pagination, jdbcTemplate, offset, pageSize, pageNum, sql, args);
		pagination.setElements((List) jdbcTemplate.queryForList(sql, args));
		return pagination;
	}


	/**
	 * 分页查询返回封装VO对象
	 * 
	 * @param sql
	 * @param offset	与pageNum二选一
	 * @param pageSize
	 * @param pageNum	与offset二选一
	 * @param rowMapper
	 * @param args
	 * @return
	 */
	protected <T> Pagination<T> queryPagination(String sql, Integer offset, Integer pageSize, Integer pageNum, RowMapper<T> rowMapper, Object... args) {

		Pagination<T> pagination = new Pagination<T>();
		sql = this.initPaginationSql(pagination, jdbcTemplate, offset, pageSize, pageNum, sql, args);
		pagination.setElements(jdbcTemplate.query(sql, rowMapper, args));
		return pagination;
	}

	/**
	 * 初始化
	 * 
	 * @param pagination
	 * @param jdbcTemplate
	 * @param pageSize
	 * @param pageNum
	 * @param sql
	 * @param args
	 * @return
	 */
	private <T> String initPaginationSql(Pagination<T> pagination, JdbcTemplate jdbcTemplate, Integer offset, Integer pageSize, Integer pageNum, String sql, Object... args) {

		if(offset != null && pageNum != null){
			//throws R
		}
		
		if(offset != null){
			pagination.setOffset(offset);
		}else if(pageNum != null){
			pagination.setPageNum(pageNum);
		}
		
		pagination.setPageSize(pageSize);
		String countSql = checkGroupBy(sql);
		LOG.debug("Pagination.initSql " + countSql);
		long totalElements = jdbcTemplate.queryForObject(countSql, args, Long.class);
		pagination.setTotalElements(totalElements);

		int firstResult = 0;
		if(offset != null){
			firstResult = offset;
		}else if(pageNum != null){
			int lagePageNum = pagination.getLastPageNumber();
			if (Integer.MAX_VALUE == pagination.getPageNum() || pagination.getPageNum() > lagePageNum) {
				pagination.setPageNum(lagePageNum);
			}
			if (pagination.getPageNum() < 1) {
				pagination.setPageNum(1);
			}
			firstResult = (pagination.getPageNum() - 1) * pagination.getPageSize();
		}
		sql += " limit " + firstResult + "," + pageSize;
		//LOG.debug("Pagination.initSql " + sql);
		return sql;
	}

	/**
	 * 去除select 子句，未考虑union的情况
	 * 
	 * @param sql
	 * @return
	 */
	private static String removeSelect(String sql) {

		Assert.hasText(sql);
		int beginPos = sql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	/**
	 * 去除orderby 子句
	 * 
	 * @param sql
	 * @return
	 */
	private static String removeOrders(String sql) {

		Assert.hasText(sql);
		Pattern p = Pattern.compile("order\\s+by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 处理group by子句
	 * 
	 * @param sql
	 * @return
	 */
	private static String checkGroupBy(String sql) {
		Assert.hasText(sql);
		sql = removeSelect(removeOrders(sql));
		Pattern p = Pattern.compile("group\\s+by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		StringBuffer last = new StringBuffer();
		boolean checkFlag = false;
		while (m.find()) {
			checkFlag = true;
			Pattern tempPattern = Pattern.compile("having[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
			Matcher tempMatcher = tempPattern.matcher(m.group());
			while (tempMatcher.find()) {
				tempMatcher.appendReplacement(sb, "");
			}
			tempMatcher.appendTail(sb);
		}
		if (checkFlag) {
			Pattern tempPattern = Pattern.compile("group\\s+by", Pattern.CASE_INSENSITIVE);
			Matcher tempMatcher = tempPattern.matcher(sb.toString());
			while (tempMatcher.find()) {
				tempMatcher.appendReplacement(last, "");
			}
			tempMatcher.appendTail(last);
			if (last.toString().indexOf(",") != -1) {
				last = new StringBuffer(last.toString().split(",")[0]);
			}
			//return "select count(distinct " + last + ") " + sql;	应该改成截掉gorup by
			return "select count(*) from (" + "select " + last + " " + sql + ") cgsql";
		} else {
			return "select count(*) " + sql;
		}

	}

}