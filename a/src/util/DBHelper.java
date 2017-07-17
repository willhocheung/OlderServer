package util;
import java.sql.*;

public class DBHelper {
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/olderserversystem";
	private static final String username="root";
	private static final String password="123789456";
	private static Connection con;;
	private PreparedStatement pstm;
	//静态代码块加载驱动
	static{
		try {
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getCon() throws SQLException{
		if(con==null){
			con=DriverManager.getConnection(url, username, password);
			return con;	
		}
		return con;
	}
	
	public void doPstm(String sql, Object[] params) throws SQLException
	{
		if (sql != null && !sql.equals(""))
		{
			if (params == null)
				params = new Object[0];

			getCon();
			if (con != null)
			{
				try
				{
					System.out.println(sql);
					pstm = con.prepareStatement(sql,
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					for (int i = 0; i < params.length; i++)
					{
						pstm.setObject(i + 1, params[i]);
					}
					pstm.execute();
				} catch (SQLException e)
				{
					System.out.println("doPstm()方法出错！");
					e.printStackTrace();
				}
			}
		}
	}

	public ResultSet getRs() throws SQLException
	{
		return pstm.getResultSet();
	}

	public int getCount() throws SQLException
	{
		return pstm.getUpdateCount();
	}

	public void closed()
	{
		try
		{
			if (pstm != null)
				pstm.close();
		} catch (SQLException e)
		{
			System.out.println("关闭pstm对象失败！");
			e.printStackTrace();
		}
		try
		{
			if (con != null)
			{
				con.close();
			}
		} catch (SQLException e)
		{
			System.out.println("关闭con对象失败！");
			e.printStackTrace();
		}
	}
		
	}

