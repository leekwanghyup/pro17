package sec03.brd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource dataSource; 
	
	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public List<ArticleVO> selectAllArticles(){
		Connection conn = null; 
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		
		List<ArticleVO> articleList = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			String query = "SELECT LEVEL,articleNO,parentNO,title,content,id,writeDate" 
					+ " from t_board"
					+ " START WITH  parentNO=0" 
					+ " CONNECT BY PRIOR articleNO=parentNO"
					+ " ORDER SIBLINGS BY articleNO DESC";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ArticleVO article = new ArticleVO();
				article.setLevel(rs.getInt("level"));
				article.setArticleNO(rs.getInt("articleNo"));
				article.setParentNO(rs.getInt("parentNo"));
				article.setTitle(rs.getString("title"));
				article.setContent(rs.getString("content"));
				article.setId(rs.getString("id"));
				article.setWriteDate(rs.getDate("writeDate"));
				articleList.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleList; 
	}
	
	// 새글 번호 반환 
	private int getNewArticleNO() {
		Connection conn = null; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			String query = "SELECT  max(articleNO) from t_board ";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery(query);
			if (rs.next()) {
				return (rs.getInt(1) + 1);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// 새로운 글쓰기 처리 
	public void insertNewArticle(ArticleVO article) {
		Connection conn = null; 
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			int articleNO = getNewArticleNO();
			int parentNO = article.getParentNO();
			String title = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			String imageFileName = article.getImageFileName();
			String query = "INSERT INTO t_board (articleNO, parentNO, title, content, imageFileName, id) VALUES (?, ? ,?, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
