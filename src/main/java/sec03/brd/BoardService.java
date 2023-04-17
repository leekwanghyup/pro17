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
	
	public void addArticle(ArticleVO article){
		boardDAO.insertNewArticle(article);		
	}
}
