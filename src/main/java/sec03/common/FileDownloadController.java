package sec03.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download.do")
public class FileDownloadController extends HttpServlet{
	
	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request,response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doHandle(request,response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String imageFileName = (String) request.getParameter("imageFileName");
		String articleNO = request.getParameter("articleNO");
		OutputStream out = response.getOutputStream();
		String path = ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + imageFileName;
		File imageFile = new File(path);
		
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachment;fileName=" + imageFileName);
		FileInputStream in = new FileInputStream(imageFile);
		
		byte[] buffer = new byte[1024 * 8];
		int count = 0; 
		while((count = in.read(buffer))!=-1) {
			out.write(buffer, 0, count);
		}
		in.close();
		out.close();
	}
	
	
}
