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
	
}
