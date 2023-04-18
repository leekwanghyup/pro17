package sec03.brd;

import java.util.List;

public class BoardService {
	private BoardDAO boardDAO; 
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	public List<ArticleVO> listArticles(){
		List<ArticleVO> articleList = boardDAO.selectAllArticles(); 
		return articleList; 
	}
	
	public int addArticle(ArticleVO article){
		return boardDAO.insertNewArticle(article);		
	}
	
	//  글 상세 
	public ArticleVO viewArticle(int articleNO) {
		return boardDAO.selectArticle(articleNO);
	}

	// 글 수정
	public void modArticle(ArticleVO article) {
		boardDAO.updateArticle(article);
	}
	
	// 삭제 대상 글 번호 리스트
	public List<Integer> selectRemovedArticles(int  articleNO) {
		return boardDAO.selectRemovedArticles(articleNO);
	}
	
	// 글 삭제
	public void deleteArticle(int articleNO) {
		boardDAO.deleteArticle(articleNO);
	}
}
