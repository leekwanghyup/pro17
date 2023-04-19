package sec03.brd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	public List<ArticleVO> selectAllArticles(Map<String, Integer> pagingMap){
		Connection conn = null; 
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		int startRow = pagingMap.get("startRow");
		int endRow = pagingMap.get("endRow");
		
		List<ArticleVO> articleList = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			String query = "select * from (" 
					+ " select rownum as rn,level,articleNO,"
					+ " parentNO,title,content,id,writeDate" 
					+ " from t_board start with parentNO =0"
					+ " connect by prior articleNO = parentNO" 
					+" order siblings by articleNO DESC)" 
					+ "where rn between ? and ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1,startRow); // 시작 번호 
			pstmt.setInt(2,endRow); // 끝 번호 
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
	public int insertNewArticle(ArticleVO article) {
		Connection conn = null; 
		int articleNO = getNewArticleNO();
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			
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
		return articleNO;
	}
	
	// 글 상세 
	public ArticleVO selectArticle(int articleNO){
		Connection conn = null; 
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		ArticleVO article = new ArticleVO();
		try {
			conn= dataSource.getConnection();
			String query ="select articleNO,parentNO,title,content, imageFileName, id, writeDate";
			query+= " from t_board";
			query+= " where articleNO=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			rs =pstmt.executeQuery();
			if(rs.next()) {
				int _articleNO =rs.getInt("articleNO");
				int parentNO=rs.getInt("parentNO");
				String title = rs.getString("title");
				String content =rs.getString("content");
				String imageFileName= rs.getString("imageFileName");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				
				article.setArticleNO(_articleNO);
				article.setParentNO (parentNO);
				article.setTitle(title);
				article.setContent(content);
				article.setImageFileName(imageFileName);
				article.setId(id);
				article.setWriteDate(writeDate);
				rs.close();
				pstmt.close();
				conn.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return article;
	}

	public void updateArticle(ArticleVO article) {
		Connection conn = null; 
		PreparedStatement pstmt = null;
		
		int articleNO = article.getArticleNO();
		String title = article.getTitle();
		String content = article.getContent();
		String imageFileName = article.getImageFileName();
		
		try {
			conn = dataSource.getConnection();
			String query = "update t_board  set title=?, content=?";
			if (imageFileName != null && !imageFileName.isBlank()) {
				query += " ,imageFileName=?";
			}
			query += " where articleNO=?";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			if (imageFileName != null && !imageFileName.isBlank()) {
				System.out.println("바인딩되냐?");
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			} else {
				pstmt.setInt(3, articleNO);
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 삭제 대상 글번호 조회 
	public List<Integer> selectRemovedArticles(int  articleNO) {
		Connection conn = null; 
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		List<Integer> articleNOList = new ArrayList<Integer>();
		
		try {
			conn = dataSource.getConnection();
			String query = "SELECT articleNO FROM  t_board  ";
			query += " START WITH articleNO = ?";
			query += " CONNECT BY PRIOR  articleNO = parentNO";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				articleNOList.add(rs.getInt("articleNO"));
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return articleNOList; 
	}
	
	// 글 삭제
	public void deleteArticle(int articleNO) {
		Connection conn = null; 
		PreparedStatement pstmt = null;
		String query = "DELETE FROM t_board  WHERE articleNO = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
