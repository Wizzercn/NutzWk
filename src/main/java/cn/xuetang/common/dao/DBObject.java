package cn.xuetang.common.dao;

import java.io.BufferedReader;
import java.sql.ResultSet;

import org.nutz.log.Log;
import org.nutz.log.Logs;
 

/**
 * @author Wizzer.cn
 * @time   2012-9-13 下午6:13:55
 *
 */
public class DBObject {
	private static final Log log = Logs.get();
	/*
	 * 获取CLOB字段值
	 */
	public static String getClobBody(ResultSet rs, String colnumName)
	{
		String result = "";
		try
		{
			String str_Clob = "";
			StringBuffer strBuffer_CLob = new StringBuffer();
			strBuffer_CLob.append("");
			oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(colnumName);
			BufferedReader in = new BufferedReader(clob.getCharacterStream());
			while ((str_Clob = in.readLine()) != null)
			{
				strBuffer_CLob.append(str_Clob + "\n");
			}
			in.close();
			result = strBuffer_CLob.toString();
		}
		catch (Exception e)
		{
			log.debug(e.getMessage());
		}
		return result;
	}
}


