package sec03.brd;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

@WebServlet("/board/*")
public class BoardController extends HttpServlet{
	
	private BoardService boardService;
	
	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		boardService = new BoardService();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doHandle(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		String action = request.getPathInfo();
		try {
			if (action == null|| action.equals("/") || action.equals("/listArticles.do")) {
				List<ArticleVO> articleList = boardService.listArticles();
				request.setAttribute("articleList", articleList);
				nextPage = "/brd/listArticles.jsp";
			} else if (action.equals("/articleForm.do")) { // 글쓰기 폼
				nextPage = "/brd/articleForm.jsp";
			}  else if (action.equals("/addArticle.do")) {
				Map<String, String> articleMap = upload(request, response);
				
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				ArticleVO articleVO = new ArticleVO(); 
				articleVO.setParentNO(0);
				articleVO.setId("hong"); // 임시작성자
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				
				// 글번호를 받아옴
				int articleNo = boardService.addArticle(articleVO);
				// isBlank() : 해당 문자열이 공백문자 또는 빈문자인지 여부  
				if(imageFileName!=null && !imageFileName.isBlank()) {
					File srcFile = new 	File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName); // 이동할 파일 객체
					File destDir = new File(ARTICLE_IMAGE_REPO +"\\"+articleNo); // 이동할 대상 디렉토리 파일 객체

					// 디렉토리가 없으면 새로운 디렉토리 생성 
					if(!destDir.exists()) {
						destDir.mkdirs(); // 
					}
					
					/*
					 * 파일이동 메서드
					 * @Param1 : 이동할 파일의 File 객체
					 * @Param2 : 이동할 대상 디렉토리의 File 객체 
					 * @Param3 : 대상 디렉토리가 없는 경우 디렉토리 생성 여부
					 * */ 
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					
				}
				
				// 클라이언트 응답
				HttpSession session = request.getSession();
				session.setAttribute("feedback", "addNewArticle");
				response.sendRedirect(request.getContextPath()+"/board/listArticles.do");
				return; 
			} else if(action.equals("/viewArticle.do")){
				String articleNO = request.getParameter("articleNO");
				// 예외처리 필요함
				ArticleVO articleVO=boardService.viewArticle(Integer.parseInt(articleNO));
				request.setAttribute("article",articleVO);
				nextPage = "/brd/viewArticle.jsp";
			
			// 글 수정 처리 
			} else if(action.equals("/modArticle.do")) { 
				Map<String, String> articleMap = upload(request, response);
				ArticleVO articleVO = new ArticleVO();
				
				int articleNO = Integer.parseInt(articleMap.get("articleNO"));
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				articleVO.setArticleNO(articleNO);
				articleVO.setParentNO(0);
				articleVO.setId("hong"); // 임시 사용자 
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				boardService.modArticle(articleVO);
				if (imageFileName != null && !imageFileName.isBlank()) {
					String originalFileName = articleMap.get("originalFileName"); //기존파일 이름
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					if(!destDir.exists()) destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					if(originalFileName!=null) {
						File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
						oldFile.delete();
					}
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("feedback", "modArticle");
				response.sendRedirect(request.getContextPath()+"/board/listArticles.do");
				return;
			}
			
			else {
				nextPage = "/brd/listArticles.jsp";
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}

	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Map<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fileItem : items) {
				if (fileItem.isFormField()) {
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				} else {
					if (fileItem.getSize() > 0) {
						String fileName = fileItem.getName();
						articleMap.put(fileItem.getFieldName(), fileName);
						// 임시파일 저장소로 지정 
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
						fileItem.write(uploadFile);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleMap;
	}
}
